# University-Management-System

The implementation using a concurrent Programming by implements Actor Thread Pool.

There are actors (Queues) and they can submit an action to another actor's
queue. the threads fetch action from some queue, and execute it (the queue is lock by than).
if there are actions that have interdependence constraints, those actions become promises.

The kind of actors:
 student.
 course.
 department.
The actions(for example, student wants to register to Algorithms course in the computer science department) will insert to the appropriare queues.

The university has Computers(The warehouse class). only one department can use a computer at once.
