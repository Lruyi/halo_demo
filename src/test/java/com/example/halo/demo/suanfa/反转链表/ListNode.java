package com.example.halo.demo.suanfa.反转链表;

/**
 * @Description:  单链表的反转
 * @Author: Halo_ry
 * @Date: 2021/5/13 23:20
 */
public class ListNode {
    int val;
    ListNode next;

    ListNode() {}

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public static void main(String[] args) {
        ListNode head = null;
        head = addHeadNode(1, head);
        head = addHeadNode(2, head);
        head = addHeadNode(3, head);
        head = addHeadNode(4, head);


        ListNode node = reverseList(head);
        System.out.println(node);

    }

    public static ListNode addHeadNode(int value, ListNode node) {
        ListNode prev = new ListNode(value);
        prev.next = node;
        return prev;
    }

    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            // 先获取到当前节点的下一个节点
            ListNode next = curr.next;
            // 将当前节点的下一个节点指向前驱节点，也就是null
            curr.next = prev;
            // 向后移动prev 到当前节点
            prev = curr;
            // 将当前节点移动到下一个节点上
            curr = next;
        }
        return prev;
    }
}
