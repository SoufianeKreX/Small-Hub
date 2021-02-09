package com.soufianekre.smallhub.helper.markdown.extensions.emoji_;


import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

public class Emoji extends CustomNode implements Delimited {

    private static final String DELIMITER = ":";

    @Override public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override public String getClosingDelimiter() {
        return DELIMITER;
    }
}
