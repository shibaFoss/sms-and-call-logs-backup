package libs;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * AsyncJob is a alternative to {@link android.os.AsyncTask}.
 *
 * @param <TaskResult> A Generic Object Type
 * @author shiba
 * @author Jorge
 */
public class AsyncJob<TaskResult> {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private BackgroundAction actionInBackground;
    private BackgroundResultAction actionOnMainThread;

    // An optional ExecutorService to enqueue the actions
    private ExecutorService executorService;

    // The thread created for the action
    private Thread asyncThread;

    // The FutureTask created for the action
    private FutureTask asyncFutureTask;

    // The result of the background action
    private TaskResult result;


    /**
     * Instantiates a new AsyncJob
     */
    public AsyncJob() {
    }


    /**
     * Executes the provided code immediately on the UI Thread
     *
     * @param mainThreadJob Interface that wraps the code to execute
     */
    public static void doInMainThread(final MainThreadJob mainThreadJob) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mainThreadJob.doInUIThread();
            }
        });
    }


    /**
     * Executes the provided code immediately on a background thread
     *
     * @param backgroundJob Interface that wraps the code to execute
     */
    public static void doInBackground(final BackgroundJob backgroundJob) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                backgroundJob.doInBackground();
            }
        }).start();
    }


    /**
     * Executes the provided code immediately on a background thread that will be submitted to the
     * provided ExecutorService
     *
     * @param backgroundJob Interface that wraps the code to execute
     * @param executor      Will queue the provided code
     */
    public static FutureTask doInBackground(final BackgroundJob backgroundJob, ExecutorService executor) {
        FutureTask task = (FutureTask) executor.submit(new Runnable() {
            @Override
            public void run() {
                backgroundJob.doInBackground();
            }
        });

        return task;
    }


    /**
     * Begins the background execution providing a result, similar to an AsyncTask.
     * It will execute it on a new Thread or using the provided ExecutorService
     */
    public void start() {
        if (actionInBackground != null) {

            Runnable jobToRun = new Runnable() {
                @Override
                public void run() {
                    result = (TaskResult) actionInBackground.doAsync();
                    onResult();
                }
            };

            if (getExecutorService() != null) {
                asyncFutureTask = (FutureTask) getExecutorService().submit(jobToRun);
            } else {
                asyncThread = new Thread(jobToRun);
                asyncThread.start();
            }
        }
    }


    /**
     * Cancels the AsyncJob interrupting the inner thread.
     */
    public void cancel() {
        if (actionInBackground != null) {
            if (executorService != null) {
                asyncFutureTask.cancel(true);
            } else {
                asyncThread.interrupt();
            }
        }
    }


    private void onResult() {
        if (actionOnMainThread != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    actionOnMainThread.onResult(result);
                }
            });
        }
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }


    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    public BackgroundAction getBackgroundAction() {
        return actionInBackground;
    }


    /**
     * Specifies which action to run in background
     *
     * @param actionInBackground the action
     */
    public void setActionInBackground(BackgroundAction actionInBackground) {
        this.actionInBackground = actionInBackground;
    }


    public BackgroundResultAction getActionOnResult() {
        return actionOnMainThread;
    }


    /**
     * Specifies which action to run when the background action is finished
     *
     * @param actionOnMainThread the action
     */
    public void setActionOnResult(BackgroundResultAction actionOnMainThread) {
        this.actionOnMainThread = actionOnMainThread;
    }


    public interface BackgroundAction<ActionResult> {
        ActionResult doAsync();
    }

    public interface BackgroundResultAction<ActionResult> {
        void onResult(ActionResult result);
    }

    public interface MainThreadJob {
        void doInUIThread();
    }

    public interface BackgroundJob {
        void doInBackground();
    }

    /**
     * Builder class to instantiate an AsyncJob in a clean way
     *
     * @param <JobResult> the type of the expected result
     */
    public static class AsyncJobBuilder<JobResult> {

        private BackgroundAction<JobResult> backgroundAction;
        private BackgroundResultAction backgroundResultAction;
        private ExecutorService executor;


        public AsyncJobBuilder() {

        }


        /**
         * Specifies which action to run on background
         *
         * @param action the BackgroundAction to run
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> doInBackground(BackgroundAction<JobResult> action) {
            backgroundAction = action;
            return this;
        }


        /**
         * Specifies which action to run when the background action ends
         *
         * @param action the BackgroundAction to run
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> doWhenFinished(BackgroundResultAction action) {
            backgroundResultAction = action;
            return this;
        }


        /**
         * Used to provide an ExecutorService to launch the AsyncActions
         *
         * @param executor the ExecutorService which will queue the actions
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> withExecutor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }


        /**
         * Instantiates a new AsyncJob of the given type
         *
         * @return a configured AsyncJob instance
         */
        public AsyncJob<JobResult> create() {
            AsyncJob<JobResult> asyncJob = new AsyncJob<JobResult>();
            asyncJob.setActionInBackground(backgroundAction);
            asyncJob.setActionOnResult(backgroundResultAction);
            asyncJob.setExecutorService(executor);
            return asyncJob;
        }

    }

}
