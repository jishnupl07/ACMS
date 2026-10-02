package management;
import equipment.Equipment;
import activities.Activity;
import java.time.LocalDate;

public class EquipmentAllocation {
    private String allocationId;
    private LocalDate allocationDate;
    private LocalDate returnDate;
    private String status;
    private Equipment equipment;
    private Activity activity;

    public EquipmentAllocation(String allocationId, Equipment equipment, Activity activity) {
        this.allocationId = allocationId;
        this.equipment = equipment;
        this.activity = activity;
        this.allocationDate = LocalDate.now();
        this.status = "Allocated";
        equipment.setAvailable(false);
    }

    public EquipmentAllocation(String allocationId, Equipment equipment, Activity activity, LocalDate allocationDate, LocalDate returnDate, String status) {
        this.allocationId = allocationId;
        this.equipment = equipment;
        this.activity = activity;
        this.allocationDate = allocationDate;
        this.returnDate = returnDate;
        this.status = status;
    }
    
    public void returnEquipment() {
        this.status = "Returned";
        this.returnDate = LocalDate.now();
        this.equipment.setAvailable(true);
    }
    
    public String getAllocationId() { return allocationId; }
    public LocalDate getAllocationDate() { return allocationDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
    public Equipment getEquipment() { return equipment; }
    public Activity getActivity() { return activity; }
}
