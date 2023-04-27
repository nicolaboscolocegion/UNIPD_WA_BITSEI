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

public class BitseiBot {
    /**
     * A LOGGER available for all the subclasses.
     */
    private static final Logger LOGGER = LogManager.getLogger(BitseiBot.class,
            StringFormatterMessageFactory.INSTANCE);

    TelegramBot bot;
    String botUsername;
    String botToken;

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
    public String getBotUsername() {
        return "BITSEI_bot";
    }

    //TODO: probably to set private or protected
    public String getBotToken() {
        return "6209843098:AAFmLSHj1NH-dpYOIhGu6dCQaFYTqMs8Aqk";
    }

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

    public boolean sendMessageWithAttachments(String chatId, String body, final byte[] attachment) {
        SendDocument request = new SendDocument(chatId, attachment).caption(body);
        // sync
        SendResponse response = bot.execute(request);
        boolean ok = response.isOk();
        Message message = response.message();
        LOGGER.info("## BitseiBot: sendMessageWithAttachments responded: " + message + " ##");
        return ok;
    }

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