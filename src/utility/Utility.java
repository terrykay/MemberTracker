/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import Soap.AddressTO;
import Soap.CustomerTO;
import Soap.ImageTO;
import UtilityClasses.MyDate;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import model.SearchRowItem;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

/**
 *
 * @author tezk
 */
public class Utility {
    private static List <File> tempFileList;
    
    public static void addFileToTempList(File tempFile) {
        if (tempFileList == null)
            tempFileList = new ArrayList();
        
        tempFileList.add(tempFile);
    }
    
    public static List <File> getFileTempList() {
        return tempFileList;
    }

    public static void removeTempFiles() {
        if (tempFileList == null)
            return;
        
        for (File eachFile : tempFileList) {
            eachFile.delete();
        }
        tempFileList = null;
    }
    
    public static CustomerTO createCustomer() {
        //Soap.CustomerTO is generated class, create here
        CustomerTO newCustomer = new CustomerTO();
        newCustomer.setAddressId(new AddressTO());

        return newCustomer;
    }

    public static Optional<ButtonType> showConfirmationAlert(String title, String header, String body) {
        // Returns Optional, optional.get() == ButtonType.ok for OK selected
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static Optional<ButtonType> showAlert(String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static String toString(CustomerTO aCustomer) {
        StringBuilder aString = new StringBuilder();

        addNoReturn(aString, aCustomer.getForename());
        addSpace(aString);
        add(aString, aCustomer.getSurname());
        add(aString, aCustomer.getAddressId().getAddressLineOne());
        add(aString, aCustomer.getAddressId().getAddressLineTwo());
        add(aString, aCustomer.getAddressId().getTown());
        add(aString, aCustomer.getAddressId().getCounty());
        add(aString, aCustomer.getAddressId().getCountry());
        add(aString, aCustomer.getAddressId().getPostcode());
        add(aString, aCustomer.getEmail());
        add(aString, aCustomer.getTelephoneOne());
        add(aString, aCustomer.getTelephoneTwo());

        return aString.toString();
    }

    public static String toStringNameAndInsurance(Collection<SearchRowItem> customers) {
        StringBuilder string = new StringBuilder();
        
        string.append("Forename, Surname, Insurance expiry\n");
         for (SearchRowItem eachCustomer : customers) {
            CustomerTO aCustomer = SoapHandler.getCustomerByID(eachCustomer.getId());
            addNoReturn(string, aCustomer.getForename());
            addComma(string);
            addNoReturn(string, aCustomer.getSurname());
            addComma(string);
            addNoReturn(string, aCustomer.getMembership().getInsuranceExpiry().toString());
            addReturn(string);
        }

        return string.toString();
    }
    
    public static String toString(Collection<SearchRowItem> customers) {
        StringBuilder string = new StringBuilder();

        string.append("Forename, Surname, Tel no. one, Tel no. two, email, ID Valid\n");
        for (SearchRowItem eachCustomer : customers) {
            addNoReturn(string, eachCustomer.getForename());
            addComma(string);
            addNoReturn(string, eachCustomer.getSurname());
            addComma(string);
            addNoReturn(string, eachCustomer.getTelephoneOne());
            addComma(string);
            addNoReturn(string, eachCustomer.getTelephoneTwo());
            addComma(string);
            addNoReturn(string, eachCustomer.getEmail());
            addComma(string);
            string.append(checkValidID(eachCustomer.getImageCollection()).getValue());
            addReturn(string);
        }

        return string.toString();
    }

    private static void add(StringBuilder string, String what) {
        if (what != null && what.length() > 0) {
            addNoReturn(string, what);
            addReturn(string);
        }
    }

    private static void addNoReturn(StringBuilder string, String what) {
        if (what != null) {
            string.append(what);
        }
    }

    private static void addSpace(StringBuilder string) {
        string.append(" ");
    }

    private static void addReturn(StringBuilder string) {
        string.append("\n");
    }

    private static void addComma(StringBuilder string) {
        string.append(", ");
    }

    public static int getIDDaysLeft(Collection<ImageTO> images) {
        Date todaysDate = null;
        todaysDate = new Date(System.currentTimeMillis());
        Date latestDate = getLatestID(images);
        if (latestDate.before(todaysDate) || latestDate.equals(todaysDate)) {
            return 0;
        }

        return MyDate.getDaysBetween(todaysDate, latestDate);
    }

    public static StringProperty checkValidID(Collection<ImageTO> images) {
        StringProperty value = new SimpleStringProperty();

        int daysBetween = getIDDaysLeft(images);
        if (daysBetween == 0) {
            value.setValue("Expired");
        } else if (daysBetween < 365) {
            value.setValue(daysBetween + " days");
        } else {
            value.setValue((daysBetween / 365) + " years");
        }

        return value;
    }

    public static Date getLatestID(Collection<ImageTO> images) {
        Date latestDate = new Date(System.currentTimeMillis());

        for (ImageTO eachImage : images) {
            if (eachImage.getType() == 'i') {
                Date eachDate = MyDate.toDate(eachImage.getExpires());
                if (eachDate.after(latestDate)) {
                    latestDate = eachDate;
                }
            }
        }
        return latestDate;
    }

  public static BufferedImage imageToBufferedImage(Image im) {
     BufferedImage bi = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
     Graphics bg = bi.getGraphics();
     bg.drawImage(im, 0, 0, null);
     bg.dispose();
     return bi;
  }
  
      public static HttpClient createHttpClient_AcceptsUntrustedCerts() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        b.setSslcontext(sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        b.setConnectionManager(connMgr);

        // finally, build the HttpClient;
        //      -- done!
        HttpClient client = b.build();
        return client;
    }
      


 public static class DefaultTrustManager implements X509TrustManager {
    public DefaultTrustManager() {
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
}
