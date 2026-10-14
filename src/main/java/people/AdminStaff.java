package people;

import management.Notifiable;

public class AdminStaff extends Person implements Notifiable {
    private String role;

    public AdminStaff(String id, String name, int age, String contactNumber, String role) {
        super(id, name, age, contactNumber);
        this.role = role;
    }

    public String getRole() { return role; }

    // --- Normal Method Overriding (overriding concrete methods from superclass) ---

    @Override
    public String getContactInfo() {
        return super.getContactInfo() + " [Admin - Role: " + role + "]";
    }

    @Override
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s | Role: %s",
                getClass().getSimpleName(), getId(), getName(), role);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void displayInfo() {
        System.out.println("Admin [" + getId() + "] " + getName() + " - Role: " + role);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Admin " + getName() + ": " + message);
    }
}
