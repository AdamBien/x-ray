package com.abien.xray.business.configuration.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * User: blog.adam-bien.com
 * Date: 14.02.11
 * Time: 19:06
 */
@Startup
@Singleton
@Path("configuration")
@Produces(TEXT_PLAIN)
public class Configuration {

    private Map<String, String> configuration;
    private Set<String> unconfiguredFields;

    @Inject
    private Instance<ConfigurationProvider> configurationProvider;

    @PostConstruct
    public void fetchConfiguration() {
        this.configuration = new HashMap<String, String>() {{
            put("version", "0.9");
            put("hitsFlushRate", "1");
            put("referersFlushRate", "1");
            put("scavengerRuns", "Mon,Wed,Fri");
            put("debug", "false");
        }};
        this.unconfiguredFields = new HashSet<String>();
        mergeWithCustomConfiguration();
    }

    public boolean doesCustomConfigurationExist() {
        return !configurationProvider.isUnsatisfied();
    }

    void mergeWithCustomConfiguration() {
        for (ConfigurationProvider provider : configurationProvider) {
            Map<String, String> customConfiguration = provider.getConfiguration();
            this.configuration.putAll(customConfiguration);
        }
    }


    @javax.enterprise.inject.Produces
    public String getString(InjectionPoint point) {
        String fieldName = point.getMember().getName();
        String valueForFieldName = configuration.get(fieldName);
        if (valueForFieldName == null) {
            this.unconfiguredFields.add(fieldName);
        }
        return valueForFieldName;
    }

    @javax.enterprise.inject.Produces
    public long getLong(InjectionPoint point) {
        String stringValue = getString(point);
        if (stringValue == null) {
            return 0;
        }
        return Long.parseLong(stringValue);
    }

    @javax.enterprise.inject.Produces
    public int getInteger(InjectionPoint point) {
        String stringValue = getString(point);
        if (stringValue == null) {
            return 0;
        }
        return Integer.parseInt(stringValue);
    }

    @javax.enterprise.inject.Produces
    public boolean getBoolean(InjectionPoint point) {
        String stringValue = getString(point);
        if (stringValue == null) {
            return false;
        }
        return Boolean.parseBoolean(stringValue);
    }


    public Set<String> getUnconfiguredFields() {
        return this.unconfiguredFields;
    }


    @GET
    @Path("{key}")
    public String getEntry(@PathParam("key") String key) {
        return configuration.get(key);
    }

    @GET
    public String getConfiguration() {
        return this.configuration.toString();
    }

    @PUT
    @Path("{key}")
    @Consumes(TEXT_PLAIN)
    public Response addEntry(@PathParam("key") String key, String value, @Context UriInfo uriInfo) {
        Response response = null;
        if (this.configuration.containsKey(key)) {
            response = Response.noContent().build();
        } else {
            URI uri = uriInfo.getAbsolutePathBuilder().build(key);
            response = Response.created(uri).build();
        }
        this.configuration.put(key, value);
        return response;
    }

    @DELETE
    @Path("{key}")
    public Response deleteEntry(@PathParam("key") String key) {
        this.configuration.remove(key);
        return Response.noContent().build();
    }

    public void debugEnabled() {
        this.configuration.put("debug", Boolean.TRUE.toString());
    }

    public void debugDisabled() {
        this.configuration.put("debug", Boolean.FALSE.toString());
    }
}


