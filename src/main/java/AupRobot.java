import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class AupRobot extends TelegramLongPollingBot {

    private final String[] bannedWords = {"chat.whatsapp.com"};

    @Override
    public String getBotUsername() {
        return "aupRobot";
    }

    @Override
    public String getBotToken() {
        try {
            Scanner s = new Scanner(new File("token.txt"));
            String token = s.nextLine();
            if(token.isEmpty()){
                System.err.println("Errore durante la lettura del token");
                System.exit(-1);
            }
            return token;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            String message = update.getMessage().getText();
            int messageID = update.getMessage().getMessageId();
            long chatID = update.getMessage().getChatId();
            if(checkBannedWords(message)){
                try {
                    execute(new DeleteMessage(""+chatID, messageID));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkBannedWords(String message) {
        for(String s : bannedWords){
            if(message.toLowerCase().contains(s.toLowerCase())) return true;
        }
        return false;
    }
}
