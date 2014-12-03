package com.epam.cisen.sms;

public class SmsSender {

    private final static String XML_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><package login=\"%s\" sig=\"%s\" classver='2.0'><messages><msg recipient=\"%s\" sender=\"SPEEDSMS\">%s</msg></messages></package>";

    private String login;
    private String signature;

    public SmsSender(final String login, final String password) {
        this.login = login;
        signature = SignatureUtil.getSignature(login, password);
    }

    public void sendSms(final String recipient, final String message) {
        String requestXml = String.format(XML_TEMPLATE, login, signature, recipient, message);
        PostClient.prepareRequest(requestXml);
        PostClient.doRequest();
    }
}
