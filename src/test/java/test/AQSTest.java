package test;

import org.junit.Test;
import tool.TestThreadFactory;
import tool.TestUtil;
import wchx.concurrent.DisposableLock;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * aqs 测试
 *
 * @author SmallHardWorker
 * @date 2020/03/15
 */
public class AQSTest {

    @Test
    public void disposableLockTest() throws IOException {
        TestThreadFactory testThreadFactory = new TestThreadFactory();
        DisposableLock lock = new DisposableLock();
        TestUtil.cycle(2, () -> {
            testThreadFactory.newThread(() -> {
                if (lock.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + " get lock success " + new Date());
                    TestUtil.noExceptionRun(() -> Thread.sleep(5000));
                    lock.unlock();
                    if (lock.tryLock()) {
                        System.out.println(Thread.currentThread().getName() + " get lock success " + new Date());
                        TestUtil.noExceptionRun(() -> Thread.sleep(5000));
                        lock.unlock();
                    } else {
                        System.out.println(Thread.currentThread().getName() + " get lock fail " + new Date());
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " get lock fail " + new Date());
                }
            }).start();
        });
        TestUtil.noExceptionRun(() -> Thread.sleep(1000));
        TestUtil.cycle(3, () -> testThreadFactory.newThread(() -> {
            TestUtil.noExceptionRun(() -> {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + " get lock success " + new Date());
                    lock.unlock();
                } else {
                    System.out.println(Thread.currentThread().getName() + " get lock fail " + new Date());
                }
            });
        }).start());
        System.in.read();
    }
}
