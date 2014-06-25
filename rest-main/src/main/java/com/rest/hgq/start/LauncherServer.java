package com.rest.hgq.start;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
public class LauncherServer {

    private static final Logger logger = LoggerFactory.getLogger(LauncherServer.class);

    public static void main(String[] args) {

        Injector injector= Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                binder().requireExplicitBindings();
                install(new RestResourceModule());
                bind(GuiceFilter.class);
            }
        });



        Tomcat tomcat= new Tomcat();
        tomcat.enableNaming();

        tomcat.setPort(8089);
        String webappDirLocation = "rest-main/src/main/webapp/";
//        String webappDirLocation = System.getProperty("user.dir");
        Context context = null;

        logger.info("user.dir=" +System.getProperty("user.dir"));
        try {
            context = tomcat.addWebapp("/rest", new File(webappDirLocation).getAbsolutePath());
        } catch (ServletException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        context.addFilterDef(createFilterDef("guiceFilter", injector.getInstance(GuiceFilter.class)));
        context.addFilterMap(createFilterMap("guiceFilter", "/*"));

        try {
            tomcat.init();

            tomcat.start();
        } catch (LifecycleException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        tomcat.getServer().await();
    }

    private static FilterDef createFilterDef(String filterName, Filter filter) {
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(filterName);
        filterDef.setFilter(filter);
        return filterDef;
    }

    private static FilterMap createFilterMap(String filterName, String urlPattern) {
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(filterName);
        filterMap.addURLPattern(urlPattern);
        return filterMap;
    }

}
