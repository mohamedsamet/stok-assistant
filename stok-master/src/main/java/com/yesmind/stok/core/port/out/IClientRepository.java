package com.yesmind.stok.core.port.out;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientRepository {

    Client saveClient(Client client);
    Optional<Client> getClientByName(String name);
    Optional<Client> getClientByPublicId(UUID publicId);
    List<Client> searchClients(SearchDto searchDto);
    Long count(SearchDto searchDto);
    Optional<Client> findLastClient();

}
