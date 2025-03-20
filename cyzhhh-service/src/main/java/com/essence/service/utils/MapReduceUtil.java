package com.essence.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MapReduceUtil {


//懒汉式单例创建线程池
    @Slf4j
    public static class ThreadPoolLazyHolder {

        /**
         * CPU核数
         **/
        public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

        /**
         * 空闲保活时限，单位秒
         */
        public static final int KEEP_ALIVE_SECONDS = 30;
        /**
         * 有界队列size
         */
        public static final int QUEUE_SIZE = 10000;


        public static final int MAXIMUM_POOL_SIZE = CPU_COUNT;


        //线程池： 用于CPU密集型任务
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                MAXIMUM_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(QUEUE_SIZE),
                new CustomThreadFactory("cpu"));


        public static ThreadPoolExecutor getInnerExecutor() {
            return EXECUTOR;
        }

        static {
            log.info("线程池已经初始化");

            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new ShutdownHookThread("任务线程池", new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            //优雅关闭线程池
                            shutdownThreadPoolGracefully(EXECUTOR);
                            return null;
                        }
                    }));
        }


        public static class CustomThreadFactory implements ThreadFactory {
            //线程池数量
            private static final AtomicInteger poolNumber = new AtomicInteger(1);
            private final ThreadGroup group;

            //线程数量
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String threadTag;

            public CustomThreadFactory(String threadTag) {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() :
                        Thread.currentThread().getThreadGroup();
                this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
            }

            @Override
            public Thread newThread(Runnable target) {
                Thread t = new Thread(group, target,
                        threadTag + threadNumber.getAndIncrement(),
                        0);
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        }

        public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
            if (!(threadPool instanceof ExecutorService) || threadPool.isTerminated()) {
                return;
            }
            try {
                threadPool.shutdown();   //拒绝接受新任务
            } catch (SecurityException e) {
                return;
            } catch (NullPointerException e) {
                return;
            }
            try {
                // 等待 60 s，等待线程池中的任务完成执行
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    // 调用 shutdownNow 取消正在执行的任务
                    threadPool.shutdownNow();
                    // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                    if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("线程池任务未正常执行结束");
                    }
                }
            } catch (InterruptedException ie) {
                // 捕获异常，重新调用 shutdownNow            threadPool.shutdownNow();
            }
            //任然没有关闭，循环关闭1000次，每次等待10毫秒
            if (!threadPool.isTerminated()) {
                try {
                    for (int i = 0; i < 1000; i++) {
                        if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                            break;
                        }
                        threadPool.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                } catch (Throwable e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        public static class ShutdownHookThread extends Thread {
            private volatile boolean hasShutdown = false;
            private static AtomicInteger shutdownTimes = new AtomicInteger(0);
            private final Callable callback;

            /**
             * Create the standard hook thread, with a call back, by using {@link Callable} interface.
             * * @param name
             *
             * @param callback The call back function.
             */
            public ShutdownHookThread(String name, Callable callback) {
                super("JVM退出钩子(" + name + ")");

                this.callback = callback;
            }

            /**
             * Thread run method.         * Invoke when the jvm shutdown.
             */
            @Override
            public void run() {
                synchronized (this) {
                    System.out.println(getName() + " starting.... ");
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                        long beginTime = System.currentTimeMillis();
                        try {
                            this.callback.call();
                        } catch (Exception e) {
                            System.out.println(getName() + " error: " + e.getMessage());
                        }
                        long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                        System.out.println(getName() + "  耗时(ms): " + consumingTimeTotal);
                    }
                }
            }
        }


    }


    private MapReduceUtil() {

    }


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor innerExecutor = ThreadPoolLazyHolder.getInnerExecutor();
    public static void execute(Runnable runnable){
        innerExecutor.execute(runnable);
    }

    public static <T, S> List<S> handle(List<T> tList, Function<List<T>, S> reduceFunc) {

        List<Work<T, S>> workList = new ArrayList<>();

        List<List<T>> lists = pineList(tList, CPU_COUNT);
        for (List<T> list : lists) {
            Work<T, S> work = new Work<>(list, reduceFunc);
            workList.add(work);
        }

        List<Future<S>> results = null;
        List<S> collect = new ArrayList<>();
        try {
            results = innerExecutor.invokeAll(workList);

            for (Future<S> result : results) {
                S s = result.get();
                collect.add(s);

            }

            return collect;
        } catch (Exception e) {
            e.printStackTrace();
            return collect;

        }

        //合并结果  
    }

    ;

    private static class Work<T, S> implements Callable<S> {

        /**
         * 需要处理的对象集合，每个线程传递自己的对象.
         */
        private List list;

        private Function<List<T>, S> reduceFunc;


        public Work(List<T> list, Function<List<T>, S> reduceFunc) {
            this.list = list;
            this.reduceFunc = reduceFunc;

        }

        @Override
        public S call()  {
            S s = (S) reduceFunc.apply(list);
            return s;
        }

    }

    private static <K> List<List<K>> pineList(List<K> source, int n) {
        List<List<K>> result = new ArrayList<List<K>>();
        int remainder = source.size() % n; //(先计算出余数)  
        int number = source.size() / n; //然后是商  
        int offset = 0;//偏移量  
        for (int i = 0; i < n; i++) {
            List<K> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }

        return result;
    }


    public static <T, S> List<S> handleList(List<T> tList, Function<List<T>, List<S>> reduceFunc) {
        List<List<S>> mapResults = handle(tList, reduceFunc);
        List<S> results = new ArrayList<>();
        for (List<S> result : mapResults) {
            if (result != null) {
                results.addAll(result);
            }
        }
        return results;


        //合并结果
    }

}