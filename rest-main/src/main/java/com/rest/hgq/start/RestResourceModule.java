package com.rest.hgq.start;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.rest.hgq.common.filters.HerenCorsFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 上午11:17
 * To change this template use File | Settings | File Templates.
 */
public class RestResourceModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(GuiceContainer.class);
        bind(JacksonJaxbJsonProvider.class).in(Singleton.class);
        bind(HerenCorsFilter.class).in(Singleton.class);
        filter("/api/*").through(HerenCorsFilter.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put("com.sun.jersey.config.property.packages", "com.rest.hgq.api"); //PROPERTY_PACKAGES
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

        serve("/api/*").with(GuiceContainer.class, params);
        install(new JpaPersistModule("domain"));
//        install(new JpaPersistModule("domain").properties(mySqlProperties()));
        bind(PersistFilter.class).in(Singleton.class);
        filter("/api/*").through(PersistFilter.class);

    }
}
