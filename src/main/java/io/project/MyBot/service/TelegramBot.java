package io.project.MyBot.service;

import io.project.MyBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public TelegramBot(@Value("${bot.token}") String botToken, BotConfig botConfig) {
        super(botToken);
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId= update.getMessage().getChatId();


            switch (messageText){
                case "/start":
                    starCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Sorry, bro, i don't understand u");
            }
        }
    }

    private void starCommandReceived(long chatId, String firstName){
        String answer = "Hi, "+ firstName + ", nice to meet you!";
        sendMessage(chatId ,answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        }
        catch (TelegramApiException exception){

        }
    }

}
