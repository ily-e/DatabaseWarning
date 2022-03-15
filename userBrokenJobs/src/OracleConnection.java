import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OracleConnection {

    private static Connection connect;
    private static String baseIp;
    private static String basePort;
    private static String baseName;
    private static String baseUser;
    private static String basePass;


    private OracleConnection() {}

    private static void initVar(String ibaseIp,
                         String ibasePort,
                         String ibaseName,
                         String ibaseUser,
                         String ibasePass){
        baseIp = ibaseIp;
        basePort = ibasePort;
        baseName = ibaseName;
        baseUser = ibaseUser;
        basePass = ibasePass;
    }


    private static void initOracleConnection(){

        String url = "jdbc:oracle:thin:@"+baseIp+":"+basePort+":"+baseName;
        String name = baseUser;
        String password = basePass;
        try {

            if (connect != null){
                connect.close();
            }

            Class.forName("oracle.jdbc.OracleDriver");
            //System.out.println("Драйвер подключен");
            //Создаём соединение
            connect = DriverManager.getConnection(url, name, password);
            //System.out.println("Соединение установлено");

        }
        catch (Exception ex) {
            //выводим наиболее значимые сообщения
            Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    public static Connection initConnect(String baseIp,
                                         String basePort,
                                         String baseName,
                                         String baseUser,
                                         String basePass){
        if (connect == null){
            initVar(baseIp,
                    basePort,
                    baseName,
                    baseUser,
                    basePass);
            initOracleConnection();

        }
        return connect;
    }

    public static Connection getConnect() throws Exception {
        if (connect == null){
            throw new Exception("Соединение отстутствует.");
        }
        return connect;
    }


}
