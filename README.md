# Problem Statement
Imagine at the end of a political conference, Republicans and Democrats are trying to leave the venue and ordering Uber 
rides at the same time. To avoid conflicts, each ride can have either all Democrats or Republicans or two Democrats and 
two Republicans.

All other combinations can result in a fist-fight.

Your task as an Uber developer is to model the ride requests as threads. Once an acceptable combination of riders is 
possible, threads are allowed to proceed with the ride.

Each thread invokes the method `seated()` when selected by the system for the next ride. When all threads are seated,
any one of four threads can invoke the method `drive()` to inform the driver to start the ride.

## Solution approach
1. Visualize the seating management as the responsibility of a class (`UberRide`).
2. Assume each thread is named with `Democrat` or `Republican` for ease of identification.
3. Whenever a thread invokes `seated()`, the appropriate method `seatDemocrat()` or `seatRepublican()` is invoked based 
on the thread name. From point 4, we follow the flow of the `seatDemocrat()` method, since `seatRepublican()` is very
similar in implementation.
4. Say `seatDemocrat()` is invoked for the first time. The democrat should be made to wait for the next riders till an 
acceptable combination is reached. To do this, the count of waiting of democrats is increased, `lock()` is released, and
the thread acquires the `democrats` semaphore.
5. If `seatDemocrat` is invoked when there are already 3 waiting democrats, the `democrats` semaphore can be signalled
thrice to release three waiting democrats. Similarly, if 1 democrat and at least 2 republicans are waiting, the
`democrats` semaphore can be signalled once and the `republicans` semaphore can be signalled twice. The corresponding 
count variables are decremented accordingly. Thereafter, the lock acquired by current thread is released.
6. Every thread which is now eligible to sit must acquire the `findSeat` semaphore. The thread which confirms the 
seating arrangement acquires the semaphore before releasing the `lock`, to ensure that any confirmed group can take 
seats and drive off only after the previous group has left.
7. Once our riders are selected, we seat them in the car, and make them wait at a `CyclicBarrier` for 4 threads, to
ensure all 4 threads are seated. Once the barrier breaks, the `drive()` method is invoked as a callback by the thread
which caused the barrier to break. The `drive()` method prints the message about which thread signalled driver, and 
releases the `findSeat` semaphore 4 times to allow the next 4 threads to find a seat.

At the initialization, both semaphores should be initialized with `0` so that the first few passengers wait till an 
acceptable combination is reached.

## Sample Output
```
Number of democrats: 16
Number of republicans: 4
Seated passenger : Republican-4
Seated passenger : Democrat-3
Seated passenger : Democrat-0
Seated passenger : Republican-1
Thread : Republican-1 signalled driver to drive away
Seated passenger : Democrat-8
Seated passenger : Democrat-5
Seated passenger : Democrat-6
Seated passenger : Democrat-7
Thread : Democrat-7 signalled driver to drive away
Seated passenger : Democrat-11
Seated passenger : Republican-2
Seated passenger : Democrat-9
Seated passenger : Republican-10
Thread : Republican-10 signalled driver to drive away
Seated passenger : Democrat-15
Seated passenger : Democrat-13
Seated passenger : Democrat-12
Seated passenger : Democrat-16
Thread : Democrat-16 signalled driver to drive away
Seated passenger : Democrat-18
Seated passenger : Democrat-19
Seated passenger : Democrat-14
Seated passenger : Democrat-17
Thread : Democrat-17 signalled driver to drive away
All passengers have been accommodated into Ubers
```