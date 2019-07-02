# University-Management-System

Concurrent Programming using Actor Thread Pool.

There are actors (Queues) and they can submit an action to another actor's
queue. the kind of actors: student, course and department.

The actions (for example, student wants to register to Algorithms course in the computer science department) will be inserted to the appropriare queues.
The threads fetch action from some queue, and execute it (the queue is lock by than).
If there are actions that have interdependence constraints, those actions become promises.

The university has Computers(The warehouse class). only one department can use a computer at once.
