package management;

import java.time.LocalDateTime;

/**
 * Base abstract class for all camp domain entities.
 * Serves as the upper bound for bounded generic types (e.g. Repository<T extends CampEntity>).
 */
public abstract class CampEntity {
    protected String id;
    protected String name;
    protected LocalDateTime createdAt;

    public CampEntity(String id) {
        this(id, id);
    }

    public CampEntity(String id, String name) {
        this.id = id;
        this.name = (name != null && !name.trim().isEmpty()) ? name : id;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Concrete method providing general entity details.
     * Overridden by subclasses to demonstrate normal method overriding of non-abstract methods.
     */
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s", getClass().getSimpleName(), id, name);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
