package com.exatask.platform.consumer;

import com.exatask.platform.consumer.constants.CommandLine;
import com.exatask.platform.consumer.consumers.AppConsumer;
import com.exatask.platform.consumer.entities.Consumer;
import com.exatask.platform.consumer.utilities.CommandLineUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConsumerRunner implements CommandLineRunner {

    private static final AppLogger LOGGER = AppLogManager.getLogger();

    @Override
    public void run(String... args) throws Exception {

        Map<String, List<String>> commandLineArgs = CommandLineUtility.parseArguments(args);

        if (commandLineArgs.containsKey(CommandLine.ALL_CONSUMERS)) {
            executeAllConsumers(commandLineArgs.get(CommandLine.ALL_CONSUMERS).get(0).trim());

        } else if (commandLineArgs.containsKey(CommandLine.CONSUMER)) {

            List<String> allConsumers = commandLineArgs.get(CommandLine.CONSUMER);
            for (String consumer : allConsumers) {
                executeConsumer(consumer);
            }
        }
    }

    private void executeAllConsumers(String value) {

        if (!value.equalsIgnoreCase("true") && !value.equals("1")) {
            LOGGER.warn("all-consumers argument mentioned with invalid value", Collections.singletonMap("value", value));
            return;
        }

        String[] consumerBeanNames = ApplicationContextUtility.getBeanNames(AppConsumer.class);
        for (String consumer : consumerBeanNames) {

            Object consumerBean = ApplicationContextUtility.getBean(consumer);
            Method[] beanMethods = consumerBean.getClass().getMethods();
            for (Method beanMethod : beanMethods) {

                try {
                    executeConsumer(consumerBean, beanMethod);
                } catch (InvocationTargetException | IllegalAccessException exception) {
                    LOGGER.error(exception);
                }
            }
        }
    }

    private void executeConsumer(String consumer) {

        Consumer consumerInfo = CommandLineUtility.parseConsumer(consumer);
        if (ObjectUtils.isEmpty(consumerInfo)) {
            LOGGER.warn("Provided consumer-action information is invalid", Collections.singletonMap("consumer", consumer));
            return;
        }

        Object consumerBean = ApplicationContextUtility.getBean(consumerInfo.getConsumer());
        Method beanMethod;

        try {
            beanMethod = consumerBean.getClass().getMethod(consumerInfo.getAction());
            executeConsumer(consumerBean, beanMethod);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            LOGGER.error(exception);
        }
    }

    private void executeConsumer(Object bean, Method beanMethod) throws InvocationTargetException, IllegalAccessException {

        if (ObjectUtils.isEmpty(bean) || ObjectUtils.isEmpty(beanMethod)) {
            LOGGER.error("Invalid/Empty Bean and method provided for invocation");
            return;

        } else if (!(bean instanceof AppConsumer)) {
            LOGGER.error("Consumer bean is not an instance of AppConsumer base class", Collections.singletonMap("consumer", bean.getClass()));
            return;
        }

        if (!beanMethod.isAccessible()) {

            Map<String, Object> extraParams = new HashMap<>();
            extraParams.put("consumer", bean.getClass());
            extraParams.put("action", beanMethod.getName());
            LOGGER.error("Action is not accessible on provided consumer bean", extraParams);
            return;
        }

        beanMethod.invoke(bean);
    }
}
