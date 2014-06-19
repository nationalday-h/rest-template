package com.rest.hgq.start;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class RestContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        RestResourceModule restResourceModule=new RestResourceModule();
        return Guice.createInjector(restResourceModule);
    }
}
