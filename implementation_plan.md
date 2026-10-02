# Implementation Plan: Adventure Camp Management System

This document outlines a phased implementation plan for the Adventure Camp Management System, based on the provided PRD. It prioritizes a Command Line Interface (CLI) implementation first, followed by a JavaFX Graphical User Interface (GUI).

## Phase 1: Project Setup & Core Structure
**Goal:** Initialize the project, setup the build system, and create the foundational package structure.

1.  **Initialize Maven Project:**
    *   Create a new Maven project.
    *   Add JavaFX dependencies to `pom.xml` (for Phase 6).
    *   Configure the Maven compiler plugin.
2.  **Create Package Structure:**
    *   `people`
    *   `activities`
    *   `equipment`
    *   `management`
    *   `exceptions`
    *   `cli`
    *   `ui`
3.  **Implement Custom Exceptions (`exceptions` package):**
    *   Create base `CampException` (extends `Exception`).
    *   Create subclasses: `RegistrationException`, `EquipmentUnavailableException`, `SafetyViolationException`, `CapacityExceededException`.

## Phase 2: Core Domain Models & Interfaces
**Goal:** Implement the primary base classes, interfaces, and the generic repository.

1.  **Interfaces:**
    *   `Schedulable` (in `activities` or `management`): `getStartTime()`, `getEndTime()`, `updateSchedule(DateTime)`.
    *   `Notifiable` (in `management` or `people`): `sendNotification(String)`.
2.  **Generic Repository (`management` package):**
    *   Implement `Repository<T>` using an internal `List<T>`.
    *   Add methods: `add(T)`, `remove(T)`, `get(int)`, `getAll()`, `size()`.
3.  **Base Abstract Classes:**
    *   `Person` (`people` package): fields (ID, name, age, contactNumber), abstract method `displayInfo()`.
    *   `Equipment` (`equipment` package): fields (ID, name, availability, condition), related methods.
    *   `Activity` (`activities` package): abstract class implementing `Schedulable`. Fields for ID, title, description, participants, feedback, allocations, etc. Abstract method `conductActivity()`.

## Phase 3: Concrete Implementations
**Goal:** Implement specific subclasses for people, activities, and equipment.

1.  **People Subclasses (`people` package):**
    *   `Participant`: Add emergency contact, medical notes, skill level, and `List<Registration>`. Implement `displayInfo()`.
    *   `Instructor`: Implements `Notifiable`. Add specialization, certifications, availability, and `List<Activity>`. Implement `displayInfo()`.
    *   `AdminStaff`: Implements `Notifiable`. Implement `displayInfo()`.
2.  **Activity Subclasses (`activities` package):**
    *   `AdventureActivity`: Add terrain type, risk level. Implement `conductActivity()`.
    *   `WaterActivity`: Add lifeguard required boolean, water body type. Implement `conductActivity()`.
    *   `TeamBuildingActivity`: Add team size. Implement `conductActivity()`.
3.  **Equipment Subclasses (`equipment` package):**
    *   `SafetyEquipment`: Add inspection date, safety rating. Override `checkCondition()`.
    *   `SportsEquipment`: Add sport type. Override `checkCondition()`.
4.  **Supporting Classes:**
    *   `Location` (`management` package): ID, name, address, terrain, capacity.

## Phase 4: Management & Linking Classes
**Goal:** Implement classes that tie the system together (scheduling, registration, attendance, etc.).

1.  **Scheduling (`management` package):**
    *   `ScheduleEntry`: ID, Date, Start time, End time, Activity.
    *   `CampSchedule`: Contains `Repository<ScheduleEntry>`. Methods to add/remove/reschedule.
2.  **Operations (`management` package):**
    *   `Registration`: ID, date, status, Participant, Activity.
    *   `AttendanceRecord`: Participant, Activity, Date, status (present/absent).
    *   `Attendance`: Contains `Repository<AttendanceRecord>`.
    *   `EquipmentAllocation`: ID, dates, status, Equipment, Activity.
    *   `SafetyCheck`: ID, date, passed/failed, remarks.
    *   `Feedback`: ID, rating, comments, date, Participant, Activity.
3.  **The Main Camp Class (`management` package):**
    *   `Camp`: Coordinate the system. Fields: ID, name, dates, capacity, `CampSchedule`, `Repository<Registration>`, `Repository<SafetyCheck>`.
    *   Implement methods: `registerParticipant()`, `scheduleActivity()`, `generateReport()`, `checkCapacity()`, `addSafetyCheck()`. Ensure appropriate exceptions are thrown.

## Phase 5: Command Line Interface (CLI) Implementation
**Goal:** Build a text-based, menu-driven CLI to interact with the system logic. **(PRIORITY)**

1.  **Main Menu (`cli.MainCLI`):**
    *   Initialize `Camp` and preload sample data.
    *   Display a numbered menu matching the major features (Dashboard, Participants, Instructors, Activities, Schedule, Equipment, Attendance, Safety, Feedback, Reports).
2.  **Sub-menus & Handlers:**
    *   Create handlers to capture `Scanner` input for managing entities (e.g., prompting for ID, Name, Age when adding a Participant).
    *   Invoke appropriate methods on the `Camp` class and domain models.
3.  **CLI Error Handling:**
    *   Wrap calls in `try-catch` blocks specifically targeting the custom `CampException` hierarchy, outputting readable messages to `System.out` instead of stack traces.

## Phase 6: Graphical User Interface (GUI) Implementation
**Goal:** Build the simple desktop JavaFX GUI to interact with the same underlying system logic.

1.  **Main Application Window (`ui.MainApp`):**
    *   Set up the primary stage.
    *   Create a main layout with a sidebar (Navigation) and a center content area.
2.  **UI Screens (Controllers and Views in `ui` package):**
    *   **Dashboard:** High-level camp stats.
    *   **Participants Screen:** Table view of participants, form to add/edit.
    *   **Instructors / Activities / Schedule / Equipment / etc:** Map the functionality implemented in the CLI to graphical components (Tables, Forms, ComboBoxes).
3.  **GUI Error Handling:**
    *   Implement generic dialog boxes (`Alert` in JavaFX) to catch and display `CampException` messages gracefully.

## Phase 7: Testing & Refinement
**Goal:** Ensure the system works as intended per the PRD definition of done.

1.  **Manual Testing:** Walk through the application flow from start to finish in both CLI and GUI.
2.  **Constraint Checking:** Verify exceptions are thrown and caught in both interfaces when capacity is full, equipment is unavailable, or safety checks fail.
3.  **Code Review:** Ensure encapsulation is respected. Ensure the core logic remains independent of the UI layer (meaning both CLI and GUI can use the exact same classes from `people`, `activities`, `management`, etc. without modification).
