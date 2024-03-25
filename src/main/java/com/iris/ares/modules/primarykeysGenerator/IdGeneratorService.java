package com.iris.ares.modules.primarykeysGenerator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class IdGeneratorService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public String generateUniqueId(String prefix, String sequenceName) {
        prefix = prefix.toUpperCase();
        if (prefix.length() != 3) {
            throw new IllegalArgumentException("Le préfixe doit contenir exactement 3 lettres.");
        }
        String sequence = entityManager.createNativeQuery("SELECT nextval('" + sequenceName + "')").getSingleResult().toString();
        String formattedSequence = String.format("%05d", Integer.parseInt(sequence));
        return prefix + formattedSequence;
    }
}
