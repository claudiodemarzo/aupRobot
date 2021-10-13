import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AupRobot extends TelegramLongPollingBot {

    private final String[] bannedWords = {"chat.whatsapp.com"};
    private final String[] adminIDs = {"205308699", "399858060", "211257255", "1083185376", "120165672", "852402119", "869495414", "1279505788", "183350229", "456967399", "1240827364", "345762113", "1625643261", "1598042032", "534668906", "874311009"};
    private HashMap<String, String> macro;
    private final File macro_pointer = new File("demacro.claudio.json");

    public AupRobot(){
        if(!macro_pointer.exists()){
            try {
                macro_pointer.createNewFile();
                macro = new HashMap<>();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(macro_pointer));
                macro = (HashMap<String, String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

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
            }else{
                if(message.startsWith(".")||message.startsWith("!")){
                    StringTokenizer st = new StringTokenizer(message.substring(1));
                    String command = st.nextToken();
                    boolean hasArgs = st.countTokens() != 1;
                    String[] args = new String[st.countTokens()-1];
                    if(hasArgs){
                        for(int i = 0; i< args.length; i++) args[i] = st.nextToken();
                    }
                    if(macro.containsKey(command)) {
                        try {
                            execute(new SendMessage(chatID, macro.get(command)));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }else{
                        switch(command.toLowerCase()){
                            case "newmacro":
                                if(hasArgs && args.length == 2){
                                    macro.put(args[0], args[1]);
                                    writeFile();
                                    try {
                                        execute(new SendMessage(chatID, "Macro Saved"));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                        }
                    }
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

    private void writeFile(){
        try {
            ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(macro_pointer, false));
            fos.writeObject(macro);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
