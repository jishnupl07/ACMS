# Adventure Camp Management System (ACMS)

## Project Overview

The Adventure Camp Management System is a semester-level college project designed to demonstrate core Object-Oriented Programming (OOP) concepts. The system allows users to manage a fictional adventure camp.

The primary goal of this project is to build a clean, understandable, and demonstrable academic application that makes good use of:

- Classes and Objects
- Encapsulation, Abstraction, Inheritance, Polymorphism
- Interfaces and Delegation
- Java Collections and User-defined Generic Types (`Repository<T>`)
- Exception Handling

**Note:** This is an academic project. It does not use databases, REST APIs, web services, or cloud infrastructure. All data is stored in memory using Java Collections.

## Interfaces: CLI & GUI

To provide a comprehensive demonstration, this project is split into two interfaces prioritizing functionality in the console first:
1. **Command Line Interface (CLI):** A text-based menu-driven interface to interact with the camp system. (Prioritized Implementation)
2. **Graphical User Interface (GUI):** A desktop application built using JavaFX for a more visual experience.

## Features

- **Camp Management:** Create and manage camp details.
- **People Management:** Add and manage participants, instructors, and admin staff.
- **Activity Management:** Create adventure, water, and team-building activities. Assign instructors and register participants.
- **Scheduling:** Schedule and reschedule activities.
- **Location Management:** Manage locations where activities take place.
- **Equipment Management:** Track equipment, check conditions, and allocate equipment to activities.
- **Safety Checks:** Perform and record safety inspections for equipment and activities.
- **Attendance:** Record participant attendance for scheduled activities.
- **Feedback:** Collect and store participant feedback.
- **Notifications:** Send and display in-app notifications.
- **Reporting:** Generate and view simple camp reports.

## Technology Stack

- **Language:** Java
- **UI Framework (for GUI):** JavaFX
- **Styling (for GUI):** CSS (minimal, for basic JavaFX styling)
- **Build Tool:** Maven

## Project Structure

```text
AdventureCamp/
├── docs/
│   └── class_structure.md       # Approved class diagram reference
├── src/
│   └── main/
│       ├── java/
│       │   ├── people/          # Person, Participant, Instructor, AdminStaff
│       │   ├── activities/      # Activity, AdventureActivity, WaterActivity, etc.
│       │   ├── equipment/       # Equipment, SafetyEquipment, SportsEquipment
│       │   ├── management/      # Camp, Registration, Attendance, Schedule, etc.
│       │   ├── exceptions/      # Custom exception hierarchy
│       │   ├── cli/             # Command Line Interface menus and logic
│       │   └── ui/              # JavaFX UI screens and controllers
│       └── resources/
│           └── styles.css       # JavaFX styling
├── pom.xml                      # Maven configuration
└── README.md                    # Project documentation
```

## Setup and Execution

1. **Prerequisites:** Ensure you have Java Development Kit (JDK) 11 or higher and Maven installed on your system.
2. **Clone the Repository:** Clone this repository to your local machine.
3. **Build:** Navigate to the project root directory and run `mvn clean install`.

### Running the CLI Version
Execute the main CLI class using Maven or Java:
```bash
mvn exec:java -Dexec.mainClass="cli.MainCLI"
```

### Running the GUI Version
Execute the application using the JavaFX plugin:
```bash
mvn javafx:run
```

*Upon starting either version, the application may preload sample data (e.g., a few participants, instructors, and activities) to facilitate immediate demonstration.*

## License

This project is created for academic and educational purposes.
