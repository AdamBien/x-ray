package com.abien.xray.business.hits.control;

import java.util.Map;
import java.util.function.Predicate;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class FilterProviderTest {

    private FilterProvider cut;

    @Before
    public void init() {
        this.cut = new FilterProvider();
        this.cut.initEngine();
    }

    @Test
    public void createPredicate() {
        Predicate<Map.Entry<String, String>> predicate = this.cut.createFromNashornScript("function test(i){return true;}");
        boolean result = predicate.test(null);
        assertTrue(result);
    }

}
