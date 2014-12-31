/*
 */
package com.abien.xray.business.monitoring.boundary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class HealthMonitorTest {

    HealthMonitor cut;

    @Before
    public void inject() {
        this.cut = new HealthMonitor();
        this.cut.methods = new Cache<String, String>() {
            Map<String, String> backing = new HashMap<>();

            @Override
            public String get(String key) {
                return this.backing.get(key);
            }

            @Override
            public Map<String, String> getAll(Set<? extends String> keys) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean containsKey(String key) {
                return this.backing.containsKey(key);
            }

            @Override
            public void loadAll(Set<? extends String> keys, boolean replaceExistingValues, CompletionListener completionListener) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void put(String key, String value) {
                this.backing.put(key, value);
            }

            @Override
            public String getAndPut(String key, String value) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void putAll(Map<? extends String, ? extends String> map) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean putIfAbsent(String key, String value) {
                this.backing.putIfAbsent(key, value);
                return true;
            }

            @Override
            public boolean remove(String key) {
                this.backing.remove(key);
                return true;
            }

            @Override
            public boolean remove(String key, String oldValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAndRemove(String key) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean replace(String key, String oldValue, String newValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean replace(String key, String value) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAndReplace(String key, String value) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void removeAll(Set<? extends String> keys) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void removeAll() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void clear() {
                this.backing.clear();
            }

            @Override
            public <C extends Configuration<String, String>> C getConfiguration(Class<C> clazz) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> T invoke(String key, EntryProcessor<String, String, T> entryProcessor, Object... arguments) throws EntryProcessorException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> Map<String, EntryProcessorResult<T>> invokeAll(Set<? extends String> keys, EntryProcessor<String, String, T> entryProcessor, Object... arguments) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getName() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public CacheManager getCacheManager() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void close() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isClosed() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> T unwrap(Class<T> clazz) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void registerCacheEntryListener(CacheEntryListenerConfiguration<String, String> cacheEntryListenerConfiguration) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<String, String> cacheEntryListenerConfiguration) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Iterator<Cache.Entry<String, String>> iterator() {
                List<Cache.Entry<String, String>> collect = this.backing.entrySet().stream().map(e -> new Cache.Entry<String, String>() {

                    @Override
                    public String getKey() {
                        return e.getKey();
                    }

                    @Override
                    public String getValue() {
                        return e.getValue();
                    }

                    @Override
                    public <T> T unwrap(Class<T> clazz) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                }).collect(Collectors.toList());
                return collect.iterator();
            }
        };
    }

    @Test
    public void getSlowestMethodsWithEmptyMap() {
        List<String> slowestMethods = this.cut.getSlowestMethods();
        assertTrue(slowestMethods.isEmpty());
    }

    @Test
    public void getSlowestMethods() {

        this.cut.methods.put("slower", "2");
        this.cut.methods.put("very slow", "42");
        this.cut.methods.put("slow", "1");

        List<String> slowestMethods = this.cut.getSlowestMethods();
        assertThat(slowestMethods.size(), is(3));
        Iterator<String> iterator = slowestMethods.iterator();
        assertTrue(iterator.next().startsWith("very slow"));
        assertTrue(iterator.next().startsWith("slower"));
        assertTrue(iterator.next().startsWith("slow"));
    }

}
