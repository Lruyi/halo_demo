package com.halo.demo.suanfa.反转链表;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2021/5/17 10:51
 */
public class Main {
    public static void main(String[] args) {
        Node node = null;
//        node = addNode("a", node);
//        node = addNode("b", node);
//        node = addNode("c", node);
        node = addNodeTail("a", node);
        node = addNodeTail("b", node);
        node = addNodeTail("c", node);
        printNode(node);
    }

    public static Node addNode(String value, Node node) {
        Node prev = new Node(value);
        prev.next = node;
        return prev;
    }

    public static Node addNodeTail(String value, Node node) {
        Node frist = node;
        Node tail = new Node(value);
        if (node == null) return tail;
        while (node.next != null) {
            node = node.next;
        }
        node.next = tail;
        return frist;
    }

    public static void printNode(Node node) {
        System.out.print(node.value + "-->");
        while (node.next != null) {
            System.out.print(node.next.value + "-->");
            node = node.next;
        }
        System.out.println("null");
    }

    public static class Node {
        private String value;
        private Node next;
        public Node(String value) {
            this.value = value;
        }
    }


}
