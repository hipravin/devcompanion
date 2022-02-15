package com.hipravin.devcompanion.init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class is a design attempt to prevent handling requests before app is fully initialized
 */
@Component
public class AwaitApplicationReadyInterceptor implements HandlerInterceptor {

    private static Logger log = LoggerFactory.getLogger(AwaitApplicationReadyInterceptor.class);
    private boolean applicationReady = false;

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
