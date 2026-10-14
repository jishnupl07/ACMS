package management;

import equipment.Equipment;
import activities.Activity;
import java.time.LocalDate;

public class EquipmentAllocation extends CampEntity {
    private LocalDate allocationDate;
    private LocalDate returnDate;
    private String status;
    private Equipment equipment;
    private Activity activity;

    public EquipmentAllocation(String allocationId, Equipment equipment, Activity activity) {
        super(allocationId, "Alloc-" + allocationId);
        this.equipment = equipment;
        this.activity = activity;
        this.allocationDate = LocalDate.now();
        this.status = "Allocated";
        equipment.setAvailable(false);
    }

    public EquipmentAllocation(String allocationId, Equipment equipment, Activity activity, LocalDate allocationDate, LocalDate returnDate, String status) {
        super(allocationId, "Alloc-" + allocationId);
        this.equipment = equipment;
        this.activity = activity;
        this.allocationDate = allocationDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // --- Method Overloading: returnEquipment ---

    public void returnEquipment() {
        returnEquipment(LocalDate.now());
    }

    /** Overloaded returnEquipment: allows specifying a custom return date. */
    public void returnEquipment(LocalDate returnDate) {
        this.status = "Returned";
        this.returnDate = returnDate;
        if (equipment != null) {
            equipment.setAvailable(true);
        }
    }

    public String getAllocationId() { return getId(); }
    public LocalDate getAllocationDate() { return allocationDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
    public Equipment getEquipment() { return equipment; }
    public Activity getActivity() { return activity; }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String eqName = equipment != null ? equipment.getName() : "None";
        String aTitle = activity != null ? activity.getTitle() : "None";
        return String.format("[%s] ID: %s | Equipment: %s | Activity: %s | Status: %s",
                getClass().getSimpleName(), getId(), eqName, aTitle, status);
    }
}
