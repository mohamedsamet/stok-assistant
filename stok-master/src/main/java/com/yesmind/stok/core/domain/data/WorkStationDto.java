package com.yesmind.stok.core.domain.data;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkStationDto {

    private UUID publicId;

    private String machine;

    @Setter
    private String name;
    private String reference;
    private String operator;
    private LocalDate date;
    private Integer duration;
    private Boolean closed;
    private Boolean deleted;

    private List<TransformationDto> transformations;

}
