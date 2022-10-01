package seo;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import seo.interfaces.seoHandlerInterface;


public class SeoHandler extends WebSettings implements seoHandlerInterface {
    /** Stores automation report object */
    private final AutomationReport report;
    /** Stores HTML DOM */
    private final Document htmlDOM;

    /**
     * Initializes elements with default values
     * @param driver web driver object
     * @param report automation report object
     */
    SeoHandler(WebDriver driver, AutomationReport report){
        this.report = report;
        this.htmlDOM = getHTMLDocument(driver.getPageSource());
    }

    /**
     * Converts HTML to JSoup Document for easy navigation
     *
     * @param htmlString string in HTML
     * @return Jsoup Document
     */
    @Override
    public Document getHTMLDocument(String htmlString) {
        return Jsoup.parse(htmlString, serverName, Parser.xmlParser());
    }

    /**
     * Validates HTML Syntax for loaded webpage
     *
     * @return true if html is validated
     */
    @Override
    public Boolean htmlSyntaxCheck() {
        return Jsoup.isValid(htmlDOM.toString(), Whitelist.basic());
    }

    /**
     * Validates string inside title tag in DOM
     *
     * @param title expected title
     * @return true if title matches with expected string
     */
    @Override
    public Boolean titleCheck(String title) {
        report.log("Title validation in process", true);
        title = title.split("-")[0].trim();
        String websiteTitle = htmlDOM.title();
        report.log("Expected Title: " + title, true);
        report.log("Obtained Title: "+ websiteTitle, true);

        if(websiteTitle.contains(title)){
            return true;
        } else {
            report.log("Title validation failed, missing field " + StringUtils.difference(websiteTitle,title), false);
            return false;
        }
    }

    /**
     * Validates meta data in DOM
     *
     * @param metaData expected meta
     * @return true if meta matches with expected string
     */
    @Override
    public Boolean metaDataCheck(String metaData) {
        report.log("Meta validation in process", true);
        metaData = metaData.split("-")[0].trim();
        String metaDataContent = htmlDOM.select("meta[name=description]").get(0).attr("content");
        report.log("Expected MetaData Content: " + metaData,true );
        report.log("Obtained MetaData Content: " + metaDataContent, true);

        if(metaDataContent.contains(metaData)) {
            return true;
        } else {
            report.log("Meta validation failed, missing field" + StringUtils.difference(metaDataContent, metaData), false);
            return false;
        }
    }

    /**
     * Validates canonical link inside DOM
     *
     * @param canonicalLink expected link text
     * @return true if expected canonical link is present
     */
    @Override
    public Boolean canonicalHrefCheck(String canonicalLink) {
        report.log("Canonical Href validation in process", true);
        String href = htmlDOM.select("link[rel=canonical]").attr("href");
        report.log("Expected Canonical Link Check: " + canonicalLink, true);
        report.log("Canonical Link Check: "  +href, true);
        if(canonicalLink.equals(href)) {
            return true;
        } else {
            report.log("Title validation failed, missing field" + StringUtils.difference(href,canonicalLink), false);
            return false;
        }
    }

    /**
     * Validates canonical parent tag inside DOM
     * @return true if expected canonical tag is present in DOM
     */
    @Override
    public Boolean canonicalParentCheck() {
        report.log("Canonical parent validation in process", true);
        boolean canonicalParentCheck = htmlDOM.head()
                .select("meta[name=description]").get(0)
                .hasAttr("content");
        report.log("Canonical parent check validation passed: " + canonicalParentCheck, canonicalParentCheck);
        return canonicalParentCheck;
    }

    /**
     * Checks snowplow tracking key in DOM
     *
     * @param trackingID expected tracking id for snowplow event
     * @return true if tracking ID is present in DOM
     */
    @Override
    public Boolean snowplowTrackingCheck(String trackingID) {
        //@todo check snowplow tracking check
        return null;
    }

    /**
     * Validates gtm key in DOM
     *
     * @param gtmKey expected GTM Key
     * @return true if GTM Key matches expected string
     */
    @Override
    public Boolean gtmKeyCheck(String gtmKey) {
        report.log("GTM validation in process", true);
        Elements elements = htmlDOM.body().select("iframe[src]").attr("src", "//www.googletagmanager.com/ns.html?id=" + gtmKey);
        if(elements.size() > 0){
            return true;
        } else {
            report.log("GTM validation failed, unable to find iframe[src] with key: " + gtmKey, false);
            return false;
        }
    }

    /**
     * Validates analytics key in DOM
     *
     * @param googleAnalyticsKey expected analytics key
     * @return true if key is matched in DOM
     */
    @Override
    public Boolean googleAnalyticsCheck(String googleAnalyticsKey) {
        report.log("Google analytics key validation in process", true);
        Elements elements = htmlDOM.getElementsByTag("script")
                .attr("src", "https://www.googletagmanager.com/gtag/js?id=" + googleAnalyticsKey);
        if(elements.size() > 0){
            return true;
        } else {
            report.log("Google analytics validation failed, unable to find <script> tag with key: " + googleAnalyticsKey, false);
            return false;
        }
    }
}