package com.cineverse.repository;

import com.cineverse.entity.Movie;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class MovieRepository {

    public Movie save(Movie movie) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Movie mergedMovie = em.merge(movie);
            transaction.commit();
            return mergedMovie;
        } finally {
            em.close();
        }
    }

    public Optional<Movie> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(Movie.class, id));
        } finally {
            em.close();
        }
    }

    public List<Movie> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                em.remove(movie);
            }
            transaction.commit();
        } finally {
            em.close();
        }
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
