package com.cineverse;

import com.cineverse.entity.*;
import com.cineverse.repository.UserRepository;
import com.cineverse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class CineverseApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(CineverseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to CineVerse!");
            System.out.println("The default admin credentials are: email - admin@cineverse.com, password - admin123");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createDefaultAdmin() {
        if (userRepository.findByEmail("admin@cineverse.com").isEmpty()) {
            Admin admin = new Admin();
            admin.setName("Admin");
            admin.setEmail("admin@cineverse.com");
            admin.setPassword("admin123");
            userRepository.save(admin);
        }
    }

    private void register(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            userService.registerUser(name, email, password);
            System.out.println("Registration successful!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.loginUser(email, password);
            System.out.println("Login successful! Welcome, " + user.getName());
            if (user instanceof Admin) {
                showAdminMenu(scanner);
            } else {
                showUserMenu(scanner, user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Movie");
            System.out.println("2. Update Movie");
            System.out.println("3. Remove Movie");
            System.out.println("4. View All Movies");
            System.out.println("5. Manage Showtimes");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addMovie(scanner);
                    break;
                case 2:
                    updateMovie(scanner);
                    break;
                case 3:
                    removeMovie(scanner);
                    break;
                case 4:
                    viewAllMovies();
                    break;
                case 5:
                    manageShowtimes(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showUserMenu(Scanner scanner, User user) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View All Movies and Showtimes");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Add a Review");
            System.out.println("4. View My Bookings");
            System.out.println("5. Cancel a Booking");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    bookTicket(scanner, user);
                    break;
                case 3:
                    addReview(scanner, user);
                    break;
                case 4:
                    viewMyBookings(user);
                    break;
                case 5:
                    cancelBooking(scanner, user);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addMovie(Scanner scanner) {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter duration (in minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter release date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate releaseDate = LocalDate.parse(dateStr);

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setDuration(duration);
        movie.setReleaseDate(releaseDate);

        movieService.addMovie(movie);
        System.out.println("Movie added successfully!");
    }

    private void updateMovie(Scanner scanner) {
        System.out.print("Enter movie ID to update: ");
        long movieId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter new duration: ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new release date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate releaseDate = LocalDate.parse(dateStr);

        Movie movieDetails = new Movie();
        movieDetails.setTitle(title);
        movieDetails.setGenre(genre);
        movieDetails.setDuration(duration);
        movieDetails.setReleaseDate(releaseDate);

        try {
            movieService.updateMovie(movieId, movieDetails);
            System.out.println("Movie updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeMovie(Scanner scanner) {
        System.out.print("Enter movie ID to remove: ");
        long movieId = scanner.nextLong();
        scanner.nextLine();

        try {
            movieService.removeMovie(movieId);
            System.out.println("Movie removed successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        System.out.println("\n--- All Movies ---");
        for (Movie movie : movies) {
            System.out.println("\nMovie ID: " + movie.getId() + ", Title: " + movie.getTitle() + " (" + movie.getGenre() + ")");
            System.out.println("  Showtimes:");
            List<ShowTime> showTimes = showTimeService.getShowTimesForMovie(movie.getId());
            if (showTimes.isEmpty()) {
                System.out.println("    No showtimes available.");
            } else {
                for (ShowTime st : showTimes) {
                    System.out.println("    - ID: " + st.getId() + ", Time: " + st.getShowTime() + ", Seats: " + st.getAvailableSeats());
                }
            }
        }
    }

    private void manageShowtimes(Scanner scanner) {
        System.out.println("\nManage Showtimes:");
        System.out.println("1. Add Showtime");
        System.out.println("2. View Showtimes for a Movie");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addShowtime(scanner);
                break;
            case 2:
                viewShowtimesForMovie(scanner);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void addShowtime(Scanner scanner) {
        System.out.print("Enter movie ID: ");
        long movieId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter show time (YYYY-MM-DDTHH:MM): ");
        String timeStr = scanner.nextLine();
        LocalDateTime showTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.print("Enter available seats: ");
        int seats = scanner.nextInt();
        scanner.nextLine();

        try {
            showTimeService.addShowTime(movieId, showTime, seats);
            System.out.println("Showtime added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewShowtimesForMovie(Scanner scanner) {
        System.out.print("Enter movie ID: ");
        long movieId = scanner.nextLong();
        scanner.nextLine();

        try {
            List<ShowTime> showTimes = showTimeService.getShowTimesForMovie(movieId);
            System.out.println("\nShowtimes for Movie ID " + movieId + ":");
            for (ShowTime st : showTimes) {
                System.out.println("  - ID: " + st.getId() + ", Time: " + st.getShowTime() + ", Seats: " + st.getAvailableSeats());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bookTicket(Scanner scanner, User user) {
        System.out.print("Enter showtime ID to book: ");
        long showTimeId = scanner.nextLong();
        System.out.print("Enter number of seats: ");
        int seatCount = scanner.nextInt();
        scanner.nextLine();

        try {
            bookingService.createBooking(user, showTimeId, seatCount);
            System.out.println("Booking successful!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addReview(Scanner scanner, User user) {
        System.out.print("Enter movie ID to review: ");
        long movieId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();
        System.out.print("Enter your rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        try {
            reviewService.addReview(user, movieId, comment, rating);
            System.out.println("Review added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyBookings(User user) {
        List<Booking> bookings = bookingService.getBookingsForUser(user);
        System.out.println("\nMy Bookings:");
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getId() + ", Movie: " + booking.getMovie().getTitle() + ", Seats: " + booking.getSeatCount());
        }
    }

    private void cancelBooking(Scanner scanner, User user) {
        System.out.print("Enter booking ID to cancel: ");
        long bookingId = scanner.nextLong();
        scanner.nextLine();

        try {
            bookingService.cancelBooking(bookingId);
            System.out.println("Booking canceled successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
