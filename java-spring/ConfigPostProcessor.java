package com.natera.myome.processing.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;

/**
    There are two ways to configure this into place:
    1. New way as of Spring 2.7;
        - the name of this class should appear in
            src/main/resources/META-INF/spring/org.springframework.boot.env.EnvironmentPostProcessor.imports
        - i.e. that file will show:
            com.natera.myome.processing.config.ConfigPostProcessor

    2. The old way with earlier spring:
        - Different file:
            src/main/resources/META-INF/spring.factories
        - Shows like this:
            org.springframework.boot.env.EnvironmentPostProcessor=com.natera.myome.processing.config.ConfigPostProcessor


    Note also that this runs BEFORE logging setup so logging is lost. The way around that is either
    System.out.println() or this DeferredLog trick we are using.
*/
public class ConfigPostProcessor implements EnvironmentPostProcessor, ApplicationListener<ApplicationPreparedEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DeferredLog dlog = new DeferredLog();
    private final String lookingFor = "sendout.processing.phr.scheduling.enabled";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        application.addListeners(this);
        final var changed = new HashMap<String, Object>();
        dlog.info("Checking configs...");

        // Loop through existing property sources to find and transform specific keys
        for (PropertySource<?> source : environment.getPropertySources()) {
            dlog.info("Got source: " + source);
            if (source.containsProperty(lookingFor)) {
                Object originalValue = source.getProperty("sendout.processing.phr.scheduling.enabled");
                dlog.info("!!!!!!! Got value: " + originalValue);
                changed.put(lookingFor, true);
            }
        }

        // Inject the changed values back with the highest priority
        if (!changed.isEmpty()) {
            environment.getPropertySources().addFirst(
                new MapPropertySource("transformedConfigProps", changed)
            );
        }
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        // Flush log:
        logger.info("Flushing logs from work done earlier during bootup...");
        dlog.replayTo(getClass());
    }
}
