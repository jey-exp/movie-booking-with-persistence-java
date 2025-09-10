package com.cineverse.service;

import com.cineverse.entity.Booking;
import com.cineverse.entity.ShowTime;
import com.cineverse.entity.User;
import com.cineverse.exception.BookingNotFoundException;
import com.cineverse.exception.SeatsUnavailableException;
import com.cineverse.repository.BookingRepository;
import com.cineverse.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowTimeRepository showTimeRepository;

    public Booking createBooking(User user, Long showTimeId, int seatCount) {
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("ShowTime not found"));

        if (showTime.getAvailableSeats() < seatCount) {
            throw new SeatsUnavailableException("Not enough seats available.");
        }

        showTime.setAvailableSeats(showTime.getAvailableSeats() - seatCount);
        showTimeRepository.save(showTime);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMovie(showTime.getMovie());
        booking.setShowTime(showTime);
        booking.setSeatCount(seatCount);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForUser(User user) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(user.getId()))
                .toList();
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        ShowTime showTime = booking.getShowTime();
        showTime.setAvailableSeats(showTime.getAvailableSeats() + booking.getSeatCount());
        showTimeRepository.save(showTime);

        bookingRepository.delete(booking);
    }
}
