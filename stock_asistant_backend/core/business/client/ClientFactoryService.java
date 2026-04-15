package com.yesmind.stok.core.business.client;

import com.yesmind.stok.application.exception.ConflictException;
import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.ReferenceGenerationUtils;
import com.yesmind.stok.core.business.mapper.ClientMapper;
import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.ReferenceTools;
import com.yesmind.stok.core.domain.entity.Client;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.port.in.client.IClientFactory;
import com.yesmind.stok.core.port.out.IClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientFactoryService implements IClientFactory {

    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public ClientDto buildClient(ClientDto clientDto) {
        Optional<Client> client = clientRepository.getClientByName(clientDto.getName());

        if (client.isPresent()) {
            throw new ConflictException(String.format("Client with name %s already exists", clientDto.getName()));
        }

        Client clientToBuild = clientRepository.getClientByPublicId(clientDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Client Not found by public id"));

        clientToBuild.setCreationStatus(CreationStatus.COMPLETED);
        clientToBuild.setName(clientDto.getName());
        clientToBuild.setAddress(clientDto.getAddress());
        clientToBuild.setFiscalId(clientDto.getFiscalId());
        clientToBuild.setEmail(clientDto.getEmail());
        clientToBuild.setTel(clientDto.getTel());

        Client savedClient = clientRepository.saveClient(clientToBuild);
        return ClientMapper.toClientDto(savedClient);

    }

    @Transactional
    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        Client client = clientRepository.getClientByPublicId(clientDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Client with public id %s does not exist", clientDto.getPublicId())));

        if (!client.getName().equals(clientDto.getName())) {
            clientRepository.getClientByName(clientDto.getName())
                    .ifPresent(clientWithSameName -> {throw new ConflictException("Client with same name already exist");});
        }
        client.setName(clientDto.getName());
        client.setAddress(clientDto.getAddress());
        client.setEmail(clientDto.getEmail());
        client.setFiscalId(clientDto.getFiscalId());
        client.setTel(clientDto.getTel());
        Client clientSaved = clientRepository.saveClient(client);
        return ClientMapper.toClientDto(clientSaved);

    }

    @Override
    @Transactional
    public void removeClient(UUID clientPublicId) {
        Optional<Client> client = clientRepository.getClientByPublicId(clientPublicId);

        client.ifPresentOrElse(
                this::removeClient,
                () -> {
                    throw new ResourceNotFoundException(
                            String.format("Client with publicId %s does not exists", clientPublicId));
                });

    }

    @Transactional
    @Override
    public ClientDto buildDraftClient() {
        Optional<Client> lastClientEntry = clientRepository.findLastClient();

        String reference = lastClientEntry
                .map(client -> ReferenceGenerationUtils.generateReference(client.getReference(), ReferenceTools.CLIENT))
                .orElseGet(() -> ReferenceGenerationUtils.generateFirstReference(ReferenceTools.CLIENT));

        Client savedClient = clientRepository.saveClient(ClientMapper.toClientByReference(reference));
        return ClientMapper.toClientDto(savedClient);
    }

    private void removeClient(Client client) {
        client.setDeleted(true);
        clientRepository.saveClient(client);
    }
}
