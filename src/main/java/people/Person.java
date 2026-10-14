package people;

import management.CampEntity;

public abstract class Person extends CampEntity {
    private int age;
    private String contactNumber;

    public Person(String id, String name, int age, String contactNumber) {
        super(id, name);
        this.age = age;
        this.contactNumber = contactNumber;
    }

    public int getAge() { return age; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    /**
     * Concrete method in Person.
     * Demonstrates normal method overriding when overridden by subclasses.
     */
    public String getContactInfo() {
        return getName() + " (Contact: " + contactNumber + ")";
    }

    /**
     * Normal method overriding of concrete getDetails() from CampEntity.
     */
    @Override
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s | Age: %d | Contact: %s",
                getClass().getSimpleName(), getId(), getName(), age, contactNumber);
    }

    public abstract void displayInfo();
}
