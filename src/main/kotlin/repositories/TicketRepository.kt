package repositories

import models.Ticket

class TicketRepository {
    private val tickets = mutableListOf<Ticket>()

    fun getTicketById(ticketId: String): Ticket? = tickets.find { it.id == ticketId }

    fun getAllTickets(): List<Ticket> = tickets

    fun addTicket(ticket: Ticket) {
        tickets.add(ticket)
    }

    fun deleteTicket(ticketId: String) {
        tickets.removeIf { it.id == ticketId }
    }
}