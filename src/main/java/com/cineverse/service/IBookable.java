package com.cineverse.service;

import com.cineverse.entity.Booking;
import com.cineverse.entity.ShowTime;
import com.cineverse.entity.User;

public interface IBookable {

    Booking bookTickets(User user, ShowTime showTime, Integer seatCount);
}
