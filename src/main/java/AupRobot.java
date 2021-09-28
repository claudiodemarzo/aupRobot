import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class AupRobot extends TelegramLongPollingBot {

    private final String[] bannedWords = {"chat.whatsapp.com"};
    private final String[] adminIDs = {"205308699", "399858060", "211257255", "1083185376", "120165672", "852402119", "869495414", "1279505788", "183350229", "456967399", "1240827364", "345762113", "1625643261", "1598042032", "534668906", "874311009"};

    @Override
    public String getBotUsername() {
        return "aupRobot";
    }

    @Override
    public String getBotToken() {
        try {
            Scanner s = new Scanner(new File("token.txt"));
            String token = s.nextLine();
            if (token.isEmpty()) {
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
        System.out.println(update.toString());
        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            int messageID = update.getMessage().getMessageId();
            String chatID = "" + update.getMessage().getChatId();
            String userID = "" + update.getMessage().getFrom().getId();
            if(!isAdmin(userID)) {
                if (checkBannedWords(message)) {
                    try {
                        execute(new DeleteMessage(chatID, messageID));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    //}
                }
            }
        }
    }

    private boolean checkBannedWords(String message) {
        for (String s : bannedWords) {
            if (message.toLowerCase().contains(s.toLowerCase())) return true;
        }
        return false;
    }
    private boolean isAdmin(String id){
        for (String s : adminIDs) if(id.equalsIgnoreCase(s)) return true;
        return false;
    }
}
