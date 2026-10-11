package management;

@FunctionalInterface
public interface Notifiable {
    void sendNotification(String message);
}
