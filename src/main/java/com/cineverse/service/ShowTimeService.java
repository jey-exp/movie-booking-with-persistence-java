package com.cineverse.service;

import com.cineverse.entity.Movie;
import com.cineverse.entity.ShowTime;
import com.cineverse.exception.InvalidShowTimeException;
import com.cineverse.exception.MovieNotFoundException;
import com.cineverse.repository.MovieRepository;
import com.cineverse.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowTimeService {

    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ShowTime addShowTime(Long movieId, LocalDateTime showTime, int availableSeats) {
        if (showTime.isBefore(LocalDateTime.now())) {
            throw new InvalidShowTimeException("Show time cannot be in the past.");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        ShowTime newShowTime = new ShowTime();
        newShowTime.setMovie(movie);
        newShowTime.setShowTime(showTime);
        newShowTime.setAvailableSeats(availableSeats);

        return showTimeRepository.save(newShowTime);
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
