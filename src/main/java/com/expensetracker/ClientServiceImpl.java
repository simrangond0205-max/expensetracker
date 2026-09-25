package com.SpringBootMVC.ExpensesTracker.service;

import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.SpringBootMVC.ExpensesTracker.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {

    ClientRepository clientRepository;
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(Objects.requireNonNull(client, "client must not be null"));
    }

    @Override
    public Client findClientById(int id) {
        return clientRepository.findById(id).orElse(null);
    }
}
