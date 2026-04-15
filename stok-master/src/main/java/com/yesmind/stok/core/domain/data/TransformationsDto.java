package com.yesmind.stok.core.domain.data;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransformationsDto {

    @NonNull
    private UUID workStationPublicId;

    List<TransformationDto> transformations;
}
