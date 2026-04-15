package com.yesmind.stok.adapter.rest.client.search;

import com.yesmind.stok.core.domain.data.*;
import com.yesmind.stok.core.domain.entity.Client;
import com.yesmind.stok.core.port.in.client.IClientSearch;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClientSearchController implements IClientSearchController {

    private final IClientSearch clientSearch;

    @Override
    @PostMapping(value = "/client/search")
    public ResponseEntity<ClientResponse> searchClients(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(clientSearch.search(searchDto));
    }

    @Override
    @GetMapping("/client/search/{uuid}")
    public ResponseEntity<ClientDto> searchClientByUuid(@PathVariable("uuid") UUID clientUuid) {
        return ResponseEntity.ok(clientSearch.findByUuid(clientUuid));
    }
}
