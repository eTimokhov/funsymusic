package com.etimokhov.funsymusic.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Setup logic on application startup runs here
 */
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger LOG = LoggerFactory.getLogger(MediaStorageInitializer.class);

    private final MediaStorageInitializer mediaStorageInitializer;

    public StartupApplicationListener(MediaStorageInitializer mediaStorageInitializer) {
        this.mediaStorageInitializer = mediaStorageInitializer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOG.info("Running custom application startup logic.");
        mediaStorageInitializer.initMediaFilesStorageDirectory();
    }


}
