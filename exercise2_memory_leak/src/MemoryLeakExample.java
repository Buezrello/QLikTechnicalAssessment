import java.util.HashMap;
import java.util.Map;

/**
 * Memory leak simulation.
 * The UniqueKey objects are stored in the HashMap but are never removed.
 * They remain strongly referenced by the map even though they are no longer needed.
 */
public class MemoryLeakExample {
    // Simulating a record that generates memory leaks
    private record UniqueKey(String name) { }

    public static void main(String[] args) {
        Map<UniqueKey, String> cache = new HashMap<>();

        // Populating the map with objects that aren't properly cleaned up
        for (int i = 0; i < 500_000; i++) {
            UniqueKey uniqueKey = new UniqueKey("Object-" + i);
            cache.put(uniqueKey, "Value for " + i);
        }

        System.out.println("Cache populated with 500,000 entries. Observe memory usage...");

        // Simulating an application that keeps running
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
