package example;

import java.util.concurrent.RejectedExecutionException;

public interface Executor {
    /**
     * Executes the given command
     * at some time in the future.
     * The command may execute in a new thread,
     * in a pooled thread, or in the calling
     * thread, at the discretion of the Executor
     * implementation.
     */
    void execute(Runnable command);
}




