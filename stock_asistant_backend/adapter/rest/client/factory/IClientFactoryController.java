package com.yesmind.stok.adapter.rest.client.factory;

import com.yesmind.stok.core.domain.data.ClientDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IClientFactoryController {

    void removeClient(UUID productPublicId);

    ResponseEntity<ClientDto> createClient(ClientDto productDto);

    ResponseEntity<ClientDto> createDraftClient();

    ResponseEntity<ClientDto> updateClient(ClientDto productDto);
}