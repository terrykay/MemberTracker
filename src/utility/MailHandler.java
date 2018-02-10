package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.SearchRowItem;

public class MailHandler {

    private static MailHandler mailHandler = null;
    private List<String> recipientsEmail;
    private Map<String, SearchRowItem> recipients;
    private String fromDNR = "donotreply@naturistfoundation.org";
    private String fromNat = "natfound@hotmail.co.uk";
    private String from = null;
    private final String host = "idserver";
    private final Properties properties;
    private final Session session;
    private String subject;
    private String emailBody;
    private String errorMessage = null;
    public String getErrorMessage() { return errorMessage; }

    protected MailHandler() {
        // Get the mail Session object
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        session = Session.getDefaultInstance(properties);
        recipientsEmail = new ArrayList();
        recipients = new HashMap();
    }

    public static MailHandler getInstance() {
        if (mailHandler == null) {
            mailHandler = new MailHandler();
        }
        return mailHandler;
    }

    public void setEmailBody(String body) {
        emailBody = body;
    }
    
    public void setSubject(String aSubject) {
        subject = aSubject;
    }
    
    public void setFrom(String newFrom) {
        from = newFrom;
    }

    public void setRecipients(List<SearchRowItem> recipients) {
        for (SearchRowItem eachRecipient : recipients) {
            String email = eachRecipient.getEmail();
            if (email != null) {
                this.recipientsEmail.add(email);
                this.recipients.put(email, eachRecipient);
            }
        }
    }
    
    public void setRecipient(String anEmail) {
        this.recipientsEmail.clear();
        this.recipientsEmail.add(anEmail);
    }
    
    public void setDoNotReply () {
        from = fromDNR;
    }



    public boolean send() {
        // Create new email for each recipient to ensure privacy and no cross posted data...
        boolean success = true;
        for (String eachRecipient : recipientsEmail) {
            try {
                if (from == null)
                    from = fromNat;
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(eachRecipient));
                //message.addRecipient(Message.RecipientType.TO, new InternetAddress("tel@tezk.co.uk"));
                message.setSubject(subject);              
                message.setText(emailBody);
                // Send message  
                Transport.send(message);
            } catch (MessagingException e) {
                errorMessage = e.getMessage();
                System.err.println("Problem sending email : " + errorMessage);
                
                success = false;
            }
        }
        return success;
    }
}
