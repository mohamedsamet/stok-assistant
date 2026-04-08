package com.yesmind.stok.adapter.rest.client.search;

import com.yesmind.stok.core.domain.data.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IClientSearchController {

    ResponseEntity<ClientResponse> searchClients(SearchDto searchDto);
    ResponseEntity<ClientDto> searchClientByUuid(UUID searchtUuid);
}
