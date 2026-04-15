package com.yesmind.stok.core.port.in.client;

import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.ClientResponse;
import com.yesmind.stok.core.domain.data.SearchDto;

import java.util.UUID;

public interface IClientSearch {

    ClientDto findByUuid(UUID clientUuid);
    ClientResponse search(SearchDto searchDto);
}
