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
