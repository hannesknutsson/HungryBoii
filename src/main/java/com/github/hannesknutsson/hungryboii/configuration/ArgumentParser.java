package com.github.hannesknutsson.hungryboii.configuration;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgumentParser {

    @Option(name = "-t", aliases = {"--token"}, required = true,
            usage = "Specify discord API token")
    private static String discordApiToken = null;

    public static boolean parseArguments(String... args) {
        CmdLineParser parser = new CmdLineParser(new ArgumentParser());
        boolean errorFree = true;

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Bad input argument(s) when starting bot: " + e.getMessage() + ".\nUsage:\n");
            parser.printUsage(System.err);
            errorFree = false;
        }
        return errorFree;
    }

    private ArgumentParser() {
    }

    public static String getDiscordApiToken() {
        return discordApiToken;
    }
}