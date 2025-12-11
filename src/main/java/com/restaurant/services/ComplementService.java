package com.restaurant.services;

import com.restaurant.entity.Complement;
import java.util.List;

public interface ComplementService {
    Complement ajouterComplement(Complement complement);
    Complement modifierComplement(Complement complement);
    boolean archiverComplement(int id);
    Complement getComplementById(int id);
    List<Complement> getAllComplements();
    List<Complement> getComplementsActifs();
    List<Complement> getComplementsParType(String type);
    List<Complement> getFrites();
    List<Complement> getBoissons();
}
