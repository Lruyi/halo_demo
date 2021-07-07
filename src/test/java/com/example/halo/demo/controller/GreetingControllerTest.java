package com.example.halo.demo.controller;

import cn.hutool.setting.dialect.Props;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.example.halo.demo.entity.*;
import com.example.halo.demo.entity.account.Account;
import com.example.halo.demo.entity.account.Account2;
import com.example.halo.demo.entity.account.Account3;
import com.example.halo.demo.entity.account.AddMoneyThread;
import com.example.halo.demo.utils.MyUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

/**
 * @Description: 一个JUnit4的单元测试用例执行顺序为: @BeforeClass -> @Before -> @Test -> @After -> @AfterClass
 * @Author: Halo_ry
 * @Date: 2020/3/24 7:38
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringRunner.class)
class GreetingControllerTest {

    @Test
    void greeting() {
    }

//    public static void main(String[] args) {
//        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;
//
//        System.out.println(f1 == f2);// true
//        System.out.println(f3 == f4);// false
//    }

    /**
     * 对于包装类型和它的基本类型进行‘==’比较时，都会把包装类型拆箱成基本类型，再进行比较。
     * 这样比较的就是值了，而不是引用地址，对于基本类型‘==’比较的是值，而对于引用类型‘==’比较的是引用地址
     *
     * @param args
     */
    public static void main(String[] args) {
        Integer a = new Integer(3);
        Integer b = 3;                  // 将3自动装箱成Integer类型
        int c = 3;
        System.out.println(a == b);     // false 两个引用没有引用同一对象
        System.out.println(a == c);     // true a自动拆箱成int类型再和c比较
    }

    /**
     * intern方法：
     * jkd6: intern()方法会把首次遇到的字符串实例复制到永久代中，返回的也是永久代中这个字符串的引用，而用StringBuilder创建的字符串实例在Java堆上，所以必然不是同一个引用。
     * jkd7及以后：intern()方法不会再复制实例，而是在常量池中记录首次出现的实例引用，因此intern方法返回的引用和由StringBuilder创建的那个字符串实例是同一个。
     * <p>
     * good 不是关键词，所以是首次出现
     * java是关键词，所以不是首次出现，不符合‘首次出现的原则’，所以返回false，而good是首次出现的，返回true
     */
    @Test
    public void test1() {
        String s1 = new StringBuilder("go").append("od").toString();
        System.out.println(s1.intern() == s1); // true
        String s2 = new StringBuilder("ja").append("va").toString();
        System.out.println(s2.intern() == s2); // false
    }

    @Test
    public void test2() {
        String s1 = "Programming";
        String s2 = new String("Programming");
        String s3 = "Program";
        String s4 = "ming";
        String s5 = "Program" + "ming"; // 查看class文件发现，这个编译后会变成：  String s5 = "Programming";
        String s6 = s3 + s4;// 查看class文件发现，这个编译后没有变：  String s6 = s3 + s4;
        System.out.println(s1 == s2); // false
        System.out.println(s1 == s2.intern()); // true
        System.out.println(s1 == s5); // true
        System.out.println(s1 == s6); // false
        System.out.println(s1 == s6.intern()); // true
        System.out.println(s2 == s2.intern()); // false
        System.out.println(s1 == s1.intern()); // true
    }

    /**
     * 基于序列化和反序列化的深度拷贝
     */
    @Test
    public void test3() {
        try {
            Greeting greeting = new Greeting(12, "halo");
            Greeting clone = MyUtils.clone(greeting);
            System.out.println(JSON.toJSONString(clone));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于Cloneable 的浅拷贝
     * 实现Cloneable接口并重写Object类中的clone()方法
     */
    @Test
    public void test4() {
        try {
            Greeting greeting = new Greeting(12, "halo");
            Greeting clone = greeting.clone();
            System.out.println(JSON.toJSONString(clone));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AtomicInteger num = new AtomicInteger(0);

    /**
     * 二分查找法
     */
    @Test
    public void test5() {
        int[] arr = {5, 13, 19, 30, 49, 80, 99, 106};
        int key = 64;
        // 二分查询
        int index = halfSearch(key, arr);
        System.out.println("key的索引位子：" + index);
    }

    /**
     * 二分查询
     *
     * @param key
     * @param arr
     * @return
     */
    private int halfSearch(int key, int[] arr) {
        // 定义索引位子
        int min, mid, max;
        min = 0;
        max = arr.length - 1;
        if (key < arr[min]) {
            return -1;
        }
        while (min <= max) {
            mid = max + min >> 1;
            if (key > arr[mid]) {
                min = mid + 1;
            } else if (key < arr[mid]) {
                max = mid - 1;
            } else {
                return mid;
            }
        }
        return min;
    }

    /**
     * 排序
     */
    @Test
    public void sortTest() {
        int[] arr = {34, 12, 69, 24, 100, 21, 40};
        // 选择排序
//        arr = selectSort(arr);
        // 冒泡排序
//        arr = bubbleSort(arr);
        // 快速排序
        quickSort(arr, 0, arr.length - 1);
//        quick_Sort(arr, 0, arr.length - 1);
        print(arr);

    }

    /**
     * 快速排序，时间复杂度（O(nlogn)）
     *
     * @param arr
     * @param begin
     * @param end
     */
    void quick_Sort(int[] arr, int begin, int end) {
        if (begin > end) {
            return;
        }
        int tmp = arr[begin];
        int i = begin;
        int j = end;
        while (i != j) {
            while (arr[j] >= tmp && j > i) {
                j--;
            }
            while (arr[i] <= tmp && j > i) {
                i++;
            }
            if (j > i) {
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
            }
        }
        arr[begin] = arr[i];
        arr[i] = tmp;
        quick_Sort(arr, begin, i - 1);
        quick_Sort(arr, i + 1, end);
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param low
     * @param high
     */
    private void quickSort(int[] arr, int low, int high) {
        // 找寻基准数据的正确索引
        int index = getIndex(arr, low, high);
        // 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
        quickSort(arr, 0, index - 1);
        quickSort(arr, index + 1, high);
    }

    private static int getIndex(int[] arr, int low, int high) {
        // 基准数据
        int tmp = arr[low];
        while (low < high) {
            // 当队尾的元素大于等于基准数据时,向前挪动high指针
            while (low < high && arr[high] >= tmp) {
                high--;
            }
            // 如果队尾元素小于tmp了,需要将其赋值给low
            arr[low] = arr[high];
            // 当队首元素小于等于tmp时,向前挪动low指针
            while (low < high && arr[low] <= tmp) {
                low++;
            }
            // 当队首元素大于tmp时,需要将其赋值给high
            arr[high] = arr[low];
        }
        // 跳出循环时low和high相等,此时的low或high就是tmp的正确索引位置
        // 由原理部分可以很清楚的知道low位置的值并不是tmp,所以需要将tmp赋值给arr[low]
        arr[low] = tmp;
        return low; // 返回tmp的正确位置
    }


    /**
     * 冒泡排序:（先找出最大的放到最后面）
     * 第一次循环先把最大的数字找出来放到最后一位，
     * 第二次循环会把第二大的数字放到倒数第二位，
     * ... ...
     * <p>
     * 原始：{34, 12, 69, 24, 100, 21, 40}
     * <p>
     * 第1次循环 [12,34,24,69,21,40,100]
     * 第2次循环 [12,24,34,21,40,69,100]
     * 第3次循环 [12,24,21,34,40,69,100]
     * 第4次循环 [12,21,24,34,40,69,100]
     * 第5次循环 [12,21,24,34,40,69,100]
     * 第6次循环 [12,21,24,34,40,69,100]
     *
     * @param arr
     * @return
     */
    private int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "次循环 " + JSON.toJSONString(arr));
        }
        return arr;
    }

    /**
     * 选择排序: （先找出最小的，放到最前面）
     * 第一次循环会把最小的找出来放到第一位，以此类推
     * <p>
     * 原始：{34, 12, 69, 24, 100, 21, 40}
     * <p>
     * 第1次循环 [12,34,69,24,100,21,40]
     * 第2次循环 [12,21,69,34,100,24,40]
     * 第3次循环 [12,21,24,69,100,34,40]
     * 第4次循环 [12,21,24,34,100,69,40]
     * 第5次循环 [12,21,24,34,40,100,69]
     * 第6次循环 [12,21,24,34,40,69,100]
     *
     * @param arr
     * @return
     */
    private int[] selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "次循环 " + JSON.toJSONString(arr));
        }
        return arr;
    }

    /**
     * 打印
     *
     * @param arr
     */
    private void print(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            if (i < arr.length - 1) {
                System.out.print(arr[i] + ", ");
            } else {
                System.out.print(arr[i] + "]");
            }
        }
    }

    @Test
    public void test6() {
        String str = "12345";
        Integer integer = Integer.valueOf(str);
        int i = Integer.parseInt(str);

        int num = 1234;
        String s1 = num + "";
        String s = String.valueOf(num);

        String ss1 = "你好";
        try {
            // 怎样将GB2312编码的字符串转换为ISO-8859-1编码的字符串？
            String ss2 = new String(ss1.getBytes("GB2312"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        String str = "nihaomahalo";
//        String newStr = reverse(str);
        String newStr = myReverse(str);
        System.out.println(newStr);
    }

    private String myReverse(String originStr) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isBlank(originStr) || originStr.length() <= 1) {
            return originStr;
        }
        char[] chars = originStr.toCharArray();
        // ritar快捷键
        for (int i = chars.length - 1; i >= 0; i--) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    public static String reverse(String originStr) {
        if (originStr == null || originStr.length() <= 1)
            return originStr;
        return reverse(originStr.substring(1)) + originStr.charAt(0);
    }


    /**
     * 可能的结果：pong ping pong /  pong pong ping
     */
    @Test
    public void test8() {
        Thread t = new Thread() {
            public void run() {
                pong();
            }
        };
        t.run();
        t.start();
        System.out.println("ping");

    }

    private static void pong() {
        System.out.println("pong");
    }

    /**
     * 结果：A1
     * 分析：interrupt方法不会中断一个正在运行的线程，而是发出中断请求，然后由线程在下一个合适的时刻中断自己。
     * 这个时刻叫取消点，例如：wait, sleep, join等，将严格地处理这种请求。中断后抛出一个异常InterruptedException
     */
    @Test
    public void test9() {
        Thread t1 = new Thread(() -> {
            try {
                int i = 0;
                while (i++ < 100) {
                }
                System.out.println("A1");
            } catch (Exception e) {
                System.out.println("B1");
            }
        });
        t1.start();
        t1.interrupt();
    }

    /**
     * 结果：B2
     */
    @Test
    public void test10() {
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
                System.out.println("A2");
            } catch (InterruptedException e) {
                System.out.println("B2");
            }
        });
        t2.start();
        t2.interrupt();
    }

    /**
     * 结果：b default
     */
    @Test
    public void test11() {
        String index = "b";
        switch (index) {
            case "a":
                System.out.println("a");
            case "b":
                System.out.println("b");
            default:
                System.out.println("default");
        }
    }

    /**
     * 用lambda表达式怎么从一个List<String> 中打印出重复次数大于3的元素
     */
    @Test
    public void test12() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("a");
        list.add("c");
        list.add("a");
        list.add("c");
        list.add("c");
        list.add("c");
        list.add("d");
        list.add("f");

        list.stream().collect(groupingBy(Function.identity(), counting())).forEach((k, v) -> {
            if (v > 3) {
                System.out.println(k);
            }
        });

        // -------------------------------------------------------------------------

        list.stream().collect(groupingBy(Function.identity(), mapping(Function.identity(), counting()))).forEach((k, v) -> {
            if (v > 3) {
                System.out.println(k);
            }
        });
    }

    /**
     * 排序
     */
    @Test
    public void test13() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("Hao LUO", 33));
        list.add(new Student("XJ WANG", 32));
        list.add(new Student("KJ WANG", 32));
        list.add(new Student("Bruce LEE", 60));
        list.add(new Student("Bob YANG", 22));
        // 先年龄排序，再name第一个字母排序
        list.sort(Comparator.comparing(Student::getAge).thenComparing(Student::getName));
        for (Student stu : list) {
            System.out.println(stu);
        }
    }

    /**
     * CountDownLatch使用
     * 主线程调用await()方法,等到其他三个线程执行完后才继续执行
     *
     * @throws InterruptedException
     */
    @Test
    public void CountDownLatchTest() throws InterruptedException {
        // 创建CountDownLatch实例,计数器的值初始化为3
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        // 创建三个线程,每个线程等待1s,表示执行比较耗时的任务
        for (int i = 0; i < 3; i++) {
            final int num = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("thread %d has finished", num));
                // 任务完成后调用CountDownLatch的countDown()方法
                countDownLatch.countDown();
            }).start();
        }
        /*主线程调用await()方法,等到其他三个线程执行完后才继续执行*/
        countDownLatch.await();
        System.out.print("all threads have finished, main thread will continue run");
    }

    /**
     * 信号量
     */
    @Test
    public void SemaphoreTest() {
        ExecutorService service = Executors.newCachedThreadPool();
        final Semaphore sp = new Semaphore(3);//创建Semaphore信号量，初始化许可大小为3
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            Runnable runnable = () -> {
                try {
                    sp.acquire();//请求获得许可，如果有可获得的许可则继续往下执行，许可数减1。否则进入阻塞状态
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3 - sp.availablePermits()) + "个并发");
                try {
                    Thread.sleep((long) (Math.random() * 100000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
                sp.release();//释放许可，许可数加1
                //下面代码有时候执行不准确，因为其没有和上面的代码合成原子单元
                System.out.println("线程" + Thread.currentThread().getName() + "已离开，当前已有" + (3 - sp.availablePermits()) + "个并发");
            };
            service.execute(runnable);
        }
    }

    @Test
    public void SemaphoreTest2() {
        // 线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        // 只能5个线程同时访问
        final Semaphore semp = new Semaphore(5);
        // 模拟20个客户端访问
        for (int index = 0; index < 20; index++) {
            final int NO = index;
            Runnable runnable = () -> {
                try {
                    // 获取许可
                    semp.acquire();
                    System.out.println("获取许可: " + NO);
                    //模拟实际业务逻辑
                    Thread.sleep((long) (Math.random() * 100));
                    // 访问完后，释放
                    semp.release();
                    System.out.println("释放：" + NO);
                } catch (InterruptedException e) {

                }
            };
            exec.execute(runnable);
        }
//        exec.shutdown();
//        while (!exec.isTerminated()) {}

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 退出线程池
        exec.shutdown();
    }

    /**
     * 银行转账
     * 多个线程进行银行存钱      synchronized
     * <p>
     * 如果所有任务在关闭后都已完成，则返回{@code true}。
     * 请注意，除非首先调用{@code shutdown}或{@code shutdownNow}，否则{@code isTerminated}永远不会是{@code true}。
     * threadPool.shutdown();
     * while (!threadPool.isTerminated()) {}
     */
    @Test
    public void test14() {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        Account account = new Account();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> account.deposit(BigDecimal.ONE);
            threadPool.execute(runnable);
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        System.out.println("账户余额: " + account.getBalance());
        System.out.println("synchronized耗时：" + (System.currentTimeMillis() - l));
    }

    /**
     * 银行转账
     * 多个线程进行银行存钱      加锁Lock
     */
    @Test
    public void test15() {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        Account2 account = new Account2();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> account.deposit(BigDecimal.ONE);
            threadPool.execute(runnable);
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        System.out.println("账户余额: " + account.getBalance());
        System.out.println("ReentrantLock耗时：" + (System.currentTimeMillis() - l));
    }

    /**
     * 银行转账
     * 多个线程进行银行存钱
     */
    @Test
    public void test16() {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        Account3 account = new Account3();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            threadPool.execute(new AddMoneyThread(account, BigDecimal.ONE));
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        System.out.println("账户余额: " + account.getBalance());
        System.out.println("ReentrantLock耗时：" + (System.currentTimeMillis() - l));
    }

    @Test
    public void test17() {
        String str = "北京市(朝阳区)(西城区)(海淀区)";
        Pattern p = Pattern.compile(".*?(?=\\()");
        Matcher m = p.matcher(str);
        if (m.find()) {
            System.out.println(m.group());
        }
    }

    @Test
    public void test18() {
        Account account = new Account();
        Class aClass = account.getClass();
        try {
            Object obj = aClass.newInstance();

            Method sleep = aClass.getMethod("sleep", String.class);
            sleep.invoke(obj, "halo");
            Constructor[] constructors = aClass.getConstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如下，调用join方法，会等到调用该join方法的线程执行完毕后，再执行其他线程，结果如下：
     * 第1个睡了10毫秒。。。
     * 第2个睡了10毫秒。。。
     * 第3个睡了10毫秒。。。
     * 第4个睡了10毫秒。。。
     * 第5个睡了10毫秒。。。
     * 第6个睡了10毫秒。。。
     * 第7个睡了10毫秒。。。
     * 第8个睡了10毫秒。。。
     * 第9个睡了10毫秒。。。
     * 第10个睡了10毫秒。。。
     * 我是主线程。。。
     */
    @Test
    public void test19() {
        for (int i = 0; i < 10; i++) {
            int num = i;
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10);
                    System.out.println("第" + (num + 1) + "个睡了10毫秒。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            // 启动线程
            thread.start();
            try {
                //这个方法会等副线程执行完成之后才会接下来执行主线程，会影响主线程的执行
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("我是主线程。。。");
    }

    /**
     * 如下，没有调用join方法，启多个线程不一定那个先执行，每次执行结果不一样，结果如下：
     * --------------------------------
     * 第8个睡了10毫秒。。。
     * 第10个睡了10毫秒。。。
     * 第9个睡了10毫秒。。。
     * 第1个睡了10毫秒。。。
     * 第6个睡了10毫秒。。。
     * 第5个睡了10毫秒。。。
     * 第2个睡了10毫秒。。。
     * 第7个睡了10毫秒。。。
     * 第3个睡了10毫秒。。。
     * 第4个睡了10毫秒。。。
     * 我是主线程。。。
     * ------------------------
     * <p>
     * 第3个睡了10毫秒。。。
     * 第6个睡了10毫秒。。。
     * 第2个睡了10毫秒。。。
     * 第4个睡了10毫秒。。。
     * 第10个睡了10毫秒。。。
     * 第7个睡了10毫秒。。。
     * 第1个睡了10毫秒。。。
     * 第8个睡了10毫秒。。。
     * 第5个睡了10毫秒。。。
     * 第9个睡了10毫秒。。。
     * 我是主线程。。。
     */
    @Test
    public void test20() {
        for (int i = 0; i < 10; i++) {
            int num = i;
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10);
                    System.out.println("第" + (num + 1) + "个睡了10毫秒。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我是主线程。。。");
    }

    /**
     * 给一个整数数组，找到两个数使得他们的和等于一个给定的数 target
     */
    @Test
    public void test21() {
        int[] arr = {23, 33, 99, 44, 11, 8, 62};
        int target = 77;
        List<Integer> list = towSum4Target(arr, target);
        if (!CollectionUtils.isEmpty(list)) {
            for (Integer integer : list) {
                System.out.println(integer);
            }
        } else {
            System.out.println("未找到");
        }
    }

    private static List<Integer> towSum4Target(int[] arr, int target) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    list.add(arr[i]);
                    list.add(arr[j]);
                    return list;
                }
            }
        }
        return null;
    }

    /**
     * 要求：将 aabbbcdaa 输出为：abcda，也就是去掉连续重复的字母
     */
    @Test
    public void test22() {
        String str = "aabbbcdaa";
        char[] chars = str.toCharArray();
        char min = chars[0];
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(min));
        for (char c : chars) {
            if (c != min) {
                result.add(String.valueOf(c));
            }
            min = c;
        }
        System.out.println(String.join("", result));
    }

    /**
     * 要求：将 aabbbcdaa 输出为：abcda，也就是去掉连续重复的字母
     */
    @Test
    public void test23() {
        String str = "aabbbcdaa";
        char[] chars = str.toCharArray();
        List<String> result = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (true) {
            if (j > chars.length - 1) {
                result.add(String.valueOf(chars[i]));
                break;
            }
            if (chars[i] != chars[j]) {
                result.add(String.valueOf(chars[i]));
            }
            i++;
            j++;
        }
        System.out.println(String.join("", result));
    }

    @Test
    public void test24() {
        int MAXIMUM_CAPACITY = 1 << 30;// 1 * 2^30
        int sshift = 0;
        int ssize = 1;
        while (ssize < 16) {
            ++sshift;
            ssize <<= 1;
        }
        System.out.println("MAXIMUM_CAPACITY " + MAXIMUM_CAPACITY);// 1073741824
        System.out.println(32 - sshift);// 28
        System.out.println(ssize - 1);// 15

        int n = 18;
        int i = n >>> 3;
        System.out.println("i = " + i);// 2   i = 18/3^2
    }

    @Test
    public void test25() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        Properties prop = Props.getProp("application.yml");
        String driverClassName = prop.getProperty("driver-class-name");
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);

        DruidPooledConnection connection = druidDataSource.getConnection();
        String sql = "select * from people where id =1";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
        statement.close();
        connection.close();
        druidDataSource.close();
    }

    @Test
    public void test26() {
        int[] nums = {1,1,2};
            List<Integer> newNum = new ArrayList<Integer>();
            int tmp = nums[0];
            newNum.add(tmp);
            for(int i=0; i < nums.length; i++){
                if(nums[i] != tmp){
                    newNum.add(nums[i]);
                }
                tmp = nums[i];
            }
        for (int i = 0; i < newNum.size(); i++) {
            System.out.println(newNum.get(i));
        }
    }

    /**
     * TreeSet 默认自然排序（升序）
     */
    @Test
    public void test27() {
        // TreeSet 默认自然排序（升序）
        TreeSet<Integer> treeSet = new TreeSet<>();
//        TreeSet<Integer> treeSet = new TreeSet<>(cmp);
        treeSet.add(9);
        treeSet.add(2);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(13);
        treeSet.add(-1);
        while (!treeSet.isEmpty()) {
            System.out.print(treeSet.pollFirst() + " ");
//            System.out.print(treeSet.pollLast() + " ");
        }
    }
    // 定义一个降序比较器
    private static Comparator<Integer> cmp = (o1, o2) -> (o2 - o1);

    /**
     * TreeMap: 空参构造，默认key升序排序
     *          如果传入了一个自定义降序比较器，那么就是降序排序
     */
    @Test
    public void test28() {
        TreeMap<Integer, Object> treeMap = new TreeMap<>();
        treeMap.put(6, "ni");
        treeMap.put(9, "hao");
        treeMap.put(3, "bu");
        treeMap.put(16, "shi");
        treeMap.put(2, "er");
        System.out.println(treeMap); // {2=er, 3=bu, 6=ni, 9=hao, 16=shi}


        TreeMap<Integer, Object> treeMap2 = new TreeMap<>(cmp);
        treeMap2.put(6, "ni");
        treeMap2.put(9, "hao");
        treeMap2.put(3, "bu");
        treeMap2.put(16, "shi");
        treeMap2.put(2, "er");
        System.out.println(treeMap2); // {16=shi, 9=hao, 6=ni, 3=bu, 2=er}
    }

    /**
     * LinkedHashMap 先进先出
     */
    @Test
    public void test29() {
        Map<Object, Object> linkedHashMap = Maps.newLinkedHashMap();
        Map<String, Object> map = new LinkedHashMap<>();
        linkedHashMap.put("CNY", 1);
        linkedHashMap.put("USB", 7.8);
        linkedHashMap.put("TWD", 0.5);
        linkedHashMap.put("JPY", 0.75);
        for (Map.Entry<Object, Object> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }
    }

    @Test
    public void test30() {

    }

}