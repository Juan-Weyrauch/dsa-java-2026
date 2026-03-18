/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║              LINKED LIST REFERENCE SHEET                ║
 * ║  Singly · Doubly · Circular  —  quick study cheatsheet  ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * This file covers three list variants using a single Node class
 * that has both `next` and `prev` pointers (used only when needed).
 *
 * Sections:
 *   1. Node                – shared node definition
 *   2. SinglyLinkedList    – next only, no tail
 *   3. DoublyLinkedList    – next + prev, with tail
 *   4. CircularLinkedList  – next only, tail.next wraps to head
 *   5. Main                – interactive demo
 */
public class LinkedList {

    // =========================================================
    // 1. NODE
    // =========================================================

    /**
     * Generic node used by all three list types.
     *
     * For singly lists  → only `next` is used.
     * For doubly lists  → both `next` and `prev` are used.
     * For circular lists → only `next` is used; tail.next = head.
     */
    static class Node {
        int data;
        Node next;
        Node prev; // only used in DoublyLinkedList

        /** Creates a node with the given value. Both pointers start null. */
        public Node(int data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    // =========================================================
    // 2. SINGLY LINKED LIST
    //    head → [A] → [B] → [C] → null
    // =========================================================

    /**
     * Classic singly linked list.
     *
     * <pre>
     *  head → [10] → [20] → [30] → null
     * </pre>
     *
     * Operations: O(1) at head, O(n) at tail or by index.
     */
    static class SinglyLinkedList {

        Node head;

        // ── Add ──────────────────────────────────────────────

        /**
         * Inserts a new node at the BEGINNING of the list.
         *
         * Before: head → [B] → [C]
         * After:  head → [A] → [B] → [C]
         *
         * @param data value to insert
         */
        public void addFirst(int data) {
            Node newNode = new Node(data);
            newNode.next = head;
            head = newNode;
        }

        /**
         * Inserts a new node at the END of the list.
         *
         * Before: head → [A] → [B]
         * After:  head → [A] → [B] → [C]
         *
         * @param data value to insert
         */
        public void addLast(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                return;
            }
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        /**
         * Inserts a new node AFTER the node at position `index` (0-based).
         * If index is out of bounds, prints a warning and does nothing.
         *
         * Example: addAtIndex(1, 99) on [10→20→30] → [10→20→99→30]
         *
         * @param index 0-based position of the node to insert after
         * @param data  value to insert
         */
        public void addAtIndex(int index, int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                return;
            }
            Node current = head;
            for (int i = 0; i < index; i++) {
                if (current.next == null) {
                    System.out.println("Index " + index + " out of bounds.");
                    return;
                }
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }

        // ── Delete ───────────────────────────────────────────

        /**
         * Removes the FIRST node of the list.
         *
         * Before: head → [A] → [B] → [C]
         * After:  head → [B] → [C]
         */
        public void removeFirst() {
            if (head == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            head = head.next;
        }

        /**
         * Removes the LAST node of the list.
         *
         * Before: head → [A] → [B] → [C]
         * After:  head → [A] → [B]
         */
        public void removeLast() {
            if (head == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            // Only one element
            if (head.next == null) {
                head = null;
                return;
            }
            Node current = head;
            while (current.next.next != null) {
                current = current.next;
            }
            current.next = null;
        }

        /**
         * Removes the first node whose data matches `value`.
         *
         * @param value the data value to search and remove
         */
        public void removeByValue(int value) {
            if (head == null) return;
            if (head.data == value) {
                head = head.next;
                return;
            }
            Node current = head;
            while (current.next != null && current.next.data != value) {
                current = current.next;
            }
            if (current.next == null) {
                System.out.println("Value " + value + " not found.");
                return;
            }
            current.next = current.next.next;
        }

        // ── Search ───────────────────────────────────────────

        /**
         * Returns the 0-based index of the first node with `value`, or -1 if not found.
         *
         * @param value data to look for
         * @return index of the node, or -1
         */
        public int search(int value) {
            Node current = head;
            int index = 0;
            while (current != null) {
                if (current.data == value) return index;
                current = current.next;
                index++;
            }
            return -1;
        }

        // ── Display ──────────────────────────────────────────

        /**
         * Prints the list in a readable format.
         * Example output:  head → [10] → [20] → [30] → null
         */
        public void print() {
            StringBuilder sb = new StringBuilder("  head → ");
            Node current = head;
            while (current != null) {
                sb.append("[").append(current.data).append("] → ");
                current = current.next;
            }
            sb.append("null");
            System.out.println(sb);
        }
    }

    // =========================================================
    // 3. DOUBLY LINKED LIST
    //    null ← [A] ⇄ [B] ⇄ [C] → null
    //    head ↑                 ↑ tail
    // =========================================================

    /**
     * Doubly linked list — each node knows both its successor and predecessor.
     *
     * <pre>
     *  null ← [10] ⇄ [20] ⇄ [30] → null
     *  head ↑                    ↑ tail
     * </pre>
     *
     * Benefit over singly: O(1) removal from tail (no need to traverse).
     */
    static class DoublyLinkedList {

        Node head;
        Node tail;

        // ── Add ──────────────────────────────────────────────

        /**
         * Inserts a new node at the BEGINNING.
         *
         * Before: null ← [B] ⇄ [C] → null
         * After:  null ← [A] ⇄ [B] ⇄ [C] → null
         *
         * @param data value to insert
         */
        public void addFirst(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
                return;
            }
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        /**
         * Inserts a new node at the END.
         *
         * Before: null ← [A] ⇄ [B] → null
         * After:  null ← [A] ⇄ [B] ⇄ [C] → null
         *
         * @param data value to insert
         */
        public void addLast(int data) {
            Node newNode = new Node(data);
            if (tail == null) {
                head = newNode;
                tail = newNode;
                return;
            }
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        /**
         * Inserts a new node AFTER the node at position `index` (0-based).
         *
         * @param index 0-based position to insert after
         * @param data  value to insert
         */
        public void addAtIndex(int index, int data) {
            Node newNode = new Node(data);
            Node current = head;
            for (int i = 0; i < index; i++) {
                if (current == null) {
                    System.out.println("Index " + index + " out of bounds.");
                    return;
                }
                current = current.next;
            }
            if (current == null) {
                System.out.println("Index " + index + " out of bounds.");
                return;
            }
            newNode.next = current.next;
            newNode.prev = current;
            if (current.next != null) {
                current.next.prev = newNode;
            } else {
                tail = newNode; // inserted after tail
            }
            current.next = newNode;
        }

        // ── Delete ───────────────────────────────────────────

        /**
         * Removes the FIRST node. O(1).
         */
        public void removeFirst() {
            if (head == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            if (head == tail) { // only one element
                head = null;
                tail = null;
                return;
            }
            head = head.next;
            head.prev = null;
        }

        /**
         * Removes the LAST node. O(1) thanks to the `tail` pointer.
         */
        public void removeLast() {
            if (tail == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            if (head == tail) { // only one element
                head = null;
                tail = null;
                return;
            }
            tail = tail.prev;
            tail.next = null;
        }

        /**
         * Removes the first node whose data matches `value`.
         *
         * @param value data to remove
         */
        public void removeByValue(int value) {
            Node current = head;
            while (current != null) {
                if (current.data == value) {
                    if (current.prev != null) current.prev.next = current.next;
                    else head = current.next; // removing head

                    if (current.next != null) current.next.prev = current.prev;
                    else tail = current.prev; // removing tail

                    return;
                }
                current = current.next;
            }
            System.out.println("Value " + value + " not found.");
        }

        // ── Display ──────────────────────────────────────────

        /**
         * Prints the list left-to-right (head → tail).
         * Example: head → [10] ⇄ [20] ⇄ [30] ← tail
         */
        public void printForward() {
            StringBuilder sb = new StringBuilder("  head → ");
            Node current = head;
            while (current != null) {
                sb.append("[").append(current.data).append("]");
                if (current.next != null) sb.append(" ⇄ ");
                current = current.next;
            }
            sb.append(" ← tail");
            System.out.println(sb);
        }

        /**
         * Prints the list right-to-left (tail → head).
         * Demonstrates the power of the `prev` pointer.
         */
        public void printBackward() {
            StringBuilder sb = new StringBuilder("  tail → ");
            Node current = tail;
            while (current != null) {
                sb.append("[").append(current.data).append("]");
                if (current.prev != null) sb.append(" ⇄ ");
                current = current.prev;
            }
            sb.append(" ← head");
            System.out.println(sb);
        }
    }

    // =========================================================
    // 4. CIRCULAR LINKED LIST
    //    head → [A] → [B] → [C] → (back to head)
    // =========================================================

    /**
     * Circular singly linked list — the tail's `next` points back to `head`.
     *
     * <pre>
     *  head → [10] → [20] → [30] ─┐
     *           ↑__________________|
     * </pre>
     *
     * Useful for: round-robin scheduling, circular buffers, game turn loops.
     *
     * ⚠ Never use `current != null` to traverse — it will loop forever!
     *   Always use `current != head` as the stop condition.
     */
    static class CircularLinkedList {

        Node head;
        Node tail;

        // ── Add ──────────────────────────────────────────────

        /**
         * Inserts a new node at the BEGINNING.
         *
         * @param data value to insert
         */
        public void addFirst(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
                tail.next = head; // circular link
                return;
            }
            newNode.next = head;
            head = newNode;
            tail.next = head; // keep circle intact
        }

        /**
         * Inserts a new node at the END.
         *
         * @param data value to insert
         */
        public void addLast(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
                tail.next = head;
                return;
            }
            tail.next = newNode;
            tail = newNode;
            tail.next = head; // keep circle intact
        }

        // ── Delete ───────────────────────────────────────────

        /**
         * Removes the FIRST node.
         */
        public void removeFirst() {
            if (head == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            if (head == tail) { // one element
                head = null;
                tail = null;
                return;
            }
            head = head.next;
            tail.next = head; // update circular link
        }

        /**
         * Removes the LAST node.
         */
        public void removeLast() {
            if (tail == null) {
                System.out.println("List is empty, nothing to remove.");
                return;
            }
            if (head == tail) {
                head = null;
                tail = null;
                return;
            }
            // Traverse to find node before tail
            Node current = head;
            while (current.next != tail) {
                current = current.next;
            }
            current.next = head; // new tail points to head
            tail = current;
        }

        // ── Display ──────────────────────────────────────────

        /**
         * Prints the list showing the circular wrap.
         * Example:  head → [10] → [20] → [30] → (head)
         */
        public void print() {
            if (head == null) {
                System.out.println("  (empty list)");
                return;
            }
            StringBuilder sb = new StringBuilder("  head → ");
            Node current = head;
            do {
                sb.append("[").append(current.data).append("] → ");
                current = current.next;
            } while (current != head);
            sb.append("(head)");
            System.out.println(sb);
        }
    }

    // =========================================================
    // 5. MAIN — interactive demo
    // =========================================================

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         LINKED LIST DEMO — Study Mode        ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");

        // ── Singly Linked List demo ──────────────────────────
        System.out.println("━━━  SINGLY LINKED LIST  ━━━");
        SinglyLinkedList singly = new SinglyLinkedList();

        System.out.println("addLast  10, 20, 30:");
        singly.addLast(10);
        singly.addLast(20);
        singly.addLast(30);
        singly.print();

        System.out.println("addFirst 5:");
        singly.addFirst(5);
        singly.print();

        System.out.println("addAtIndex(2, 99)  — insert 99 after index 2:");
        singly.addAtIndex(2, 99);
        singly.print();

        System.out.println("removeFirst:");
        singly.removeFirst();
        singly.print();

        System.out.println("removeLast:");
        singly.removeLast();
        singly.print();

        System.out.println("removeByValue(99):");
        singly.removeByValue(99);
        singly.print();

        System.out.println("search(20) → index: " + singly.search(20));
        System.out.println("search(99) → index: " + singly.search(99) + "  (-1 = not found)\n");

        // ── Doubly Linked List demo ──────────────────────────
        System.out.println("━━━  DOUBLY LINKED LIST  ━━━");
        DoublyLinkedList doubly = new DoublyLinkedList();

        System.out.println("addLast  100, 200, 300:");
        doubly.addLast(100);
        doubly.addLast(200);
        doubly.addLast(300);
        doubly.printForward();

        System.out.println("addFirst 50:");
        doubly.addFirst(50);
        doubly.printForward();

        System.out.println("addAtIndex(1, 75)  — insert 75 after index 1:");
        doubly.addAtIndex(1, 75);
        doubly.printForward();

        System.out.println("Backward traversal (using prev pointers):");
        doubly.printBackward();

        System.out.println("removeLast (O(1) with tail pointer):");
        doubly.removeLast();
        doubly.printForward();

        System.out.println("removeFirst:");
        doubly.removeFirst();
        doubly.printForward();

        System.out.println("removeByValue(75):");
        doubly.removeByValue(75);
        doubly.printForward();
        System.out.println();

        // ── Circular Linked List demo ────────────────────────
        System.out.println("━━━  CIRCULAR LINKED LIST  ━━━");
        CircularLinkedList circular = new CircularLinkedList();

        System.out.println("addLast  A=1, B=2, C=3:");
        circular.addLast(1);
        circular.addLast(2);
        circular.addLast(3);
        circular.print();

        System.out.println("addFirst 0:");
        circular.addFirst(0);
        circular.print();

        System.out.println("removeFirst (removes 0):");
        circular.removeFirst();
        circular.print();

        System.out.println("removeLast (removes 3):");
        circular.removeLast();
        circular.print();

        System.out.println("\n✔  All demos complete. Check README.md for concept explanations.");
    }
}   