package com.epam.cisen.email;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.AbstractMessenger;
import com.epam.cisen.core.api.Messenger;
import com.epam.cisen.core.api.dto.ToSend;
import com.epam.cisen.core.api.util.PropertiesUtil;

@Component(label = "Email messenger", metatype = true, policy = ConfigurationPolicy.REQUIRE)
@Service(Messenger.class)
@Properties({ @Property(name = EmailMessenger.SMTP_SERVER, label = "SMTP server"),
        @Property(name = EmailMessenger.SMTP_PORT, label = "SMTP server port", intValue = 465),
        @Property(name = EmailMessenger.SMTP_LOGIN, label = "Login"),
        @Property(name = EmailMessenger.SMTP_PASSWORD, label = "Password", passwordValue = ""),
        @Property(name = EmailMessenger.SENDER_EMAIL, label = "Sender FROM address", value = "cisen@fakedomen.info") })
public class EmailMessenger extends AbstractMessenger<EmailConfigDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailMessenger.class);

    static final String SMTP_SERVER = "email.config.smtp.server";
    static final String SMTP_PORT = "email.config.smtp.port";
    static final String SMTP_LOGIN = "email.config.smtp.login";
    static final String SMTP_PASSWORD = "email.config.smtp.password";
    static final String SENDER_EMAIL = "email.config.sender.email";

    private String server;
    private int port;
    private String login;
    private String password;
    private String from;

    @Override
    protected EmailConfigDTO getPluginTemplateConfig() {
        return new EmailConfigDTO();
    }

    @Override
    protected void setupPlugin(ComponentContext componentContext) {

        final Dictionary properties = componentContext.getProperties();
        server = PropertiesUtil.toString(properties.get(SMTP_SERVER), StringUtils.EMPTY);
        port = PropertiesUtil.toInteger(properties.get(SMTP_PORT), 465);
        login = PropertiesUtil.toString(properties.get(SMTP_LOGIN), StringUtils.EMPTY);
        password = PropertiesUtil.toString(properties.get(SMTP_PASSWORD), StringUtils.EMPTY);
        from = PropertiesUtil.toString(properties.get(SENDER_EMAIL), StringUtils.EMPTY);
    }

    @Override
    protected void send(EmailConfigDTO configDTO, ToSend message) {

        try {
            Email email = new SimpleEmail();
            email.setHostName(server);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(login, password));
            email.setSSLOnConnect(true);
            email.setFrom(from);
            email.setSubject(message.getSubject());
            email.setMsg(message.getBody());
            email.addTo(configDTO.getRecipient());
            email.send();

        } catch (EmailException ex) {
            LOGGER.error("Send E-mail exception.", ex);
        }
    }
}
