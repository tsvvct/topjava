package ru.javawebinar.topjava;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealServiceTest;

import java.util.concurrent.TimeUnit;

public class TimeMeasure {

    public static StringBuilder stringBuilder;

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            stringBuilder.append('\t')
                    .append(description.getMethodName())
                    .append(" duration: ")
                    .append(formatDuration(nanos))
                    .append('\n');
        }

        public String formatDuration(long nanos) {
            StringBuilder result = new StringBuilder();
            long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos);
            if (minutes != 0) {
                result.append(minutes).append("m ");
            }
            long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos) - TimeUnit.MINUTES.toSeconds(minutes);
            if (seconds != 0 || minutes != 0) {
                result.append(seconds).append("s ");
            }
            long millis = TimeUnit.NANOSECONDS.toMillis(nanos) -
                    (TimeUnit.SECONDS.toMillis(seconds) + TimeUnit.MINUTES.toMillis(minutes));

            result.append(millis).append("ms");

            return result.toString();
        }
    };

    @ClassRule
    public static final ExternalResource externalResource = new ExternalResource() {
        private final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

        @Override
        protected void before() {
            stringBuilder = new StringBuilder("\n--=( Test's summary )=--\n");
        }

        @Override
        protected void after() {
            log.debug(stringBuilder.toString());
        }
    };
}
