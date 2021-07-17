package thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResultGetter {

    public static void main(String[] args) throws InterruptedException {

        int result = useReentrantLock();
    }

    //1 use future
    public static int useFuture() throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newSingleThreadExecutor();

        Callable<Integer> call = (Callable) () -> 1;

        Future<Integer> submit = es.submit(call);

        return submit.get();

    }

    // 2. use future task

    public static int useFutureTask() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Callable<Integer> call = (Callable) () -> 1;
        FutureTask<Integer> futureTask = new FutureTask<>(call);
        es.submit(futureTask);

        return futureTask.get();
    }

    //3. use completable future future

    public static int useCompletableFuture() {

        return CompletableFuture.supplyAsync(()->1).join();
    }

    //4 use runnable and join
    public static int useRunable0() throws InterruptedException {

        AtomicInteger intValue = new AtomicInteger();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                intValue.set(1);
            }
        });

        thread.start();
        thread.join();
        return intValue.get();
    }

    //5. use runnable and countdownlatch

    public static int useRunable1() throws InterruptedException {
        AtomicInteger intValue = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                intValue.set(1);
                countDownLatch.countDown();
            }
        });

        thread.start();
        countDownLatch.await();

        return intValue.get();
    }

    //6.synchronized

    public static int useSynchronized() throws InterruptedException {
        Object lock = new Object();
        AtomicInteger intValue = new AtomicInteger(0);
        int result;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    intValue.set(1);
                    lock.notifyAll();
                }
            }
        });
        thread.start();

        synchronized (lock) {
            while (intValue.get() == 0) {
                lock.wait();
            }
            return intValue.get();
        }
    }

    //7. Semaphore

    public static int useSemaphore() throws InterruptedException {
        AtomicInteger intValue = new AtomicInteger(0);
        Semaphore semaphore = new Semaphore(1);
        int result = 0;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    intValue.set(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();
            }
        });

        thread.start();

        while(true) {
            semaphore.acquire();
            result = intValue.get();
            semaphore.release();
            if(result != 0) {
                return result;
            }

        }
    }

    //8 ReentrantLock

    public static int useReentrantLock() throws InterruptedException {
        AtomicInteger intValue = new AtomicInteger(0);
        ReentrantLock lock = new ReentrantLock();
        Condition complete = lock.newCondition();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                intValue.set(1);
                complete.signal();
                lock.unlock();
            }
        });

        thread.start();

        lock.lock();
        while(intValue.get()==0) {
            complete.await();
        }
        lock.unlock();

        return intValue.get();


    }


}
