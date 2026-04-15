package com.yesmind.stok.unit.work_stataion;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.application.exception.WorkStationClosedException;
import com.yesmind.stok.core.business.WorkStationService;
import com.yesmind.stok.core.business.mapper.WorkStationMapper;
import com.yesmind.stok.core.domain.data.*;
import com.yesmind.stok.core.domain.entity.*;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import com.yesmind.stok.core.port.out.IWorkStationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;

@ExtendWith(MockitoExtension.class)
class WorkStationServiceTest {

    @InjectMocks
    private WorkStationService workStationService;

    @Mock
    private IWorkStationRepository workStationRepository;

    @Mock
    private IProductSearch productSearch;

    @Captor
    private ArgumentCaptor<WorkStation> workStationArgumentCaptor;

    @Test
    void shouldBuildWorkStation() {
        WorkStationDto workStationDto = WorkStationDto.builder()
                .duration(1)
                .machine("m1")
                .operator("o1")
                .name("station 1")
                .build();

        WorkStation savedWorkStation = WorkStationMapper.toWorkStation(workStationDto);
        savedWorkStation.setClosed(true);
        savedWorkStation.setTransformations(new ArrayList<>());
        savedWorkStation.setPublicId(UUID.randomUUID());

        Mockito.when(workStationRepository.findWorkStationByUuid(any())).thenReturn(Optional.of(WorkStation.builder().publicId(UUID.randomUUID()).build()));
        Mockito.when(workStationRepository.saveWorkStation(workStationArgumentCaptor.capture())).thenReturn(savedWorkStation);

        WorkStationDto saved = workStationService.buildWorkStation(workStationDto);

        Mockito.verify(workStationRepository, Mockito.times(1)).saveWorkStation(any());

        Assertions.assertNotNull(saved);
        Assertions.assertEquals("station 1", saved.getName());
        Assertions.assertEquals("m1", saved.getMachine());
        Assertions.assertEquals("o1", saved.getOperator());
        Assertions.assertEquals(1, saved.getDuration());
        Assertions.assertEquals(true, saved.getClosed());
        Assertions.assertNotNull(saved.getPublicId());

        Assertions.assertNotNull(workStationArgumentCaptor.getValue());
        Assertions.assertEquals("station 1", workStationArgumentCaptor.getValue().getName());
        Assertions.assertEquals("m1", workStationArgumentCaptor.getValue().getMachine());
        Assertions.assertEquals("o1", workStationArgumentCaptor.getValue().getOperator());
        Assertions.assertEquals(1, workStationArgumentCaptor.getValue().getDuration());
    }

    @Test
    void shouldThrowResourceNotFound() {
        UUID publicId = UUID.randomUUID();

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                workStationService.findWorkStation(publicId));

        String expectedMessage = "Work Station with UUID "+ publicId + " Not Found";
        Assertions.assertEquals(expectedMessage, resourceNotFoundException.getMessage());
    }

    @Test
    void shouldFindWorkStation() {
        UUID publicId = UUID.randomUUID();

        WorkStation workStation1 = buildWorkStation("op1", "m1", false, "st1", new ArrayList<>());
        workStation1.setPublicId(publicId);

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.of(workStation1));

        WorkStationDto workStationDto = workStationService.findWorkStation(publicId);

        Assertions.assertEquals("st1", workStationDto.getName());
        Assertions.assertEquals("m1", workStationDto.getMachine());
        Assertions.assertEquals("op1", workStationDto.getOperator());
        Assertions.assertEquals(1, workStationDto.getDuration());
        Assertions.assertEquals(false, workStationDto.getClosed());

    }

    @Test
    void shouldFindAllWorkStations() {
        StationTransformation stationTransformation1 = buildTransformation(1.41F, TransformationType.INPUT, true,
                buildProduct(UUID.nameUUIDFromBytes("test".getBytes()), ProductType.RAW, 1F, 5F, "p1"));

        StationTransformation stationTransformation2 = buildTransformation(1.21F, TransformationType.INPUT, false,
                buildProduct(UUID.nameUUIDFromBytes("test".getBytes()), ProductType.FINAL, 1F, 5F, "p1"));

        StationTransformation stationTransformation3 = buildTransformation(3F, TransformationType.OUTPUT, false,
                buildProduct(UUID.nameUUIDFromBytes("test2".getBytes()), ProductType.FINAL, 0.5F, 9F, "p2"));


        WorkStation workStation1 = buildWorkStation("op1", "m1", false, "st1", Collections.singletonList(stationTransformation1));

        List<StationTransformation> stationTransformations = new ArrayList<>();
        stationTransformations.add(stationTransformation2);
        stationTransformations.add(stationTransformation3);

        WorkStation workStation2 = buildWorkStation("op2", "m2", true, "st2", stationTransformations);

        List<WorkStation> workStations = new ArrayList<>();
        workStations.add(workStation1);
        workStations.add(workStation2);

        Mockito.when(workStationRepository.searchStations(any())).thenReturn(workStations);

        List<WorkStationDto> workStationDtos = workStationService.searchWorkStations(new SearchDto())
                .getStations();

        Assertions.assertNotNull(workStationDtos);
        Assertions.assertEquals(2, workStationDtos.size());

        WorkStationDto workStationDto1 = workStationDtos.stream()
                .filter(station -> station.getName().equals("st1"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(workStationDto1);
        Assertions.assertEquals("st1", workStationDto1.getName());
        Assertions.assertEquals(1, workStationDto1.getDuration());
        Assertions.assertEquals("op1", workStationDto1.getOperator());
        Assertions.assertEquals("m1", workStationDto1.getMachine());
        Assertions.assertEquals(0, workStationDto1.getTransformations().size());

        WorkStationDto workStationDto2 = workStationDtos.stream()
                .filter(station -> station.getName().equals("st2"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(workStationDto2);
        Assertions.assertEquals("st2", workStationDto2.getName());
        Assertions.assertEquals(1, workStationDto2.getDuration());
        Assertions.assertEquals("op2", workStationDto2.getOperator());
        Assertions.assertEquals("m2", workStationDto2.getMachine());
        Assertions.assertEquals(2, workStationDto2.getTransformations().size());

        TransformationDto productTransformation1 = workStationDto2.getTransformations().stream()
                .filter(trans -> trans.getProductPublicId().equals(UUID.nameUUIDFromBytes("test".getBytes())))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(productTransformation1);
        Assertions.assertEquals(UUID.nameUUIDFromBytes("test".getBytes()), productTransformation1.getProductPublicId());
        Assertions.assertEquals(BigDecimal.valueOf(1.21d).setScale(3, RoundingMode.HALF_DOWN), productTransformation1.getQuantity());
        Assertions.assertEquals(TransformationType.INPUT, productTransformation1.getType());
        Assertions.assertEquals(BigDecimal.valueOf(5d), productTransformation1.getProduct().getQuantity());

        TransformationDto productTransformation2 = workStationDto2.getTransformations().stream()
                .filter(trans -> trans.getProductPublicId().equals(UUID.nameUUIDFromBytes("test2".getBytes())))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(productTransformation2);
        Assertions.assertEquals(UUID.nameUUIDFromBytes("test2".getBytes()), productTransformation2.getProductPublicId());
        Assertions.assertEquals(BigDecimal.valueOf(3).setScale(3, RoundingMode.HALF_DOWN), productTransformation2.getQuantity());
        Assertions.assertEquals(TransformationType.OUTPUT, productTransformation2.getType());
        Assertions.assertEquals(BigDecimal.valueOf(9d), productTransformation2.getProduct().getQuantity());
    }

    @Test
    void shouldThrowResourceNotFoundWhenSetWorkStationTransformations() {

        UUID publicId = UUID.randomUUID();
        TransformationsDto transformationDto = TransformationsDto.builder()
                .workStationPublicId(publicId)
                .transformations(null)
                .build();

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> workStationService.setTransformations(transformationDto));

        String expectedMessage = "Work Station with UUID " + publicId + " Not Found";
        Assertions.assertEquals(expectedMessage, resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundWhenCloseWorkStation() {

        UUID publicId = UUID.randomUUID();

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> workStationService.closeWorkStation(publicId));

        String expectedMessage = "Work Station with UUID " + publicId + " Not Found";
        Assertions.assertEquals(expectedMessage, resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowClosedExceptionWhenSetWorkStationTransformations() {

        UUID publicId = UUID.randomUUID();
        TransformationsDto transformationDto = TransformationsDto.builder()
                .workStationPublicId(publicId)
                .transformations(null)
                .build();

        WorkStation workStation = buildWorkStation("op2", "m2", true, "st2", new ArrayList<>());
        workStation.setPublicId(publicId);

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.of(workStation));

        WorkStationClosedException workStationClosedException = Assertions.assertThrows(WorkStationClosedException.class,
                () -> workStationService.setTransformations(transformationDto));

        String expectedMessage = "Work Station with UUID " + publicId + " is Closed for Transformation";
        Assertions.assertEquals(expectedMessage, workStationClosedException.getMessage());
    }

    @Test
    void shouldThrowClosedExceptionWhenCloseWorkStation() {

        UUID publicId = UUID.randomUUID();

        WorkStation workStation = buildWorkStation("op2", "m2", true, "st2", new ArrayList<>());
        workStation.setPublicId(publicId);

        Mockito.when(workStationRepository.findWorkStationByUuid(publicId)).thenReturn(Optional.of(workStation));

        WorkStationClosedException workStationClosedException = Assertions.assertThrows(WorkStationClosedException.class,
                () -> workStationService.closeWorkStation(publicId));

        String expectedMessage = "Work Station with UUID " + publicId + " is Closed for Transformation";
        Assertions.assertEquals(expectedMessage, workStationClosedException.getMessage());
    }

    @Test
    void shouldSetWorkStationTransformations() {

        UUID workStationPublicId = UUID.randomUUID();
        UUID product1PublicId = UUID.randomUUID();
        UUID product2PublicId = UUID.randomUUID();

        List<TransformationDto> transformationsDtos = new ArrayList<>();
        TransformationDto transformationDto1 = TransformationDto.builder()
                .type(TransformationType.INPUT)
                .quantity(BigDecimal.valueOf(2d))
                .productPublicId(product1PublicId)
                .product(ProductDto.builder()
                        .publicId(product1PublicId)
                        .build())
                .build();

        TransformationDto transformationDto2 = TransformationDto.builder()
                .type(TransformationType.OUTPUT)
                .quantity(BigDecimal.valueOf(6d))
                .productPublicId(product2PublicId)
                .product(ProductDto.builder()
                        .publicId(product2PublicId)
                        .build())
                .build();

        transformationsDtos.add(transformationDto1);
        transformationsDtos.add(transformationDto2);
        TransformationsDto transformationDto = TransformationsDto.builder()
                .workStationPublicId(workStationPublicId)
                .transformations(transformationsDtos)
                .build();
        UUID productPublicId = UUID.randomUUID();
        StationTransformation transformation1 = buildTransformation(1F, TransformationType.INPUT, false,
                buildProduct(productPublicId, ProductType.RAW, 0F, 0F, "  "));

        WorkStation workStation = buildWorkStation("op2", "m2", false, "st2", Collections.singletonList(transformation1));

        Mockito.when(workStationRepository.findWorkStationByUuid(workStationPublicId)).thenReturn(Optional.of(workStation));
        Mockito.when(workStationRepository.saveWorkStation(workStationArgumentCaptor.capture())).thenReturn(workStation);
        Map<UUID, Product> productsMap = new HashMap<>();
        productsMap.put(product1PublicId, buildProduct(product1PublicId, ProductType.RAW, 5F, 2F, "pro1"));
        productsMap.put(product2PublicId, buildProduct(product2PublicId, ProductType.RAW, 9F, 3F, "pro2"));

        Mockito.when(productSearch.findByUuids(anySet())).thenReturn(productsMap);
        workStationService.setTransformations(transformationDto);

        Assertions.assertNotNull(workStationArgumentCaptor.getValue());
        Assertions.assertFalse(workStationArgumentCaptor.getValue().getClosed());
        Assertions.assertEquals(2, workStationArgumentCaptor.getValue().getTransformations().size());
        Assertions.assertTrue(workStationArgumentCaptor.getValue().getTransformations().stream()
                .map(StationTransformation::getProduct)
                .map(Product::getPublicId)
                .toList()
                .containsAll(Arrays.asList(product1PublicId, product2PublicId)));
    }

    @Test
    void shouldCloseWorkStation() {

        UUID workStationPublicId = UUID.randomUUID();
        UUID product1PublicId = UUID.randomUUID();
        UUID product2PublicId = UUID.randomUUID();
        UUID product3PublicId = UUID.randomUUID();

        StationTransformation transformation1 = buildTransformation(6.3657F, TransformationType.INPUT, false,
                buildProduct(product1PublicId, ProductType.RAW, 0F, 118.41F, "prod1"));

        StationTransformation transformation2 = buildTransformation(2.22F, TransformationType.INPUT, false,
                buildProduct(product2PublicId, ProductType.RAW, 0F, 3.1F, "prod2"));

        Product product3 = buildProduct(product3PublicId, ProductType.FINAL, 0F, 1.90F, "prod3");
        StationTransformation transformation3 = buildTransformation(1.39F, TransformationType.OUTPUT, false, product3);

        StationTransformation transformation4 = buildTransformation(1F, TransformationType.OUTPUT, true, product3);

        StationTransformation transformation5 = buildTransformation(1F, TransformationType.INPUT, false, product3);

        List<StationTransformation> transformationsArray = new ArrayList<>();
        transformationsArray.add(transformation1);
        transformationsArray.add(transformation2);
        transformationsArray.add(transformation3);
        transformationsArray.add(transformation4);
        transformationsArray.add(transformation5);
        WorkStation workStation = buildWorkStation("op2", "m2", false, "st2", transformationsArray);

        Mockito.when(workStationRepository.findWorkStationByUuid(workStationPublicId)).thenReturn(Optional.of(workStation));
        Mockito.when(workStationRepository.saveWorkStation(workStationArgumentCaptor.capture())).thenReturn(workStation);

        workStationService.closeWorkStation(workStationPublicId);

        Assertions.assertNotNull(workStationArgumentCaptor.getValue());
        Assertions.assertTrue(workStationArgumentCaptor.getValue().getClosed());
        Assertions.assertEquals(5, workStationArgumentCaptor.getValue().getTransformations().size());

        Product product1 = workStationArgumentCaptor.getValue().getTransformations().stream()
                .map(StationTransformation::getProduct)
                .filter(product -> product.getPublicId().equals(product1PublicId))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(product1);
        Assertions.assertEquals(BigDecimal.valueOf(112.044d), product1.getQuantity());

        Product product2 = workStationArgumentCaptor.getValue().getTransformations().stream()
                .map(StationTransformation::getProduct)
                .filter(product -> product.getPublicId().equals(product2PublicId))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(product2);
        Assertions.assertEquals(BigDecimal.valueOf(0.88d).setScale(3, RoundingMode.HALF_DOWN), product2.getQuantity());

        Product product3Saved = workStationArgumentCaptor.getValue().getTransformations().stream()
                .map(StationTransformation::getProduct)
                .filter(product -> product.getPublicId().equals(product3PublicId))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(product3Saved);
        Assertions.assertEquals(BigDecimal.valueOf(2.29d).setScale(3, RoundingMode.HALF_DOWN), product3Saved.getQuantity());


    }

    private WorkStation buildWorkStation(String operator, String machine, boolean closed, String name,
                                         List<StationTransformation> transformations) {
        return WorkStation.builder()
                .duration(1)
                .id(System.currentTimeMillis())
                .date(LocalDate.now())
                .operator(operator)
                .machine(machine)
                .closed(closed)
                .name(name)
                .transformations(transformations)
                .build();
    }

    private StationTransformation buildTransformation(float quantity, TransformationType type, boolean deleted, Product product) {
        return StationTransformation.builder()
                .quantity(BigDecimal.valueOf(quantity).setScale(3, RoundingMode.HALF_DOWN))
                .type(type)
                .deleted(deleted)
                .product(product)
                .build();
    }

    private Product buildProduct(UUID publicId, ProductType type, float initalValue, float quantity, String name) {
        return Product.builder()
                .quantity(BigDecimal.valueOf(quantity))
                .type(type)
                .deleted(false)
                .name(name)
                .publicId(publicId)
                .unit(Unit.KG)
                .initialValue(BigDecimal.valueOf(initalValue))
                .build();
    }

}
