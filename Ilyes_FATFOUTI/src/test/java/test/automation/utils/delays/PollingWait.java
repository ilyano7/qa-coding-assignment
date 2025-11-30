package test.automation.utils.delays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PollingWait<T> {

    public static HashMap<String, List<Duration>> timeWastedByCategories = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(PollingWait.class.getSimpleName());
    private static final String START_MESSAGE = "%s starting : %d s limit with %d s delay between tries";
    private static final String SUCCESS_MESSAGE = "%s : value obtained :)";
    private static final String FAILURE_MESSAGE = "%s : timeout reached. Failure :(";


    public Optional<T> waitForValue(Actions delay, int allowedSeconds, int delayBetweenTries) {
        long start = System.nanoTime();
        LOGGER.info(String.format(START_MESSAGE, delay.getDescription(), allowedSeconds, delayBetweenTries));
        ActionExecutor executor = new ActionExecutor(delay, delayBetweenTries, TimeUnit.SECONDS);
        ExecutorStopper stopper = new ExecutorStopper(allowedSeconds, TimeUnit.SECONDS);

        executor.setStopper(stopper);
        executor.run();

        long end = System.nanoTime();

        // Keeping track of time wasted
        Duration newDuration = Duration.ofNanos(end - start);
        List<Duration> durationList = timeWastedByCategories.getOrDefault(delay.getUniqueId(), new ArrayList<>());
        durationList.add(newDuration);
        timeWastedByCategories.put(delay.getUniqueId(), durationList);

        return executor.getResult();
    }

    public static Duration getTotalTimeWasted() {
        Duration totalTimeWasted = Duration.ZERO;
        for (Map.Entry<String, List<Duration>> entry : timeWastedByCategories.entrySet()) {
            for (Duration duration : entry.getValue()) {
                totalTimeWasted = totalTimeWasted.plus(duration);
            }
        }
        return totalTimeWasted;
    }


    private class ActionExecutor extends Thread implements test.automation.utils.delays.ActionExecutor {
        Optional<T> result = Optional.empty();
        Actions<T> delay;
        ExecutorStopper stopper;
        int delayBetweenTries;
        TimeUnit timeUnit;

        boolean stopped = false;

        public ActionExecutor(Actions<T> delay, int delayBetweenTries, TimeUnit timeUnit) {
            this.delay = delay;
            this.delayBetweenTries = delayBetweenTries;
            this.timeUnit = timeUnit;
        }

        public Optional<T> getResult() {
            return result;
        }

        public Actions<T> getDelay() {
            return delay;
        }

        public void kill() {
            stopped = true;
        }

        public void setStopper(ExecutorStopper stopper) {
            this.stopper = stopper;
            stopper.setExecutor(this);
        }

        @Override
        public void run() {
            if (stopper != null) {
                stopper.start();
                while (!result.isPresent() && !stopped) {
                    result = delay.perform();
                    if (!result.isPresent()) {
                        try {
                            timeUnit.sleep(delayBetweenTries);
                        } catch (InterruptedException e) {
                            LOGGER.error(e.getMessage());
                        }
                        LOGGER.debug("Couldn't get a value... Retrying");
                    }
                }
                stopper.interrupt();
            }
        }

    }

    private class ExecutorStopper extends Thread {
        ActionExecutor executor;
        int timeout;
        TimeUnit timeUnit;

        public ExecutorStopper(int timeout, TimeUnit timeUnit) {
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        public void setExecutor(ActionExecutor actionExecutor) {
            this.executor = actionExecutor;
        }

        @Override
        public void run() {
            try {
                timeUnit.sleep(timeout);
                LOGGER.info(String.format(FAILURE_MESSAGE, executor.getDelay().getDescription()));
                executor.kill();
            } catch (InterruptedException e) {
                LOGGER.info(String.format(SUCCESS_MESSAGE, executor.getDelay().getDescription()));
            }
        }
    }

}