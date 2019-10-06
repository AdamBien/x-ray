package com.abien.xray.business.hits.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author airhacks.com
 */
@Dependent
public class FilterProvider {

    private ScriptEngine nashorn;

    @Inject
    XRayLogger LOG;

    @PostConstruct
    public void initEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.nashorn = manager.getEngineByName("javascript");
    }

    public Predicate<Map.Entry<String, Long>> createFromNashornScript(String script) {
        Predicate filter = f -> true;

        if (script == null || script.isEmpty()) {
            return filter;
        }
        try {
            this.nashorn.eval(script);
            LOG.log(Level.FINE, "Script {0} evaluated", new Object[]{script});
            Invocable invocable = (Invocable) this.nashorn;
            Predicate predicate = invocable.getInterface(Predicate.class);
            if (predicate != null) {
                LOG.log(Level.FINE, "Predicate successfully created from script");
                filter = predicate;
            } else {
                LOG.log(Level.FINE, "Could not create predicate, using default filter");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Filter cannot be executed: {0}", new Object[]{ex});
        }

        return filter;
    }

}
