package test.automation.hooks;

import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.utils.delays.PollingWait;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CucumberHooks {
    private static final Logger LOGGER = LogManager.getLogger(CucumberHooks.class.getSimpleName());
    private static final String TIME_WASTED_SO_FAR =
            "Fun fact : so far the test run wasted %d seconds waiting for things to happen";
    private static final String TIME_WASTED_FOR_GIVEN_DELAY_TYPE =
            "\t%s : %d seconds wasted (%d seconds average by delay, %d seconds maximum delay)";

    private static String scenarioName;
    private TestContext context;
    SeleniumClient seleniumClient;


    public CucumberHooks(TestContext context, SeleniumClient seleniumClient) {
        this.context = context;
        this.seleniumClient = seleniumClient;
    }

    public void setStartTimestamp(long timestamp) {
    }

    @Before
    public void setup(Scenario scenario) {
        context.setScenario(scenario);

    }

    @Before
    public void displayName(Scenario scenario) {
        this.scenarioName = scenario.getName();
        LOGGER.warn("-------------------------------------------------------------------------------------------------");
        LOGGER.warn(String.format("Running - %s - %s", scenarioName, scenario.getId()));
    }

    @After
    public void displayStatus(Scenario scenario) {
        LOGGER.warn(String.format("Finished execution : - %s - %s", scenarioName, scenario.getId()));
        LOGGER.warn("-------------------------------------------------------------------------------------------------");

        if (scenario.isFailed()) {
            LOGGER.error(String.format("SCENARIO FAILED ! : - %s - %s", scenarioName, scenario.getId()));
            LOGGER.info("-------------------------------------------------------------------------------------------------");
        }
        LOGGER.info(String.format(TIME_WASTED_SO_FAR, getTimeWastedSoFar()));
        for (Map.Entry<String, List<Duration>> entry : PollingWait.timeWastedByCategories.entrySet()) {
            List<Duration> durationsForCategory = entry.getValue();
            Duration totalDurationForCategory = Duration.ZERO;
            Duration maximumDelay = Duration.ZERO;
            for (Duration duration : durationsForCategory) {
                totalDurationForCategory = totalDurationForCategory.plus(duration);
                if (duration.compareTo(maximumDelay) > 0) {
                    maximumDelay = duration;
                }
            }
            LOGGER.info(String.format(TIME_WASTED_FOR_GIVEN_DELAY_TYPE,
                    entry.getKey(),
                    totalDurationForCategory.getSeconds(),
                    totalDurationForCategory.dividedBy(durationsForCategory.size()).getSeconds(),
                    maximumDelay.getSeconds()));
        }
    }

    private long getTimeWastedSoFar() {
        return PollingWait.getTotalTimeWasted().getSeconds();
    }
}
