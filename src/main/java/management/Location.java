package management;

public class Location extends CampEntity {
    private String address;
    private String terrainType;
    private int capacity;

    public Location(String locationId, String name, String address, String terrainType, int capacity) {
        super(locationId, name);
        this.address = address;
        this.terrainType = terrainType;
        this.capacity = capacity;
    }

    public String getLocationId() { return getId(); }
    public int getCapacity() { return capacity; }
    public String getAddress() { return address; }
    public String getTerrainType() { return terrainType; }

    // --- Method Overloading: displayDetails ---

    public void displayDetails() {
        displayDetails(false);
    }

    /** Overloaded displayDetails: optionally prints detailed address and terrain info. */
    public void displayDetails(boolean includeAddress) {
        if (includeAddress) {
            System.out.println(String.format("Location: %s (%s) | Address: %s | Terrain: %s | Max Capacity: %d",
                    getName(), getLocationId(), address, terrainType, capacity));
        } else {
            System.out.println("Location: " + getName() + " (" + capacity + " max)");
        }
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s | Terrain: %s | Capacity: %d",
                getClass().getSimpleName(), getId(), getName(), terrainType, capacity);
    }
}
