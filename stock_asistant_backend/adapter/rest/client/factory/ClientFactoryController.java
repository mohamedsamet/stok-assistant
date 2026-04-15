package com.yesmind.stok.adapter.rest.client.factory;

import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.port.in.client.IClientFactory;
import com.yesmind.stok.core.port.in.product.IProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClientFactoryController implements IClientFactoryController {

    private final IClientFactory clientFactory;

    @Override
    @DeleteMapping("/client/{publicId}")
    public void removeClient(@PathVariable("publicId") UUID clientPublicId) {
        clientFactory.removeClient(clientPublicId);
    }

    @Override
    @PostMapping("/client")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(clientFactory.buildClient(clientDto));
    }

    @Override
    @PostMapping("/client/draft")
    public ResponseEntity<ClientDto> createDraftClient() {
        return ResponseEntity.ok(clientFactory.buildDraftClient());
    }

    @Override
    @PutMapping("/client")
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(clientFactory.updateClient(clientDto));
    }
}
