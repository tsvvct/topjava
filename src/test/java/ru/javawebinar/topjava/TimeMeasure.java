package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealServiceTest;

public class TimeMeasure extends ExternalResource {

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    public static StringBuilder stringBuilder;

    @Rule
    public final TestRule watchman = new TestWatcher() {
        private long testStart;
        private StringBuilder localStringBuilder;

        @Override
        protected void succeeded(Description description) {
            localStringBuilder.append(" - succeeded ");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            localStringBuilder.append(" - failed ");
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            localStringBuilder.append(" - skipped ");
        }

        @Override
        protected void starting(Description description) {
            localStringBuilder = new StringBuilder(description.getMethodName());
            testStart = System.nanoTime();
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            long testEnd = System.nanoTime();
            localStringBuilder.append(" - duration: ")
                    .append(formatDuration((testEnd - testStart) / 1000000))
                    .append("\n");
            log.debug(localStringBuilder.toString());
            stringBuilder.append(localStringBuilder.insert(0, "\t"));
        }

        private String formatDuration(Long duration) {
            StringBuilder stringBuilder = (new StringBuilder()).append(duration % 1000).append("ms");
            if ((duration / 1000) % 60 != 0) {
                stringBuilder.insert(0, ((duration / 1000) % 60)).insert(1, "s ");
            }
            if ((duration / 60000) % 60 != 0) {
                stringBuilder.insert(0, (duration / 60000) % 60).insert(1, "m ");
            }
            return stringBuilder.toString();
        }
    };

    @ClassRule
    public static final ExternalResource resource = new TimeMeasure() {

        @Override
        protected void before() throws Throwable {
            stringBuilder = new StringBuilder("\n--=( Test's summary )=--\n");
        }

        @Override
        protected void after() {
            log.debug(stringBuilder.toString());
        }
    };
}
