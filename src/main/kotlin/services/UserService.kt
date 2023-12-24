package services

import models.User
import utils.HandleException
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.security.MessageDigest

class UserService: UserOperations, HandleException {
    private val users = mutableListOf<User>()

    override fun registerUser(username: String, password: String) {
        try {
            val passwordHash = hashPassword(password)
            val user = User(username, passwordHash)
            users.add(user)
            println("User registered successfully.")
        } catch (e: Exception) {
            handleException("Error registering user", e)
        }
    }

    override fun authenticateUser(username: String, password: String): Boolean {
        val user = users.find { it.username == username }
        return user?.let { verifyPassword(password, it.passwordHash) } ?: false
    }

    private fun hashPassword(password: String): String {
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val passwordBytes = password.toByteArray()
            val hashBytes = messageDigest.digest(passwordBytes)
            return hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            handleException("Error hashing password", e)
            throw e
        }
    }

    private fun verifyPassword(inputPassword: String, storedPasswordHash: String): Boolean {
        try {
            val inputHash = hashPassword(inputPassword)
            return inputHash == storedPasswordHash
        } catch (e: Exception) {
            handleException("Error verifying password", e)
            throw e
        }
    }

    override fun importUsersFromCSV(filePath: String) {
        try {
            BufferedReader(FileReader(filePath)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val userData = line?.split(",")
                    if (userData != null && userData.size == 2) {
                        val username = userData[0]
                        val passwordHash = userData[1]
                        val user = User(username, passwordHash)
                        users.add(user)
                    }
                }
            }
        } catch (e: Exception) {
            handleException("Error importing users from CSV", e)
        }
    }

    override fun exportUsersToCSV(filePath: String) {
        try {
            FileWriter(filePath).use { writer ->
                users.forEach { user ->
                    writer.append("${user.username},${user.passwordHash}\n")
                }
            }
        } catch (e: Exception) {
            handleException("Error exporting users to CSV", e)
        }
    }
    override fun handleException(message: String, e: Exception?) {
        println("$message: ${e?.message}")
        System.exit(0)
    }
}
