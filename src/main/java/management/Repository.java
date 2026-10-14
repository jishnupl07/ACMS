package management;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic repository demonstrating bounded type parameters.
 * T is bounded by CampEntity (T extends CampEntity).
 * This ensures that every element stored in this repository possesses CampEntity features (id, name, etc.).
 */
public class Repository<T extends CampEntity> {
    private List<T> items = new ArrayList<>();

    // --- Method Overloading: add ---

    /** Adds a single entity to the repository. */
    public void add(T item) { 
        items.add(item); 
    }

    /** Overloaded add: adds a list of entities to the repository. */
    public void add(List<T> newItems) {
        items.addAll(newItems);
    }

    // --- Method Overloading: remove ---

    /** Removes a specific entity from the repository. */
    public void remove(T item) { 
        items.remove(item); 
    }

    /** Overloaded remove: removes an entity by its string ID, enabled by bounded type CampEntity. */
    public boolean remove(String id) {
        return items.removeIf(item -> item.getId() != null && item.getId().equalsIgnoreCase(id));
    }

    // --- Method Overloading: get ---

    /** Gets an entity by list index. */
    public T get(int index) { 
        return items.get(index); 
    }

    /** Overloaded get: retrieves an entity by its CampEntity ID, enabled by bounded type. */
    public Optional<T> get(String id) {
        return findById(id);
    }

    public List<T> getAll() { return new ArrayList<>(items); }
    public int size() { return items.size(); }

    /** Bounded type lookup: directly accesses item.getId() from CampEntity. */
    public Optional<T> findById(String id) {
        return findFirst(item -> item.getId() != null && item.getId().equalsIgnoreCase(id));
    }

    // --- Lambda & Functional Operations ---

    /** Finds the first item matching the given predicate lambda. */
    public Optional<T> findFirst(Predicate<T> predicate) {
        return items.stream().filter(predicate).findFirst();
    }

    /** Filters items matching the given predicate lambda. */
    public List<T> filter(Predicate<T> predicate) {
        return items.stream().filter(predicate).collect(Collectors.toList());
    }

    /** Performs an action on each item using a consumer lambda. */
    public void forEach(Consumer<T> action) {
        items.forEach(action);
    }

    /** Removes all items that satisfy the given predicate lambda. */
    public boolean removeIf(Predicate<T> predicate) {
        return items.removeIf(predicate);
    }

    /** Maps items to another type using a mapping function lambda. */
    public <R> List<R> map(Function<T, R> mapper) {
        return items.stream().map(mapper).collect(Collectors.toList());
    }

    /** Sorts items in place using a comparator lambda. */
    public void sort(Comparator<T> comparator) {
        items.sort(comparator);
    }

    /** Checks if any item satisfies the given predicate lambda. */
    public boolean anyMatch(Predicate<T> predicate) {
        return items.stream().anyMatch(predicate);
    }

    /** Checks if all items satisfy the given predicate lambda. */
    public boolean allMatch(Predicate<T> predicate) {
        return items.stream().allMatch(predicate);
    }

    /** Counts how many items satisfy the given predicate lambda. */
    public long count(Predicate<T> predicate) {
        return items.stream().filter(predicate).count();
    }
}
