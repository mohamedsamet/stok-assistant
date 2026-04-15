package com.yesmind.stok.core.business;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.application.exception.WorkStationClosedException;
import com.yesmind.stok.core.business.mapper.ProductMapper;
import com.yesmind.stok.core.business.mapper.WorkStationMapper;
import com.yesmind.stok.core.domain.data.*;
import com.yesmind.stok.core.domain.entity.*;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import com.yesmind.stok.core.port.in.work_station.IWorkStationFactory;
import com.yesmind.stok.core.port.in.work_station.IWorkStationSearch;
import com.yesmind.stok.core.port.in.work_station.IWorkStationSetting;
import com.yesmind.stok.core.port.out.IWorkStationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkStationService implements IWorkStationFactory, IWorkStationSetting, IWorkStationSearch {

    private final IWorkStationRepository workStationRepository;
    private final IProductSearch productSearchService;

    @Override
    @Transactional
    public WorkStationDto buildWorkStation(WorkStationDto workStationDto) {
        WorkStation workStation = workStationRepository.findWorkStationByUuid(workStationDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Work station with public id %s not found", workStationDto.getPublicId())));

        workStation.setDuration(workStationDto.getDuration());
        workStation.setDate(workStationDto.getDate());
        workStation.setMachine(workStationDto.getMachine());
        workStation.setOperator(workStationDto.getOperator());
        workStation.setName(workStationDto.getName());
        workStation.setCreationStatus(CreationStatus.COMPLETED);

        WorkStation savedWorkStation = workStationRepository.saveWorkStation(workStation);
        return WorkStationMapper.toWorkStationDto(savedWorkStation);
    }


    @Override
    @Transactional
    public WorkStationDto buildDraftWorkStation() {
        Optional<WorkStation> lastWorkStationEntry = workStationRepository.findLastWorkStation();

        String reference = lastWorkStationEntry
                .map(workStation -> ReferenceGenerationUtils.generateReference(workStation.getReference(), ReferenceTools.STATION))
                .orElseGet(() -> ReferenceGenerationUtils.generateFirstReference(ReferenceTools.STATION));

        WorkStation savedStation = workStationRepository.saveWorkStation(WorkStationMapper.toWorkStationByReference(reference));
        return WorkStationMapper.toWorkStationDto(savedStation);
    }

    @Override
    @Transactional
    public void deleteWorkStation(UUID publicId) {
        WorkStation workStation = workStationRepository.findWorkStationByUuid(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Work station with public id %s not found", publicId)));

        workStation.setDeleted(true);
        workStationRepository.saveWorkStation(workStation);
    }

    @Transactional
    @Override
    public WorkStationDto updateWorkStation(UUID publicId, WorkStationDto workStationDto) {
        WorkStation workStation = workStationRepository.findWorkStationByUuid(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Work station with public id %s not found", publicId)));

        workStation.setName(workStationDto.getName());
        workStation.setOperator(workStationDto.getOperator());
        workStation.setDate(workStationDto.getDate());
        workStation.setMachine(workStationDto.getMachine());
        workStation.setDuration(workStationDto.getDuration());

        WorkStation workStationSaved = workStationRepository.saveWorkStation(workStation);

        return WorkStationMapper.toWorkStationDto(workStationSaved);
    }

    @Override
    public WorkStationResponse searchWorkStations(SearchDto searchDto) {

        if (searchDto.getName() != null) {
            searchDto.setName("%" + searchDto.getName() + "%");
        }
        List<WorkStationDto> stations = workStationRepository.searchStations(searchDto)
                .stream()
                .map(WorkStationMapper::toWorkStationDto)
                .toList();

        return WorkStationResponse.builder()
                .stations(stations)
                .count(workStationRepository.count(searchDto))
                .build();

    }

    @Override
    public WorkStationDto findWorkStation(UUID workStationUuid) {
        Optional<WorkStation> workStation = workStationRepository.findWorkStationByUuid(workStationUuid);

        if (workStation.isEmpty()) {
            handleWorkStationNotFound(workStationUuid);
        }

        return WorkStationMapper.toWorkStationDto(workStation.get());

    }

    @Override
    @Transactional
    public WorkStationDto setTransformations(TransformationsDto transformationsDto) {
        Optional<WorkStation> workStationOpt = workStationRepository.findWorkStationByUuid(transformationsDto.getWorkStationPublicId());

        if (workStationOpt.isEmpty()) {
            handleWorkStationNotFound(transformationsDto.getWorkStationPublicId());
        }

        WorkStation workStation = workStationOpt.get();

        if (Boolean.TRUE.equals(workStation.getClosed())) {
            throw new WorkStationClosedException(
                    String.format("Work Station with UUID %s is Closed for Transformation", workStation.getPublicId()));
        }

        setDeletedTransformations(workStation);

        List<StationTransformation> transformations = WorkStationMapper.
                toTransformations(
                        transformationsDto.getTransformations(),
                        getUuidProductMap(transformationsDto),
                        workStation);

        workStation.setTransformations(transformations);

        workStationRepository.saveWorkStation(workStation);
        return WorkStationMapper.toWorkStationDto(workStation);
    }

    @Override
    @Transactional
    public WorkStationDto closeWorkStation(UUID workStationPublicId) {
        Optional<WorkStation> workStationOpt = workStationRepository.findWorkStationByUuid(workStationPublicId);

        if (workStationOpt.isEmpty()) {
            handleWorkStationNotFound(workStationPublicId);
        }

        WorkStation workStation = workStationOpt.get();

        if (Boolean.TRUE.equals(workStation.getClosed())) {
            throw new WorkStationClosedException(
                    String.format("Work Station with UUID %s is Closed for Transformation", workStation.getPublicId()));
        }

        workStation.setClosed(Boolean.TRUE);

        processTransformCalculation(workStation);

        workStationRepository.saveWorkStation(workStation);
        return WorkStationMapper.toWorkStationDto(workStation);
    }

    private void processTransformCalculation(WorkStation workStation) {
        workStation.getTransformations().stream()
                .filter(transformation -> Boolean.FALSE.equals(transformation.getDeleted()))
                .filter(stationTransformation -> !stationTransformation.getProduct().getDeleted())
                .forEach(stationTransformation -> {
            BigDecimal quantity = stationTransformation.getProduct().getQuantity();
            BigDecimal quantityRounded = quantity.setScale(3, RoundingMode.HALF_DOWN);
            BigDecimal payloadRounded = stationTransformation.getQuantity().setScale(3, RoundingMode.HALF_DOWN);
            stationTransformation.getProduct().setQuantity(
                    stationTransformation.getType().equals(TransformationType.INPUT) ?
                            quantityRounded.subtract(payloadRounded).setScale(3, RoundingMode.DOWN) :
                            quantityRounded.add(payloadRounded).setScale(3, RoundingMode.DOWN));
        });
    }

    private void setDeletedTransformations(WorkStation workStation) {
        List<StationTransformation> existingTransformations = workStation.getTransformations();
        existingTransformations.forEach(existing -> existing.setDeleted(true));
    }

    private Map<UUID, Product> getUuidProductMap(TransformationsDto transformationsDto) {
        Set<UUID> productUuids = transformationsDto.getTransformations().stream()
                .map(TransformationDto::getProductPublicId)
                .collect(Collectors.toSet());

        return productSearchService.findByUuids(productUuids);
    }

    private void handleWorkStationNotFound(UUID workStationPublicId) {
        throw new ResourceNotFoundException(
                String.format("Work Station with UUID %s Not Found", workStationPublicId));
    }
}
