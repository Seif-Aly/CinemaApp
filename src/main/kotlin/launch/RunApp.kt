package launch

import repositories.MovieRepository
import repositories.SessionRepository
import repositories.TicketRepository
import services.CinemaService
import services.UserService
import utils.FileUtilWithCSV
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RunApp {
    fun authentication(){
        val userService = UserService()

        // Import existing users from CSV
        userService.importUsersFromCSV("users.csv")

        while (true) {
            println("1. Login")
            println("2. Register")
            println("3. Exit program")

            print("Enter your choice: ")

            when (readLine()?.toIntOrNull()) {
                1 -> {
                    // User Login
                    print("Enter username: ")
                    val authUsername = readLine().toString()
                    print("Enter password: ")
                    val authPassword = readLine().toString()

                    if (userService.authenticateUser(authUsername, authPassword)) {
                        println("Authentication successful. Access granted.\n")
                        break
                    } else {
                        println("Authentication failed. Please try again.\n")
                    }
                }
                2 -> {
                    // User Registration
                    print("Enter new username: ")
                    val username = readLine().toString()
                    print("Enter new password: ")
                    val password = readLine().toString()

                    userService.registerUser(username, password)
                    // Save registered user to CSV
                    userService.exportUsersToCSV("users.csv")

                    println("User registered successfully. Please login.\n")
                }
                3 -> {
                    println("Exiting the program")
                    return
                }
                else -> println("Invalid choice. Please enter a valid option.\n")
            }
        }
    }
    fun mainMenu() {
        val movieRepository = MovieRepository()
        val sessionRepository = SessionRepository()
        val ticketRepository = TicketRepository()
        val fileUtil = FileUtilWithCSV()
        val cinemaService = CinemaService(movieRepository, sessionRepository, ticketRepository,fileUtil)

        cinemaService.importDataFromCSV("movies.csv", "sessions.csv", "tickets.csv")

        while (true) {
            println("1. Browse Movies")
            println("2. Edit Movie or Session")
            println("3. Exit")

            print("\nEnter your choice: ")
            when (readLine()?.toIntOrNull()) {
                1 -> {
                    browseMovies(cinemaService)
                }
                2 -> {
                    editMovieOrSession(cinemaService)
                }
                3 -> {
                    // Export data to CSV
                    cinemaService.exportDataToCSV("movies.csv",
                        "sessions.csv", "tickets.csv")
                    println("Data updated to files. \n Exiting the application.")
                    return
                }
                else -> println("Invalid choice. Please enter a valid option.")
            }
        }
    }

    private fun editMovieOrSession(cinemaService: CinemaService) {
        println("\n1. Edit Movie")
        println("2. Edit Session")
        print("\nEnter your choice: ")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                editMovie(cinemaService)
            }
            2 -> {
                editSession(cinemaService)
            }
            else -> {
                println("Invalid choice.")
            }
        }
    }

    private fun editMovie(cinemaService: CinemaService) {
        print("Enter Movie ID to edit: ")
        val movieId = readLine()?.trim()
        if (movieId != null && movieId.isNotEmpty()) {
            print("Enter new title: ")
            val title = readLine().toString()
            print("Enter new type: ")
            val type = readLine().toString()
            print("Enter new duration (Int) : ")
            val duration = readLine()?.toIntOrNull() ?: 0
            if(duration == 0){
                println("Invalid duration. Please try again\n")
                return
            }
            print("Enter new description: ")
            val description = readLine().toString()

            if (cinemaService.editMovie(movieId, title, type, duration, description)) {
                println("Movie edited successfully.")
            } else {
                println("Invalid Movie ID.")
            }
        } else {
            println("Invalid Movie ID.")
        }
    }

    private fun editSession(cinemaService: CinemaService) {
        print("Enter Session ID to edit: ")
        val sessionId = readLine()?.trim()
        if (sessionId != null && sessionId.isNotEmpty()) {
            print("Enter new Movie ID: ")
            val movieId = readLine().toString()

            // Read and parse LocalDateTime input
            print("Enter new showing time (dd/MM/yyyy HH:mm): ")
            val showingTimeInput = readLine().toString()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val showingTime = try {
                LocalDateTime.parse(showingTimeInput, formatter)
            } catch (e: Exception) {
                null
            }

            if (showingTime != null) {
                print("Enter new available seats (comma-separated): ")
                val availableSeatsStr = readLine().toString()

                val availableSeats = availableSeatsStr.split(",").map { it.toIntOrNull() }
                if (availableSeats.all { it != null }) {
                    if (cinemaService.editSession(sessionId, movieId, showingTime, availableSeats.filterNotNull())) {
                        println("Session edited successfully.")
                    } else {
                        println("Invalid Session ID or Movie ID.")
                    }
                } else {
                    println("Invalid available seats format.")
                }
            } else {
                println("Invalid showing time format. Please use dd/MM/yyyy HH:mm.")
            }
        } else {
            println("Invalid Session ID.")
        }
    }

    private fun browseMovies(cinemaService: CinemaService) {
        println("\nAvailable Movies:")
        val movies = cinemaService.getMovies()
        movies.forEach { movie ->
            println("\n${movie.id}. ${movie.title}")
            println("Description: ${movie.description}, with Duration ${movie.duration} min ")
        }

        print("Enter the movie ID to view sessions: ")
        val movieId = readLine()?.trim()

        if (movieId != null && movieId.isNotEmpty()) {
            val sessions = cinemaService.getSessionsByMovieId(movieId)
            if (sessions.isNotEmpty()) {
                println("Sessions for ${sessions.first().movie.title}:")
                sessions.forEach { session ->
                    println("${session.id}. ${session.showingTime}")
                }

                print("\nEnter the session ID to manage tickets: ")
                val sessionId = readLine()?.trim()

                if (sessionId != null && sessionId.isNotEmpty()) {
                    val availableSeats = cinemaService.displayAvailableSeats(sessionId)
                    if (availableSeats != null) {
                        println("Available Seats: $availableSeats")

                        println("\n1. Book a ticket")
                        println("2. Refund a ticket")

                        print("\nEnter your choice: ")
                        when (readLine()?.toIntOrNull()) {
                            1 -> {
                                bookTicket(cinemaService, sessionId, availableSeats)
                            }
                            2 -> {
                                refundTicket(cinemaService)
                            }
                            else -> {
                                println("Invalid choice.")
                            }
                        }
                    } else {
                        println("Invalid session ID.")
                    }
                } else {
                    println("Invalid session ID.")
                }
            } else {
                println("No sessions available for the selected movie.")
            }
        } else {
            println("Invalid movie ID.")
        }
    }
    private fun bookTicket(cinemaService: CinemaService, sessionId: String, availableSeats: List<Int>) {
        print("\nEnter the seat number to book: ")
        val seatNumber = readLine()?.toIntOrNull()

        if (seatNumber != null && seatNumber in availableSeats) {
            val ticket = cinemaService.sellTicket(sessionId, seatNumber)
            if (ticket != null) {
                println("Ticket Sold: $ticket")
            } else {
                println("Failed to sell ticket.")
            }
        } else {
            println("Invalid seat number or seat already booked.")
        }
    }
    private fun refundTicket(cinemaService: CinemaService) {
        print("Enter Ticket ID for Refund: ")
        val ticketId = readLine()?.trim()
        if (ticketId != null && ticketId.isNotEmpty()) {
            if (cinemaService.refundTicket(ticketId)) {
                println("Ticket refunded successfully.")
            } else {
                println("Invalid ticket ID or ticket not found.")
            }
        } else {
            println("Invalid ticket ID.")
        }
    }
}
