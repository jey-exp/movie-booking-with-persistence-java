package com.cineverse.repository;

import com.cineverse.entity.Booking;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class BookingRepository {

    public Booking save(Booking booking) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Booking mergedBooking = em.merge(booking);
            transaction.commit();
            return mergedBooking;
        } finally {
            em.close();
        }
    }

    public Optional<Booking> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(Booking.class, id));
        } finally {
            em.close();
        }
    }

    public List<Booking> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Booking booking) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(booking) ? booking : em.merge(booking));
            transaction.commit();
        } finally {
            em.close();
        }
    }
}
