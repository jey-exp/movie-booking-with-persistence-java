package com.cineverse.repository;

import com.cineverse.entity.ShowTime;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class ShowTimeRepository {

    public ShowTime save(ShowTime showTime) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ShowTime mergedShowTime = em.merge(showTime);
            transaction.commit();
            return mergedShowTime;
        } finally {
            em.close();
        }
    }

    public Optional<ShowTime> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(ShowTime.class, id));
        } finally {
            em.close();
        }
    }

    public List<ShowTime> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM ShowTime s", ShowTime.class).getResultList();
        } finally {
            em.close();
        }
    }
}
