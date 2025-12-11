package com.restaurant.services.Impl;

import com.restaurant.entity.Complement;
import com.restaurant.repository.ComplementRepository;
import com.restaurant.repository.Impl.ComplementRepositoryImpl;
import com.restaurant.services.ComplementService;
import java.util.List;

public class ComplementServiceImpl implements ComplementService {
    
    private ComplementRepository complementRepository = new ComplementRepositoryImpl();
    
    @Override
    public Complement ajouterComplement(Complement complement) {
        return complementRepository.save(complement);
    }
    
    @Override
    public Complement modifierComplement(Complement complement) {
        return complementRepository.update(complement);
    }
    
    @Override
    public boolean archiverComplement(int id) {
        return complementRepository.archive(id);
    }
    
    @Override
    public Complement getComplementById(int id) {
        return complementRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Complement> getAllComplements() {
        return complementRepository.findAll();
    }
    
    @Override
    public List<Complement> getComplementsActifs() {
        return complementRepository.findAllActive();
    }
    
    @Override
    public List<Complement> getComplementsParType(String type) {
        return complementRepository.findByType(type);
    }
    
    @Override
    public List<Complement> getFrites() {
        return complementRepository.findFrites();
    }
    
    @Override
    public List<Complement> getBoissons() {
        return complementRepository.findBoissons();
    }
}
