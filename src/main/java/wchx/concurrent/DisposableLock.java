package wchx.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 一次性锁
 *
 * @author SmallHardWorker
 * @date 2020/03/15
 */
public class DisposableLock {

    private final DisposableLock.Sync sync;

    public DisposableLock() {
        sync = new Sync();
    }

    static class Sync extends AbstractQueuedSynchronizer {

        private AtomicInteger status = new AtomicInteger(0);

        Sync() {
            setState(0);
        }

        @Override
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (0 == c) {
                if (!hasQueuedPredecessors() && status.compareAndSet(0, 1) && compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                } else if (2 == status.get()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        private boolean acquireTruly() {
            if (Thread.currentThread() == getExclusiveOwnerThread() && status.get() < 2) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (0 == c) {
                free = true;
            }
            setState(c);
            status.compareAndSet(1, 2);
            return free;
        }
    }

    public boolean tryLock() {
        sync.acquire(1);
        return sync.acquireTruly();
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        if (sync.tryAcquireNanos(1, unit.toNanos(timeout))) {
            return sync.acquireTruly();
        } else {
            return false;
        }
    }

    public void unlock() {
        sync.release(1);
    }
}
