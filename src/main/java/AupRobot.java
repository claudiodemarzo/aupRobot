import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AupRobot extends TelegramLongPollingBot {

    /*private final String DBURL = "jdbc:mariadb://aup.it:3306/nusgudpi_auprobot", DBUNAME = "nusgudpi_auprobot", DBPASSWORD = "taMUD-}EE_s=";
    */private final String[] bannedWords = {"chat.whatsapp.com", "t.me/friendd_it_bot", "#vivilocosi", "#vivilocosì", "vivilocosi", "vivilocosì"};
    /*private ArrayList<String> adminIDs = new ArrayList<>();
    private Connection sqlConnection;*/

    /*{
        try {
            sqlConnection = DriverManager.getConnection(DBURL, DBUNAME, DBPASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /*private final Runnable fetchAdmins = ()->{
        try {
            adminIDs = new ArrayList<>();
            Statement stmt = sqlConnection.createStatement();
            ResultSet rs = stmt.executeQuery("select id from AMMINISTRATORI_BOT");
            while(rs.next()){
                adminIDs.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };
    private boolean started = false;*/

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
        if (update.hasMessage() && update.getMessage() != null) {
            //fetchAdmins.run();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(true);
            String date = sdf.format(gc.getTime());
            String message = update.getMessage().getText();
            int messageID = update.getMessage().getMessageId();
            String chatID = "" + update.getMessage().getChatId();
            String userID = "" + update.getMessage().getFrom().getId();
            Date messageDate = new Date((long)update.getMessage().getDate()*1000);
            System.out.println("Processed @ ["+date+"] Received @ ["+sdf.format(messageDate)+"] Processing message: "+update.getMessage().getFrom().getFirstName() + " "+update.getMessage().getFrom().getLastName() + "@"+update.getMessage().getChat().getTitle()+" : "+update.getMessage().getText());
            /*if (!isAdmin(userID)) {*/
                if (checkBannedWords(message)) {
                    try {
                        execute(new DeleteMessage(chatID, messageID));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    //}
                }
            /*} else {
                String[] arguments = null;
                boolean hasArgs = false;
                if (message.startsWith("/")) {        //is a command
                    /*System.out.println("Command recognized");
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
                    /*switch (command.toLowerCase()) {
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
                        case "addadmin":
                        case "addadmin@auprobot":
                            if (update.getMessage().isReply()) {
                                try {
                                    String replyUserID = update.getMessage().getReplyToMessage().getFrom().getId().toString();
                                    System.err.println(replyUserID);
                                    Statement stmt = sqlConnection.createStatement();
                                    ResultSet rs = stmt.executeQuery("select * from AMMINISTRATORI_BOT where id = '" + replyUserID + "'");
                                    boolean found = false;
                                    while (rs.next()) {
                                        if (rs.getString("id").equalsIgnoreCase(replyUserID)) found = true;
                                    }
                                    if (found) {
                                        SendMessage sd = new SendMessage();
                                        sd.setChatId(chatID);
                                        sd.setText("Questo utente è già amministratore!");
                                        sd.setReplyToMessageId(messageID);
                                        sd.setParseMode(ParseMode.HTML);
                                        execute(sd);
                                    } else {
                                        stmt.executeUpdate("insert into AMMINISTRATORI_BOT values ('" + replyUserID + "')");
                                        SendMessage sd = new SendMessage();
                                        sd.setChatId(chatID);
                                        sd.setText("Amministratore aggiunto!");
                                        sd.setReplyToMessageId(messageID);
                                        sd.setParseMode(ParseMode.HTML);
                                        execute(sd);
                                    }
                                } catch (SQLException | TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }else{
                                System.err.println("Not a reply" +
                                        "");
                            }
                    }
                }
            }*/
        }
    }

    private boolean checkBannedWords(String message) {
        for (String s : bannedWords) {
            if (message.toLowerCase().contains(s.toLowerCase())) return true;
        }
        return false;
    }

    /*private boolean isAdmin(String id) {
        for (String s : adminIDs) if (id.equalsIgnoreCase(s)) return true;
        return false;
    }*/
}