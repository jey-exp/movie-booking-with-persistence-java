package com.cineverse.service;

import com.cineverse.entity.Movie;
import com.cineverse.entity.Review;
import com.cineverse.entity.User;
import com.cineverse.exception.DuplicateReviewException;
import com.cineverse.exception.InvalidRatingException;
import com.cineverse.repository.MovieRepository;
import com.cineverse.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Review addReview(User user, Long movieId, String comment, int rating) {
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException("Rating must be between 1 and 5.");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check for duplicate review
        Optional<Review> existingReview = reviewRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(user.getId()) && r.getMovie().getId().equals(movieId))
                .findFirst();

        if (existingReview.isPresent()) {
            throw new DuplicateReviewException("You have already reviewed this movie.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setComment(comment);
        review.setRating(rating);

        return reviewRepository.save(review);
    }
}
