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
6. Once our riders are selected, we seat them in the car, and make them wait at a `CyclicBarrier` for 4 threads, to
ensure all 4 threads are seated. Once the barrier breaks, the `drive()` method is invoked as a callback by the thread
which causes the barrier to break.

At the initialization, both semaphores should be initialized with `0` so that the first few passengers wait till an 
acceptable combination is reached.

## Sample Output
```
Number of democrats: 32
Number of republicans: 32
Republican-0 is attempting to be seated
Republican-0 is waiting for its turn
Democrat-2 is attempting to be seated
Democrat-2 is waiting for its turn
Democrat-1 is attempting to be seated
Democrat-3 is attempting to be seated
Democrat-3 is waiting for its turn
Democrat-1 is waiting for its turn
Republican-4 is attempting to be seated
Republican-5 is attempting to be seated
Republican-5 is waiting for its turn
Seated passenger : Republican-4
Seated passenger : Republican-5
Seated passenger : Democrat-3
Seated passenger : Democrat-2
Democrat-6 is attempting to be seated
Democrat-6 is waiting for its turn
Democrat-7 is attempting to be seated
Democrat-7 is waiting for its turn
Republican-8 is attempting to be seated
Seated passenger : Democrat-1
Seated passenger : Democrat-6
Seated passenger : Republican-0
Seated passenger : Republican-8
Democrat-14 is attempting to be seated
Democrat-14 is waiting for its turn
Democrat-16 is attempting to be seated
Democrat-16 is waiting for its turn
Republican-13 is attempting to be seated
Democrat-12 is attempting to be seated
Republican-11 is attempting to be seated
Democrat-10 is attempting to be seated
Democrat-9 is attempting to be seated
Republican-13 is waiting for its turn
Thread : Democrat-2 signalled driver to drive away
Seated passenger : Democrat-12
Republican-11 is waiting for its turn
Seated passenger : Democrat-7
Thread : Democrat-7 signalled driver to drive away
Democrat-10 is waiting for its turn
Seated passenger : Democrat-16
Seated passenger : Democrat-14
Seated passenger : Democrat-10
Seated passenger : Democrat-9
Seated passenger : Republican-13
Thread : Democrat-10 signalled driver to drive away
Seated passenger : Republican-11
Thread : Republican-13 signalled driver to drive away
Republican-15 is attempting to be seated
Republican-15 is waiting for its turn
Democrat-18 is attempting to be seated
Democrat-18 is waiting for its turn
Democrat-17 is attempting to be seated
Democrat-17 is waiting for its turn
Democrat-19 is attempting to be seated
Democrat-20 is attempting to be seated
Democrat-19 is waiting for its turn
Seated passenger : Democrat-20
Republican-21 is attempting to be seated
Seated passenger : Democrat-19
Seated passenger : Democrat-17
Republican-21 is waiting for its turn
Seated passenger : Democrat-18
Thread : Democrat-18 signalled driver to drive away
Democrat-22 is attempting to be seated
Democrat-22 is waiting for its turn
Democrat-23 is attempting to be seated
Republican-28 is attempting to be seated
Republican-28 is waiting for its turn
Democrat-24 is attempting to be seated
Republican-29 is attempting to be seated
Seated passenger : Democrat-22
Democrat-27 is attempting to be seated
Seated passenger : Republican-21
Seated passenger : Republican-15
Democrat-24 is waiting for its turn
Seated passenger : Democrat-23
Republican-29 is waiting for its turn
Seated passenger : Democrat-27
Seated passenger : Democrat-24
Seated passenger : Republican-28
Thread : Democrat-23 signalled driver to drive away
Seated passenger : Republican-29
Republican-30 is attempting to be seated
Republican-30 is waiting for its turn
Republican-31 is attempting to be seated
Republican-31 is waiting for its turn
Thread : Republican-29 signalled driver to drive away
Republican-32 is attempting to be seated
Democrat-33 is attempting to be seated
Republican-32 is waiting for its turn
Republican-34 is attempting to be seated
Seated passenger : Republican-34
Seated passenger : Republican-30
Seated passenger : Republican-31
Democrat-35 is attempting to be seated
Seated passenger : Republican-32
Republican-37 is attempting to be seated
Democrat-33 is waiting for its turn
Democrat-36 is attempting to be seated
Democrat-35 is waiting for its turn
Thread : Republican-32 signalled driver to drive away
Republican-37 is waiting for its turn
Democrat-36 is waiting for its turn
Republican-38 is attempting to be seated
Democrat-39 is attempting to be seated
Democrat-39 is waiting for its turn
Seated passenger : Democrat-39
Seated passenger : Democrat-33
Seated passenger : Republican-37
Seated passenger : Republican-38
Thread : Republican-38 signalled driver to drive away
Republican-40 is attempting to be seated
Republican-40 is waiting for its turn
Republican-41 is attempting to be seated
Seated passenger : Republican-41
Seated passenger : Democrat-35
Seated passenger : Democrat-36
Democrat-43 is attempting to be seated
Democrat-43 is waiting for its turn
Seated passenger : Republican-40
Thread : Republican-40 signalled driver to drive away
Democrat-42 is attempting to be seated
Democrat-42 is waiting for its turn
Democrat-44 is attempting to be seated
Democrat-44 is waiting for its turn
Republican-45 is attempting to be seated
Republican-45 is waiting for its turn
Republican-46 is attempting to be seated
Seated passenger : Republican-46
Seated passenger : Democrat-43
Seated passenger : Republican-45
Seated passenger : Democrat-42
Thread : Democrat-42 signalled driver to drive away
Democrat-48 is attempting to be seated
Democrat-48 is waiting for its turn
Republican-47 is attempting to be seated
Republican-47 is waiting for its turn
Democrat-51 is attempting to be seated
Democrat-51 is waiting for its turn
Republican-49 is attempting to be seated
Seated passenger : Republican-49
Democrat-50 is attempting to be seated
Seated passenger : Democrat-48
Seated passenger : Republican-47
Seated passenger : Democrat-44
Thread : Democrat-44 signalled driver to drive away
Democrat-50 is waiting for its turn
Republican-52 is attempting to be seated
Republican-52 is waiting for its turn
Republican-53 is attempting to be seated
Republican-56 is attempting to be seated
Seated passenger : Republican-52
Republican-55 is attempting to be seated
Seated passenger : Republican-53
Republican-59 is attempting to be seated
Democrat-60 is attempting to be seated
Republican-63 is attempting to be seated
Democrat-54 is attempting to be seated
Seated passenger : Democrat-50
Republican-57 is attempting to be seated
Seated passenger : Democrat-51
Republican-56 is waiting for its turn
Thread : Democrat-51 signalled driver to drive away
Republican-55 is waiting for its turn
Republican-62 is attempting to be seated
Republican-62 is waiting for its turn
Seated passenger : Republican-59
Democrat-60 is waiting for its turn
Republican-26 is attempting to be seated
Republican-26 is waiting for its turn
Seated passenger : Republican-26
Republican-63 is waiting for its turn
Republican-25 is attempting to be seated
Republican-25 is waiting for its turn
Seated passenger : Republican-62
Republican-57 is waiting for its turn
Democrat-61 is attempting to be seated
Democrat-61 is waiting for its turn
Seated passenger : Republican-25
Thread : Republican-25 signalled driver to drive away
Democrat-58 is attempting to be seated
Seated passenger : Democrat-58
Seated passenger : Democrat-60
Seated passenger : Democrat-61
Seated passenger : Republican-63
Thread : Republican-63 signalled driver to drive away
Seated passenger : Democrat-54
Seated passenger : Republican-57
Seated passenger : Republican-55
Seated passenger : Republican-56
Thread : Republican-56 signalled driver to drive away
All passengers have been accommodated into Ubers
```