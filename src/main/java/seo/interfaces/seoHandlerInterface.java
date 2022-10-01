package seo.interfaces;

import org.jsoup.nodes.Document;

public interface seoHandlerInterface extends validators {

    /**
     * Converts HTML to JSoup Document for easy navigation
     * @param htmlString string in HTML
     * @return Jsoup Document
     */
    Document getHTMLDocument(String htmlString);

    /**
     * Validates HTML Syntax for loaded webpage
     * @return true if html is validated
     */
    Boolean htmlSyntaxCheck();
}
