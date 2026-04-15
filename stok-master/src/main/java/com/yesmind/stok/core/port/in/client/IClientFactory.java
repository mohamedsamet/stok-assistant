package com.yesmind.stok.core.port.in.client;

import com.yesmind.stok.core.domain.data.ClientDto;

import java.util.UUID;

public interface IClientFactory {

    ClientDto buildClient(ClientDto clientDto);
    ClientDto updateClient(ClientDto clientDto);
    void removeClient(UUID clientPublicId);
    ClientDto buildDraftClient();
}
