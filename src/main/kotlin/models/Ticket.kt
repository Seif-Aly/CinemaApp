package models

data class Ticket(
    private var _id: String,
    private var _session: Session,
    private var _seatNumber: Int
) {
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var session: Session
        get() = _session
        set(value) {
            _session = value
        }

    var seatNumber: Int
        get() = _seatNumber
        set(value) {
            _seatNumber = value
        }

    override fun toString(): String {
        return """Ticket ID: $id
                Session: $session
                Seat Number: $seatNumber
                """.trimIndent()
    }
}
