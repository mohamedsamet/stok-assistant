package com.yesmind.stok.core.business.mapper;

import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.entity.Client;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClientMapper {

    private ClientMapper() {}

    public static Client toClient(ClientDto clientDto) {
        return Client.builder()
                .name(clientDto.getName())
                .address(clientDto.getAddress())
                .email(clientDto.getEmail())
                .deleted(false)
                .fiscalId(clientDto.getFiscalId())
                .tel(clientDto.getTel())
                .creationStatus(clientDto.getCreationStatus())
                .build();
    }

    public static ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .name(client.getName())
                .publicId(client.getPublicId())
                .reference(client.getReference())
                .address(client.getAddress())
                .email(client.getEmail())
                .creationDate(client.getCreationDate())
                .fiscalId(client.getFiscalId())
                .tel(client.getTel())
                .build();
    }

    public static Client toClientByReference(String reference) {
        return Client.builder()
                .name("")
                .creationDate(LocalDateTime.now())
                .reference(reference)
                .deleted(false)
                .creationStatus(CreationStatus.DRAFT)
                .build();
    }
}
