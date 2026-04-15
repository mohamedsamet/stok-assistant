package com.yesmind.stok.core.business.client;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.mapper.ClientMapper;
import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.ClientResponse;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Client;
import com.yesmind.stok.core.port.in.client.IClientSearch;
import com.yesmind.stok.core.port.out.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientSearchService implements IClientSearch {

    private final IClientRepository clientRepository;

    @Override
    public ClientDto findByUuid(UUID clientUuid) {
        Optional<Client> client = clientRepository.getClientByPublicId(clientUuid);

        if (client.isPresent()) {
            return ClientMapper.toClientDto(client.get());
        }

        throw new ResourceNotFoundException(String.format("Client with UUID %s Not Found", clientUuid));
    }

    @Override
    public ClientResponse search(SearchDto searchDto) {
        if (searchDto.getName() != null) {
            searchDto.setName("%" + searchDto.getName() + "%");
        }
        List<ClientDto> clients = clientRepository.searchClients(searchDto)
                .stream()
                .map(ClientMapper::toClientDto)
                .toList();

        return ClientResponse.builder()
                .clients(clients)
                .count(clientRepository.count(searchDto))
                .build();

    }

}
