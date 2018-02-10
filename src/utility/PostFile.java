/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author tezk
 */
import Soap.ImageTO;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SoapHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

public class PostFile {

    public static String post(ImageTO imageTO) {
        HttpClient httpClient=null;
        try {
            // HttpClient httpclient = new DefaultHttpClient();
            httpClient = Utility.createHttpClient_AcceptsUntrustedCerts();
        } catch (KeyStoreException ex) {
            Logger.getLogger(PostFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PostFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(PostFile.class.getName()).log(Level.SEVERE, null, ex);
        }
//        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost("https://idserver:8181/IDTrackerServer/ImageServlet");
        File file = new File(imageTO.getFilename());

        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/jpeg");
        mpEntity.addPart("userfile", cbFile);
        httppost.setHeader("details", imageTO.getDetails());
        httppost.setHeader("customerid", "" + imageTO.getCustomerId());
        httppost.setHeader("fileurl", imageTO.getUrl());
        httppost.setHeader("filename", new File(imageTO.getFilename()).getName());
        httppost.setHeader("sessionid", SoapHandler.getSessionId());
        httppost.setEntity(mpEntity);
        String url = null;
        try {
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpClient.execute(httppost);
            System.out.println("Passed " + response);
            HttpEntity resEntity = response.getEntity();
            String uploadResponse = response.getStatusLine().toString();
            System.out.println("Response : " + response.getStatusLine());
            if (!uploadResponse.contains("200")) {
                System.err.println("Encountered error from server");
                httpClient.getConnectionManager().shutdown();
                return null;
            }
            String tempString = EntityUtils.toString(resEntity);
            System.out.println(tempString);
            if (resEntity != null) {
                resEntity.consumeContent();
            }
            url = tempString.split("%")[1];
        } catch (IOException e) {
            System.err.println("Error sending image : " + e.getMessage());
            return null;
        }

        httpClient.getConnectionManager().shutdown();
        
        System.out.println("returning URL = "+url);
        
        return url;
    }


}
