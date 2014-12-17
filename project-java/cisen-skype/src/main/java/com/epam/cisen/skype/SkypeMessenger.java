package com.epam.cisen.skype;

import com.epam.cisen.core.api.AbstractMessenger;
import com.epam.cisen.core.api.Messenger;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.ToSend;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.apache.commons.io.IOUtils.closeQuietly;

@Component
@Service(Messenger.class)
public class SkypeMessenger extends AbstractMessenger<SkypeConfigDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkypeMessenger.class);

    private static final String SKYPE_SERVICE_HOST = "127.0.0.1";
    private static final int SKYPE_SERVICE_PORT = 9000;

    private static class SkypeMessage {

        private static String MESSAGE_PATTERN = "%s|%s|%s|%s";

        private final String username;
        private final String message;
        private final String changeTopic;
        private final String topic;

        public SkypeMessage(String username, String message, boolean changeTopic, String topic) {
            if (StringUtils.isEmpty(username)) {
                throw new IllegalArgumentException("No Skype user name");
            }
            this.username = StringUtils.defaultString(username);
            this.message = message;
            this.changeTopic = String.valueOf(changeTopic);
            this.topic = topic;
        }

        @Override
        public String toString() {
            return String.format(MESSAGE_PATTERN, username, message, changeTopic, topic);
        }
    }

    Socket socket = null;
    DataOutputStream out;

    @Override
    protected SkypeConfigDTO getPluginTemplateConfig() {
        return new SkypeConfigDTO();
    }

    @Override
    protected void setupPlugin(ComponentContext componentContext) {

    }

    @Activate
    public void activate() {
        LOGGER.info("SkypeMessenger activate");
        try {
            registerPlugin();
            socket = new Socket(SKYPE_SERVICE_HOST, SKYPE_SERVICE_PORT);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            LOGGER.error("Create socket: ", ex);
        }
    }

    @Deactivate
    public void deactivate() {
        closeQuietly(out);
        closeQuietly(socket);
    }

    @Override
    protected void send(SkypeConfigDTO configDTO, ToSend message) {
        LOGGER.info("Try to send message via 'Skype' program");

        try {
            SkypeMessage telnetMessage = new SkypeMessage(configDTO.getRecipient(),
                    message.getBody(), configDTO.isUpdateChatName(),
                    message.getSubject());

            out.writeUTF(telnetMessage.toString());
            out.flush();
        } catch (Exception ex) {
            LOGGER.error("Fail to send Skype message", ex);
        }
    }

}
