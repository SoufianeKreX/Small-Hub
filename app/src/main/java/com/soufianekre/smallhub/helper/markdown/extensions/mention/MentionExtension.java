package com.soufianekre.smallhub.helper.markdown.extensions.mention;


import com.soufianekre.smallhub.helper.markdown.extensions.mention.internal.MentionDelimiterProcessor;
import com.soufianekre.smallhub.helper.markdown.extensions.mention.internal.MentionNodeRenderer;

import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MentionExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {
    private MentionExtension() {}

    public static Extension create() {
        return new MentionExtension();
    }

    @Override public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new MentionDelimiterProcessor());
    }

    @Override public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(MentionNodeRenderer::new);
    }
}
