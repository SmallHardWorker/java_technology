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

        Sync() {
            setState(0);
        }

        @Override
        protected final boolean tryAcquire(int acquires) {
            if (0 == getState() && !hasQueuedPredecessors() && compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            } else {
                return 2 == getState();
            }
        }

        private boolean acquireTruly() {
            return Thread.currentThread() == getExclusiveOwnerThread() && 1 == getState();
        }

        @Override
        protected final boolean tryRelease(int releases) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            return compareAndSetState(1, 2);
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
