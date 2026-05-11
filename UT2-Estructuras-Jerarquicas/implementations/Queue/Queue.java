import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║               QUEUE REFERENCE SHEET                      ║
 * ║         Array-based implementation — study cheatsheet    ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * A Queue is a FIFO (First In, First Out) data structure.
 * Think of a line at the supermarket: you join at the BACK,
 * and you're served from the FRONT.
 *
 *   enqueue(x)  → add to the BACK  (rear)
 *   dequeue()   → remove from FRONT
 *   peek()      → look at FRONT without removing
 *
 * Visual:
 *   After enqueue(10), enqueue(20), enqueue(30):
 *
 *   front → [ 10 | 20 | 30 ] ← rear
 *
 *   dequeue() → returns 10
 *   front → [ 20 | 30 ] ← rear
 *
 * ── Circular buffer trick ────────────────────────────────────
 * A naive array queue wastes space after dequeues (dead cells at front).
 * A CIRCULAR buffer reuses those cells by wrapping indices with modulo:
 *
 *   rear = (rear + 1) % capacity
 *
 * This lets the queue use all available slots without shifting elements.
 *
 * Sections:
 *   1. ArrayQueue  — circular buffer implementation
 *   2. Main        — interactive console demo
 */
public class Queue {

    // =========================================================
    // 1. ARRAY-BASED QUEUE (circular buffer)
    // =========================================================

    /**
     * Queue implemented with a circular array buffer.
     *
     * <pre>
     *  capacity = 5, size = 3:
     *
     *  index:  [ 0 | 1 | 2 | 3 | 4 ]
     *  data:   [ 20| 30| --| --| 10]
     *                         ↑rear  ↑front   (wrapped around!)
     * </pre>
     *
     * front  — index of the next element to dequeue
     * rear   — index where the next enqueue will go
     * size   — current number of elements
     *
     * Time complexity of all core operations: O(1).
     * Space: O(capacity) — fixed at creation time.
     */
    static class ArrayQueue {

        private int[] data;
        private int front;      // index of the front element
        private int rear;       // index where next element will be inserted
        private int size;       // current number of elements
        private int capacity;

        /**
         * Creates an empty queue with the given capacity.
         *
         * @param capacity maximum number of elements the queue can hold
         */
        public ArrayQueue(int capacity) {
            this.capacity = capacity;
            this.data = new int[capacity];
            this.front = 0;
            this.rear = 0;
            this.size = 0;
        }

        // ── Core operations ───────────────────────────────────

        /**
         * Adds a value to the BACK of the queue. O(1).
         *
         * Before: front [ 10 | 20 ] rear
         * enqueue(30)
         * After:  front [ 10 | 20 | 30 ] rear
         *
         * Uses modulo to wrap: rear = (rear + 1) % capacity
         *
         * @param value the value to enqueue
         * @return true if successful, false if full (overflow)
         */
        public boolean enqueue(int value) {
            if (isFull()) {
                System.out.println("  ✗ Queue overflow! Cannot enqueue " + value + " (capacity=" + capacity + ")");
                return false;
            }
            data[rear] = value;
            rear = (rear + 1) % capacity; // circular wrap
            size++;
            return true;
        }

        /**
         * Removes and returns the FRONT element. O(1).
         *
         * Before: front [ 10 | 20 | 30 ] rear
         * dequeue() → returns 10
         * After:       front [ 20 | 30 ] rear
         *
         * Uses modulo to wrap: front = (front + 1) % capacity
         *
         * @return the front value, or Integer.MIN_VALUE if empty (underflow)
         */
        public int dequeue() {
            if (isEmpty()) {
                System.out.println("  ✗ Queue underflow! Cannot dequeue from empty queue.");
                return Integer.MIN_VALUE;
            }
            int value = data[front];
            front = (front + 1) % capacity; // circular wrap
            size--;
            return value;
        }

        /**
         * Returns the FRONT element WITHOUT removing it. O(1).
         *
         * @return the front value, or Integer.MIN_VALUE if empty
         */
        public int peek() {
            if (isEmpty()) {
                System.out.println("  ✗ Queue is empty, nothing to peek.");
                return Integer.MIN_VALUE;
            }
            return data[front];
        }

        // ── State checks ──────────────────────────────────────

        /**
         * Returns true if the queue has no elements.
         * Condition: size == 0
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Returns true if the queue is at capacity.
         * Condition: size == capacity
         */
        public boolean isFull() {
            return size == capacity;
        }

        /**
         * Returns the number of elements currently in the queue.
         */
        public int size() {
            return size;
        }

        /**
         * Removes all elements (resets the queue to empty).
         */
        public void clear() {
            front = 0;
            rear = 0;
            size = 0;
        }

        // ── Display ──────────────────────────────────────────

        /**
         * Prints the queue from front to back.
         *
         * Example output:
         *   front → [ 10 | 20 | 30 ] ← rear
         *
         * ⚠ Iterates using (front + i) % capacity to handle wrap-around.
         */
        public void print() {
            if (isEmpty()) {
                System.out.println("  front → [  ] ← rear  (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("  front → [ ");
            for (int i = 0; i < size; i++) {
                sb.append(data[(front + i) % capacity]);
                if (i < size - 1) sb.append(" | ");
            }
            sb.append(" ] ← rear");
            System.out.println(sb);
        }

        /**
         * Prints a debug view showing the raw internal array state,
         * including front and rear index positions.
         * Useful for understanding the circular buffer mechanics.
         */
        public void printDebug() {
            System.out.print("  raw array: [ ");
            for (int i = 0; i < capacity; i++) {
                System.out.print(data[i]);
                if (i < capacity - 1) System.out.print(" | ");
            }
            System.out.println(" ]");
            System.out.println("  front index=" + front + "  rear index=" + rear + "  size=" + size);
        }
    }

    // =========================================================
    // 2. MAIN — interactive console menu
    // =========================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayQueue queue = new ArrayQueue(8);

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       QUEUE INTERACTIVE DEMO — Study Mode    ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("  Current queue:");
            queue.print();
            System.out.println("  Size: " + queue.size() + " / 8");
            System.out.println();
            System.out.println("  ┌──────────────────────────────────────┐");
            System.out.println("  │  1. enqueue(x) — add to back         │");
            System.out.println("  │  2. dequeue()  — remove from front   │");
            System.out.println("  │  3. peek()     — view front          │");
            System.out.println("  │  4. isEmpty?   — check empty         │");
            System.out.println("  │  5. isFull?    — check full          │");
            System.out.println("  │  6. print      — view queue          │");
            System.out.println("  │  7. debug      — show raw array      │");
            System.out.println("  │  8. clear      — reset               │");
            System.out.println("  │  0. exit                             │");
            System.out.println("  └──────────────────────────────────────┘");
            System.out.print("  Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("  Value to enqueue: ");
                    try {
                        int val = Integer.parseInt(sc.nextLine().trim());
                        boolean ok = queue.enqueue(val);
                        if (ok) System.out.println("  ✓ Enqueued " + val + " to back.");
                    } catch (NumberFormatException e) {
                        System.out.println("  ✗ Invalid number.");
                    }
                    break;

                case 2:
                    int dequeued = queue.dequeue();
                    if (dequeued != Integer.MIN_VALUE)
                        System.out.println("  ✓ Dequeued from front: " + dequeued);
                    break;

                case 3:
                    int front = queue.peek();
                    if (front != Integer.MIN_VALUE)
                        System.out.println("  ✓ Front element: " + front);
                    break;

                case 4:
                    System.out.println("  isEmpty() → " + queue.isEmpty());
                    break;

                case 5:
                    System.out.println("  isFull()  → " + queue.isFull());
                    break;

                case 6:
                    queue.print();
                    break;

                case 7:
                    System.out.println("  Debug view (circular buffer internals):");
                    queue.printDebug();
                    break;

                case 8:
                    queue.clear();
                    System.out.println("  ✓ Queue cleared.");
                    break;

                case 0:
                    running = false;
                    System.out.println("  Bye! You've got this :)");
                    break;

                default:
                    System.out.println("  ✗ Invalid option.");
            }
        }
        sc.close();
    }
}
