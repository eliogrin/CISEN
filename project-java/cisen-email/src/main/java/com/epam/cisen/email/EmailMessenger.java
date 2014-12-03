package com.epam.cisen.email;

import com.epam.cisen.core.api.AbstractMessenger;
import com.epam.cisen.core.api.Messenger;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.ToSend;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(Messenger.class)
public class EmailMessenger extends AbstractMessenger<EmailConfigDTO> {

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final int SMTP_INT = 465;
    private static final String CISEN_USER = "cisen_user_email_address";
    private static final String CISEN_PASSWORD = "cisen_user_email_pass";

    @Override
    protected EmailConfigDTO getPluginTemplateConfig() {
        return new EmailConfigDTO();
    }

    @Override
    protected void send(EmailConfigDTO configDTO, ToSend message) {

        boolean disabledForDemo = true;
        try {
            Email email = new SimpleEmail();
            email.setHostName(SMTP_SERVER);
            email.setSmtpPort(SMTP_INT);
            email.setAuthenticator(new DefaultAuthenticator(CISEN_USER, CISEN_PASSWORD));
            email.setSSLOnConnect(true);
            email.setFrom(CISEN_USER);
            email.setSubject(message.getSubject());
            email.setMsg(message.getBody());
            email.addTo(configDTO.getRecipient());

            if (!disabledForDemo) {
                email.send();
            }

        } catch (EmailException ex) {
            System.out.println("Fail to send email: " + ex.getMessage());
        }
    }

    @Override
    protected List<EmailConfigDTO> getTestData() {
        List<EmailConfigDTO> result = new ArrayList<>();

        EmailConfigDTO config = new EmailConfigDTO();
        config.setRecipient("email_for_test");
        result.add(config);
        return result;
    }
}
