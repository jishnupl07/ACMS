# Product Requirements Document (PRD)

# Adventure Camp Management System

## 1. Project Overview

### 1.1 Purpose

The Adventure Camp Management System is an object-oriented Java
application for managing the main operations of an adventure camp.

The system manages:

-   Camp information and capacity
-   Participants
-   Instructors and activity assignments
-   Adventure activities
-   Activity scheduling
-   Participant registration
-   Attendance
-   Equipment and equipment allocation
-   Safety checks
-   Participant feedback
-   Notifications
-   Domain-specific exceptions

The implementation must follow the class structure defined in
`docs/class_structure.md`.

### 1.2 Primary Goal

Build a working object-oriented application in which the classes in the
class diagram are implemented as Java classes/interfaces, their
responsibilities are respected, and the relationships between the
objects are demonstrated through actual program execution.

### 1.3 Design Principles

The implementation should demonstrate:

-   Encapsulation
-   Abstraction
-   Inheritance
-   Interface implementation
-   Polymorphism
-   Delegation
-   Object composition/association
-   Collections using `List`
-   User-defined generic types
-   Exception handling
-   Separation of responsibilities between classes

------------------------------------------------------------------------

# 2. Scope

## 2.1 In Scope

The application must support:

1.  Creating and managing a camp.
2.  Creating participants, instructors and administrative staff.
3.  Creating different activity types.
4.  Assigning instructors to activities.
5.  Adding participants to activities through registration.
6.  Checking activity and camp capacity.
7.  Scheduling activities.
8.  Recording attendance.
9.  Managing equipment.
10. Allocating equipment to activities.
11. Performing safety checks.
12. Recording participant feedback.
13. Sending notifications through the `Notifiable` interface.
14. Generating a basic camp report.
15. Handling expected application errors through custom exceptions.
16. Storing reusable collections through the user-defined generic
    `Repository<T>` class.

## 2.2 Out of Scope

Unless explicitly added later, the first version does not require:

-   A web application
-   A mobile application
-   A database
-   Online payment processing
-   User authentication
-   Cloud deployment
-   External APIs
-   Maps/GPS integration
-   Email/SMS provider integration

The core requirement is a functional object-oriented application based
on the supplied class structure.

------------------------------------------------------------------------

# 3. Users and Roles

## 3.1 Participant

A participant can:

-   Register for an activity.
-   Cancel a registration.
-   View the camp schedule.
-   Participate in activities.
-   Submit feedback for an activity.

## 3.2 Instructor

An instructor can:

-   Be assigned to activities.
-   Remove an activity assignment.
-   Conduct an activity.
-   Check the participants of an activity.
-   Send notifications.
-   View/manage assigned activities.

## 3.3 Admin Staff

Administrative staff can:

-   Manage camp information.
-   Add participants.
-   Assign instructors.
-   Schedule activities.
-   Generate camp reports.
-   Send notifications.

------------------------------------------------------------------------

# 4. Functional Requirements

## FR-01: Camp Management

The system shall allow creation of a `Camp` containing:

-   Camp ID
-   Camp name
-   Start date
-   End date
-   Capacity
-   Status
-   A `CampSchedule`
-   A repository of registrations
-   A repository of safety checks

The `Camp` class shall coordinate major camp operations without
redundantly storing participants, instructors and activities in separate
lists.

### Required operations

-   `registerParticipant(Participant, Activity)`
-   `scheduleActivity(Activity, DateTime)`
-   `generateReport()`
-   `checkCapacity()`
-   `addSafetyCheck(SafetyCheck)`

------------------------------------------------------------------------

## FR-02: Person Management

`Person` shall be an abstract base class.

The following classes shall inherit from `Person`:

-   `Participant`
-   `Instructor`
-   `AdminStaff`

Common information shall remain in `Person`.

Specialized information and behavior shall remain in the subclasses.

------------------------------------------------------------------------

## FR-03: Participant Management

A `Participant` shall contain:

-   Skill level
-   Emergency contact
-   Medical record
-   Registrations

A participant shall be able to:

-   Register for an activity through the camp.
-   Cancel a registration.
-   View the camp schedule.
-   Submit feedback.

The participant's registrations shall be stored using:

`List<Registration>`

------------------------------------------------------------------------

## FR-04: Instructor Management

An `Instructor` shall contain:

-   Specialization
-   Certifications
-   Availability status
-   Assigned activities

Assigned activities shall be stored using:

`List<Activity>`

The instructor shall support:

-   Assigning an activity
-   Removing an activity
-   Conducting an activity
-   Checking activity participants
-   Sending notifications

The relationship between instructors and participants shall be mediated
through activities.

An instructor should not maintain a separate participant list.

------------------------------------------------------------------------

# 5. Activity Management

## FR-05: Activity Abstraction

`Activity` shall be an abstract class.

It shall contain:

-   Activity ID
-   Title
-   Description
-   Duration
-   Maximum participants
-   Start time
-   Location
-   Assigned instructor
-   Participants
-   Feedback
-   Equipment allocations

Participants shall be stored using:

`List<Participant>`

Feedback shall be stored using:

`List<Feedback>`

Equipment allocations shall be stored using:

`List<EquipmentAllocation>`

### Required operations

-   `checkAvailability()`
-   `conductActivity()`
-   `getStartTime()`
-   `getEndTime()`
-   `updateSchedule(DateTime)`
-   `addParticipant(Participant)`
-   `removeParticipant(Participant)`
-   `getParticipants()`
-   `addFeedback(Feedback)`

## FR-06: Activity Types

The system shall implement:

-   `AdventureActivity`
-   `WaterActivity`
-   `TeamBuildingActivity`

Each shall override `conductActivity()`.

Specialized activity behavior shall remain inside its corresponding
subclass.

Examples:

-   Adventure activities shall support terrain/risk checks.
-   Water activities shall support lifeguard and water-body information.
-   Team-building activities shall support team formation.

------------------------------------------------------------------------

# 6. Scheduling

## FR-07: Schedulable Interface

`Schedulable` shall define:

-   `getStartTime()`
-   `getEndTime()`
-   `updateSchedule(DateTime)`

`Activity` shall implement `Schedulable`.

This demonstrates interface implementation and allows activities to be
treated as schedulable objects.

## FR-08: Camp Schedule

`CampSchedule` shall manage scheduled activities through:

`Repository<ScheduleEntry>`

It shall support:

-   Adding an activity to the schedule
-   Removing an activity
-   Rescheduling an activity
-   Retrieving activities for a date
-   Displaying the schedule

`Camp.scheduleActivity()` shall delegate the detailed scheduling
operation to its `CampSchedule`.

## FR-09: Schedule Entry

`ScheduleEntry` shall represent one scheduled activity occurrence.

It shall contain:

-   Entry date
-   Activity
-   Start time
-   End time

It shall support updating its scheduled time.

------------------------------------------------------------------------

# 7. Registration

## FR-10: Participant Registration

A participant shall register for an activity through the camp.

Expected flow:

1.  Participant requests registration.
2.  Camp checks capacity.
3.  Activity availability is checked.
4.  Registration eligibility is checked.
5.  A `Registration` object is created.
6.  The registration is stored in the camp's `Repository<Registration>`.
7.  The registration is associated with its participant and activity.
8.  The activity receives the participant if registration succeeds.

## FR-11: Registration Lifecycle

`Registration` shall support:

-   Processing registration
-   Eligibility checking
-   Confirmation
-   Cancellation
-   Status retrieval

Possible status values may include:

-   Pending
-   Confirmed
-   Cancelled

The implementation should use constants or an enum if appropriate rather
than scattering literal status strings throughout the code.

------------------------------------------------------------------------

# 8. Attendance

## FR-12: Attendance Management

`Attendance` shall maintain attendance records for activity
participation.

It shall contain:

`Repository<AttendanceRecord>`

An `AttendanceRecord` shall contain:

-   Participant
-   Activity
-   Date
-   Present/absent status

The system shall support:

-   Marking a participant present
-   Marking a participant absent
-   Checking a participant's attendance
-   Generating an attendance report

Attendance shall be associated with the participant and activity through
`AttendanceRecord`.

------------------------------------------------------------------------

# 9. Equipment Management

## FR-13: Equipment Abstraction

`Equipment` shall be an abstract class.

It shall contain:

-   Equipment ID
-   Name
-   Availability
-   Condition

It shall support:

-   Condition checking
-   Allocation
-   Release
-   Activity compatibility/availability checking

## FR-14: Equipment Types

The system shall implement:

-   `SafetyEquipment`
-   `SportsEquipment`

Both shall inherit from `Equipment`.

`SafetyEquipment` shall additionally support safety inspection and
safety rating.

`SportsEquipment` shall support sport type, condition and activity
compatibility.

## FR-15: Equipment Allocation

`EquipmentAllocation` shall represent an equipment assignment to an
activity.

It shall contain:

-   Allocation ID
-   Allocation date
-   Return date
-   Status
-   Equipment
-   Activity

It shall support:

-   Allocating equipment
-   Returning equipment
-   Checking availability
-   Retrieving allocation status

The same equipment object should not be allocated to incompatible or
unavailable activities.

------------------------------------------------------------------------

# 10. Safety Management

## FR-16: Safety Checks

`SafetyCheck` shall record:

-   Check ID
-   Check date
-   Pass/fail result
-   Remarks

It shall support:

-   Performing an equipment safety check
-   Checking activity safety
-   Recording the result
-   Checking whether the inspection passed

`Camp` shall maintain safety checks using:

`Repository<SafetyCheck>`

Safety checks should be performed before an activity is allowed to
proceed when required safety conditions have not been satisfied.

------------------------------------------------------------------------

# 11. Location Management

## FR-17: Location

`Location` shall represent the physical location of an activity.

It shall contain:

-   Location ID
-   Name
-   Address
-   Terrain type
-   Capacity

It shall support:

-   Checking availability
-   Reserving a time
-   Releasing a reservation
-   Returning location details

An activity shall reference its assigned `Location`.

The implementation should prevent scheduling an activity at a location
when the location is unavailable for the requested time.

------------------------------------------------------------------------

# 12. Feedback

## FR-18: Activity Feedback

Participants shall be able to submit feedback for activities.

`Feedback` shall contain:

-   Feedback ID
-   Rating
-   Comments
-   Submitted date
-   Participant
-   Activity

The system shall support:

-   Submitting feedback
-   Updating comments
-   Retrieving feedback

Feedback shall be associated with both the participant who submitted it
and the activity being reviewed.

------------------------------------------------------------------------

# 13. Notifications

## FR-19: Notifiable Interface

`Notifiable` shall define:

`sendNotification(String)`

The following classes shall implement it:

-   `Instructor`
-   `AdminStaff`

The interface shall allow notification behavior to be handled
polymorphically.

The initial implementation may display notifications through the console
rather than integrating an external messaging service.

------------------------------------------------------------------------

# 14. User-Defined Generic Type

## FR-20: Repository`<T>`{=html}

The project shall implement a user-defined generic class:

`Repository<T>`

It shall internally use:

`List<T>`

### Required operations

-   `add(T)`
-   `remove(T)`
-   `get(int)`
-   `getAll()`
-   `size()`

The generic class shall be reusable for different domain objects.

Examples:

-   `Repository<Registration>`
-   `Repository<SafetyCheck>`
-   `Repository<ScheduleEntry>`
-   `Repository<AttendanceRecord>`

The implementation must demonstrate that the same generic class can
operate with different object types without duplicating
collection-management code.

------------------------------------------------------------------------

# 15. Exception Handling

## FR-21: Exception Hierarchy

All custom exceptions shall inherit from:

`CampException`

The subclasses shall be:

-   `RegistrationException`
-   `EquipmentUnavailableException`
-   `SafetyViolationException`
-   `CapacityExceededException`

## FR-22: Capacity Exception

`Camp.registerParticipant()` shall throw `CapacityExceededException`
when the camp or relevant activity cannot accept another participant.

## FR-23: Registration Exception

Registration processing shall throw `RegistrationException` when
registration cannot be completed because eligibility, status or other
registration rules fail.

## FR-24: Equipment Exception

`EquipmentAllocation.allocateEquipment()` shall throw
`EquipmentUnavailableException` when the requested equipment cannot be
allocated.

## FR-25: Safety Exception

Safety operations shall throw `SafetyViolationException` when required
safety conditions are not satisfied.

Exceptions shall be caught at an appropriate application/control layer
and presented as meaningful error messages.

------------------------------------------------------------------------

# 16. Core Business Workflows

## 16.1 Register Participant

``` text
Participant
    ↓
Camp.registerParticipant()
    ↓
Camp.checkCapacity()
    ↓
Activity.checkAvailability()
    ↓
Registration.checkEligibility()
    ↓
Create Registration
    ↓
Repository<Registration>.add()
    ↓
Activity.addParticipant()
```

Failure at a validation step shall result in the appropriate custom
exception.

## 16.2 Schedule Activity

``` text
AdminStaff / Camp
    ↓
Camp.scheduleActivity()
    ↓
CampSchedule.addActivityToSchedule()
    ↓
Create ScheduleEntry
    ↓
Repository<ScheduleEntry>.add()
```

## 16.3 Assign Instructor

``` text
Instructor.assignActivity()
    ↓
Activity.instructor = Instructor
    ↓
Instructor.assignedActivities.add(Activity)
```

An instructor's participant information shall be obtained from the
assigned activity rather than maintained separately.

## 16.4 Allocate Equipment

``` text
Activity
    ↓
EquipmentAllocation.allocateEquipment()
    ↓
Equipment.isAvailableFor(Activity)
    ↓
Equipment.allocate()
    ↓
Store EquipmentAllocation
```

If equipment is unavailable:

``` text
EquipmentUnavailableException
```

## 16.5 Record Attendance

``` text
Attendance.markActivityAttendance()
    ↓
Create/Update AttendanceRecord
    ↓
Repository<AttendanceRecord>
```

## 16.6 Perform Safety Check

``` text
SafetyCheck.performCheck()
    ↓
Equipment / Activity safety validation
    ↓
recordResult()
    ↓
Camp.safetyChecks
```

If required safety conditions fail:

``` text
SafetyViolationException
```

------------------------------------------------------------------------

# 17. Data and Storage Requirements

For the first implementation, data may be stored in memory using Java
collections.

No database is required.

The main storage structures are:

  -----------------------------------------------------------------------
  Domain                              Storage
  ----------------------------------- -----------------------------------
  Participant registrations           `List<Registration>` inside
                                      `Participant`

  Assigned activities                 `List<Activity>` inside
                                      `Instructor`

  Activity participants               `List<Participant>` inside
                                      `Activity`

  Activity feedback                   `List<Feedback>` inside `Activity`

  Activity equipment allocations      `List<EquipmentAllocation>` inside
                                      `Activity`

  Schedule entries                    `Repository<ScheduleEntry>`

  Camp registrations                  `Repository<Registration>`

  Camp safety checks                  `Repository<SafetyCheck>`

  Attendance records                  `Repository<AttendanceRecord>`
  -----------------------------------------------------------------------

The system should avoid maintaining multiple independent collections for
the same relationship unless there is a clear responsibility for each
collection.

------------------------------------------------------------------------

# 18. Package Structure

The implementation should follow the logical package structure
represented by the class diagram:

``` text
src/
└── main/
    └── java/
        ├── people/
        │   ├── Person.java
        │   ├── Participant.java
        │   ├── Instructor.java
        │   ├── AdminStaff.java
        │   ├── EmergencyContact.java
        │   └── MedicalRecord.java
        │
        ├── activities/
        │   ├── Activity.java
        │   ├── AdventureActivity.java
        │   ├── WaterActivity.java
        │   ├── TeamBuildingActivity.java
        │   ├── Schedulable.java
        │   ├── Location.java
        │   └── Feedback.java
        │
        ├── equipment/
        │   ├── Equipment.java
        │   ├── SafetyEquipment.java
        │   └── SportsEquipment.java
        │
        ├── management/
        │   ├── Camp.java
        │   ├── CampSchedule.java
        │   ├── ScheduleEntry.java
        │   ├── Registration.java
        │   ├── Attendance.java
        │   ├── AttendanceRecord.java
        │   ├── SafetyCheck.java
        │   ├── EquipmentAllocation.java
        │   ├── Notifiable.java
        │   └── Repository.java
        │
        └── exceptions/
            ├── CampException.java
            ├── RegistrationException.java
            ├── EquipmentUnavailableException.java
            ├── SafetyViolationException.java
            └── CapacityExceededException.java
```

The package names and final file locations should remain consistent with
the actual implementation.

------------------------------------------------------------------------

# 19. Non-Functional Requirements

## NFR-01: Object-Oriented Design

The implementation shall use the classes and relationships in
`docs/class_structure.md` rather than implementing the application as
one large procedural class.

## NFR-02: Maintainability

Each class shall have a clear responsibility.

Business logic should not be duplicated across multiple classes.

## NFR-03: Type Safety

The user-defined `Repository<T>` shall use Java generics rather than raw
collections.

Raw types should be avoided.

## NFR-04: Encapsulation

Attributes shall normally be private.

Access and modification should happen through appropriate methods.

## NFR-05: Validation

Invalid operations should be rejected using validation and the
appropriate custom exception.

## NFR-06: Extensibility

The design should allow additional activity types and equipment types to
be introduced through inheritance without changing the base abstractions
unnecessarily.

------------------------------------------------------------------------

# 20. Acceptance Criteria

The project is considered functionally complete when the following can
be demonstrated in a test/demo program:

### People

-   [ ] A participant can be created.
-   [ ] An instructor can be created.
-   [ ] An admin staff member can be created.
-   [ ] Common person behavior is inherited from `Person`.

### Activities

-   [ ] Adventure, water and team-building activities can be created.
-   [ ] Each activity type overrides `conductActivity()`.
-   [ ] An activity can have an instructor.
-   [ ] An activity can contain multiple participants.
-   [ ] An activity can have feedback and equipment allocations.

### Scheduling

-   [ ] An activity can be scheduled.
-   [ ] A scheduled activity is represented by a `ScheduleEntry`.
-   [ ] Activities can be rescheduled.
-   [ ] Activities for a specific date can be retrieved.

### Registration

-   [ ] A participant can register for an activity.
-   [ ] Registration status can be changed.
-   [ ] Registration can be cancelled.
-   [ ] Capacity is checked.
-   [ ] Invalid registration produces `RegistrationException`.
-   [ ] Capacity failure produces `CapacityExceededException`.

### Attendance

-   [ ] Attendance can be marked.
-   [ ] Attendance records identify the participant and activity.
-   [ ] Attendance reports can be generated.

### Equipment

-   [ ] Equipment can be allocated.
-   [ ] Equipment can be returned.
-   [ ] Availability is checked before allocation.
-   [ ] Unavailable equipment produces `EquipmentUnavailableException`.

### Safety

-   [ ] Equipment safety checks can be performed.
-   [ ] Activity safety can be checked.
-   [ ] Results and remarks can be recorded.
-   [ ] Safety violations produce `SafetyViolationException`.

### Feedback

-   [ ] A participant can submit feedback for an activity.
-   [ ] Feedback can be updated.
-   [ ] Feedback can be retrieved.

### Interfaces

-   [ ] `Activity` implements `Schedulable`.
-   [ ] `Instructor` implements `Notifiable`.
-   [ ] `AdminStaff` implements `Notifiable`.

### Generics

-   [ ] `Repository<T>` is implemented as a user-defined generic class.
-   [ ] It works with at least four different types.
-   [ ] It internally uses `List<T>`.
-   [ ] No raw `Repository` types are used.

### Exceptions

-   [ ] All custom exceptions inherit from `CampException`.
-   [ ] Exceptions are thrown from the appropriate business operations.
-   [ ] Exceptions are handled without terminating the application
    unexpectedly.

------------------------------------------------------------------------

# 21. Demonstration Scenario

The final application should be able to demonstrate a realistic
end-to-end scenario:

1.  Create an adventure camp.
2.  Create several participants.
3.  Create an instructor.
4.  Create an adventure activity.
5.  Assign the instructor to the activity.
6.  Assign a location.
7.  Schedule the activity.
8.  Register participants.
9.  Perform a safety check.
10. Allocate required equipment.
11. Conduct the activity.
12. Mark participant attendance.
13. Collect participant feedback.
14. Generate a camp report.
15. Demonstrate at least one handled exception.

The demonstration should show object interaction rather than simply
constructing objects without using their behavior.

------------------------------------------------------------------------

# 22. Implementation Priority

## Phase 1 --- Core Domain

Implement:

1.  `Person`
2.  `Participant`
3.  `Instructor`
4.  `AdminStaff`
5.  `Activity`
6.  Activity subclasses
7.  `Location`

## Phase 2 --- Camp and Scheduling

Implement:

1.  `Camp`
2.  `CampSchedule`
3.  `ScheduleEntry`
4.  `Schedulable`

## Phase 3 --- Registration and Attendance

Implement:

1.  `Registration`
2.  `Attendance`
3.  `AttendanceRecord`

## Phase 4 --- Equipment and Safety

Implement:

1.  `Equipment`
2.  `SafetyEquipment`
3.  `SportsEquipment`
4.  `EquipmentAllocation`
5.  `SafetyCheck`

## Phase 5 --- Feedback and Notifications

Implement:

1.  `Feedback`
2.  `Notifiable`
3.  Notification behavior

## Phase 6 --- Generic Repository

Implement:

1.  `Repository<T>`
2.  Replace applicable direct collection-management code with the
    repository.
3.  Demonstrate the repository with multiple types.

## Phase 7 --- Exceptions

Implement:

1.  `CampException`
2.  `RegistrationException`
3.  `EquipmentUnavailableException`
4.  `SafetyViolationException`
5.  `CapacityExceededException`

Integrate them into the relevant operations.

## Phase 8 --- Integration and Testing

Create a main/demo program that exercises the complete workflow and
verifies the acceptance criteria.

------------------------------------------------------------------------

# 23. Technical Constraints

-   Use Java.
-   Follow the class structure in `docs/class_structure.md`.
-   Use `List` for collection storage where appropriate.
-   Do not introduce `Map` unless the project requirements are
    explicitly changed.
-   Implement `Repository<T>` as a user-defined generic type.
-   Keep inheritance relationships consistent with the class diagram.
-   Keep interfaces as interfaces.
-   Keep custom exceptions in the exception hierarchy.
-   Avoid redundant collections that duplicate ownership of the same
    relationship.
-   Keep classes focused on their defined responsibilities.

------------------------------------------------------------------------

# 24. Definition of Done

The project is done when:

1.  All classes in the approved class structure are implemented.
2.  All inheritance relationships compile and work.
3.  All interfaces are implemented.
4.  Core relationships between participants, instructors, activities,
    equipment and management classes work.
5.  Registration, scheduling, attendance, safety, equipment allocation
    and feedback workflows execute successfully.
6.  Custom exceptions are thrown and handled correctly.
7.  `Repository<T>` is implemented and used with multiple domain types.
8.  The project compiles without errors.
9.  A demonstration program exercises the major use cases.
10. The implementation remains consistent with
    `docs/class_structure.md`.
