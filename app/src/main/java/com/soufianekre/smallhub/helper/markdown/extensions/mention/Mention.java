package com.soufianekre.smallhub.helper.markdown.extensions.mention;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

public class Mention extends CustomNode implements Delimited {

    private static final String DELIMITER = "@";

    @Override public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override public String getClosingDelimiter() {
        return " ";
    }
}

