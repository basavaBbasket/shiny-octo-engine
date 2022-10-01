package seo;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SeoHandler extends WebSettings {

	private AutomationReport report;

	public SeoHandler(AutomationReport report)
	{
		this.report=report;
	}
	
	public Document getJsoupDocumentForDesktop(String path) throws IOException
	{
		String baseURL=trimmedServerName;
		Document HPdoc = null ;
		String extendedURL=baseURL+path;
		
		if(baseURL.equals("www"))
		{
			System.out.println("URL: " +extendedURL);
			report.log("URL: " +extendedURL, true);
			HPdoc = Jsoup.connect(extendedURL)
					.timeout(0)
					.maxBodySize(0)
					.userAgent("Opera/12.02")
					.get();
		}
		else
		{
			System.out.println("URL: " +extendedURL);
			report.log("URL: " +extendedURL, true);
			HPdoc = Jsoup
			    .connect(extendedURL)
			    .timeout(0)
			    .maxBodySize(0)
			    .header("Authorization", "Basic YmJhZG1pbjpiYXNrM3QjQCE=")
			    .userAgent("Opera/12.02")
			    .get();
		}
		
		
		return HPdoc;
	}
	
	public Document getJsoupDocumentForPWA(String path) throws IOException
	{
		String baseURL=trimmedServerName;
		Document HPdoc = null ;
		String extendedURL=baseURL+path;;
		
		if(baseURL.equals("www"))
		{
			System.out.println("URL: " +extendedURL);
			report.log("URL: " +extendedURL, true);
			HPdoc = Jsoup.connect(extendedURL)
					.timeout(0)
					.maxBodySize(0)
					.userAgent("Opera/12.02 (Android 4.1; Linux; Opera Mobi/ADR-1111101157; U; en-US) Presto/2.9.201 Version/12.02")
					.get();
		}
		else
		{
			System.out.println("URL: " +extendedURL);
			report.log("URL: " +extendedURL, true);
			Response response = Jsoup
				    .connect(extendedURL)
				    .timeout(0)
				    .maxBodySize(0)
				    .header("Authorization", "Basic YmJhZG1pbjpiYXNrM3QjQCE=")
				    .userAgent("Opera/12.02 (Android 4.1; Linux; Opera Mobi/ADR-1111101157; U; en-US) Presto/2.9.201 Version/12.02")
				    .execute();
			if (200 == response.statusCode()) {
		        HPdoc = response.parse();
		        //System.out.println(HPdoc);

		        /* what ever you want to do */
		        }
			
			/*
			HPdoc = Jsoup
			    .connect(extendedURL)
			    .timeout(0)
			    .header("Authorization", "Basic YmJhZG1pbjpiYXNrM3QjQCE=")
			    .userAgent("Opera/12.02 (Android 4.1; Linux; Opera Mobi/ADR-1111101157; U; en-US) Presto/2.9.201 Version/12.02")
			    .get();*/
		}
		
		
		return HPdoc;
	}
	
	public boolean checkSeoHandlerGoogleAnalyticsCodeValue(String googleAnalyticsKey,Document doc) throws IOException
	{
		 Elements scriptElements = doc.getElementsByTag("script");
		 String pattern = "UA-\\d+-\\d";
		 int count=0;
		 String GNtext="";
		 String GNtextFinal="";
		 
		 
		 for (Element element :scriptElements )
		 {                
		        for (DataNode node : element.dataNodes())
		        {
		        		GNtext=node.getWholeData().trim();
		        		Pattern r = Pattern.compile(pattern);
		        		Matcher m = r.matcher(GNtext);
		        		      while (m.find( ))
		        		      {
		        		    	  GNtextFinal = m.group();
		        		      }		        	
		        }
		  }
		 
		 System.out.println(GNtextFinal); 
		 report.log("Excel Google Analytics Id: "+googleAnalyticsKey, true);
		 report.log("Obtained Google Analytics Id: "+GNtextFinal, true);
		 
		 if(googleAnalyticsKey.equals(GNtextFinal))
			 return true;
		 else {
			 report.log("Missing Content : "+ StringUtils.difference( GNtextFinal,googleAnalyticsKey), false);
			 return false;
		 }
	}

	public String extractGoogleAnalyticsKey(List<WebElement> elements){
		String finale = "";
		System.out.println("Total WebElements found >> "+elements.size());
		Pattern pattern = Pattern.compile("UA-\\d+-\\d");
		for(WebElement element: elements) {
			String text = element.getAttribute("src");
//			System.out.println("\n>>>>>>>"+text);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				finale = matcher.group(0);
				System.out.println("GAnalytics Key >> "+finale+"\n");
			}
		}
		return finale;
	}
	
	public boolean checkSeoHandlerDesktopId(String DesktopId,Document doc) throws IOException
	{
		 String GNurl = doc.select("iframe").attr("src");
		 String idValue = GNurl.split("id=")[1];
		 System.out.println("GA url"+GNurl);
		 System.out.println(idValue);
		 report.log( "Excel Desktop Id: "+ DesktopId, true);
		 report.log("Obtained Desktop Id: "+ idValue, true);
		
		 if(DesktopId.equals(idValue))
		 {
			 //report.log(false, "You are using Desktop and Desktop Id is: "+ idValue, true);
			 return true;
		 }
		 else {
			 report.log("Missing Content : "+StringUtils.difference( idValue,DesktopId), false);
			 return false;
		 }
	}

	//{Author >> Ajay; Modified on >> 6Sept2021}
	public String extractGTMFromList(List<WebElement> GNurl){
		String finale = "";
		System.out.println("Total WebElements found >> "+GNurl.size());
		Pattern pattern = Pattern.compile("(<iframe).*?(src=\"\\/\\/www\\.googletagmanager.com?)");
		for(WebElement element: GNurl){
			String text = element.getAttribute("innerHTML");
//			System.out.println(text+"\n");
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()){
				finale = text;
				Pattern extract = Pattern.compile("id=(.*?)\"");
				Matcher extract1 = extract.matcher(finale);
				while (extract1.find()){
					finale = extract1.group(1);
				}
				System.out.println("GTM Found >> "+finale);
			}
		}
		return finale;
	}
	
	public boolean checkSeoHandlerPWAId(String PWAId,Document doc) throws IOException
	{
		 String GNurl = doc.select("iframe").attr("src");
		 String idValue = GNurl.split("id=")[1];
		 System.out.println("GA url"+GNurl);
		 System.out.println(idValue);
		 report.log( "Excel PWA Id: "+ PWAId, true);
		 report.log("Obtained PWA Id: "+ idValue, true);
		
		 if(PWAId.equals(idValue))
		 {
			 report.log( "You are using PWA and PWA Id is: "+ idValue, true);
			 return true;
		 }
		 else {
			 report.log("Missing Content : "+StringUtils.difference(idValue,PWAId), false);
			 return false;
		 }
	}
	
	public boolean titleCheckOfHomePage(String title,Document doc)
	{
		String websiteTitle = doc.title();
		System.out.println("Title: "+websiteTitle);
		report.log("Excel Title: "+title, true);
		report.log("Obtained Title: "+websiteTitle, true);
		
		if(websiteTitle.contains(title))
			return true;
		else {
			report.log("Missing Content : "+StringUtils.difference(websiteTitle,title), false);
			return false;
		}
		
	}
	
	public boolean canonicalCheckOfHomePage(String hrefValue,Document doc,String path)
	{
		String server= trimmedServerName;
		
		if(!server.equals("www"))
		{
		hrefValue=server+path;
		String href = doc.select("link[rel=canonical]").attr("href");
		System.out.println("MetaData Content: "+href);
		report.log("Excel Canonical Link Check: "+hrefValue, true);
		report.log("Canonical Link Check: "+href, true);
		if(hrefValue.equals(href))
			return true;
		else {
			report.log("Missing Content : "+StringUtils.difference(href,hrefValue), false);
			return false;
		}
		}
		else 
		{
			String href = doc.select("link[rel=canonical]").attr("href");
			System.out.println("MetaData Content: "+href);
			report.log("Excel Canonical Link Check: "+hrefValue, true);
			report.log("Canonical Link Check: "+href, true);
			if(hrefValue.equals(href))
				return true;
			else {
				report.log("Missing Content : "+StringUtils.difference(href,hrefValue), false);
				return false;
			}
		}

		
	}
	
	public  boolean metaDataCheckOfHomePage(String Content,Document doc)
	{
		String metaDataContent = doc.select("meta[name=description]").get(0).attr("content");
		System.out.println("Excel MetaData Content: "+Content);
		System.out.println("MetaData Content: "+metaDataContent);
		report.log("Excel MetaData Content: "+Content,true );
		report.log("Obtained MetaData Content: "+metaDataContent, true);
		
		if(Content.equals(metaDataContent))
			return true;
		else {
			report.log("Missing Content : "+StringUtils.difference(metaDataContent,Content), false);
			return false;
		}
		
	}
	
	public  boolean canonicalParentCheckOfHomePage(String tagName,Document doc)
	{
		Element metaDataContent = doc.select("meta[name=description]").get(0).parent();
		String parentTag=metaDataContent.tagName();
		System.out.println("Parent Tag Name : "+parentTag);
		report.log("Excel Parent Tag Name : "+tagName, true);
		report.log("Obtained Parent Tag Name : "+parentTag, true);	
		if(tagName.equals(parentTag))
			return true;
		else {
			report.log("Missing Content : "+StringUtils.difference(parentTag,tagName), false);
			return false;
		}
		
	}

}
