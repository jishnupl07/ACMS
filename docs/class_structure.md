---
config:
  layout: elk
---

classDiagram

%% =====================================================
%% PEOPLE
%% =====================================================

namespace People {

    class Person {
        <<abstract>>
        -String id
        -String name
        -int age
        -String contactNumber
        +getId() String
        +getName() String
        +setContactNumber(String) void
        +displayInfo()* void
    }

    class Participant {
        -String emergencyContact
        -List~String~ medicalNotes
        -String skillLevel
        -List~Registration~ registrations
        +register(Camp, Activity) Registration
        +cancelRegistration(Registration) void
        +submitFeedback(Activity, int, String) Feedback
        +displayInfo() void
    }

    class Instructor {
        -String specialization
        -List~String~ certifications
        -boolean isAvailable
        -List~Activity~ assignedActivities
        +assignActivity(Activity) void
        +removeActivity(Activity) void
        +conductActivity(Activity) void
        +checkParticipants(Activity) void
        +sendNotification(String) void
        +displayInfo() void
    }

    class AdminStaff {
        -String role
        -String department
        +manageCamp(Camp) void
        +manageRegistration(Registration) void
        +sendNotification(String) void
        +displayInfo() void
    }
}

Person <|-- Participant
Person <|-- Instructor
Person <|-- AdminStaff


%% =====================================================
%% ACTIVITIES
%% =====================================================

namespace Activities {

    class Activity {
        <<abstract>>
        -String activityId
        -String title
        -String description
        -int durationMinutes
        -int maxParticipants
        -DateTime startTime
        -Location location
        -Instructor instructor
        -List~Participant~ participants
        -List~Feedback~ feedbacks
        -List~EquipmentAllocation~ equipmentAllocations
        +checkAvailability() boolean
        +conductActivity()* void
        +getStartTime() DateTime
        +getEndTime() DateTime
        +updateSchedule(DateTime) void
        +addParticipant(Participant) void
        +removeParticipant(Participant) void
        +getParticipants() List~Participant~
        +addFeedback(Feedback) void
    }

    class AdventureActivity {
        -String terrainType
        -String riskLevel
        +conductActivity() void
    }

    class WaterActivity {
        -boolean lifeguardRequired
        -String waterBodyType
        +conductActivity() void
    }

    class TeamBuildingActivity {
        -int teamSize
        +conductActivity() void
    }

    class Schedulable {
        <<interface>>
        +getStartTime() DateTime
        +getEndTime() DateTime
        +updateSchedule(DateTime) void
    }

    class Location {
        -String locationId
        -String name
        -String address
        -String terrainType
        -int capacity
        +checkAvailability(DateTime) boolean
        +reserve(DateTime) void
        +release(DateTime) void
        +getLocationDetails() String
    }

    class Feedback {
        -String feedbackId
        -int rating
        -String comments
        -Date submittedDate
        -Participant participant
        -Activity activity
        +submit() void
        +updateComment(String) void
        +getFeedback() String
    }
}

Activity <|-- AdventureActivity
Activity <|-- WaterActivity
Activity <|-- TeamBuildingActivity
Schedulable <|.. Activity


%% =====================================================
%% EQUIPMENT
%% =====================================================

namespace Equipment {

    class Equipment {
        <<abstract>>
        -String equipmentId
        -String name
        -boolean isAvailable
        -String condition
        +checkCondition()* String
        +allocate() void
        +release() void
        +isAvailableFor(Activity) boolean
    }

    class SafetyEquipment {
        -Date lastInspectionDate
        -String safetyRating
        +checkCondition() String
    }

    class SportsEquipment {
        -String sportType
        -String condition
        +checkCondition() String
        +checkCompatibility(Activity) boolean
    }
}

Equipment <|-- SafetyEquipment
Equipment <|-- SportsEquipment


%% =====================================================
%% MANAGEMENT
%% =====================================================

namespace Management {

    class Camp {
        -String campId
        -String campName
        -Date startDate
        -Date endDate
        -int capacity
        -String status
        -CampSchedule schedule
        -Repository~Registration~ registrations
        -Repository~SafetyCheck~ safetyChecks
        +registerParticipant(Participant, Activity) Registration
        +scheduleActivity(Activity, DateTime) void
        +generateReport() String
        +checkCapacity() boolean
        +addSafetyCheck(SafetyCheck) void
    }

    class CampSchedule {
        -Repository~ScheduleEntry~ entries
        +addActivityToSchedule(Activity, Date) void
        +removeActivity(Activity) void
        +rescheduleActivity(Activity, DateTime) void
        +getActivitiesForDate(Date) List~Activity~
        +displaySchedule() String
    }

    class ScheduleEntry {
        -String entryId
        -Date date
        -DateTime startTime
        -DateTime endTime
        -Activity activity
        +updateTime(DateTime, DateTime) void
        +getActivity() Activity
    }

    class Registration {
        -String registrationId
        -Date registrationDate
        -String status
        -Participant participant
        -Activity activity
        +processRegistration() void
        +cancelRegistration() void
        +confirmRegistration() void
    }

    class Attendance {
        -Date date
        -Repository~AttendanceRecord~ records
        +markAttendance(Participant, Activity, boolean) void
        +getParticipantAttendance(Participant) boolean
        +getAttendanceReport() String
    }

    class AttendanceRecord {
        -Participant participant
        -Activity activity
        -Date date
        -boolean present
        +markPresent() void
        +markAbsent() void
        +isPresent() boolean
    }

    class SafetyCheck {
        -String checkId
        -Date checkDate
        -boolean passed
        -String remarks
        +performCheck(Equipment) void
        +checkActivitySafety(Activity) boolean
        +recordResult(boolean, String) void
        +isPassed() boolean
    }

    class EquipmentAllocation {
        -String allocationId
        -Date allocationDate
        -Date returnDate
        -String status
        -Equipment equipment
        -Activity activity
        +allocateEquipment(Equipment, Activity) void
        +returnEquipment() void
        +checkAvailability() boolean
        +getAllocationStatus() String
    }

    class Notifiable {
        <<interface>>
        +sendNotification(String) void
    }

    class Repository~T~ {
        -List~T~ items
        +add(T) void
        +remove(T) void
        +get(int) T
        +getAll() List~T~
        +size() int
    }
}

Notifiable <|.. Instructor
Notifiable <|.. AdminStaff


%% =====================================================
%% EXCEPTIONS
%% =====================================================

namespace Exceptions {

    class CampException {
        <<exception>>
        -String message
        +CampException(String)
        +getMessage() String
    }

    class RegistrationException {
        <<exception>>
        +RegistrationException(String)
    }

    class EquipmentUnavailableException {
        <<exception>>
        +EquipmentUnavailableException(String)
    }

    class SafetyViolationException {
        <<exception>>
        +SafetyViolationException(String)
    }

    class CapacityExceededException {
        <<exception>>
        +CapacityExceededException(String)
    }
}

CampException <|-- RegistrationException
CampException <|-- EquipmentUnavailableException
CampException <|-- SafetyViolationException
CampException <|-- CapacityExceededException