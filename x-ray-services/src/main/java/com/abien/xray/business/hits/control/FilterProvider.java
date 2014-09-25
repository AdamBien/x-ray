package com.abien.xray.business.hits.control;

import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
        try {
            this.nashorn.eval(script);
        } catch (ScriptException ex) {
            throw new IllegalStateException("Cannot eval script", ex);
        }
        Invocable invocable = (Invocable) this.nashorn;
        Predicate filter = invocable.getInterface(Predicate.class);
        return filter;
    }

}
