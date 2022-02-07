package com.halo.demo.suanfa.反转链表;


/**
 * @Description: 单链表的新增、删除节点，实现反转
 *
 * 注意：链表添加或者删除节点后返回的总是头节点
 *
 *
 * @Author: Halo_ry
 * @Date: 2021/5/16 15:50
 */
public class Node<E> {

    private E value;
    private Node<E> next;

    public Node() {}

    public Node (E value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Node<String> node = null;

        node = addNodeHead("a", node);
        node = addNodeHead("b", node);
        node = addNodeHead("c", node);
        node = addNodeHead("d", node);
//        node = addNodeTail("a", node);
//        node = addNodeTail("b", node);
//        node = addNodeTail("c", node);
//        node = addNodeTail("d", node);
        int size = size(node);
        System.out.println("链表节点个数：" + size);
        printNode(node);
        // 反转
        node = reverseNode(node);
        printNode(node);
        // 删除头节点
//        node = removeHead(node);
        // 删除尾结点
//        node = removeTail(node);
        // 删除指定位置的节点
//        node = remove(2, node);
        printNode(node);

    }


    /**
     * 链表反转
     * @param curr
     * @return
     */
    public static <E> Node<E> reverseNode(Node<E> curr) {
        Node<E> prev = null;
        while (curr != null) {
            Node<E> next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    /**
     * 添加到 Node 头部
     * @param value
     * @param node
     * @return
     */
    public static <E> Node<E> addNodeHead(E value, Node<E> node) {
        Node<E> prev = new Node<>(value);
        prev.next = node;
        return prev;
    }

    /**
     * 添加到 Node 尾部
     * @param value
     * @param node
     * @param <E>
     * @return
     */
    public static <E> Node<E> addNodeTail(E value, Node<E> node) {
        Node<E> fristNode = node;
        Node<E> tail = new Node<>(value);
        if (node == null) {
            return tail;
        }
        while (node.next != null) {
            node = node.next;
        }
        node.next = tail;
        return fristNode;
    }

    /**
     * 删除头节点
     * @param node
     * @param <E>
     * @return
     */
    public static <E> Node<E> removeHead(Node<E> node) {
        return node.next;
    }

    /**
     * 删除尾结点
     * @param node
     * @param <E>
     * @return
     */
    public static <E> Node<E> removeTail(Node<E> node) {
        Node<E> fristNode = node;
        if (node == null || node.next == null) {
            return node;
        }
        while (node.next.next != null) {
            node = node.next;
        }
        node.next = null;
        return fristNode;
    }

    /**
     * 删除指定位置的节点
     * @param index 要删除的位置的节点
     * @param node
     * @param <E>
     * @return
     */
    public static <E> Node<E> remove(int index, Node<E> node){
        Node<E> fristNode = node;
        if (index < 0 || index > size(node)) {
            System.out.println("删除的位置小于0或者大于链表的长度,删除操作不执行");
            return node;
        }
        int i = 0;
        while (i < index - 1) {
            node = node.next;
            i++;
        }
        node.next = node.next.next;
        return fristNode;
    }

    /**
     * 统计链表节点个数
     * @param node
     * @param <E>
     * @return
     */
    public static <E> int size(Node<E> node) {
        if (node == null || node.value == null) {
            return 0;
        }
        int sum = 1;
        while (node.next != null) {
            sum++;
            node = node.next;
        }
        return sum;
    }

    /**
     * 打印链表每个节点
     * @param node
     */
    private static void printNode(Node<String> node) {
        System.out.print(node.value + "-->");
        while (node.next != null) {
            System.out.print(node.next.value + "-->");
            node = node.next;
        }
        System.out.println("null");
    }


}
