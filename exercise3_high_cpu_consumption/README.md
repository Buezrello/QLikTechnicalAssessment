# Exercise 3: CPU Consumption Implementation and Analysis

## Problem Statement

This exercise demonstrates how to monitor and analyze the CPU consumption of a Java application and identify the part of the code responsible for high CPU usage. The steps include commands and tools for effective analysis.

---

## Code Description

The `HighCpuExample.java` program creates multiple threads performing computationally expensive calculations in an infinite loop. This simulates high CPU usage, which is then analyzed using thread dumps and optional monitoring tools.

### Key Features:
- Spawns 4 threads performing continuous operations using `Math.random` in an infinite loop.
- The program runs indefinitely, allowing time to observe and analyze CPU usage.
- Dump example `thread-dump.txt` included.

---

## How to Run the Code

1. **Compile the program**:
   ```bash
   javac HighCpuExample.java
   ```

2. **Run the program**:
   ```bash
   java HighCpuExample
   ```

3. The program will display:
   ```
   High CPU consumption simulation started. Monitor the CPU usage.
   ```

---

## Steps to Monitor and Analyze CPU Usage

### **Step 1: Identify the High-CPU Process**

1. Use `top` to monitor CPU usage (Linux/macOS):
   ```bash
   top
   ```
    - Look under the `%CPU` column for the Java process consuming high CPU.
    - Note the **PID** of the process.

2. Example output from `top`:
   ```
   PID   USER    PR  NI    VIRT    RES   SHR S  %CPU   TIME+   COMMAND
   32728 user    20   0  125116   4500   2004 R  372.5  1:23.45 java
   ```
    - Here, the process with PID `32728` is consuming `372.5%` of the CPU.

---

### **Step 2: Generate a Thread Dump**

Once you identify the high-CPU process:
1. Use `jstack` to generate a thread dump for the Java process:
   ```bash
   jstack <PID> > thread-dump.txt
   ```
2. The thread dump contains detailed information about all threads in the JVM, including their states and stack traces.

---

### **Step 3: Analyze the Thread Dump**

1. Open the `thread-dump.txt` file and search for threads in the `RUNNABLE` state.
2. Look for threads with high **CPU time** (`cpu=` field).

#### Example from the Thread Dump:
```
"HighCpuThread-3" #17 daemon prio=5 os_prio=31 cpu=727057.81ms elapsed=1022.61s tid=0x00007f88fd0d2a00 nid=0x6703 runnable  [0x0000700010cc9000]
   java.lang.Thread.State: RUNNABLE
        at java.util.Random.next(java.base@17.0.11/Random.java:209)
        at java.util.Random.nextDouble(java.base@17.0.11/Random.java:463)
        at java.lang.Math.random(java.base@17.0.11/Math.java:865)
        at HighCpuExample.lambda$main$0(HighCpuExample.java:8)
        at HighCpuExample$$Lambda$14/0x0000000129001208.run(Unknown Source)
        at java.lang.Thread.run(java.base@17.0.11/Thread.java:842)
```

#### Key Observations:
- **Thread Name**: `"HighCpuThread-3"`
- **CPU Time**: `727057.81ms` – This indicates excessive CPU usage.
- **Code Responsible**:
    - The stack trace shows `Math.random` in `HighCpuExample.lambda$main$0(HighCpuExample.java:8)` is consuming the CPU.

---

### **Optional: Use Advanced Tools**

#### **Java Mission Control (JMC)**
1. Start the application with Flight Recorder enabled:
   ```bash
   java -XX:StartFlightRecording=filename=high-cpu.jfr,duration=60s HighCpuExample
   ```
2. Open the `.jfr` file in JMC:
    - Navigate to the **Thread View** tab to identify threads consuming the most CPU.
    - Inspect their stack traces to pinpoint the problematic code.

#### **jconsole**
1. Launch `jconsole`:
   ```bash
   jconsole
   ```
2. Attach to the Java process.
3. Navigate to the **Threads** tab:
    - Identify active threads and their states.

#### **perf (Linux Only)**
1. Use `perf` to profile the process:
   ```bash
   perf top -p <PID>
   ```
2. Identify methods consuming the most CPU.

---

## Conclusion

### **Key Takeaways**:
1. **Efficient Monitoring**:
    - Use `top` and `jstack` for straightforward identification of problematic threads.
2. **Comprehensive Analysis**:
    - Thread dumps directly point to the code responsible for high CPU usage.
3. **Optional Tools**:
    - Tools like JMC and `perf` provide deeper insights for advanced scenarios.

This approach works effectively for analyzing CPU usage and identifying performance bottlenecks in Java applications.
