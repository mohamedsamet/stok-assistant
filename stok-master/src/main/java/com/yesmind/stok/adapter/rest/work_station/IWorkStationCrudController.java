package com.yesmind.stok.adapter.rest.work_station;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;
import com.yesmind.stok.core.domain.data.WorkStationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IWorkStationCrudController {

    ResponseEntity<WorkStationDto> buildWorkStation(WorkStationDto workStationDto);
    ResponseEntity<WorkStationDto> buildWorkStation();
    ResponseEntity<WorkStationResponse> findWorkStations(SearchDto searchDto);
    ResponseEntity<WorkStationDto> updateWorkStation(UUID publicId, WorkStationDto workStationDto);
    ResponseEntity<WorkStationDto> findWorkStation(UUID uuid);
    void removeProduct(UUID productPublicId);
}
