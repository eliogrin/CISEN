package com.epam.cisen.skype;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
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

@Component(label = "Skype messenger plugin", metatype = true, policy = ConfigurationPolicy.REQUIRE)
@Service(Messenger.class)
@Properties({ @Property(label = "Skype service host", value = "127.0.0.1", name = SkypeMessenger.SKYPE_HOST),
        @Property(label = "Skype service port", intValue = 9000, name = SkypeMessenger.SKYPE_PORT) })
public class SkypeMessenger extends AbstractMessenger<SkypeConfigDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkypeMessenger.class);

    static final String SKYPE_HOST = "skype.host";
    static final String SKYPE_PORT = "skype.port";

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
    protected void activatePlugin(ComponentContext componentContext) {
        LOGGER.info("Skype Messenger activate");

        String host = PropertiesUtil.toString(componentContext.getProperties().get(SKYPE_HOST), "127.0.0.1");
        int port = PropertiesUtil.toInteger(componentContext.getProperties().get(SKYPE_PORT), 9000);

        try {
            socket = new Socket(host, port);
            if (socket.isConnected()) {
                out = new DataOutputStream(socket.getOutputStream());
            } else {
                LOGGER.warn("Skype service in unavailable now. Unregister plugin. Please, start service.");
                unregisterPlugin();
            }
        } catch (IOException ex) {
            LOGGER.error("Create socket: ", ex);
        }
    }

    @Override
    protected void deactivatePlugin() {
        closeQuietly(out);
        closeQuietly(socket);
    }

    @Override
    protected void send(SkypeConfigDTO configDTO, ToSend message) {
        LOGGER.info("Try to send message via 'Skype' program");

        try {
            SkypeMessage telnetMessage = new SkypeMessage(configDTO.getRecipient(), message.getBody(),
                    configDTO.isUpdateChatName(), message.getSubject());

            out.writeUTF(telnetMessage.toString());
            out.flush();
        } catch (Exception ex) {
            LOGGER.error("Fail to send Skype message", ex);
        }
    }

}
