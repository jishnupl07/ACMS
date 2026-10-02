package management;

public class Location {
    private String locationId;
    private String name;
    private String address;
    private String terrainType;
    private int capacity;

    public Location(String locationId, String name, String address, String terrainType, int capacity) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.terrainType = terrainType;
        this.capacity = capacity;
    }

    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    
    public void displayDetails() {
        System.out.println("Location: " + name + " (" + capacity + " max)");
    }
    public String getAddress() { return address; }
    public String getTerrainType() { return terrainType; }
}
