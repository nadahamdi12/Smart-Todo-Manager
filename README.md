# Smart Todo Manager (Java Swing)

A complete desktop application for task management built with Java Swing.

## 📌 Features

This application includes the following functionalities:

- User Authentication
  - Login
  - Register

- Task Management (Dashboard)
  - Add Task
  - Edit Task
  - Delete Task
  - Mark Task as Done
  - Filter Tasks

- Logout system

## 🖥️ Interface

The main dashboard is built using Java Swing and includes:

- JFrame (main window)
- JTable (display tasks dynamically)
- JButton (actions)
- JComboBox (filters)
- JPanel (layout organization)
- BorderLayout (structure management)

## 🎯 Main Dashboard Features

The To Do List Dashboard allows users to:

- Add new tasks
- Modify existing tasks
- Delete tasks
- Filter tasks (e.g. DONE / NOT DONE)
- Mark tasks as completed
- View tasks dynamically in a table (JTable)

## 🛠️ Technologies Used

- Java
- Java Swing (GUI)
- JDBC (if database is used)
- MySQL (optional depending on your setup)

## 📂 Project Structure


/src
├── views ## This layer handles everything related to the Graphical User Interface (GUI).

├── controllers ## Acts as the middle layer between the UI and the data layer.

├── models ## Represents the core data structures of the application.

├── dao ## Handles all communication with the database (SQL).

/App.java ## this is the main directory that contains all the Java source code of the application.