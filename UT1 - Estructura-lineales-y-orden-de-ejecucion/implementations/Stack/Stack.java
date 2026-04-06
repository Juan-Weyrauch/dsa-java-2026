import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║               STACK REFERENCE SHEET                     ║
 * ║         Array-based implementation — study cheatsheet   ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * A Stack is a LIFO (Last In, First Out) data structure.
 * Think of a stack of plates: you always add and remove from the TOP.
 *
 *   push(x)  → add to top
 *   pop()    → remove from top
 *   peek()   → look at top WITHOUT removing
 *
 * Visual:
 *   After push(10), push(20), push(30):
 *
 *   index 2 → [ 30 ]  ← top
 *   index 1 → [ 20 ]
 *   index 0 → [ 10 ]
 *
 * Key property: only the TOP element is ever accessible.
 *
 * Sections:
 *   1. ArrayStack  — the implementation
 *   2. Main        — interactive console demo
 */
public class Stack {

    // =========================================================
    // 1. ARRAY-BASED STACK
    // =========================================================

    /**
     * Stack implemented with a fixed-size array.
     *
     * <pre>
     *  Internal array:  [ 10 | 20 | 30 | -- | -- ]
     *  top index:                    ↑ 2
     * </pre>
     *
     * The variable `top` always points to the index of the topmost element.
     * An empty stack has top = -1.
     *
     * Time complexity of all core operations: O(1).
     * Space: O(capacity) — fixed at creation time.
     */
    static class ArrayStack {

        private int[] data;     // internal storage
        private int top;        // index of the top element (-1 if empty)
        private int capacity;   // max number of elements

        /**
         * Creates an empty stack with the given capacity.
         *
         * @param capacity maximum number of elements the stack can hold
         */
        public ArrayStack(int capacity) {
            this.capacity = capacity;
            this.data = new int[capacity];
            this.top = -1; // -1 means empty
        }

        // ── Core operations ───────────────────────────────────

        /**
         * Pushes a value onto the top of the stack. O(1).
         *
         * Before: [ 10 | 20 ]  top=1
         * push(30)
         * After:  [ 10 | 20 | 30 ]  top=2
         *
         * @param value the value to push
         * @return true if successful, false if the stack is full (overflow)
         */
        public boolean push(int value) {
            if (isFull()) {
                System.out.println("  ✗ Stack overflow! Cannot push " + value + " (capacity=" + capacity + ")");
                return false;
            }
            top++;
            data[top] = value;
            return true;
        }

        /**
         * Removes and returns the top element. O(1).
         *
         * Before: [ 10 | 20 | 30 ]  top=2
         * pop() → returns 30
         * After:  [ 10 | 20 ]  top=1
         *
         * @return the top value, or Integer.MIN_VALUE if empty (underflow)
         */
        public int pop() {
            if (isEmpty()) {
                System.out.println("  ✗ Stack underflow! Cannot pop from empty stack.");
                return Integer.MIN_VALUE;
            }
            int value = data[top];
            top--;
            return value;
        }

        /**
         * Returns the top element WITHOUT removing it. O(1).
         *
         * @return the top value, or Integer.MIN_VALUE if empty
         */
        public int peek() {
            if (isEmpty()) {
                System.out.println("  ✗ Stack is empty, nothing to peek.");
                return Integer.MIN_VALUE;
            }
            return data[top];
        }

        // ── State checks ──────────────────────────────────────

        /**
         * Returns true if the stack has no elements.
         * Condition: top == -1
         */
        public boolean isEmpty() {
            return top == -1;
        }

        /**
         * Returns true if the stack has reached its capacity.
         * Condition: top == capacity - 1
         */
        public boolean isFull() {
            return top == capacity - 1;
        }

        /**
         * Returns the number of elements currently in the stack.
         */
        public int size() {
            return top + 1;
        }

        /**
         * Removes all elements (resets the stack to empty).
         */
        public void clear() {
            top = -1;
        }

        // ── Display ──────────────────────────────────────────

        /**
         * Prints the stack from top to bottom.
         *
         * Example output:
         *   top → [ 30 ]
         *         [ 20 ]
         *         [ 10 ]  ← bottom
         */
        public void print() {
            if (isEmpty()) {
                System.out.println("  (empty stack)");
                return;
            }
            for (int i = top; i >= 0; i--) {
                String label = (i == top) ? "  top → " : "        ";
                String bottom = (i == 0) ? "  ← bottom" : "";
                System.out.println(label + "[ " + data[i] + " ]" + bottom);
            }
        }

        /**
         * Prints a one-line horizontal summary.
         * Example:  bottom [ 10 | 20 | 30 ] top
         */
        public void printHorizontal() {
            if (isEmpty()) {
                System.out.println("  bottom [  ] top  (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("  bottom [ ");
            for (int i = 0; i <= top; i++) {
                sb.append(data[i]);
                if (i < top) sb.append(" | ");
            }
            sb.append(" ] top");
            System.out.println(sb);
        }
    }

    // =========================================================
    // 2. MAIN — interactive console menu
    // =========================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayStack stack = new ArrayStack(10);

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       STACK INTERACTIVE DEMO — Study Mode    ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("  Current stack:");
            stack.printHorizontal();
            System.out.println("  Size: " + stack.size() + " / 10");
            System.out.println();
            System.out.println("  ┌─────────────────────────────┐");
            System.out.println("  │  1. push(x)  — add to top   │");
            System.out.println("  │  2. pop()    — remove top    │");
            System.out.println("  │  3. peek()   — view top      │");
            System.out.println("  │  4. isEmpty? — check empty   │");
            System.out.println("  │  5. isFull?  — check full    │");
            System.out.println("  │  6. print    — view stack    │");
            System.out.println("  │  7. clear    — reset         │");
            System.out.println("  │  0. exit                     │");
            System.out.println("  └─────────────────────────────┘");
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
                    System.out.print("  Value to push: ");
                    try {
                        int val = Integer.parseInt(sc.nextLine().trim());
                        boolean ok = stack.push(val);
                        if (ok) System.out.println("  ✓ Pushed " + val);
                    } catch (NumberFormatException e) {
                        System.out.println("  ✗ Invalid number.");
                    }
                    break;

                case 2:
                    int popped = stack.pop();
                    if (popped != Integer.MIN_VALUE)
                        System.out.println("  ✓ Popped: " + popped);
                    break;

                case 3:
                    int top = stack.peek();
                    if (top != Integer.MIN_VALUE)
                        System.out.println("  ✓ Top element: " + top);
                    break;

                case 4:
                    System.out.println("  isEmpty() → " + stack.isEmpty());
                    break;

                case 5:
                    System.out.println("  isFull()  → " + stack.isFull());
                    break;

                case 6:
                    System.out.println("  Stack (top to bottom):");
                    stack.print();
                    break;

                case 7:
                    stack.clear();
                    System.out.println("  ✓ Stack cleared.");
                    break;

                case 0:
                    running = false;
                    System.out.println("  Bye! Good luck with the exam :)");
                    break;

                default:
                    System.out.println("  ✗ Invalid option.");
            }
        }
        sc.close();
    }
}
