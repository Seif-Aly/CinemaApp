package models

data class Movie(
    private var _id: String,
    private var _title: String,
    private var _type: String,
    private var _duration: Int,
    private var _description: String
) {
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var title: String
        get() = _title
        set(value) {
            _title = value
        }

    var type: String
        get() = _type
        set(value) {
            _type = value
        }

    var duration: Int
        get() = _duration
        set(value) {
            _duration = value
        }

    var description: String
        get() = _description
        set(value) {
            _description = value
        }

    override fun toString(): String {
        return """Movie ID: $id
                Title: $title
                Type: $type
                Duration: $duration
                Description: $description
                """.trimIndent()
    }
}
