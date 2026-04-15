package com.yesmind.stok.core.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkStationResponse {

    private List<WorkStationDto> stations;
    private long count;
}
