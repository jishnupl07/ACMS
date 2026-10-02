import os

files = {}

files["src/main/java/exceptions/CampException.java"] = """
package exceptions;
public class CampException extends Exception {
    public CampException(String message) { super(message); }
}
"""
files["src/main/java/exceptions/RegistrationException.java"] = """
package exceptions;
public class RegistrationException extends CampException {
    public RegistrationException(String message) { super(message); }
}
"""
files["src/main/java/exceptions/EquipmentUnavailableException.java"] = """
package exceptions;
public class EquipmentUnavailableException extends CampException {
    public EquipmentUnavailableException(String message) { super(message); }
}
"""
files["src/main/java/exceptions/SafetyViolationException.java"] = """
package exceptions;
public class SafetyViolationException extends CampException {
    public SafetyViolationException(String message) { super(message); }
}
"""
files["src/main/java/exceptions/CapacityExceededException.java"] = """
package exceptions;
public class CapacityExceededException extends CampException {
    public CapacityExceededException(String message) { super(message); }
}
"""

files["src/main/java/management/Notifiable.java"] = """
package management;
public interface Notifiable {
    void sendNotification(String message);
}
"""
files["src/main/java/management/Schedulable.java"] = """
package management;
import java.time.LocalDateTime;
public interface Schedulable {
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    void updateSchedule(LocalDateTime startTime);
}
"""
files["src/main/java/management/Repository.java"] = """
package management;
import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private List<T> items = new ArrayList<>();
    
    public void add(T item) { items.add(item); }
    public void remove(T item) { items.remove(item); }
    public T get(int index) { return items.get(index); }
    public List<T> getAll() { return new ArrayList<>(items); }
    public int size() { return items.size(); }
}
"""

for path, content in files.items():
    d = os.path.dirname(path)
    if d:
        os.makedirs(d, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content.strip() + "\n")
print("Phase 1 & 2 base setup generated.")
