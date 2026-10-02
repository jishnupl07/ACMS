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
    
    public void returnEquipment() {
        this.status = "Returned";
        this.returnDate = LocalDate.now();
        this.equipment.setAvailable(true);
    }
    
    public Equipment getEquipment() { return equipment; }
    public Activity getActivity() { return activity; }
}
