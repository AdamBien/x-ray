package com.abien.xray.business.hits.control;

import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author airhacks.com
 */
public class FilterProvider {

    private ScriptEngine nashorn;

    @PostConstruct
    public void initEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.nashorn = manager.getEngineByName("javascript");
    }

    public Predicate<Map.Entry<String, String>> createFromNashornScript(String script) {
        Predicate filter = f -> true;

        if (script == null || script.isEmpty()) {
            return filter;
        }
        try {
            this.nashorn.eval(script);
            Invocable invocable = (Invocable) this.nashorn;
            Predicate predicate = invocable.getInterface(Predicate.class);
            if (predicate != null) {
                filter = predicate;
            }
        } catch (Exception ex) {
            System.out.println("Cannot eval script " + ex.getMessage());
        }

        return filter;
    }

}
