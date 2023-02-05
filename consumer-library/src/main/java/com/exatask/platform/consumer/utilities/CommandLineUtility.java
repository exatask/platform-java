package com.exatask.platform.consumer.utilities;

import com.exatask.platform.consumer.entities.Consumer;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CommandLineUtility {

    private static final AppLogger LOGGER = AppLogManager.getLogger();

    private static final String COMMAND_LINE_ARG_PREFIX = "--";
    private static final String COMMAND_LINE_ARG_SEPARATOR = "=";

    private static final String COMMAND_LINE_CONSUMER_ACTION_SEPARATOR = "@";

    public static Map<String, List<String>> parseArguments(String... arguments) {

        Map<String, List<String>> parsedArguments = new HashMap<>();
        for (String argument : arguments) {

            LOGGER.info("Command-line argument received", Collections.singletonMap("argument", argument));
            if (!argument.startsWith(COMMAND_LINE_ARG_PREFIX)) {
                continue;
            }

            String[] argumentParts = argument.replaceFirst(COMMAND_LINE_ARG_PREFIX, "")
                    .split(COMMAND_LINE_ARG_SEPARATOR, 2);
            if (argumentParts.length != 2) {
                LOGGER.warn("Empty value provided for command-line argument", Collections.singletonMap("key", argument));
                continue;
            }

            if (!parsedArguments.containsKey(argumentParts[0])) {
                parsedArguments.put(argumentParts[0], new ArrayList<>());
            }

            List<String> argumentValues = parsedArguments.get(argumentParts[0]);
            argumentValues.add(argumentParts[1]);
            parsedArguments.put(argumentParts[0], argumentValues);
        }

        return parsedArguments;
    }

    public static Consumer parseConsumer(String consumer) {

        String[] consumerParts = consumer.split(COMMAND_LINE_CONSUMER_ACTION_SEPARATOR, 2);
        if (consumerParts.length != 2) {
            LOGGER.warn("Empty consumer or action provided", Collections.singletonMap("consumer", consumer));
            return null;
        }

        return new Consumer(consumerParts[0], consumerParts[1]);
    }
}
