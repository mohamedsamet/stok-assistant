package com.yesmind.stok.core.business.mapper;

import com.yesmind.stok.core.domain.data.TransformationDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.StationTransformation;
import com.yesmind.stok.core.domain.entity.WorkStation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorkStationMapper {

    private WorkStationMapper() {}

    public static WorkStation toWorkStation(WorkStationDto workStationDto) {
        return WorkStation.builder()
                .machine(workStationDto.getMachine())
                .name(workStationDto.getName())
                .operator(workStationDto.getOperator())
                .date(workStationDto.getDate())
                .duration(workStationDto.getDuration())
                .build();
    }

    public static WorkStationDto toWorkStationDto(WorkStation workStation) {
        return WorkStationDto.builder()
                .name(workStation.getName())
                .publicId(workStation.getPublicId())
                .machine(workStation.getMachine())
                .operator(workStation.getOperator())
                .date(workStation.getDate())
                .duration(workStation.getDuration())
                .closed(workStation.getClosed())
                .deleted(workStation.getDeleted())
                .reference(workStation.getReference())
                .transformations(toTransformations(workStation.getTransformations()))
                .build();
    }

    private static List<TransformationDto> toTransformations(List<StationTransformation> transformations) {
        return transformations.stream()
                .filter(transformation -> Boolean.FALSE.equals(transformation.getDeleted()))
                .map(WorkStationMapper::toTransformation)
                .collect(Collectors.toList());
    }

    private static TransformationDto toTransformation(StationTransformation stationTransformation) {
        return TransformationDto.builder()
                .productPublicId(stationTransformation.getProduct().getPublicId())
                .type(stationTransformation.getType())
                .product(ProductMapper.toProductDto(stationTransformation.getProduct()))
                .quantity(stationTransformation.getQuantity().setScale(3, RoundingMode.HALF_DOWN))
                .publicId(stationTransformation.getPublicId())
                .build();

    }

    public static List<StationTransformation> toTransformations(List<TransformationDto> transformations,
                                                               Map<UUID, Product> productMap,
                                                               WorkStation workStation) {
        return transformations.stream()
                .filter(trans -> productMap.get(trans.getProductPublicId()) != null)
                .map(transformation ->
                        transform(transformation, productMap.get(transformation.getProductPublicId()), workStation))
                .collect(Collectors.toList());
    }

    private static StationTransformation transform(TransformationDto transformation, Product product, WorkStation workStation) {
        return StationTransformation.builder()
                .product(product)
                .workStation(workStation)
                .quantity(transformation.getQuantity())
                .type(transformation.getType())
                .deleted(Boolean.FALSE)
                .build();
    }

    public static WorkStation toWorkStationByReference(String reference) {
        return WorkStation.builder()
                .reference(reference)
                .creationDate(LocalDateTime.now())
                .closed(false)
                .deleted(false)
                .creationStatus(CreationStatus.DRAFT)
                .build();
    }
}
