package com.halo.demo.suanfa.链表排序;

/**
 * @Description: 链表排序
 *  解答一：归并排序（递归法）
 *
 * @Author: Halo_ry
 * @Date: 2021/5/17 19:05
 */
public class ListNode {

    private int value;

    private ListNode next;

    public ListNode(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        ListNode node = null;
        node = addHeadNode(8, node);
        node = addHeadNode(7, node);
        node = addHeadNode(1, node);
        node = addHeadNode(5, node);
        node = addHeadNode(6, node);
        node = addHeadNode(4, node);
        node = addHeadNode(2, node);
        node = addHeadNode(3, node);

        node = sortList(node);
        System.out.println(node);
    }

    private static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next, slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode tmp = slow.next;
        slow.next = null;

        ListNode left = sortList(head);
        ListNode right = sortList(tmp);

        ListNode h = new ListNode(0);
        ListNode res = h;
        while (left != null && right != null) {
            if (left.value < right.value) {
                h.next = left;
                left = left.next;
            } else {
                h.next = right;
                right = right.next;
            }
            h = h.next;
        }
        h.next = left != null ? left : right;
        return res.next;
    }

    private static ListNode addHeadNode(int value, ListNode node) {
        ListNode prev = new ListNode(value);
        prev.next = node;
        return prev;
    }


}
