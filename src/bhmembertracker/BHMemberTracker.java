/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bhmembertracker;

import controller.FXMLLoginController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author tezk
 */
public class BHMemberTracker extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            readCerts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*    Parent root = null;
        FXMLLoader custFxmlLoader=new FXMLLoader();
        custFxmlLoader.setLocation(BHMemberTracker.class.getResource("/view/FXMLCustomer.fxml"));
        //System.out.println(ProjectLog.class.getResource("/view/main.fxml"));
        try {
            root = (Parent)custFxmlLoader.load();
        }
        catch (Exception e) {
            System.err.println("Exception! "+e.getMessage());
            System.exit(0);
        }
        Scene scene = new Scene(root);

        stage.setScene(scene);
       
        FXMLCustomerController controller = custFxmlLoader.getController();
        controller.setStage(stage);
        controller.setScene(scene);
        stage.setTitle("Customer details");
        CustomerTO myCustomer = new CustomerTO();
        myCustomer.setForename("Terry");
        myCustomer.setSurname("Kay");
        CarTO aCar = new CarTO();
        aCar.setRegno("PIL3861");
        CarTO bCar = new CarTO();
        bCar.setRegno("LB05FDJ");
        bCar.setMake("Ford");
        bCar.setModel("Fiesta");
        CarTO cCar = new CarTO();
        
        ChildTO aChild = new ChildTO();
        aChild.setForename("Ethan");
        aChild.setSurname("Kay");
        long YEAR = 365*24*60*60*1000;
      //  aChild.setDob(new Date(System.currentTimeMillis()-YEAR*3));
        
        
        cCar.setRegno("F818OJO");
        cCar.setColour("White");
        
       
        myCustomer.getCarCollection().add(aCar);
        myCustomer.getCarCollection().add(bCar);
        myCustomer.getCarCollection().add(cCar);
        myCustomer.getChildCollection().add(aChild);
        controller.setCustomerDetails(myCustomer);
        //stage.show();
         */
 /*
        FXMLLoader searchFxmlLoader=new FXMLLoader();
        searchFxmlLoader.setLocation(BHMemberTracker.class.getResource("/view/FXMLSearchCustomer.fxml"));
        //System.out.println(ProjectLog.class.getResource("/view/main.fxml"));
        Parent searchRoot=null;
        try {
            searchRoot = searchFxmlLoader.load();
        }
        catch (Exception e) {
            System.err.println("Exception! "+e.getMessage());
            System.exit(0);
        }
        Scene bscene = new Scene(searchRoot);

        Stage bstage = new Stage();
        
        bstage.setScene(bscene);
       
        FXMLSearchCustomerController searchcontroller = searchFxmlLoader.getController();
        searchcontroller.setStage(bstage);
        searchcontroller.setScene(bscene);
        searchcontroller.loadCustomers();
        bstage.setTitle("Customer search");
        bstage.show();
         */
        //FXMLSearchCustomerController controller = new FXMLSearchCustomerController();
        //controller = (FXMLSearchCustomerController)controller.load();
        //controller.loadCustomers();
        //controller.loadCustomers();
        FXMLLoginController controller = new FXMLLoginController();
        controller = (FXMLLoginController) controller.load();
        controller.getStage().show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

    private static void readCerts() throws FileNotFoundException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
    }

}
