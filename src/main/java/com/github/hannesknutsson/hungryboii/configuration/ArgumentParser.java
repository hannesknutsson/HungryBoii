package com.github.hannesknutsson.hungryboii.configuration;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentParser {

    private static Logger LOG = LoggerFactory.getLogger(ArgumentParser.class);

    @Option(name = "-t", aliases = {"--token"}, required = true,
            usage = "Specify discord API token")
    private static String discordApiToken = null;

    public static boolean parseArguments(String... args) {
        CmdLineParser parser = new CmdLineParser(new ArgumentParser());
        boolean errorFree = true;

        try {
            parser.parseArgument(args);
            LOG.info("Successfully parsed input argument(s)!");
        } catch (CmdLineException e) {
            LOG.info("Failed parsing input argument(s) when starting bot!");
            System.err.println("\n\n");
            parser.printUsage(System.err);
            System.err.println("\n\n");
            errorFree = false;
        }

        LOG.info("Using token: *{}*", discordApiToken);

        return errorFree;
    }

    private ArgumentParser() {
    }

    public static String getDiscordApiToken() {
        return discordApiToken;
    }
}