package seo.interfaces;

public interface validators {

    /**
     * Validates string inside title tag in DOM
     * @param title expected title
     * @return true if title matches with expected string
     */
    Boolean titleCheck(String title);

    /**
     * Validates meta data in DOM
     * @param metaData expected meta
     * @return true if meta matches with expected string
     */
    Boolean metaDataCheck(String metaData);

    /**
     * Validates canonical link inside DOM
     * @param canonicalLink expected link text
     * @return true if expected canonical link is present
     */
    Boolean canonicalHrefCheck(String canonicalLink);

    /**
     * Validates canonical parent tag inside DOM
     * @return true if expected canonical tag is present in DOM
     */
    Boolean canonicalParentCheck();

    /**
     * Checks snowplow tracking key in DOM
     * @param trackingID expected tracking id for snowplow event
     * @return true if tracking ID is present in DOM
     */
    Boolean snowplowTrackingCheck(String trackingID);

    /**
     * Validates gtm key in DOM
     * @param gtmKey expected GTM Key
     * @return true if GTM Key matches expected string
     */
    Boolean gtmKeyCheck(String gtmKey);

    /**
     * Validates analytics key in DOM
     * @param googleAnalyticsKey expected analytics key
     * @return true if key is matched in DOM
     */
    Boolean googleAnalyticsCheck(String googleAnalyticsKey);
}
