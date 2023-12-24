package models

import java.time.LocalDateTime

data class Session(
    private var _id: String,
    private var _movie: Movie,
    private var _showingTime: LocalDateTime,
    private var _availableSeats: MutableList<Int>
) {
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var movie: Movie
        get() = _movie
        set(value) {
            _movie = value
        }

    var showingTime: LocalDateTime
        get() = _showingTime
        set(value) {
            _showingTime = value
        }

    var availableSeats: MutableList<Int>
        get() = _availableSeats
        set(value) {
            _availableSeats = value
        }

    override fun toString(): String {
        return """Session ID: $id
                Movie: $movie
                Showing Time: $showingTime
                Available Seats: $availableSeats
                """.trimIndent()
    }
}
