# Stacks & Queues — Study README

Quick reference for your algorithms course. Covers both structures with diagrams, complexity tables, and common exam traps.

---

## Stack — LIFO (Last In, First Out)

### Core idea
The last element you put in is the first one you get out. Like a stack of plates — you always grab from the top.

```
After push(10), push(20), push(30):

  top → [ 30 ]
        [ 20 ]
        [ 10 ]  ← bottom
```

### Operations

| Operation    | What it does                          | Time |
|--------------|---------------------------------------|------|
| `push(x)`    | Add x to the top                      | O(1) |
| `pop()`      | Remove and return the top element     | O(1) |
| `peek()`     | Read the top WITHOUT removing it      | O(1) |
| `isEmpty()`  | Check if top == -1                    | O(1) |
| `isFull()`   | Check if top == capacity - 1          | O(1) |
| `size()`     | Return top + 1                        | O(1) |

### Array implementation internals

```
capacity = 5
data: [ 10 | 20 | 30 | -- | -- ]
top:                  ↑ 2

push(40):  top → 3,  data[3] = 40
pop():     value = data[top],  top--
isEmpty(): top == -1
isFull():  top == capacity - 1
```

### When to use a Stack
- **Undo/redo** — last action is the first to undo
- **Balanced parentheses** check: `( [ { } ] )`
- **DFS** (Depth-First Search) traversal
- **Recursion** is a stack under the hood — the call stack

### Overflow vs Underflow

```
Overflow  → push() when isFull()  — no more room
Underflow → pop()  when isEmpty() — nothing to remove
```
Always check before operating!

---

## Queue — FIFO (First In, First Out)

### Core idea
The first element you put in is the first one you get out. Like a supermarket line — you join at the back, leave from the front.

```
After enqueue(10), enqueue(20), enqueue(30):

  front → [ 10 | 20 | 30 ] ← rear

dequeue() → removes 10, returns 10
  front → [ 20 | 30 ] ← rear
```

### Operations

| Operation     | What it does                            | Time |
|---------------|-----------------------------------------|------|
| `enqueue(x)`  | Add x to the rear (back)                | O(1) |
| `dequeue()`   | Remove and return the front element     | O(1) |
| `peek()`      | Read the front WITHOUT removing it      | O(1) |
| `isEmpty()`   | Check if size == 0                      | O(1) |
| `isFull()`    | Check if size == capacity               | O(1) |
| `size()`      | Return current count                    | O(1) |

### Circular buffer — the key trick

A naive array queue shifts elements or wastes space at the front after dequeues. A **circular buffer** solves this with modulo arithmetic:

```java
rear  = (rear  + 1) % capacity;  // on enqueue
front = (front + 1) % capacity;  // on dequeue
```

Example — capacity = 5, after some enqueues and dequeues:

```
index:  [  0  |  1  |  2  |  3  |  4  ]
data:   [ 30  | --  | --  | 10  | 20  ]
                                  ↑front   ↑rear (wrapped around!)
```
The queue logically reads: 10, 20, 30 — using all slots efficiently.

### When to use a Queue
- **BFS** (Breadth-First Search) traversal
- **Task scheduling** — CPU processes, print jobs
- **Buffers** — keyboard input, network packets
- **Simulation** of real-world lines (bank, hospital)

---

## Stack vs Queue — Side-by-side

```
Stack (LIFO):          Queue (FIFO):
  push → top           enqueue → rear
  pop  ← top           dequeue ← front

  [ 30 ] ← top          front → [ 10 | 20 | 30 ] ← rear
  [ 20 ]
  [ 10 ]
```

| Property          | Stack           | Queue                  |
|-------------------|-----------------|------------------------|
| Order             | LIFO            | FIFO                   |
| Add element       | push to top     | enqueue to rear        |
| Remove element    | pop from top    | dequeue from front     |
| Peek              | top element     | front element          |
| Array trick       | just a top index| circular buffer        |
| Use case          | undo, DFS, recursion | BFS, scheduling, buffers |

---

## Common Exam Traps ⚠️

```java
// 1. Stack: popping the value vs just moving the pointer
int value = data[top];
top--;               // ← two separate steps, easy to merge wrong

// 2. Queue: forgetting the circular wrap
rear = rear + 1;              // WRONG — goes out of bounds eventually
rear = (rear + 1) % capacity; // CORRECT

// 3. Checking empty BEFORE operating
stack.pop();   // NullPointerException or wrong result if empty!
if (!stack.isEmpty()) stack.pop();  // safe

// 4. Stack size formula
size = top + 1;  // because top is 0-based (-1 when empty)

// 5. Queue full vs empty — both can have front == rear
// That's why you track `size` separately:
isEmpty() → size == 0
isFull()  → size == capacity
// Without size, you can't tell them apart!
```

---

## Quick Mental Models

- **Stack** → a spring-loaded plate dispenser. Last plate in = first plate out.
- **Queue** → a supermarket checkout line. First person in = first person served.
- **Circular buffer** → a clock face. When the hand reaches 12 it wraps back to 1 — no wasted space.
