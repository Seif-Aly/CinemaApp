
# Cinema Management Program User Guide

## Overview
The Cinema Management Program is a console-based application designed for cinema workers to manage data about films at the box office. This guide provides detailed instructions on how to use the application.

## Main Menu
Upon running the program, you'll be presented with the main menu:
1. Login
2. Register
3. Exit program
### Option 1 (Login):
Enter your username and password to access the program.
On successful login, proceed to the main menu.
### Option 2 (Register):
If you don't have an account, register by entering a new username and password.
Registered users are stored in the "users.csv" file.
### Option 3 (Exit Program):
Choose to exit the program.

## Main Application Menu
After logging in, the main application menu will be displayed:
1. Browse Movies
2. Edit Movie or Session
3. Exit
### Option 1 (Browse Movies):
View available movies with details (ID, Title, Type, Duration, Description).
Choose a movie to view sessions.
### Option 2 (Edit Movie or Session):
Change details about the selected movie or session
### Option 3 (Exit):
Save data to CSV files and exit the application.

## Movie Sessions Menu
After selecting a movie in the "Browse Movies" menu, you'll see:
Enter the movie ID to view sessions:

## Session Menu
After entering a valid movie ID, the session menu will be displayed:
1. <Session 1 showing time>
2. <Session 2 showing time>
...
Options 1, 2, ..., N:
Choose a session to manage tickets or refund.

## Ticket Management
In the selected session menu, you'll see:
Available Seats: <seat1, seat2, ..., seatN>
1. Book a ticket
2. Refund a ticket

### Option 1 (Book a Ticket):
Enter the seat number to book a ticket.
Ticket details will be displayed.

### Option 2 (Refund a Ticket):
Enter the Ticket ID to refund a ticket.

## Editing Data
In the session menu, you can edit data about movies and sessions:
1. Edit Movie
2. Edit Session
### Option 1 (Edit Movie):
Modify information about the selected movie.
### Option 2 (Edit Session):
Change details about the selected session, such as showing time or available seats.

## Additional Notes
Data about movies, sessions, and tickets are stored in CSV files (movies.csv, sessions.csv, tickets.csv).
