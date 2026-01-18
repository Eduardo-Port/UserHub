package io.github.Eduardo_Port.userhubapi.utils;

import org.owasp.html.HtmlPolicyBuilder;
import org.jsoup.parser.Parser;

public final class XssSanitizerUtils {

    private XssSanitizerUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    public static String stripAllTags(String input) {
        if (input == null) return null;

        String safeHtml = new HtmlPolicyBuilder().toFactory().sanitize(input);
        return Parser.unescapeEntities(safeHtml, true);
    }
}
