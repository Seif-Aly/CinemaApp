package repositories

import models.Movie

class MovieRepository {
    private val movies = mutableListOf<Movie>()

    fun getAllMovies(): List<Movie> = movies.toList()

    fun getMovieById(movieId: String): Movie? = movies.find { it.id == movieId }

    fun addMovie(movie: Movie) {
        movies.add(movie)
    }

    fun updateMovie(updatedMovie: Movie) {
        val index = movies.indexOfFirst { it.id == updatedMovie.id }
        if (index != -1) {
            movies[index] = updatedMovie
        }
    }
}
