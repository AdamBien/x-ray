package com.abien.xray.business.useragent.control;

import com.abien.xray.business.useragent.entity.UserAgent;
import java.util.Iterator;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: blog.adam-bien.com Date: 07.01.11 Time: 13:59
 */
@Ignore
public class UserAgentStatisticsIT {

    UserAgentStatistics cut;

    @Before
    public void initializeCUT() {
        this.cut = new UserAgentStatistics();
    }

    @Test
    public void getMostPopularAgentsWithEmtpyDB() {
        List<UserAgent> mostPopularAgents = this.cut.getMostPopularAgents(2);
        assertTrue(mostPopularAgents.isEmpty());
    }

    @Test
    public void getMostPopularAgentsSorting() {
        UserAgent first = new UserAgent("first");
        UserAgent second = new UserAgent("second");
        first.increaseCounter();
        List<UserAgent> mostPopularAgents = this.cut.getMostPopularAgents(2);
        assertFalse(mostPopularAgents.isEmpty());
        Iterator<UserAgent> userAgentIterator = mostPopularAgents.iterator();
        UserAgent actual = userAgentIterator.next();
        assertThat(actual, is(first));
        actual = userAgentIterator.next();
        assertThat(actual, is(second));
    }
}
