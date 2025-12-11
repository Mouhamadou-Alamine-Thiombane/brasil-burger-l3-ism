package com.restaurant.services.Impl;

import com.restaurant.entity.Client;
import com.restaurant.repository.ClientRepository;
import com.restaurant.repository.Impl.ClientRepositoryImpl;
import com.restaurant.services.ClientService;
import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    
    private ClientRepository clientRepository = new ClientRepositoryImpl();
    
    @Override
    public Client inscrireClient(Client client) {
        return clientRepository.save(client);
    }
    
    @Override
    public Client modifierClient(Client client) {
        return clientRepository.update(client);
    }
    
    @Override
    public boolean archiverClient(int id) {
        return clientRepository.archive(id);
    }
    
    @Override
    public Optional<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }
    
    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    
    @Override
    public Optional<Client> getClientByTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone);
    }
    
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    @Override
    public List<Client> getClientsActifs() {
        return clientRepository.findAllActive();
    }
    
    @Override
    public boolean authentifierClient(String email, String motDePasse) {
        return clientRepository.authenticate(email, motDePasse);
    }
    
    @Override
    public List<Client> rechercherClients(String nom) {
        return clientRepository.searchByNom(nom);
    }
}
