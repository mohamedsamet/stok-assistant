package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.ProductType;
import com.yesmind.stok.core.domain.entity.Unit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private String name;
    private String email;
    private String address;
    private UUID publicId;
    private String reference;
    private String fiscalId;
    private String tel;
    private LocalDateTime creationDate;
    @Setter
    private CreationStatus creationStatus;
}
