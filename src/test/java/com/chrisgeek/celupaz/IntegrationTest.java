package com.chrisgeek.celupaz;

import com.chrisgeek.celupaz.config.AsyncSyncConfiguration;
import com.chrisgeek.celupaz.config.DatabaseTestcontainer;
import com.chrisgeek.celupaz.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        CelupazmasterApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.chrisgeek.celupaz.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
