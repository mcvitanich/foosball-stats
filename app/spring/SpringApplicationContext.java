package spring;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static play.Logger.error;
import static spring.RuntimeEnvironment.currentRuntimeEnvironment;

public class SpringApplicationContext {
    private static AnnotationConfigApplicationContext ctx;

    private SpringApplicationContext() {
    }

    public static void initialize() {
        try {
            ctx = new AnnotationConfigApplicationContext();
            ctx.getEnvironment().setActiveProfiles(currentRuntimeEnvironment().name());
            ctx.scan("spring", "spring.config", "controllers", "services", "utils", "jobs");
            ctx.refresh();
        } catch (Exception e) {
            String msg = "Application Context could not be initialized properly";
            error(msg + ": " + ExceptionUtils.getStackTrace(e));
            throw new IllegalStateException(msg, e);
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        return ctx.getBean(beanClass);
    }

    public static <T> T getBeanNamed(String beanName, Class<T> beanClass) {
        return ctx.getBean(beanName, beanClass);
    }

    public static void close() {
        if (ctx != null) {
            ctx.close();
            ctx = null;
        }
    }
}