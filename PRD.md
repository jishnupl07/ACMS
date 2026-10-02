# PRD --- Adventure Camp Management System

## 1. Project Overview

### 1.1 Purpose

The Adventure Camp Management System is a **semester-level college
project** that demonstrates object-oriented programming by building a
small, usable application for managing an adventure camp.

The application should have a simple and intuitive graphical user
interface and implement the functionality represented by the approved
class diagram in:

`docs/class_structure.md`

The goal is **not** to build a production-grade or commercial system.
The goal is to build a clean, understandable and demonstrable academic
project that makes good use of:

-   Classes and objects
-   Encapsulation
-   Abstraction
-   Inheritance
-   Interfaces
-   Polymorphism
-   Delegation
-   Collections
-   User-defined generic types
-   Exception handling

------------------------------------------------------------------------

# 2. Project Goals

The application should allow a user to:

1.  Create and manage a camp.
2.  Add participants, instructors and admin staff.
3.  Create different types of activities.
4.  Assign instructors to activities.
5.  Register participants for activities.
6.  Schedule activities.
7.  Manage locations.
8.  Manage equipment.
9.  Allocate equipment to activities.
10. Perform safety checks.
11. Record attendance.
12. Collect feedback.
13. Send/display notifications.
14. View a simple camp report.

The application should be small enough to complete within a semester
while still demonstrating all important classes and relationships in the
class diagram.

------------------------------------------------------------------------

# 3. Technology Stack

Use a **minimal technology stack**.

### Required

-   **Java** --- application logic and object-oriented implementation
-   **JavaFX** --- graphical user interface
-   **CSS** --- only if needed for basic JavaFX styling

### Optional build tool

-   **Maven** --- dependency management and project build

No other technology is required.

### Explicitly do not use

The project does **not** require:

-   REST APIs
-   Web services
-   External APIs
-   Google Maps or map services
-   Cloud services
-   Authentication systems
-   Online payment gateways
-   SMS/email APIs
-   Microservices
-   Spring Boot
-   React/Angular/other web frameworks
-   Complex databases
-   Docker
-   Deployment infrastructure

------------------------------------------------------------------------

# 4. Data Storage

For this semester project, data should be stored **in memory** using
Java collections.

Do not introduce a database unless the project requirements are changed
later.

Use:

``` text
List<T>
```

where appropriate.

The project must also demonstrate the user-defined generic class:

``` text
Repository<T>
```

which internally uses a `List<T>`.

Example:

``` text
Repository<Registration>
Repository<ScheduleEntry>
Repository<SafetyCheck>
Repository<AttendanceRecord>
```

Data only needs to remain available while the application is running.

Persistent storage across application restarts is not required.

------------------------------------------------------------------------

# 5. Class Structure

The implementation must follow the approved class structure in:

`docs/class_structure.md`

The class diagram is the source of truth for the major classes,
inheritance relationships and interfaces.

The implementation should not introduce unnecessary classes simply to
make the project appear larger.

Small helper classes are allowed when they directly support the existing
design.

------------------------------------------------------------------------

# 6. Main Packages

The project should use a simple package structure corresponding to the
class diagram:

``` text
src/main/java/
├── people/
├── activities/
├── equipment/
├── management/
└── exceptions/
```

A separate UI package may be added:

``` text
└── ui/
```

for JavaFX screens/controllers.

The UI code should not contain the core business logic.

------------------------------------------------------------------------

# 7. People

## 7.1 Person

`Person` is the abstract parent class.

It contains common information:

-   ID
-   Name
-   Age
-   Contact number

It provides common methods such as:

-   `getId()`
-   `getName()`
-   `setContactNumber()`
-   `displayInfo()`

`displayInfo()` should be abstract so subclasses provide their own
implementation.

## 7.2 Participant

A participant should contain:

-   Emergency contact
-   Medical notes
-   Skill level
-   Registrations

The participant can:

-   Register for an activity
-   Cancel a registration
-   Submit feedback
-   Display their information

Registrations should be stored as:

``` text
List<Registration>
```

## 7.3 Instructor

An instructor should contain:

-   Specialization
-   Certifications
-   Availability
-   Assigned activities

Assigned activities should be stored as:

``` text
List<Activity>
```

The instructor can:

-   Assign an activity
-   Remove an activity
-   Conduct an activity
-   Check activity participants
-   Send a notification

The instructor should **not** maintain a separate participant list.

Participants are obtained through the instructor's assigned activities.

## 7.4 AdminStaff

Admin staff can:

-   Manage camp information
-   Manage registrations
-   Send notifications
-   Display staff information

------------------------------------------------------------------------

# 8. Activities

## 8.1 Activity

`Activity` is an abstract class and the main class for camp activities.

It contains:

-   Activity ID
-   Title
-   Description
-   Duration
-   Maximum participants
-   Start time
-   Location
-   Instructor
-   Participants
-   Feedback
-   Equipment allocations

Use:

``` text
List<Participant>
List<Feedback>
List<EquipmentAllocation>
```

where appropriate.

Required behavior includes:

-   Check availability
-   Conduct activity
-   Get start time
-   Get end time
-   Update schedule
-   Add participant
-   Remove participant
-   Get participants
-   Add feedback

## 8.2 Activity Types

Implement:

-   `AdventureActivity`
-   `WaterActivity`
-   `TeamBuildingActivity`

Each must override:

``` text
conductActivity()
```

The subclasses should have their own simple specialized properties.

Examples:

### AdventureActivity

-   Terrain type
-   Risk level

### WaterActivity

-   Whether a lifeguard is required
-   Water body type

### TeamBuildingActivity

-   Team size

------------------------------------------------------------------------

# 9. Scheduling

## 9.1 Schedulable

`Schedulable` is an interface containing:

-   `getStartTime()`
-   `getEndTime()`
-   `updateSchedule(DateTime)`

`Activity` implements this interface.

This should be demonstrated in the Java implementation.

## 9.2 CampSchedule

`CampSchedule` manages scheduled activities.

It contains:

``` text
Repository<ScheduleEntry>
```

It should support:

-   Add activity to schedule
-   Remove activity
-   Reschedule activity
-   Get activities for a date
-   Display schedule

`Camp` should delegate detailed scheduling work to its `CampSchedule`.

## 9.3 ScheduleEntry

`ScheduleEntry` represents one scheduled activity.

It stores:

-   Entry ID
-   Date
-   Start time
-   End time
-   Activity

------------------------------------------------------------------------

# 10. Camp

`Camp` is the main management class.

It should contain only the information it is genuinely responsible for.

It should **not** contain redundant:

``` text
List<Participant>
List<Instructor>
List<Activity>
```

because these relationships are already maintained by the relevant
classes.

`Camp` should contain:

-   Camp ID
-   Camp name
-   Start date
-   End date
-   Capacity
-   Status
-   `CampSchedule`
-   `Repository<Registration>`
-   `Repository<SafetyCheck>`

Required methods:

-   `registerParticipant(Participant, Activity)`
-   `scheduleActivity(Activity, DateTime)`
-   `generateReport()`
-   `checkCapacity()`
-   `addSafetyCheck(SafetyCheck)`

The `Camp` class should act as a coordinator rather than becoming a
large storage class.

------------------------------------------------------------------------

# 11. Registration

`Registration` represents a participant's registration for an activity.

It contains:

-   Registration ID
-   Registration date
-   Status
-   Participant
-   Activity

It supports:

-   Process registration
-   Confirm registration
-   Cancel registration

### Registration flow

``` text
Participant
    ↓
Camp.registerParticipant()
    ↓
Check camp/activity capacity
    ↓
Create Registration
    ↓
Store Registration
    ↓
Add Participant to Activity
```

If registration is invalid, the appropriate exception should be thrown.

------------------------------------------------------------------------

# 12. Attendance

`Attendance` manages attendance for activities.

It contains:

``` text
Repository<AttendanceRecord>
```

`AttendanceRecord` identifies:

-   Participant
-   Activity
-   Date
-   Present/absent status

The application should allow the user to:

-   Select an activity
-   View its participants
-   Mark participants present or absent
-   View a simple attendance report

------------------------------------------------------------------------

# 13. Equipment

## 13.1 Equipment

`Equipment` is an abstract class.

It contains:

-   Equipment ID
-   Name
-   Availability
-   Condition

It supports:

-   Check condition
-   Allocate
-   Release
-   Check whether it is available for an activity

## 13.2 SafetyEquipment

Contains:

-   Last inspection date
-   Safety rating

Overrides:

``` text
checkCondition()
```

## 13.3 SportsEquipment

Contains:

-   Sport type
-   Condition

Overrides:

``` text
checkCondition()
```

and supports compatibility checking with activities.

------------------------------------------------------------------------

# 14. Equipment Allocation

`EquipmentAllocation` represents equipment being assigned to an
activity.

It contains:

-   Allocation ID
-   Allocation date
-   Return date
-   Status
-   Equipment
-   Activity

It supports:

-   Allocate equipment
-   Return equipment
-   Check availability
-   Get allocation status

Example:

``` text
Activity
    ↓
EquipmentAllocation
    ↓
Equipment
```

------------------------------------------------------------------------

# 15. Safety

`SafetyCheck` represents a safety inspection.

It contains:

-   Check ID
-   Check date
-   Passed/failed result
-   Remarks

It supports:

-   Performing an equipment check
-   Checking activity safety
-   Recording the result
-   Checking whether the check passed

The camp stores safety checks using:

``` text
Repository<SafetyCheck>
```

------------------------------------------------------------------------

# 16. Location

`Location` represents the place where an activity occurs.

It contains:

-   Location ID
-   Name
-   Address
-   Terrain type
-   Capacity

It supports:

-   Checking availability
-   Reserving a time
-   Releasing a reservation
-   Displaying location details

An activity should have a `Location`.

No external map service is required.

The location can simply be selected from the locations created inside
the application.

------------------------------------------------------------------------

# 17. Feedback

Participants can submit feedback for activities.

`Feedback` contains:

-   Feedback ID
-   Rating
-   Comments
-   Submitted date
-   Participant
-   Activity

It supports:

-   Submit feedback
-   Update comment
-   Get feedback

A simple rating such as 1--5 is sufficient.

------------------------------------------------------------------------

# 18. Notifications

`Notifiable` is an interface containing:

``` text
sendNotification(String)
```

It is implemented by:

-   `Instructor`
-   `AdminStaff`

For this project, notification functionality can simply display a
message in the UI or console.

No external notification service is required.

Example:

``` text
Notification:
"Water Rafting starts at 10:00 AM."
```

------------------------------------------------------------------------

# 19. Generic Type

The project must include the user-defined generic class:

``` text
Repository<T>
```

It should internally use:

``` text
List<T>
```

and provide:

-   `add(T)`
-   `remove(T)`
-   `get(int)`
-   `getAll()`
-   `size()`

It should be used with multiple types.

At minimum:

``` text
Repository<Registration>
Repository<ScheduleEntry>
Repository<SafetyCheck>
Repository<AttendanceRecord>
```

This demonstrates that the same user-defined generic class can store
different object types.

------------------------------------------------------------------------

# 20. Exceptions

Create the following hierarchy:

``` text
CampException
├── RegistrationException
├── EquipmentUnavailableException
├── SafetyViolationException
└── CapacityExceededException
```

## RegistrationException

Thrown when a registration cannot be completed.

## EquipmentUnavailableException

Thrown when equipment cannot be allocated.

## SafetyViolationException

Thrown when a required safety condition fails.

## CapacityExceededException

Thrown when the camp/activity has reached its capacity.

The UI should catch these exceptions and show a simple user-friendly
message rather than crashing.

------------------------------------------------------------------------

# 21. User Interface

The application should have a **simple desktop UI using JavaFX**.

The UI does not need to be elaborate.

The main goal is that a user can understand the application without
needing to interact with Java objects directly.

## 21.1 Main Window

Use a simple layout such as:

``` text
+------------------------------------------------+
| Adventure Camp Management System               |
+----------------+-------------------------------+
| Dashboard      |                               |
| Participants   |       Main Content            |
| Instructors    |                               |
| Activities     |                               |
| Schedule       |                               |
| Equipment      |                               |
| Attendance     |                               |
| Safety         |                               |
| Feedback       |                               |
| Reports        |                               |
+----------------+-------------------------------+
```

A sidebar/navigation panel is sufficient.

## 21.2 Dashboard

Show simple information such as:

-   Camp name
-   Camp dates
-   Number of registrations
-   Number of activities
-   Upcoming activity
-   Basic capacity information

Do not build complex charts unless they are useful and easy to
implement.

## 21.3 Participants Screen

Allow the user to:

-   View participants
-   Add a participant
-   View participant details
-   Register a participant for an activity

Use a simple table and forms.

## 21.4 Instructors Screen

Allow the user to:

-   View instructors
-   Add an instructor
-   Assign an instructor to an activity
-   View assigned activities

## 21.5 Activities Screen

Allow the user to:

-   View activities
-   Create an activity
-   Choose activity type
-   Assign instructor
-   Select location
-   View participants
-   Add/remove participants

## 21.6 Schedule Screen

Display scheduled activities by date.

Allow:

-   Schedule activity
-   Reschedule activity
-   Remove activity

A simple table grouped or filtered by date is sufficient.

## 21.7 Equipment Screen

Allow the user to:

-   View equipment
-   Add equipment
-   View availability
-   Allocate equipment
-   Return equipment

## 21.8 Attendance Screen

Allow the user to:

1.  Select an activity.
2.  View registered participants.
3.  Mark present/absent.
4.  View attendance.

## 21.9 Safety Screen

Allow the user to:

-   Select equipment/activity
-   Perform a safety check
-   Enter remarks
-   View pass/fail result

## 21.10 Feedback Screen

Allow the user to:

-   Select an activity
-   Select a participant
-   Enter rating
-   Enter comments
-   View submitted feedback

## 21.11 Reports Screen

Provide a simple text/table-based report containing information such as:

-   Camp information
-   Activities
-   Instructors
-   Registrations
-   Attendance
-   Equipment allocation
-   Safety checks

No advanced reporting framework is required.

------------------------------------------------------------------------

# 22. UI Design Requirements

The UI should be:

-   Simple
-   Consistent
-   Easy to navigate
-   Readable
-   Appropriate for a college project

Use:

-   Buttons
-   Labels
-   Text fields
-   Combo boxes
-   Tables
-   Date pickers where useful
-   Dialog boxes for errors and confirmations

Avoid unnecessary animations and complicated UI components.

A small amount of CSS can be used to make the application visually
consistent.

------------------------------------------------------------------------

# 23. Application Flow

A typical demonstration should work like this:

``` text
Start Application
      ↓
Create / Load Sample Camp
      ↓
Dashboard
      ↓
Create Participants
      ↓
Create Instructor
      ↓
Create Activity
      ↓
Assign Instructor
      ↓
Select Location
      ↓
Schedule Activity
      ↓
Register Participants
      ↓
Perform Safety Check
      ↓
Allocate Equipment
      ↓
Mark Attendance
      ↓
Conduct Activity
      ↓
Submit Feedback
      ↓
View Report
```

The project may preload a small set of sample data so that the
application can be demonstrated immediately.

------------------------------------------------------------------------

# 24. Sample Data

For easier demonstration, the application may start with sample data
such as:

### Participants

-   3--5 participants

### Instructors

-   2 instructors

### Activities

-   Rock Climbing
-   Kayaking
-   Team Challenge

### Equipment

-   Helmets
-   Life Jackets
-   Climbing Ropes

### Locations

-   Climbing Wall
-   River Zone
-   Camp Ground

The user should still be able to add additional objects through the UI.

------------------------------------------------------------------------

# 25. Error Handling in the UI

When an operation fails, display a simple dialog.

Examples:

``` text
Registration Failed
The activity has reached its maximum participant capacity.
```

``` text
Equipment Unavailable
The selected equipment is currently allocated to another activity.
```

``` text
Safety Check Failed
The activity cannot proceed because the required safety check failed.
```

The application should remain running after an error.

------------------------------------------------------------------------

# 26. Testing Requirements

Testing should focus on demonstrating that the class structure works.

At minimum test:

### Inheritance

-   `Participant`, `Instructor`, and `AdminStaff` inherit from `Person`.
-   Activity subclasses inherit from `Activity`.
-   Equipment subclasses inherit from `Equipment`.

### Interfaces

-   `Activity` implements `Schedulable`.
-   `Instructor` implements `Notifiable`.
-   `AdminStaff` implements `Notifiable`.

### Registration

-   Successful registration.
-   Duplicate/invalid registration.
-   Capacity exceeded.

### Scheduling

-   Add activity.
-   Remove activity.
-   Reschedule activity.

### Equipment

-   Successful allocation.
-   Return equipment.
-   Allocation failure.

### Safety

-   Passed safety check.
-   Failed safety check.

### Attendance

-   Mark present.
-   Mark absent.
-   Generate attendance report.

### Feedback

-   Submit feedback.
-   Update feedback.

### Generic Repository

Demonstrate:

``` text
Repository<Registration>
Repository<Activity>
Repository<AttendanceRecord>
```

or other suitable types.

------------------------------------------------------------------------

# 27. Recommended Project Structure

``` text
AdventureCamp/
│
├── docs/
│   └── class_structure.md
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── people/
│       │   ├── activities/
│       │   ├── equipment/
│       │   ├── management/
│       │   ├── exceptions/
│       │   └── ui/
│       │
│       └── resources/
│           └── styles.css
│
├── pom.xml
└── README.md
```

A test directory may be added if unit tests are required:

``` text
src/test/java/
```

------------------------------------------------------------------------

# 28. What Should Not Be Over-Engineered

This is a semester project. Prefer straightforward implementations.

Do not add:

-   Database layers
-   Repository/service/controller architectures beyond what is necessary
-   Dependency injection frameworks
-   REST controllers
-   API clients
-   Authentication
-   Complex design patterns
-   Cloud infrastructure
-   Multi-user concurrency
-   Advanced analytics
-   External messaging systems
-   Map integrations
-   Payment systems

The objective is to make the **existing object-oriented design work
well**, not to create an enterprise application.

------------------------------------------------------------------------

# 29. Definition of Done

The project is complete when:

-   All classes in `docs/class_structure.md` are implemented.
-   Inheritance relationships work.
-   Interfaces are implemented.
-   Instructor/activity/participant relationships work.
-   Activities can be created and scheduled.
-   Participants can register for activities.
-   Capacity is checked.
-   Attendance can be recorded.
-   Equipment can be allocated and returned.
-   Safety checks can be performed.
-   Feedback can be submitted.
-   Notifications can be displayed.
-   Custom exceptions work.
-   `Repository<T>` is implemented and used with multiple types.
-   The JavaFX UI provides access to the major features.
-   The application can be demonstrated from start to finish.
-   No external API, database or cloud service is required.
-   The application remains understandable to a student reviewing the
    source code.

------------------------------------------------------------------------

# 30. Final Project Principle

Keep the implementation **simple, functional and consistent with the
class diagram**.

The project should demonstrate that the team understands how the classes
interact in an object-oriented system.

A smaller implementation that clearly demonstrates:

``` text
Inheritance
     +
Interfaces
     +
Composition / Association
     +
Delegation
     +
Lists
     +
Generic Repository<T>
     +
Exception Handling
     +
JavaFX UI
```

is preferable to a technically complex application that introduces
unnecessary technologies.
