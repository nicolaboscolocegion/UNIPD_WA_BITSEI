/*
 * Copyright (c) 2021-2022 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.bitsei.utils.mail;


import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * Provides methods for sending e-mails.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public final class MailManager {

    /**
     * Empty constructor.
     */
    public MailManager(){
        // empty constructor
    }

    /**
     * A LOGGER available for all the subclasses.
     */
    private static final Logger LOGGER = LogManager.getLogger(MailManager.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * Name of the configuration file.
     */
    private static final String CONFIG_FILE = "mailManager.properties";

    /**
     * The value of the property containing email of the sender.
     */
    private static final String from;

    /**
     * The mail session.
     */
    private static final Session session;

    /**
     * Loads the configuration from {@code CONFIG_FILE} and initializes the mailer.
     */
    static {
        Properties cfg = loadConfiguration();

        // set up the configuration for JavaMail
        final Properties p = new Properties();

        System.out.println("## CFG: " + cfg + " ##");
        System.out.println("## P: " + p + " ##");
        String tmp = cfg.getProperty(MailManager.class.getName() + ".from");
        if (tmp == null || tmp.isBlank()) {
            LOGGER.error("Property %s missing or empty.", MailManager.class.getName() + ".from");
            throw new IllegalStateException(
                    String.format("Property %s missing or empty.", MailManager.class.getName() + ".from"));
        }
        from = tmp;
        p.put("mail.from", from);


        tmp = cfg.getProperty(MailManager.class.getName() + ".smtp.host");
        if (tmp == null || tmp.isBlank()) {
            LOGGER.error("Property %s missing or empty.", MailManager.class.getName() + ".smtp.host");
            throw new IllegalStateException(
                    String.format("Property %s missing or empty.", MailManager.class.getName() + ".smtp.host"));
        }
        p.put("mail.smtp.host", tmp);

        tmp = cfg.getProperty(MailManager.class.getName() + ".smtp.port");
        if (tmp != null && !tmp.isBlank()) {
            p.put("mail.smtp.port", tmp);
        }

        tmp = cfg.getProperty(MailManager.class.getName() + ".smtp.userName");
        if (tmp == null || tmp.isBlank()) { // ensure that null and blank are the same
            tmp = null;
        }
        final String username = tmp;

        tmp = cfg.getProperty(MailManager.class.getName() + ".stmp.password");
        if (tmp == null || tmp.isBlank()) { // ensure that null and blank are the same
            tmp = null;
        }
        final String password = tmp;

        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.debug", "false");

        if (username != null && password != null) {
            p.put("mail.smtp.auth", "true");
            session = Session.getInstance(p, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } else {
            session = Session.getInstance(p);
        }


    }


    /**
     * Sends an e-mail without attachments.
     *
     * @param to          the receiver(s) of the e-mail.
     * @param subject     the subject of the e-mail.
     * @param message     the body of the e-mail.
     * @param messageMIME the MIME media type of the message.
     * @throws MessagingException if there is any problem in sending the e-mail.
     */
    public static void sendMail(final String to, final String subject, final String message, final String messageMIME) throws
            MessagingException {

        if (to == null || to.isBlank()) {
            LOGGER.error("Recipient of the email missing or empty.");
            throw new MessagingException("Recipient of the email missing or empty.");
        }

        if (subject == null || subject.isBlank()) {
            LOGGER.error("Subject of the email missing or empty.");
            throw new MessagingException("Subject of the email missing or empty.");
        }

        if (message == null || message.isBlank()) {
            LOGGER.error("Body of the email missing or empty.");
            throw new MessagingException("Body of the email missing or empty.");
        }

        if (messageMIME == null) {
            LOGGER.error("MIME media type of the email message missing.");
            throw new MessagingException("MIME media type of the email message missing.");
        }

        final MimeMessage mm = new MimeMessage(session); // the message
        InternetAddress ia = null; // to and bcc addresses

        try {

            mm.setFrom();

            ia = new InternetAddress(to);
            mm.addRecipient(Message.RecipientType.TO, ia);

            ia = new InternetAddress(from);
            mm.addRecipient(Message.RecipientType.BCC, ia);

            mm.setSubject(subject);

            // create the message part
            mm.setContent(message, messageMIME);

            // Send the message
            Transport.send(mm);

        } catch (AddressException e) {
            LOGGER.error(
                    new StringFormattedMessage("Invalid e-mail address %s for e-mail with subject %s.", to, subject),
                    e);
            throw e;
        } catch (final MessagingException e) {
            LOGGER.error(new StringFormattedMessage("Error while sending e-mail with subject %s to %s.", subject, to),
                    e);
            throw e;
        }

        LOGGER.info("## MailManager: E-mail with subject %s successfully sent to %s. ##", subject, to);
    }

    /**
     * Sends an e-mail with an attachment.
     *
     * @param to                 the receiver(s) of the e-mail.
     * @param subject            the subject of the e-mail.
     * @param message            the textual part of the e-mail.
     * @param messageMIME        the MIME media type of the message.
     * @param attachment         the attachment to the e-mail.
     * @param attachmentMIME     the MIME media type of the attachment to the e-mail.
     * @param attachmentFileName the file name to assign to the attachment.
     * @throws MessagingException if there is any problem in sending the e-mail.
     */
    public static void sendAttachmentMail(final String to, final String subject, final String message, final String messageMIME, final byte[] attachment, final String attachmentMIME, final String attachmentFileName) throws
            MessagingException {

        if (to == null || to.isBlank()) {
            LOGGER.error("Recipient of the email missing or empty.");
            throw new MessagingException("Recipient of the email missing or empty.");
        }

        if (subject == null || subject.isBlank()) {
            LOGGER.error("Subject of the email missing or empty.");
            throw new MessagingException("Subject of the email missing or empty.");
        }

        if (message == null || message.isBlank()) {
            LOGGER.error("Body of the email missing or empty.");
            throw new MessagingException("Body of the email missing or empty.");
        }

        if (messageMIME == null) {
            LOGGER.error("MIME media type of the email message missing.");
            throw new MessagingException("MIME media type of the email message missing.");
        }

        if (attachment == null) {
            LOGGER.error("Attachment to the email missing.");
            throw new MessagingException("Attachment to the email missing.");
        }

        if (attachmentMIME == null) {
            LOGGER.error("MIME media type of the email attachment missing.");
            throw new MessagingException("MIME media type of the email attachment missing.");
        }

        if (attachmentFileName == null || attachmentFileName.isBlank()) {
            LOGGER.error("File name of the attachment missing or empty.");
            throw new MessagingException("File name of the attachment missing or empty.");
        }

        final MimeMessage mm = new MimeMessage(session); // the message
        final Multipart multipart = new MimeMultipart(); // the body of the message
        MimeBodyPart messageBodyPart = null; // part of the body
        InternetAddress ia = null; // to and bcc addresses

        try {

            mm.setFrom();

            ia = new InternetAddress(to);
            mm.addRecipient(Message.RecipientType.TO, ia);

            ia = new InternetAddress(from);
            mm.addRecipient(Message.RecipientType.BCC, ia);

            mm.setSubject(subject);

            // create the message part
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, messageMIME);
            multipart.addBodyPart(messageBodyPart);

            // create the attachment part
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment, attachmentMIME)));
            messageBodyPart.setFileName(attachmentFileName);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            mm.setContent(multipart);

            // Send the message
            Transport.send(mm);

        } catch (AddressException e) {
            LOGGER.error(
                    new StringFormattedMessage("Invalid e-mail address %s for e-mail with subject %s.", to, subject),
                    e);
            throw e;
        } catch (final MessagingException e) {
            LOGGER.error(new StringFormattedMessage("Error while sending e-mail with subject %s to %s.", subject, to),
                    e);
            throw e;
        }

        LOGGER.debug("E-mail with subject %s and attachment %s successfully sent to %s.", subject, attachmentFileName,
                to);
    }

    /**
     * sends an e-mail with an attachment.
     *
     * @param to                 the receiver(s) of the e-mail.
     * @param subject            the subject of the e-mail.
     * @param message            the textual part of the e-mail.
     * @param messageMIME        the MIME media type of the message.
     * @param attachmentUrl      the URL of the attachment to the e-mail.
     * @param attachmentMIME     the MIME media type of the attachment to the e-mail.
     * @param attachmentFileName the file name to assign to the attachment.
     * @throws IOException        if there is any problem in reading the attachment.
     * @throws MessagingException if there is any problem in sending the e-mail.
     */
    public static void sendAttachmentMail(final String to, final String subject, final String message, final String messageMIME, final String attachmentUrl, final String attachmentMIME, final String attachmentFileName) throws
            IOException, MessagingException {
        if (attachmentUrl == null || attachmentUrl.isBlank()) {
            LOGGER.error("URL of the attachment to the email missing.");
            throw new MessagingException("URL of the attachment to the email missing.");
        }
        byte[] attachment = Files.readAllBytes(Paths.get(attachmentUrl));
        sendAttachmentMail(to, subject, message, messageMIME, attachment, attachmentMIME, attachmentFileName);

    }


    /**
     * Loads the configuration for the {@code MailManager}.
     *
     * @return the configuration for the {@code MailManager}.
     */

    private static Properties loadConfiguration() {

        // Get the class loader
        ClassLoader cl = MailManager.class.getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
            LOGGER.debug("Using system class loader.");
        }

        // The properties holding the configuration of the MailManager
        final Properties cfg = new Properties();

        try (InputStream is = cl.getResourceAsStream(CONFIG_FILE)) {

            if (is == null) {
                LOGGER.error("Configuration file %s cannot be opened.", CONFIG_FILE);
                throw new IllegalStateException(String.format("Configuration file %s cannot be opened.", CONFIG_FILE));
            }

            cfg.load(is);

        } catch (IOException ioe) {
            LOGGER.error(new StringFormattedMessage("Configuration file %s cannot be loaded.", CONFIG_FILE), ioe);
            throw new IllegalStateException(String.format("Configuration file %s cannot be loaded.", CONFIG_FILE), ioe);
        }

        return cfg;

    }

    public static void main(String[] args) {
        System.out.println("##  " + MailManager.class.getName());
        try {
            MailManager.sendMail("martinellim45@gmail.com", "Test", "Read this pls", "text/html;charset=UTF-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

