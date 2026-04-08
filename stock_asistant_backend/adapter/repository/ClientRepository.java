package com.yesmind.stok.adapter.repository;

import com.yesmind.stok.adapter.repository.jpa.ClientJpaRepository;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Client;
import com.yesmind.stok.core.port.out.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ClientRepository implements IClientRepository {

    private final ClientJpaRepository clientJpaRepository;

    @Override
    public Client saveClient(Client client) {
        return clientJpaRepository.save(client);
    }

    @Override
    public Optional<Client> getClientByName(String name) {
        return clientJpaRepository.findByNameAndDeletedIsFalseOrDeletedIsNull(name);
    }

    @Override
    public Optional<Client> getClientByPublicId(UUID publicId) {
        return clientJpaRepository.findByPublicId(publicId);
    }

    @Override
    public List<Client> searchClients(SearchDto searchDto) {
        long limit = searchDto.getPageSize() != 0 ? searchDto.getPageSize() : 10;
        BigInteger offset = BigInteger.valueOf(searchDto.getPage() * limit);

        return clientJpaRepository.search(searchDto.getName(), offset, limit);
    }

    @Override
    public Long count(SearchDto searchDto) {
        return clientJpaRepository.countAll(searchDto.getName());
    }

    @Override
    public Optional<Client> findLastClient() {
        return clientJpaRepository.findLastClientEntry().stream().max(
                Comparator.comparingLong(Client::getRefIncrement)
        );
    }
}
