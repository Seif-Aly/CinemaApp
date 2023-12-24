package utils

import models.Movie
import models.Session
import models.Ticket
import repositories.MovieRepository
import repositories.SessionRepository

interface FileOperations {
    fun readMoviesFromCSV(filePath: String): List<Movie>?
    fun writeMoviesToCSV(filePath: String, movies: List<Movie>)
    fun readSessionsFromCSV(filePath: String, movieRepository: MovieRepository): List<Session>
    fun writeSessionsToCSV(filePath: String, sessions: List<Session>)
    fun readTicketsFromCSV(filePath: String, sessionRepository: SessionRepository): List<Ticket>
    fun writeTicketsToCSV(filePath: String, tickets: List<Ticket>)
}