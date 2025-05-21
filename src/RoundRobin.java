import java.util.*;

class Process1 {
    int pid, arrival, burst, remaining, completion;
    public Process1(int pid, int arrival, int burst) {
        this.pid = pid; this.arrival = arrival; this.burst = burst;
        this.remaining = burst; this.completion = 0;
    }
}

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] arr = sc.nextLine().split(" ");
        String[] bur = sc.nextLine().split(" ");
        int timeQuantum = Integer.parseInt(sc.nextLine().trim());
        int n = arr.length;
        Process1[] procs = new Process1[n];
        for (int i = 0; i < n; i++)
            procs[i] = new Process1(i, Integer.parseInt(arr[i]), Integer.parseInt(bur[i]));

        Queue<Process1> queue = new LinkedList<>();
        int time = 0, completed = 0, idx = 0;
        boolean[] inQueue = new boolean[n];
        int[] waiting = new int[n], turnaround = new int[n];

        while (completed < n) {
            // Add newly arrived processes
            while (idx < n && procs[idx].arrival <= time) {
                queue.add(procs[idx]);
                inQueue[procs[idx].pid] = true;
                idx++;
            }
            // If no process is ready, jump to the next arrival time
            if (queue.isEmpty()) {
                if (idx < n) time = procs[idx].arrival;
                continue;
            }
            Process1 curr = queue.poll();
            // Execute the process for a time slice, or until completion
            int exec = Math.min(timeQuantum, curr.remaining);
            // If the process is not yet arrived, wait until it does
            time = Math.max(time, curr.arrival);
            curr.remaining -= exec;
            time += exec;

            // Add processes that arrived during execution
            while (idx < n && procs[idx].arrival <= time) {
                if (!inQueue[procs[idx].pid]) {
                    queue.add(procs[idx]);
                    inQueue[procs[idx].pid] = true;
                }
                idx++;
            }
            if (curr.remaining == 0) {
                curr.completion = time;
                completed++;
                turnaround[curr.pid] = curr.completion - curr.arrival;
                waiting[curr.pid] = turnaround[curr.pid] - curr.burst;
            } else {
                queue.add(curr);
            }
        }
        int tw = 0, tt = 0;
        for (int i = 0; i < n; i++) { tw += waiting[i]; tt += turnaround[i]; }
        System.out.println(tw / n);
        System.out.println(tt / n);
    }
}
