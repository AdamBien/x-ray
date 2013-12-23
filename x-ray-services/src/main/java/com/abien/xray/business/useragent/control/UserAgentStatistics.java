package com.abien.xray.business.useragent.control;

import com.abien.xray.business.useragent.entity.UserAgent;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ws.rs.core.HttpHeaders;

/**
 * User: blog.adam-bien.com Date: 07.01.11 Time: 13:21
 */
@Stateless
public class UserAgentStatistics {

    public void updateStatistics(String userAgent) {
        UserAgent agent = new UserAgent();
        agent.increaseCounter();
    }

    public List<UserAgent> getMostPopularAgents(int maxResult) {
        return null;
    }

    public void extractAndStoreReferer(Map<String, String> headerMap) {
        String userAgentHeaderName = "xray_" + HttpHeaders.USER_AGENT.toLowerCase();
        String userAgent = headerMap.get(userAgentHeaderName);
        updateStatistics(userAgent);
    }
}
