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
    private int successCount = 0;
    private final String host = "idserver";
    private final Properties properties;
    private final Session session;
    private String subject;
    private String emailBody;
    private String errorMessage = "";
    public String getErrorMessage() { return errorMessage; }
    private boolean addBcc = true;
    public void setAddBcc(boolean value) { addBcc = value; }

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
            if (email != null && email.length() > 4) {
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
        successCount = 0;
        errorMessage = "";
        boolean success = true;
        for (String eachRecipient : recipientsEmail) {
            try {
                if (from == null)
                    from = fromNat;
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(eachRecipient));
                message.setSubject(subject);              
                message.setText(emailBody);
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
                // Send message  
                Transport.send(message);
                // If we get here, success?
                successCount += 1;
            } catch (MessagingException e) {
                errorMessage += "Problem sending email "+eachRecipient+" : " + e.getMessage()+"\n";
                System.err.println(e);
                
                success = false;
            }
        }
        
        errorMessage = "Successfully sent "+successCount+" emails - the following had issues:\n" + errorMessage;
        
        return success;
    }
}
