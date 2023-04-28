package it.unipd.dei.bitsei.utils.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.File;
import java.io.IOException;

/**
 * BitseiBot class is used to send messages to the user.
 * This class is used to send messages to the user.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class BitseiBot {
    /**
     * A LOGGER available for all the subclasses.
     */
    private static final Logger LOGGER = LogManager.getLogger(BitseiBot.class,
            StringFormatterMessageFactory.INSTANCE);

    TelegramBot bot;
    String botUsername;
    String botToken;

    /**
     * Creates a new BitseiBot.
     */
    public BitseiBot() {
        botUsername = "BITSEI_bot";
        botToken = "6209843098:AAFmLSHj1NH-dpYOIhGu6dCQaFYTqMs8Aqk";
        bot = new TelegramBot(botToken);

        // Register for updates
        bot.setUpdatesListener(updates -> {
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    /**
     * getBotUsername method returns the bot username.
     *
     * @return the bot username
     */
    public String getBotUsername() {
        return "BITSEI_bot";
    }

    /**
     * getBotToken method returns the bot token.
     *
     * @return the bot token
     */
    //TODO: probably to set private or protected
    public String getBotToken() {
        return "6209843098:AAFmLSHj1NH-dpYOIhGu6dCQaFYTqMs8Aqk";
    }

    /**
     * sendMessage method sends a message to the user.
     *
     * @param chatId the chat id of the user
     * @param body   the body of the message
     * @return true if the message has been sent, false otherwise
     */
    public boolean sendMessage(final String chatId, final String body) {
        // Send messages
        //long chatId = update.message().chat().id();
        SendMessage request = new SendMessage(chatId, body);

        // sync
        SendResponse response = bot.execute(request);
        boolean ok = response.isOk();
        Message message = response.message();
        LOGGER.info("## BitseiBot: sendMessageSync responded: " + message + " ##");
        return ok;
    }

    /**
     * sendMessageAsync method sends a message to the user asynchronously.
     *
     * @param chatId the chat id of the user
     * @param body   the body of the message
     */
    public void sendMessageAsync(final String chatId, final String body) { //TODO
        SendMessage request = new SendMessage(chatId, body);
        // async
        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {

            }

            @Override
            public void onFailure(SendMessage request, IOException e) {

            }
        });
    }

    /**
     * sendMessageWithAttachments method sends a message with attachments to the user.
     *
     * @param chatId     the chat id of the user
     * @param body       the body of the message
     * @param attachment the attachment to be sent (byte array)
     * @return true if the message has been sent, false otherwise
     */
    public boolean sendMessageWithAttachments(String chatId, String body, final byte[] attachment) {
        SendDocument request = new SendDocument(chatId, attachment).caption(body);
        // sync
        SendResponse response = bot.execute(request);
        boolean ok = response.isOk();
        Message message = response.message();
        LOGGER.info("## BitseiBot: sendMessageWithAttachments responded: " + message + " ##");
        return ok;
    }

    /**
     * sendMessageWithAttachments method sends a message with attachments to the user.
     *
     * @param chatId        the chat id of the user
     * @param body          the body of the message
     * @param attachmentUrl the attachment to be sent (url)
     * @return true if the message has been sent, false otherwise
     */
    public boolean sendMessageWithAttachments(String chatId, String body, final String attachmentUrl) {
        File attachment = new File(attachmentUrl);
        SendDocument request = new SendDocument(chatId, attachment).caption(body);
        // sync
        SendResponse response = bot.execute(request);
        boolean ok = response.isOk();
        Message message = response.message();
        LOGGER.info("## BitseiBot: sendMessageWithAttachments responded: " + message + " ##");
        return ok;
    }

}