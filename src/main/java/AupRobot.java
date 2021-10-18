import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;

public class AupRobot extends TelegramLongPollingBot {

    private final String DBURL = "jdbc:mysql://mysql.claudiodemarzo.it:3306/AUP", DBUNAME = "AUPBot", DBPASSWORD = "aShH05YHOgS3PqRz";
    private final String[] bannedWords = {"chat.whatsapp.com", "t.me/friendd_it_bot"};
    private ArrayList<String> adminIDs;
    private final Timer fetchAdmins = new Timer(60000, (evt)->{
        try {
            adminIDs = new ArrayList<>();
            Connection sql = DriverManager.getConnection(DBURL, DBUNAME, DBPASSWORD);
            Statement stmt = sql.createStatement();
            ResultSet rs = stmt.executeQuery("select id from AMMINISTRATORI_BOT");
            while(rs.next()){
                adminIDs.add(rs.getString("id"));
            }
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });

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
            if (!isAdmin(userID)) {
                if (checkBannedWords(message)) {
                    try {
                        execute(new DeleteMessage(chatID, messageID));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    //}
                }
            } else {
                String[] arguments = null;
                boolean hasArgs = false;
                if (message.startsWith("/")) {        //is a command
                    /*System.out.println("Command recognized");*/
                    StringTokenizer st = new StringTokenizer(message.substring(1), " ");

                    if (st.countTokens() > 1) {     //args check
                        hasArgs = true;
                        arguments = new String[st.countTokens() - 1];
                    }

                    String command = st.nextToken(); //separating args from command
                    if (hasArgs) {
                        int i = 0;
                        while (st.hasMoreTokens()) {
                            arguments[i] = st.nextToken();
                            i++;
                        }
                    }
                    if (hasArgs) {
                        System.err.println("[DEBUG] Command '" + command + "' recognized. Printing arguments");
                        for (int i = 0; i < arguments.length; i++) System.err.println(arguments[i]);
                    }
                    switch (command.toLowerCase()) {
                        case "mention":
                        case "mention@auprobot":
                            if (chatID.equalsIgnoreCase("-1001739793923"))
                                if (!hasArgs || arguments.length != 1) {
                                    try {
                                        SendMessage sm = new SendMessage();
                                        sm.setChatId(chatID);
                                        sm.setText("Questo comando richiede un parametro. Ecco i parametri Disponibili:\n- DIRETTIVO\n- DMMM\n- DICATECH\n- DICAR\n- DEI\n- COMUNI\n- COORD");
                                        sm.setParseMode(ParseMode.HTML);
                                        sm.setReplyToMessageId(messageID);
                                        execute(sm);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Connection sqlConnection = DriverManager.getConnection(DBURL, DBUNAME, DBPASSWORD);
                                        Statement stmt;
                                        ResultSet rs;
                                        String tmp;
                                        SendMessage sm;
                                        switch (arguments[0].toLowerCase()) {
                                            case "direttivo":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from DIRETTIVO");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "dmmm":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'DMMM'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "dicatech":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'DICATECH'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "dicar":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'DICAR'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "dei":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'DEI'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "comuni":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'COMUNI'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            case "coord":
                                                stmt = sqlConnection.createStatement();
                                                rs = stmt.executeQuery("select * from COORDINATORI where DIPARTIMENTO = 'COORD'");
                                                tmp = "";
                                                while (rs.next()) {
                                                    tmp += rs.getString("USERNAME") + " ";
                                                }
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText(tmp);
                                                sm.setReplyToMessageId(messageID);
                                                sm.setParseMode(ParseMode.HTML);
                                                execute(sm);
                                                break;
                                            default:
                                                sm = new SendMessage();
                                                sm.setChatId(chatID);
                                                sm.setText("Parametro non Valido. Ecco i parametri Disponibili:\n- DIRETTIVO\n- DMMM\n- DICATECH\n- DICAR\n- DEI\n- COMUNI\n- COORD");
                                                sm.setParseMode(ParseMode.HTML);
                                                sm.setReplyToMessageId(messageID);
                                                execute(sm);
                                                break;
                                        }
                                    } catch (SQLException | TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            break;
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

    private boolean isAdmin(String id) {
        for (String s : adminIDs) if (id.equalsIgnoreCase(s)) return true;
        return false;
    }
}
