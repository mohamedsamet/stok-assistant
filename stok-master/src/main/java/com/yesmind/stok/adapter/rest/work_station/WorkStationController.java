package com.yesmind.stok.adapter.rest.work_station;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.data.TransformationsDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;
import com.yesmind.stok.core.domain.data.WorkStationResponse;
import com.yesmind.stok.core.port.in.work_station.IWorkStationFactory;
import com.yesmind.stok.core.port.in.work_station.IWorkStationSearch;
import com.yesmind.stok.core.port.in.work_station.IWorkStationSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WorkStationController implements IWorkStationCrudController, IWorkStationSettingsController {

    private final IWorkStationFactory workStationService;
    private final IWorkStationSetting workStationSettings;
    private final IWorkStationSearch workStationSearch;


    @Override
    @PostMapping("/station")
    public ResponseEntity<WorkStationDto> buildWorkStation(@RequestBody WorkStationDto workStationDto) {
        return ResponseEntity.ok(workStationService.buildWorkStation(workStationDto));
    }

    @Override
    @PostMapping("/station/draft")
    public ResponseEntity<WorkStationDto> buildWorkStation() {
        return ResponseEntity.ok(workStationService.buildDraftWorkStation());
    }

    @Override
    @DeleteMapping("/station/{publicId}")
    public void removeProduct(@PathVariable("publicId") UUID productPublicId) {
        workStationService.deleteWorkStation(productPublicId);
    }

    @Override
    @PostMapping("/station/search")
    public ResponseEntity<WorkStationResponse> findWorkStations(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(workStationSearch.searchWorkStations(searchDto));
    }

    @Override
    @PutMapping("/station/{publicId}")
    public ResponseEntity<WorkStationDto> updateWorkStation(@PathVariable("publicId") UUID publicId,
                                                            @RequestBody WorkStationDto workStationDto) {
        return ResponseEntity.ok(workStationService.updateWorkStation(publicId, workStationDto));
    }

    @Override
    @GetMapping("/station/{uuid}")
    public ResponseEntity<WorkStationDto> findWorkStation(@PathVariable("uuid") UUID stationUuid) {
        return ResponseEntity.ok(workStationSearch.findWorkStation(stationUuid));
    }


    @Override
    @PostMapping("/station/transformation")
    public ResponseEntity<WorkStationDto> addTransformations(@RequestBody TransformationsDto transformationDto) {
        return ResponseEntity.ok(workStationSettings.setTransformations(transformationDto));
    }

    @Override
    @PutMapping(value= "/station/close/{stationPublicId}")
    public ResponseEntity<WorkStationDto> closeWorkStation(@PathVariable("stationPublicId") UUID workStationUuid) {
        return ResponseEntity.ok(workStationSettings.closeWorkStation(workStationUuid));
    }
}
