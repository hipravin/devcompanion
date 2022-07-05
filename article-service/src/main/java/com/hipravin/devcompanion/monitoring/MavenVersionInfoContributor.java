package com.hipravin.devcompanion.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

/**
 * @deprecated there is a goal 'build-info' in spring-boot-maven plugin that supports all of this.
 */
@Component
@Deprecated
public class MavenVersionInfoContributor implements InfoContributor, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(MavenVersionInfoContributor.class);
    @Value("classpath:version.properties")
    Resource versionPropertiesResource;

    final Map<String, String> versionInfoMap = new LinkedHashMap<>();

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("versionInfo", versionInfoMap);
    }

    @Override
    public void afterPropertiesSet() {
        this.versionInfoMap.putAll(
                propertiesToMap(loadVersionInfoProperties()));
    }

    private Properties loadVersionInfoProperties() {
        try {
            final Properties versionProperties = new Properties();
            versionProperties.load(versionPropertiesResource.getInputStream());
            log.info("Version properties loaded:");
            versionProperties.forEach((k, v) -> {
                log.info("{}: {}", k, v);
            });

            return versionProperties;
        } catch (IOException e) {
            log.error("Failed to load version info properties: " + e.getMessage(), e);
            throw new UncheckedIOException(e);
        }
    }

    private static Map<String, String> propertiesToMap(Properties properties) {

        Map<String, String> result = new LinkedHashMap<>();

        Enumeration<?> propertyNamesEnumeration = properties.propertyNames();

        while (propertyNamesEnumeration.hasMoreElements()) {
            String propertyName = String.valueOf(propertyNamesEnumeration.nextElement());
            result.put(propertyName, properties.getProperty(propertyName));
        }

        return result;

//        //alternative approach which uses Properties as a Map<Object, Object>
//
//        versionProperties.forEach((ko, vo)
//                -> versionDetails.put(String.valueOf(ko), String.valueOf(vo)));
    }

    public void setVersionPropertiesResource(Resource versionPropertiesResource) {
        this.versionPropertiesResource = versionPropertiesResource;
    }
}



