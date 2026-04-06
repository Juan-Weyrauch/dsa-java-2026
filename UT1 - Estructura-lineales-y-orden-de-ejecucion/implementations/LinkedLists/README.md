# Linked Lists — Study README

A quick reference for your algorithms course. Covers the three main variants.

---

## What is a Linked List?

A linked list is a **sequence of nodes** where each node holds:
1. A **value** (the data)
2. A **pointer** (reference) to the next node

Unlike arrays, nodes are **not stored in contiguous memory** — each node just knows where the next one is. This makes insertions/deletions cheap at the cost of random access.

```
Array:   [10][20][30][40]   ← stored side by side, index in O(1)
List:    [10]→[20]→[30]→[40]→null   ← scattered in memory, must traverse
```

---

## 1. Singly Linked List

### Structure
```
head → [10] → [20] → [30] → null
```

Each node has:
- `data` — the value
- `next` — pointer to the next node (null at the end)

### Key operations

| Operation         | How it works                              | Time   |
|-------------------|-------------------------------------------|--------|
| `addFirst(x)`     | New node's next = head, then head = new   | O(1)   |
| `addLast(x)`      | Traverse to end, last.next = new          | O(n)   |
| `addAtIndex(i,x)` | Walk to index i, rewire next pointers     | O(n)   |
| `removeFirst()`   | head = head.next                          | O(1)   |
| `removeLast()`    | Walk to second-to-last, set .next = null  | O(n)   |
| `search(x)`       | Walk until found or null                  | O(n)   |

### When to use
- You only need to add/remove from the **front**
- Memory is tight (no extra `prev` pointer)
- Simple stacks and queues

### Gotcha ⚠️
`removeLast` is O(n) because you have no way to get to the second-to-last node without traversing. This is exactly why the doubly linked list exists.

---

## 2. Doubly Linked List

### Structure
```
null ← [10] ⇄ [20] ⇄ [30] → null
head ↑                    ↑ tail
```

Each node has:
- `data`
- `next` — pointer to the next node
- `prev` — pointer to the previous node

The list also keeps a `tail` reference to the last node.

### Key operations

| Operation         | How it works                                     | Time   |
|-------------------|--------------------------------------------------|--------|
| `addFirst(x)`     | Wire prev/next, update head                      | O(1)   |
| `addLast(x)`      | Wire via tail, update tail                        | O(1)   |
| `removeFirst()`   | head = head.next, clear prev                     | O(1)   |
| `removeLast()`    | tail = tail.prev, clear next                     | O(1)   |
| `removeByValue(x)`| Wire current.prev.next and current.next.prev     | O(n)   |
| Traverse backward | Follow `.prev` from tail                         | O(n)   |

### What's different from singly

```
Singly removeLast: walk all the way to second-to-last → O(n)
Doubly removeLast: tail = tail.prev → O(1)   ← the big win
```

The `prev` pointer also lets you traverse **backwards**, which is useful for things like a browser history (back button) or a music playlist.

### When to use
- You need O(1) add/remove at **both ends**
- You need to traverse in both directions
- Implementing **deques** (double-ended queues)

### Pointer rewiring diagram (removeByValue)
```
Before:  [A] ⇄ [B] ⇄ [C]
Remove B:
  A.next = C
  C.prev = A
After:   [A] ⇄ [C]
```

---

## 3. Circular Linked List

### Structure
```
head → [10] → [20] → [30] ─┐
        ↑____________________|
```

Like a singly list, but the **tail's `next` points back to `head`** instead of null. There is no end.

### Key rule: traversal condition
```java
// WRONG — infinite loop, current never becomes null
while (current != null) { ... }

// CORRECT — stop when you loop back to head
do {
    // process current
    current = current.next;
} while (current != head);
```

### Key operations

| Operation     | Extra step vs singly                        | Time   |
|---------------|---------------------------------------------|--------|
| `addFirst(x)` | After inserting, set tail.next = new head   | O(1)   |
| `addLast(x)`  | Set new tail.next = head                    | O(1)   |
| `removeFirst()` | head = head.next, tail.next = head        | O(1)   |
| `removeLast()` | Traverse to node before tail, .next = head | O(n)   |

### When to use
- **Round-robin scheduling** (CPU processes, multiplayer turns)
- **Circular buffers** (audio/video streaming)
- Any problem where you need to loop endlessly through a sequence

---

## Comparison Table

| Feature              | Singly | Doubly | Circular |
|----------------------|--------|--------|----------|
| Extra pointer/node   | —      | `prev` | tail→head|
| Add to front         | O(1)   | O(1)   | O(1)     |
| Add to back          | O(n)*  | O(1)   | O(1)     |
| Remove from front    | O(1)   | O(1)   | O(1)     |
| Remove from back     | O(n)   | O(1)   | O(n)     |
| Traverse backward    | ✗      | ✓      | ✗        |
| Has a "true" end     | ✓      | ✓      | ✗        |
| Memory per node      | low    | medium | low      |

*O(n) unless you keep a `tail` pointer, which makes it O(1)

---

## Common Bugs to Watch Out For

```java
// 1. Forgetting to update tail
tail.next = newNode;
tail = newNode;           // ← easy to forget this line!

// 2. NullPointerException on empty list
head = head.next;         // crashes if head is null — always check first

// 3. Infinite loop in circular list
while (current != null)   // ← WRONG, will loop forever
while (current != head)   // ← CORRECT

// 4. Forgetting to update both pointers in doubly
current.prev.next = current.next;   // ← also need:
current.next.prev = current.prev;   // ← this one too!

// 5. Wrong parameter order in Java methods
public void addAtIndex(Node x, int idx)  // this is what you had
public void addAtIndex(int idx, Node x)  // Java convention: type before name
//                     ↑ type  ↑ name — always type then name!
```

---

## Quick Mental Model

- **Singly** → a one-way street. Cheap at the front, expensive at the back.
- **Doubly** → a two-way street with signs at both ends. Cheap everywhere.
- **Circular** → a roundabout. No exit — loops forever, great for cycling.