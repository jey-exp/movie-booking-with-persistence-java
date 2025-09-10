package com.cineverse.service;

import com.cineverse.entity.Booking;
import com.cineverse.entity.ShowTime;
import com.cineverse.entity.User;
import com.cineverse.exception.SeatsUnavailableException;
import com.cineverse.repository.BookingRepository;
import com.cineverse.repository.ShowTimeRepository;

public class TicketBookingService implements IBookable {

    private final BookingRepository bookingRepository;
    private final ShowTimeRepository showTimeRepository;

    public TicketBookingService(BookingRepository bookingRepository, ShowTimeRepository showTimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showTimeRepository = showTimeRepository;
    }

    @Override
    public Booking bookTickets(User user, ShowTime showTime, Integer seatCount) {
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
}
