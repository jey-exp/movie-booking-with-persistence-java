package com.cineverse.repository;

import com.cineverse.entity.Review;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ReviewRepository {

    public Review save(Review review) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Review mergedReview = em.merge(review);
            transaction.commit();
            return mergedReview;
        } finally {
            em.close();
        }
    }

    public List<Review> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Review r", Review.class).getResultList();
        } finally {
            em.close();
        }
    }
}
