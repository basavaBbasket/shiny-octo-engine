package seo;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleSheetAuthentication {
	
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH =System.getProperty("user.dir")+ "/tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = System.getProperty("user.dir")+"/credentials.json";


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException
    {
		

    	String filename="/credentials.json";
        InputStream in = GoogleSheetAuthentication.class.getResourceAsStream(filename);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

     
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    public List getDetailsForPDOnionSKUID10000148Page(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<44)
                {         	
                	if(count>40)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForPDBasmatiRiceSKUID283426Page(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<39)
                {         	
                	if(count>35)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForPBAshirwaadAttaPage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<34)
                {         	
                	if(count>30)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForPBAmulPage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<29)
                {         	
                	if(count>25)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForPCBakeryCakeDiaryPage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<24)
                {         	
                	if(count>20)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    
    public List getDetailsForPCBeveragesPage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<19)
                {         	
                	if(count>15)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForC1SnackAndBrandedFoodPage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<14)
                {         	
                	if(count>10)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public List getDetailsForC1FruitsAndVegetablePage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<9)
                {         	
                	if(count>5)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }


    public List getDetailsForHomePage(String screenType)throws IOException, GeneralSecurityException 
    {
    	
    	List list=new ArrayList();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1rnpKxhiWaIwENOzTdUIw9OhXamh28WuB0v4bCJ4N5FI";
        String range = "A2:C";
        if(screenType=="PWA")
        	range = "'Checklist PWA'!A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        int count=0;
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else 
        {
            
            for (List Column : values) 
            {

            	String dataToCompare=(String) Column.get(2);
                
                if(count<4)
                {         	
                	if(count!=0)
                	{
                    System.out.println("Data: "+dataToCompare);
                	list.add(dataToCompare);
                	System.out.println("Found");
                	}
                	count++;
                }
                else
                {
                	break;
                }
            }
        }
		return list;
    }
    
    public static void main(String [] args) throws IOException, GeneralSecurityException
    {
    	
    	GoogleSheetAuthentication data=new GoogleSheetAuthentication();
    	
    	List list0=data.getDetailsForHomePage("Web");
    	List list1=data.getDetailsForC1FruitsAndVegetablePage("Web");
    	List list2=data.getDetailsForC1SnackAndBrandedFoodPage("Web");
    	List list3=data.getDetailsForPCBeveragesPage("Web");
    	List list4=data.getDetailsForPCBakeryCakeDiaryPage("Web");
    	List list5=data.getDetailsForPBAmulPage("Web");
    	List list6=data.getDetailsForPBAshirwaadAttaPage("Web");
    	List list7=data.getDetailsForPDBasmatiRiceSKUID283426Page("Web");
    	List list8=data.getDetailsForPDOnionSKUID10000148Page("Web");
    	
    	System.out.println(list0.get(0));
    	System.out.println(list0.get(1));
    	System.out.println(list0.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list1.get(0));
    	System.out.println(list1.get(1));
    	System.out.println(list1.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list2.get(0));
    	System.out.println(list2.get(1));
    	System.out.println(list2.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list3.get(0));
    	System.out.println(list3.get(1));
    	System.out.println(list3.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list4.get(0));
    	System.out.println(list4.get(1));
    	System.out.println(list4.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list5.get(0));
    	System.out.println(list5.get(1));
    	System.out.println(list5.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list6.get(0));
    	System.out.println(list6.get(1));
    	System.out.println(list6.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list7.get(0));
    	System.out.println(list7.get(1));
    	System.out.println(list7.get(2));
    	
    	System.out.println("");
    	
    	System.out.println(list8.get(0));
    	System.out.println(list8.get(1));
    	System.out.println(list8.get(2));
    	
    }
}