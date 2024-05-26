package br.com.usasistemas.usaplatform;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;

public class UsaPlatformApplicationInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        StringBuilder configLocation = new StringBuilder();
        configLocation.append("/WEB-INF/servlet-context-config.xml, ");
        configLocation.append("/WEB-INF/servlet-context-dao.xml, ");
        configLocation.append("/WEB-INF/servlet-context-bizo.xml, ");
        configLocation.append("/WEB-INF/servlet-context-controller.xml, ");
        configLocation.append("/WEB-INF/servlet-context-api.xml, ");
        configLocation.append("/WEB-INF/servlet-context-job.xml, ");

        XmlWebApplicationContext servletWebAppContext = new XmlWebApplicationContext();
        servletWebAppContext.setConfigLocation(configLocation.toString());

        ObjectifyRegistry.initialize();

        return servletWebAppContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected String getServletName() {
        return "context";
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {

        XmlWebApplicationContext rootContext = new XmlWebApplicationContext();
        rootContext.setConfigLocations("/WEB-INF/root-context.xml");
        return rootContext;
    }
    
}