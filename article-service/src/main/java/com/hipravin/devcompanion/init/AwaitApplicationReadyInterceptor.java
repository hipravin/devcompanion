package com.hipravin.devcompanion.init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class is a design attempt to prevent handling requests before app is fully initialized.
 *
 * This entire interceptor may be redundant if application is deployed in kubernetes and proper readiness probe is used,
 * because according to logs and quick test application is not ready until all ApplicationRunner's are finished.
 */
@Component
public class AwaitApplicationReadyInterceptor implements HandlerInterceptor {

    private static Logger log = LoggerFactory.getLogger(AwaitApplicationReadyInterceptor.class);
    private boolean applicationReady = false;

    /**
     * just for logging purposes to monitor when application become fully operational according to probes
     */
    @EventListener(AvailabilityChangeEvent.class)
    public void onApplicationReady(AvailabilityChangeEvent<?> availabilityChangeEvent) {
        log.info(availabilityChangeEvent.toString());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Application ready, starting handling requests");

        applicationReady = true;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {

        if(!applicationReady) {
            log.info("Application not ready for request '{}'", request.getServletPath());
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getOutputStream().print(
                    keyValueJsonAsString("error", "Application is not ready yet..."));
        }

        return applicationReady;
    }

    static String keyValueJsonAsString(String key, String value) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(Map.of(key, value));
    }
}
