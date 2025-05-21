import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class FCFS_Scheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read arrival times
        String[] arrivalStr = sc.nextLine().trim().split(" ");
        // Read burst times
        String[] burstStr = sc.nextLine().trim().split(" ");

        int n = arrivalStr.length;
        int[] arrival = new int[n];
        int[] burst = new int[n];
        for (int i = 0; i < n; i++) {
            arrival[i] = Integer.parseInt(arrivalStr[i]);
            burst[i] = Integer.parseInt(burstStr[i]);
        }

        // To ensure processes are sorted by arrival time (tie-breaker: input order)
        int[][] proc = new int[n][3]; // [arrival, burst, index]
        for (int i = 0; i < n; i++) {
            proc[i][0] = arrival[i];
            proc[i][1] = burst[i];
            proc[i][2] = i;
        }
        Arrays.sort(proc, Comparator.comparingInt(a -> a[0]));

        int currTime = 0;
        int[] waiting = new int[n];
        int[] turnaround = new int[n];

        for (int i = 0; i < n; i++) {
            int idx = proc[i][2];
            int arr = proc[i][0];
            int bur = proc[i][1];

            if (currTime < arr) currTime = arr; // CPU idle till process arrives

            waiting[idx] = currTime - arr;
            turnaround[idx] = waiting[idx] + bur;
            currTime += bur;
        }

        int totalWait = 0, totalTat = 0;
        for (int i = 0; i < n; i++) {
            totalWait += waiting[i];
            totalTat += turnaround[i];
        }

        System.out.println(totalWait / n);
        System.out.println(totalTat / n);
    }
}
