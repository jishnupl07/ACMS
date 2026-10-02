package people;

import management.Notifiable;

public class AdminStaff extends Person implements Notifiable {
    private String role;

    public AdminStaff(String id, String name, int age, String contactNumber, String role) {
        super(id, name, age, contactNumber);
        this.role = role;
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin [" + getId() + "] " + getName() + " - Role: " + role);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Admin " + getName() + ": " + message);
    }
}
