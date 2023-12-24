package services

interface UserOperations {
    fun registerUser(username: String, password: String)

    fun authenticateUser(username: String, password: String): Boolean

    fun importUsersFromCSV(filePath: String)

    fun exportUsersToCSV(filePath: String)
}