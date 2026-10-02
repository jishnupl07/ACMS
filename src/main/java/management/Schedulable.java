package management;
import java.time.LocalDateTime;
public interface Schedulable {
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    void updateSchedule(LocalDateTime startTime);
}
