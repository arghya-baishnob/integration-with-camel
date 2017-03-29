package com.ab;

import com.ab.config.CustomConfiguration;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.javaconfig.Main;
import org.apache.log4j.Logger;

/**
 * A Camel Application
 */
public class CamelApplication extends CamelConfiguration {

    private static final Logger logger = Logger.getLogger(CamelApplication.class);

    private Main main;

    public void boot()  {
        // create a Main instance
        main = new Main();
        // enable hangup support so you can press ctrl + c to terminate the JVM
        main.enableHangupSupport();
        // set the Configuration file
        main.setConfigClassesString(CustomConfiguration.class.getName());
        // run until you terminate the JVM
        logger.info("Starting Camel. Use ctrl + c to terminate the JVM.");
        try {
            main.run();
        } catch (final Exception ex) {
            logger.fatal("Camel startup failed", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        CamelApplication camApp = new CamelApplication();
        camApp.boot();
    }
}

