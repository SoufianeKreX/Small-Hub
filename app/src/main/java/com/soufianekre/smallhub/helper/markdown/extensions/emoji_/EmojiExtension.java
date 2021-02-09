package com.soufianekre.smallhub.helper.markdown.extensions.emoji_;

import com.soufianekre.smallhub.helper.markdown.extensions.emoji_.internal.EmojiDelimiterProcessor;
import com.soufianekre.smallhub.helper.markdown.extensions.emoji_.internal.EmojiNodeRenderer;

import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;



public class EmojiExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private EmojiExtension() {}

    public static Extension create() {
        return new EmojiExtension();
    }

    @Override public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new EmojiDelimiterProcessor());
    }

    @Override public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(EmojiNodeRenderer::new);
    }
}
