package com.epam.cisen.sms;

import com.epam.cisen.core.api.AbstractMessenger;
import com.epam.cisen.core.api.Messenger;
import com.epam.cisen.core.api.dto.ToSend;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component
@Service(Messenger.class)
public class SmsMessenger extends AbstractMessenger<SmsDto> {

    /**
     * Credentials for Fast SMS gate.
     * http://speedsms.com.ua/
     * http://speedsms.com.ua/SpeedSMS_API%202.0.pdf
     */
    private static final String login = "cisen_sms_login";
    private static final String password = "cisen_sms_pass";
    private SmsSender sender = null;

    @Override
    protected SmsDto getPluginTemplateConfig() {
        SmsDto smsDto = new SmsDto();
        smsDto.setRecipient("Hint: Put phone number in format 38XXXXXXXXXX");
        return smsDto;
    }

    @Override
    protected void send(SmsDto smsDTO, ToSend message) {
        getSender().sendSms(smsDTO.getRecipient(), message.getBody());
        // TODO: Remove debug msg bellow
        System.out.println(smsDTO);
        System.out.println(message);
    }

    private SmsSender getSender() {
        if (sender == null) {
            sender = new SmsSender(login, password);
        }
        return sender;
    }
}
