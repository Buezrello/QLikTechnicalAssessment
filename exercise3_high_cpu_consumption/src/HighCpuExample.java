/**
 * High CPU consumption simulation.
 * Program creates multiple threads performing computationally expensive calculations
 * in an infinite loop.
 */
public class HighCpuExample {
    public static void main(String[] args) {
        // Create multiple threads to simulate high CPU consumption
        for (int i = 0; i < 4; i++) {
            Thread cpuThread = new Thread(() -> {
                while (true) {
                    // Simulate CPU-intensive work
                    Math.pow(Math.random(), Math.random());
                }
            }, "HighCpuThread-" + i);
            cpuThread.setDaemon(true);
            cpuThread.start();
        }

        System.out.println("High CPU consumption simulation started. Monitor the CPU usage.");

        // Keep the application running to observe CPU usage
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

