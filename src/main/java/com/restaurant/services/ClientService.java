package com.restaurant.services;

import com.restaurant.entity.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client inscrireClient(Client client);
    Client modifierClient(Client client);
    boolean archiverClient(int id);
    Optional<Client> getClientById(int id);
    Optional<Client> getClientByEmail(String email);
    Optional<Client> getClientByTelephone(String telephone);
    List<Client> getAllClients();
    List<Client> getClientsActifs();
    boolean authentifierClient(String email, String motDePasse);
    List<Client> rechercherClients(String nom);
}
