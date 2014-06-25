package com.rest.hgq.start;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.rest.hgq.common.core.SystemProperty;
import com.rest.hgq.common.filters.HerenCorsFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

        installJpaModule();

        bind(PersistFilter.class).in(Singleton.class);
        filter("/api/*").through(PersistFilter.class);

    }

    private void installJpaModule() {
        //连接配置（主要是url）优先查找环境变量heren-home下配置文件rest.properties
        // 然后是包中配置文件 如没有则用persistence.xml作为jpa domain配置
        //install(new JpaPersistModule("domain").properties(mySqlProperties()));
        Properties p = new Properties();
        Map<String, String> map = SystemProperty.getHerenProperty();
        if (!map.isEmpty()) {
            if (map.containsKey("hibernate.connection.driver_class")) {
                p.put("hibernate.connection.driver_class", map.get("hibernate.connection.driver_class"));
            }
            if (map.containsKey("hibernate.connection.url")) {
                p.put("hibernate.connection.url", map.get("hibernate.connection.url"));
            }
            if (map.containsKey("hibernate.connection.username")) {
                p.put("hibernate.connection.username", map.get("hibernate.connection.username"));
            }
            if (map.containsKey("hibernate.connection.password")) {
                p.put("hibernate.connection.password", map.get("hibernate.connection.password"));
            }
            if (map.containsKey("hibernate.dialect")) {
                p.put("hibernate.dialect", map.get("hibernate.dialect"));
            }
            if (map.containsKey("hibernate.hbm2ddl.auto")) {
                p.put("hibernate.hbm2ddl.auto", map.get("hibernate.hbm2ddl.auto"));
            }
//            if (map.containsKey("url")) {
//                p.put("javax.persistence.jdbc.url", map.get("url"));
//            }
//            if (map.containsKey("hibernate.connection.username")) {
//                p.put("javax.persistence.jdbc.user", map.get("user"));
//            }
//            if (map.containsKey("password")) {
//                p.put("javax.persistence.jdbc.password", map.get("password"));
//            }
        }
        if (p.isEmpty()){
            install(new JpaPersistModule("domain"));
        } else {
            install(new JpaPersistModule("domain").properties(p));
        }
    }
}
