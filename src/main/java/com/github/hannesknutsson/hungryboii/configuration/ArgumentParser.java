package com.github.hannesknutsson.hungryboii.configuration;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentParser {

    private static Logger LOG = LoggerFactory.getLogger(ArgumentParser.class);

    @Option(name = "-t", aliases = {"--token"},
            usage = "Specify discord API token")
    private static String discordApiToken = null;

    @Option(name = "-c", aliases = {"--config"},
            usage = "Specify alternative configuration location")
    private static String configurationPath ="conf/HungryBoii.conf";

    public static boolean parseArguments(String... args) {
        CmdLineParser parser = new CmdLineParser(new ArgumentParser());
        boolean errorFree = true;

        try {
            parser.parseArgument(args);
            LOG.debug("Successfully parsed input argument(s)!");
        } catch (CmdLineException e) {
            LOG.error("Failed parsing input argument(s) when starting bot!");
            System.err.println("\n\n");
            parser.printUsage(System.err);
            System.err.println("\n\n");
            errorFree = false;
        }

        return errorFree;
    }

    private ArgumentParser() {
    }

    static String getDiscordApiToken() {
        return discordApiToken;
    }

    static String getConfigurationPath() {
        return configurationPath;
    }
}