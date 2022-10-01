package seo;

public class SeoValidator {
    private final String url;
    private final String expectedTitle;
    private final String expectedMeta;
    private final String expectedCanonicalLink;
    private final String expectedGTMKey;
    private final String expectedGoogleAnalyticsKey;
    private final String userAgents;
    private final String userAgentsDescription;

    private SeoValidator(seoValidatorBuilder builder) {
        this.url = builder.url;
        this.expectedTitle = builder.expectedTitle;
        this.expectedMeta = builder.expectedMeta;
        this.expectedCanonicalLink = builder.expectedCanonicalLink;
        this.expectedGTMKey = builder.expectedGTMKey;
        this.expectedGoogleAnalyticsKey = builder.expectedGoogleAnalyticsKey;
        this.userAgents = builder.userAgents;
        this.userAgentsDescription = builder.userAgentsDescription;
    }

    String getUrl(){
        return url;
    }
    String getExpectedTitle(){
        return expectedTitle;
    }
    String getExpectedMeta(){
        return expectedMeta;
    }
    String getExpectedCanonicalLink(){
        return expectedCanonicalLink;
    }
    String getExpectedGTMKey(){
        return expectedGTMKey;
    }
    String getExpectedGoogleAnalyticsKey(){
        return expectedGoogleAnalyticsKey;
    }
    String getUserAgents(){
        return userAgents;
    }
    String getUserAgentsDescription(){
        return userAgentsDescription;
    }

    public static class seoValidatorBuilder{
        private final String url;
        private String expectedTitle;
        private String expectedMeta;
        private String expectedCanonicalLink;
        private String expectedGTMKey;
        private String expectedGoogleAnalyticsKey;
        private String userAgents;
        private String userAgentsDescription;
        public seoValidatorBuilder(String url){
            this.url = url;
        }
        public seoValidatorBuilder setExpectedTitle(String title){
            this.expectedTitle = title;
            return this;
        }
        public seoValidatorBuilder setExpectedMeta(String metaData){
            this.expectedMeta = metaData;
            return this;
        }
        public seoValidatorBuilder setExpectedCanonicalLink(String canonicalLink){
            this.expectedCanonicalLink = canonicalLink;
            return this;
        }
        public seoValidatorBuilder setExpectedGTMKey(String gtmKey){
            this.expectedGTMKey = gtmKey;
            return this;
        }
        public seoValidatorBuilder setExpectedGoogleAnalyticsKey(String googleAnalyticsKey){
            this.expectedGoogleAnalyticsKey = googleAnalyticsKey;
            return this;
        }
        public seoValidatorBuilder setUserAgents(String userAgents){
            this.userAgents = userAgents;
            return this;
        }
        public seoValidatorBuilder setUserAgentsDescription(String userAgentsDescription){
            this.userAgentsDescription = userAgentsDescription;
            return this;
        }
        public SeoValidator build(){
            return new SeoValidator(this);
        }
    }
}
