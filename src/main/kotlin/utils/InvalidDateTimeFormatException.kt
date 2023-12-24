package utils

class InvalidDateTimeFormatException(private val customMessage: String, cause: Throwable? = null) :
    Exception(customMessage, cause)