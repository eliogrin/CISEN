package com.epam.cisen.sms;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.ComponentContext;

import com.epam.cisen.core.api.AbstractMessenger;
import com.epam.cisen.core.api.Messenger;
import com.epam.cisen.core.api.dto.ToSend;
import com.epam.cisen.core.api.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(label = "Sms messenger", metatype = true, policy = ConfigurationPolicy.REQUIRE)
@Service(Messenger.class)
@Properties({
        @Property(name = SmsMessenger.GATE, label = "Gate", description = "Use any service which send sms, for example (http://speedsms.com.ua)", value = "http://speedsms.com.ua/cgi-bin/api2.0.cgi"),
        @Property(name = SmsMessenger.LOGIN, label = "Login"),
        @Property(name = SmsMessenger.PASSWORD, label = "Password", passwordValue = "") })
public class SmsMessenger extends AbstractMessenger<SmsDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsMessenger.class);

    private final static String XML_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><package login=\"%s\" sig=\"%s\" classver='2.0'><messages><msg recipient=\"%s\" sender=\"SPEEDSMS\">%s</msg></messages></package>";

    static final String GATE = "sms.config.gate";
    static final String LOGIN = "sms.config.login";
    static final String PASSWORD = "sms.config.password";

    private String login;
    private String signature;

    private static HttpPost request;
    private static CloseableHttpClient client = HttpClients.createDefault();

    @Override
    protected SmsDto getPluginTemplateConfig() {
        SmsDto smsDto = new SmsDto();
        smsDto.setRecipient("Hint: Put phone number in format 38XXXXXXXXXX");
        return smsDto;
    }

    @Override
    protected void setupPlugin(ComponentContext componentContext) {

        final Dictionary properties = componentContext.getProperties();
        String gate = PropertiesUtil.toString(properties.get(GATE), StringUtils.EMPTY);
        login = PropertiesUtil.toString(properties.get(LOGIN), StringUtils.EMPTY);
        String password = PropertiesUtil.toString(properties.get(PASSWORD), StringUtils.EMPTY);

        signature = SignatureUtil.getSignature(login, password);

        request = new HttpPost(gate);
        request.setHeader("ContentType", "application/xml; charset=utf=8");
    }

    @Override
    protected void send(SmsDto smsDTO, ToSend message) {

        try {
            request.setEntity(new StringEntity(String.format(XML_TEMPLATE, login, signature, smsDTO.getRecipient(),
                    message.getBody()), ContentType.APPLICATION_XML));

            client.execute(request);

        } catch (IOException e) {
            // TODO: add logging to file

            LOGGER.error("Fail to end SMS", e);
        }

    }
}
