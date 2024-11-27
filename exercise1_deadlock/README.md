# Exercise 1: Java Thread Deadlock and Analysis

## Problem Statement

This exercise demonstrates a deadlock situation in Java, explains how to retrieve a thread dump, and provides steps to analyze it. Additionally, it covers strategies to resolve or avoid deadlocks in Java applications.

## Code Description

The code in `DeadlockExample.java` simulates a deadlock using two threads (`t1` and `t2`). Both threads attempt to acquire locks on two shared resources (`resource1` and `resource2`) in reverse order, leading to a deadlock.

- Dump example `deadlock-thread-dump.txt` included.

## How to Run the Code

1. **Compile the program**:
   ```bash
   javac DeadlockExample.java
   ```
2. **Run the program**:
   ```bash
   java DeadlockExample
   ```

## How to Generate a Thread Dump

To analyze a deadlock, you need a thread dump. Use the following steps to generate one:

1. Identify the PID (Process ID) of the running Java process using:
   ```bash
   jps
   ```
   This lists all running Java processes and their PIDs. Note: jps is an experimental tool and may not be available in all environments. It's possible 
to use a system process manager.
```bash
ps aux | grep java
```
In Windows a Java process PID can be found in Task Manager.

2. Generate the thread dump using the `jstack` tool:
   ```bash
   jstack <PID> > deadlock-thread-dump.txt
   ```

3. Alternatively, on Unix/Linux systems, you can use the `kill -3` command:
   ```bash
   kill -3 <PID>
   ```
   The thread dump is written to the standard output (or log file if redirected).

4. You can also use IDE tools like IntelliJ IDEA or Java Mission Control (JMC) to view and analyze thread dumps.

## Thread Dump Analysis

Analyze the thread dump to identify deadlocks:

1. Look for the `Found one Java-level deadlock` section. It explicitly lists threads involved in the deadlock and the resources they are blocking.

   Example:
   ```
   Found one Java-level deadlock:
   =============================
   "Thread-0":
   waiting to lock monitor 0x00006000022f4410 (object 0x0000000787e71bb8, a java.lang.Object),
   which is held by "Thread-1"

   "Thread-1":
   waiting to lock monitor 0x00006000022f8340 (object 0x0000000787e71ba8, a java.lang.Object),
   which is held by "Thread-0"
   ```

2. Check the thread states (`BLOCKED`, `WAITING`, etc.) and resource ownership.

3. Review the stack trace of each thread involved to understand the sequence of events.

## Understanding Deadlock

### Definition
A **deadlock** is a situation in a multithreaded application where two or more threads are blocked indefinitely, each waiting for the other to release a resource.

## Strategies to Resolve or Avoid Deadlocks

### 1. Avoid Nested Locks
Minimize acquiring multiple locks in the same block. For example:

```java
public class AvoidNestedLocksExample {
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public void performTask() {
        synchronized (resource1) {
            System.out.println("Locked resource 1");
            // Perform tasks that only need resource1
        }

        synchronized (resource2) {
            System.out.println("Locked resource 2");
            // Perform tasks that only need resource2
        }
    }
}
```

This reduces the chance of deadlocks by ensuring that no thread holds multiple locks at the same time.

### 2. Use Lock Ordering
When acquiring multiple locks is necessary, always acquire them in the same order.

```java
public class LockOrderingExample {
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public void performTask() {
        synchronized (resource1) {
            System.out.println("Locked resource 1");
            synchronized (resource2) {
                System.out.println("Locked resource 2");
                // Perform tasks that need both resources
            }
        }
    }
}
```

This ensures threads do not form a circular wait, eliminating deadlock risks.

### 3. Use `tryLock` from `ReentrantLock`
The `tryLock` method allows threads to attempt to acquire locks and fail gracefully if they cannot.

```java
import java.util.concurrent.locks.ReentrantLock;

public class TryLockExample {
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public void performTask() {
        if (lock1.tryLock()) {
            try {
                if (lock2.tryLock()) {
                    try {
                        System.out.println("Locked both resources");
                        // Perform tasks
                    } finally {
                        lock2.unlock();
                    }
                }
            } finally {
                lock1.unlock();
            }
        } else {
            System.out.println("Failed to acquire locks, retrying...");
        }
    }
}
```

### 4. Use Concurrency Utilities
Java's `java.util.concurrent` package provides tools like `ExecutorService`, `Semaphore`, and `CountDownLatch` to simplify multithreaded programming and reduce the risk of deadlocks.

## Summary
By following these strategies, you can prevent or mitigate deadlocks and ensure your application remains stable under concurrent loads.
