package services

import models.Movie
import models.Session
import models.Ticket
import repositories.MovieRepository
import repositories.SessionRepository
import repositories.TicketRepository
import utils.FileOperations
import java.time.LocalDateTime
import java.util.*

class CinemaService(
    private val movieRepository: MovieRepository,
    private val sessionRepository: SessionRepository,
    private val ticketRepository: TicketRepository,
    private val fileOperations: FileOperations
) : FileOperations by fileOperations {
    fun sellTicket(sessionId: String, seatNumber: Int): Ticket? {
        val session = sessionRepository.getSessionById(sessionId)
        return if (session != null && session.availableSeats.contains(seatNumber)) {
            val ticket = Ticket(
                _id = generateUniqueId(),
                _session = session,
                _seatNumber = seatNumber
            )
            ticketRepository.addTicket(ticket)
            session.availableSeats.remove(seatNumber)
            ticket
        } else {
            null
        }
    }

    fun refundTicket(ticketId: String): Boolean {
        val ticket = ticketRepository.getTicketById(ticketId)
        return if (ticket != null && isBeforeSessionStart(ticket.session)) {
            ticketRepository.deleteTicket(ticketId)
            val session = ticket.session
            session.availableSeats.add(ticket.seatNumber)
            true
        } else {
            false
        }
    }

    private fun isBeforeSessionStart(session: Session): Boolean {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.isBefore(session.showingTime)
    }

    fun displayAvailableSeats(sessionId: String): List<Int>? {
        val session = sessionRepository.getSessionById(sessionId)
        return session?.availableSeats
    }

    private fun generateUniqueId(): String = UUID.randomUUID().toString()

    fun importDataFromCSV(movieFilePath: String, sessionFilePath: String, ticketFilePath: String) {
        fileOperations.readMoviesFromCSV(movieFilePath)?.forEach { movieRepository.addMovie(it) }
        fileOperations.readSessionsFromCSV(sessionFilePath, movieRepository).forEach { sessionRepository.addSession(it) }
        fileOperations.readTicketsFromCSV(ticketFilePath, sessionRepository).forEach { ticketRepository.addTicket(it) }
    }

    fun exportDataToCSV(movieFilePath: String, sessionFilePath: String, ticketFilePath: String) {
        fileOperations.writeMoviesToCSV(movieFilePath, movieRepository.getAllMovies())
        fileOperations.writeSessionsToCSV(sessionFilePath, sessionRepository.getAllSessions())
        fileOperations.writeTicketsToCSV(ticketFilePath, ticketRepository.getAllTickets())
    }

    fun getMovies(): List<Movie> {
        return movieRepository.getAllMovies()
    }

    fun getSessionsByMovieId(movieId: String): List<Session> {
        return sessionRepository.getSessionsByMovieId(movieId)
    }

    fun editMovie(movieId: String, title: String, type: String, duration: Int, description: String): Boolean {
        val movie = movieRepository.getMovieById(movieId)
        return if (movie != null) {
            val editedMovie = Movie(movieId, title, type, duration, description)
            movieRepository.updateMovie(editedMovie)
            true
        } else {
            false
        }
    }

    fun editSession(sessionId: String, movieId: String, showingTime: LocalDateTime, availableSeats: List<Int>): Boolean {
        val session = sessionRepository.getSessionById(sessionId)
        return if (session != null) {
            val movie = movieRepository.getMovieById(movieId)
            if (movie != null) {
                val editedSession = Session(sessionId, movie, showingTime, availableSeats.toMutableList())
                sessionRepository.updateSession(editedSession)
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
