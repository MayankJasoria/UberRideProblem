package com.concurrency.practice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class UberRide {

    private final Semaphore democrats;
    private final Semaphore republicans;
    private final Semaphore findSeat;
    private final CyclicBarrier barrier;
    private int numDemocrats;
    private int numRepublicans;
    private final ReentrantLock lock;

    public UberRide() {
        democrats = new Semaphore(0);
        republicans = new Semaphore(0);
        findSeat = new Semaphore(4);
        numDemocrats = 0;
        numRepublicans = 0;
        lock = new ReentrantLock();
        barrier = new CyclicBarrier(4, this::drive);
    }

    private void drive() {
        System.out.println("Thread : " + Thread.currentThread().getName() + " signalled driver to drive away");
        findSeat.release(4);
    }

    private void seatDemocrat() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        if (numDemocrats == 3) {
            // allow 3 other waiting democrats to take this ride
            democrats.release(3);
            numDemocrats -= 3;
            findSeat.acquire();
            lock.unlock();
        } else if (numDemocrats == 1 && numRepublicans >= 2) {
            // allow 1 waiting democrat and 2 waiting republicans to take this ride
            democrats.release(1);
            republicans.release(2);
            numDemocrats -= 1;
            numRepublicans -= 2;
            findSeat.acquire();
            lock.unlock();
        } else {
            // add democrat to waiting state
            ++numDemocrats;
            lock.unlock();
            democrats.acquire();
            findSeat.acquire();
        }
        System.out.println("Seated passenger : " + Thread.currentThread().getName());
        barrier.await();
        // all 4 threads are seated, one which trips barrier will call 'drive()'
    }

    private void seatRepublican() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        if (numRepublicans == 3) {
            // allow 3 other waiting republicans to take this ride
            republicans.release(3);
            numRepublicans -= 3;
            findSeat.acquire();
            lock.unlock();
        } else if (numRepublicans == 1 && numDemocrats >= 2) {
            // allow 1 waiting republican and 2 waiting democrats to take this ride
            democrats.release(2);
            republicans.release(1);
            numRepublicans -= 1;
            numDemocrats -= 2;
            findSeat.acquire();
            lock.unlock();
        } else {
            // add republican to waiting state
            ++numRepublicans;
            lock.unlock();
            republicans.acquire();
            findSeat.acquire();
        }
        System.out.println("Seated passenger : " + Thread.currentThread().getName());
        barrier.await();
        // all 4 threads are seated, one which trips barrier will call 'drive()'
    }

    public void seated() throws InterruptedException, BrokenBarrierException {
        if (Thread.currentThread().getName().contains("Democrat")) {
            seatDemocrat();
        } else {
            seatRepublican();
        }
    }
}
