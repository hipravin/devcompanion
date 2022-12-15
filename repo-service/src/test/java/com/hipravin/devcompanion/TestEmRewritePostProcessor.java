package com.hipravin.devcompanion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class TestEmRewritePostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(TestEmRewritePostProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EntityManager em) {
            log.info("Tweaking entity manager in test: {}", beanName);

            return TweakEmProxy.newInstance(em);
        } else {
            return bean;
        }
    }

    public static class TweakEmProxy implements InvocationHandler {

        private EntityManager obj;

        public static Object newInstance(EntityManager obj) {
            return java.lang.reflect.Proxy.newProxyInstance(
                    obj.getClass().getClassLoader(),
                    obj.getClass().getInterfaces(),
                    new TweakEmProxy(obj));
        }

        private TweakEmProxy(EntityManager obj) {
            this.obj = obj;
        }

        public Object invoke(Object proxy, Method m, Object[] args)
                throws Throwable {
            Object result;

            if (m.getName().equals("createNativeQuery")) {
                if(args.length > 0 && args[0] instanceof String args0) {
                    log.debug("Query before: {}", args[0]);
                    args[0] = replaceIlike(args0);
                    log.debug("Query  after: {}", args[0]);
                }
            }
            result = m.invoke(obj, args);

            return result;
        }

        static String replaceIlike(String originalQuery) {
            return originalQuery.replaceAll("(?i)\silike\s", " like ");
        }
    }


}
