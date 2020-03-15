package tool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 *
 * @author SmallHardWorker
 * @date 2020/03/15
 */
public class TestThreadFactory implements ThreadFactory {
    private static AtomicInteger SEQ = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("wchx-test-thread-" + SEQ.getAndIncrement());
        return thread;
    }
}
