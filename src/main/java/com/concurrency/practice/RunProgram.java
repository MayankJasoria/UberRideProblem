package com.concurrency.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

public class RunProgram {
    public static void main(String[] args) throws InterruptedException {
        int numDemocrats = ThreadLocalRandom.current().nextInt(0, 20) * 2;
        int numRepublicans = ThreadLocalRandom.current().nextInt(0, 20) * 2;
        int numPassengers = numDemocrats + numRepublicans;
        if (numPassengers % 4 != 0) {
            // at least one side is not divisible by 4
            if (numDemocrats % 4 != 0) {
                numRepublicans += 2;
            } else {
                numDemocrats += 2;
            }
            numPassengers += 2;
        }
        System.out.println("Number of democrats: " + numDemocrats + "\nNumber of republicans: " + numRepublicans);
        final UberRide ride = new UberRide();
        List<Thread> passengers = new ArrayList<>();
        for (int i = 0; i < numPassengers; ++i) {
            Thread thread = new Thread(() -> {
                try {
                    ride.seated();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
            if (numRepublicans == 0) {
                thread.setName("Democrat-" + i);
                --numDemocrats;
            } else if (numDemocrats == 0) {
                thread.setName("Republican-" + i);
                --numRepublicans;
            } else {
                int selector = ThreadLocalRandom.current().nextInt(0, 2);
                if (selector == 0) {
                    thread.setName("Democrat-" + i);
                    --numDemocrats;
                } else {
                    thread.setName("Republican-" + i);
                    --numRepublicans;
                }
            }
            passengers.add(thread);
        }

        for (Thread t : passengers) {
            t.start();
        }

        for (Thread t : passengers) {
            t.join();
        }

        System.out.println("All passengers have been accommodated into Ubers");
    }
}
