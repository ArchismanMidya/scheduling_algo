import java.util.*;

class Process2 {
    int pid, arrival, burst, priority, completion;
    public Process2(int pid, int arrival, int burst, int priority) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.completion = 0;
    }
}

public class PriorityScheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        String[] arr = sc.nextLine().split(" ");
        String[] bur = sc.nextLine().split(" ");
        String[] prio = sc.nextLine().split(" ");

        int n = arr.length;

        Process2[] procs = new Process2[n];
        for (int i = 0; i < n; i++)
            procs[i] = new Process2(
                    i,
                    Integer.parseInt(arr[i]),
                    Integer.parseInt(bur[i]),
                    Integer.parseInt(prio[i])
            );

        int time = 0;
        int completed = 0;            // Number of completed processes
        boolean[] done = new boolean[n]; // Tracks which processes have finished

        int[] waiting = new int[n];      // Waiting times for each process
        int[] turnaround = new int[n];   // Turnaround times for each process

        while (completed < n) {
            int best = -1; // Index of the best candidate process to run

            // Find the eligible process with highest priority (lowest value)
            for (int i = 0; i < n; i++) {
                if (!done[i] && procs[i].arrival <= time) {
                    // If 'best' not set or current process has better priority
                    // or same priority but earlier arrival time
                    if (
                            best == -1 ||
                                    procs[i].priority < procs[best].priority ||
                                    (procs[i].priority == procs[best].priority &&
                                            procs[i].arrival < procs[best].arrival)
                    ) {
                        best = i;
                    }
                }
            }

            if (best == -1) {
                // No process is ready at this moment, so move time forward
                time++;
                continue;
            }

            // Process2 'best' is selected for execution
            Process2 p = procs[best];

            // Ensure current time is not before process arrival (handles idle gaps)
            time = Math.max(time, p.arrival);

            // Waiting time: time process waits in ready queue
            waiting[p.pid] = time - p.arrival;

            // Run the process to completion (non-preemptive)
            time += p.burst;

            // Record completion time for turnaround calculation
            p.completion = time;

            // Turnaround time: total time from arrival to completion
            turnaround[p.pid] = p.completion - p.arrival;

            // Mark this process as done
            done[best] = true;
            completed++;
        }

        int tw = 0, tt = 0;
        for (int i = 0; i < n; i++) {
            tw += waiting[i];
            tt += turnaround[i];
        }

        System.out.println(tw / n);
        System.out.println(tt / n);
    }
}

