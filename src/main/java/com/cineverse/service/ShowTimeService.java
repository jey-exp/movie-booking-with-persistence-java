package com.cineverse.service;

import com.cineverse.entity.Movie;
import com.cineverse.entity.ShowTime;
import com.cineverse.exception.InvalidShowTimeException;
import com.cineverse.exception.MovieNotFoundException;
import com.cineverse.repository.MovieRepository;
import com.cineverse.repository.ShowTimeRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;

    public ShowTimeService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
    }

    public ShowTime addShowTime(ShowTime showTime) {
        if (showTime.getShowTime().isBefore(LocalDateTime.now())) {
            throw new InvalidShowTimeException("Show time cannot be in the past.");
        }
        return showTimeRepository.save(showTime);
    }

    public List<ShowTime> getShowTimesForMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie not found with id: " + movieId);
        }
        return showTimeRepository.findAll().stream()
                .filter(st -> st.getMovie().getId().equals(movieId))
                .toList();
    }
}
