package com.example.halo.demo.thread.h2o;

/**
 * @Description:
 *  现在有两种线程，氧 oxygen 和氢 hydrogen，你的目标是组织这两种线程来产生水分子。
 *
 * 存在一个屏障（barrier）使得每个线程必须等候直到一个完整水分子能够被产生出来。
 *
 * 氢和氧线程会被分别给予 releaseHydrogen 和 releaseOxygen 方法来允许它们突破屏障。
 *
 * 这些线程应该三三成组突破屏障并能立即组合产生一个水分子。
 *
 * 你必须保证产生一个水分子所需线程的结合必须发生在下一个水分子产生之前。
 *
 * 换句话说:
 *
 * 如果一个氧线程到达屏障时没有氢线程到达，它必须等候直到两个氢线程到达。
 * 如果一个氢线程到达屏障时没有其它线程到达，它必须等候直到一个氧线程和另一个氢线程到达。
 * 书写满足这些限制条件的氢、氧线程同步代码。
 *
 *
 *
 * 示例 1:
 *
 * 输入: "HOH"
 * 输出: "HHO"
 * 解释: "HOH" 和 "OHH" 依然都是有效解。
 * 示例 2:
 *
 * 输入: "OOHHHH"
 * 输出: "HHOHHO"
 * 解释: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" 和 "OHHOHH" 依然都是有效解。
 *
 *
 * 提示：
 *
 * 输入字符串的总长将会是 3n, 1 ≤n≤ 50；
 * 输入字符串中的 “H” 总数将会是 2n 。
 * 输入字符串中的 “O” 总数将会是 n 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/building-h2o
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author: Halo_ry
 * @Date: 2021/3/24 20:39
 */
public class H2O1 {

    final Object lock = new Object();
    /**
     * volatile : 此处是为了 氢线程 和 氧线程中间的可见性，确保在氢线程改变值后，能在氧线程取到最新值
     * volatile 不能保证原子性，所以这里要加锁
     *
     */
    private volatile int hCount = 0;
    private volatile int oCount = 0;

    public H2O1() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        synchronized (lock) {
            while (hCount >= 2) {
                lock.wait();
            }
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            hCount++;
            if (hCount == 2 && oCount == 1) {
                hCount = 0;
                oCount = 0;
                lock.notifyAll();
            }
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized (lock) {
            while (oCount >= 1) {
                lock.wait();
            }
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            oCount++;
            if (hCount == 2 && oCount == 1) {
                hCount = 0;
                oCount = 0;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        H2O1 h2O = new H2O1();
        String input = "OOHHHH";

        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (aChar == 'H') {
                new Thread(() -> {
                    try {
                        h2O.hydrogen(() -> System.out.print("H"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                Thread t2 = new Thread(() -> {
                    try {
                        h2O.oxygen(() -> System.out.print("O"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                t2.start();
            }
        }
    }
}
