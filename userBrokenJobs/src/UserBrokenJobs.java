import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserBrokenJobs {

    private static String mailFrom;
    private static String[] mailTo;
    private static String mailPass;

    private static String warningJobs = "";

    public static void main(String[] args) throws Exception {

        searchFreeSpace(args[0],args[1],args[2],args[3],args[4]);

        if (warningJobs.length() > 1){
            mailFrom = args[5];
            mailPass = args[6];
            mailTo = new String[args.length-7];
            for (int i = 0; i < args.length-7; i++){
                mailTo[i] = args[i+7];
            }
        }

        sendMail("Внимание! Обнаружены ошибки в базе "+ args[2]+"!","\n" + warningJobs);

    }

    public static void searchFreeSpace(String BaseIp, String basePort, String baseName, String baseUser, String basePass) throws Exception {
        Connection oraCon = OracleConnection.initConnect(BaseIp, basePort, baseName, baseUser, basePass);
        try {
            Statement statement = oraCon.createStatement();
            ResultSet cFreeSpace = statement.executeQuery("" +
                    "select t.rw\n" +
                    "  from LIST_WARNING_DATABASE t");
            while (cFreeSpace.next()) {
                    warningJobs += cFreeSpace.getString("rw") + "\n";
            }
            cFreeSpace.close();
            oraCon.close();
        }
        catch (Exception ex) {
            //выводим наиболее значимые сообщения
            Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if (oraCon != null) {
                try {
                    oraCon.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void sendMail (String subject, String msg) throws Exception{

        Mail mail = new Mail(mailFrom, mailPass);
        mail.setHostPostSport("smtp.yandex.ru","465","465");
        mail.setFrom(mailFrom);
        String[] to  = new String[1];;
        to = mailTo;
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setBody(msg);
        mail.send();

    }




}
