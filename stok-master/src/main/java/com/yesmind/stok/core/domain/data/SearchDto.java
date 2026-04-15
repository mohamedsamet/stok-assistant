package com.yesmind.stok.core.domain.data;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    @Setter
    private String name;
    private UUID clientPublicId;
    private long page;
    private int pageSize;

}
