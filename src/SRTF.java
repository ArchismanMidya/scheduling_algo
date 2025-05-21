import java.util.*;

public class SRTF {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] arrStr = sc.nextLine().trim().split(" ");
        int n = arrStr.length;
        int[] arrival = new int[n];
        for (int i = 0; i < n; i++) arrival[i] = Integer.parseInt(arrStr[i]);

        // Read burst times
        String[] burstStr = sc.nextLine().trim().split(" ");
        int[] burst = new int[n];
        for (int i = 0; i < n; i++) burst[i] = Integer.parseInt(burstStr[i]);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++)
            processes.add(new Process(i, arrival[i], burst[i]));

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrival));

        // PriorityQueue: process with smallest remaining time at the top
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator
                .comparingInt((Process p) -> p.remaining)
                .thenComparingInt(p -> p.arrival)
                .thenComparingInt(p -> p.pid));

        int time = 0, completed = 0;
        int[] waiting = new int[n];
        int[] turnaround = new int[n];
        int idx = 0; // Next process to arrive

        // Add all processes that have arrived by 'time'
        while(completed < n){
            while(idx<n && processes.get(idx).arrival <= time){
                pq.add(processes.get(idx));
                idx++;
            }

            if (pq.isEmpty()) {
                // No process ready, jump to next arrival time
                if (idx < n) time = processes.get(idx).arrival;
                continue;
            }

            Process curr = pq.poll();
            time++;
            curr.remaining--;

            // If process finishes
            if (curr.remaining == 0) {
                curr.completion = time;
                completed++;
                turnaround[curr.pid] = curr.completion - curr.arrival;
                waiting[curr.pid] = turnaround[curr.pid] - curr.burst;
            } else {
                // Not finished, push back with updated remaining
                pq.add(curr);
            }
        }

        int totalWaiting = 0, totalTurnaround = 0;
        for (int i = 0; i < n; i++) {
            totalWaiting += waiting[i];
            totalTurnaround += turnaround[i];
        }
        System.out.println(totalWaiting / n);
        System.out.println(totalTurnaround / n);
    }
}

class Process {
    int pid;             // Process ID
    int arrival;         // Arrival Time
    int burst;           // Burst Time (original)
    int remaining;       // Remaining Time
    int completion;      // Completion Time

    public Process(int pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
        this.completion = 0;
    }
}


