package com.restaurant.repository;



import com.restaurant.entity.Client;
import java.util.Optional;
import java.util.List;

public interface ClientRepository extends CrudRepository<Client> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByTelephone(String telephone);
    boolean authenticate(String email, String password);
    List<Client> searchByNom(String nom);
}
