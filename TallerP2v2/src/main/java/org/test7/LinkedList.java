package org.test7;

public class LinkedList {
    private Node head;

    public LinkedList() {
        this.head = null;
    }

    public void insert(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public void swapAdjacentNodes() {
        if (head == null || head.getNext() == null) {
            return;
        }

        Node prev = null;
        Node current = head;
        head = current.getNext();

        while (current != null && current.getNext() != null) {
            Node nextNode = current.getNext();
            Node temp = nextNode.getNext();

            nextNode.setNext(current);
            current.setNext(temp);

            if (prev != null) {
                prev.setNext(nextNode);
            }

            prev = current;
            current = temp;
        }
    }

    // Getter necesario para la desencriptación
    public Node getHead() {
        return head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(" -> ");
            }
            current = current.getNext();
        }
        return sb.toString();
    }
}
