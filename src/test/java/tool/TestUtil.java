package tool;

/**
 *
 * @author SmallHardWorker
 * @date 2020/03/15
 */
public class TestUtil {

    public static void cycle(int times, Runnable runnable) {
        for (int i = 0; i < times; i++) {
            runnable.run();
        }
    }

    public static void noExceptionRun(NoExceptionRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {

        }
    }

    @FunctionalInterface
    public interface NoExceptionRunnable {

        void run() throws Exception;

    }
}
