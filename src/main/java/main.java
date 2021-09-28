import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi tbAPI = new TelegramBotsApi(DefaultBotSession.class);
            tbAPI.registerBot(new AupRobot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
