package repositories

import models.Session

class SessionRepository {
    private val sessions = mutableListOf<Session>()

    fun getSessionsByMovieId(movieId: String): List<Session> {
        return sessions.filter { it.movie.id == movieId }
    }

    fun getSessionById(sessionId: String): Session? = sessions.find { it.id == sessionId }

    fun getAllSessions(): List<Session> = sessions

    fun addSession(session: Session) {
        sessions.add(session)
    }

    fun updateSession(updatedSession: Session) {
        val index = sessions.indexOfFirst { it.id == updatedSession.id }
        if (index != -1) {
            sessions[index] = updatedSession
        }
    }
}
