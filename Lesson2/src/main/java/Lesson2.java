import org.sqlite.jdbc4.JDBC4Connection;

import java.sql.*;

public class Lesson2 {
    public static void main(String[] args) throws ClassNotFoundException {
        try(Connection connection = getConnection("Lesson2/myClients.db")) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLComand.SQLSelectNickId.getComand());
            preparedStatement.setString(1,"Admin111");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.printf("%d | %10s | %s%n",resultSet.getInt("ID"),resultSet.getString("NickID"),resultSet.getString("Password"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

   public static Connection getConnection(String path) throws ClassNotFoundException, SQLException {
       Class.forName("org.sqlite.JDBC");
       return DriverManager.getConnection("jdbc:sqlite:"+path);
   }
   public static void addClient(Connection connection,String Nick, String PassCod) throws SQLException {
       PreparedStatement preparedStatement = connection.prepareStatement(SQLComand.SQLInsert.getComand());
       preparedStatement.setString(1,Nick);
       preparedStatement.setString(2,PassCod);
       preparedStatement.executeUpdate();
   }

   public static void selectNickId(Connection connection, String Nick){}


    public static void getConnection1() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:Lesson2/myClients.db");
        Statement statement = connection.createStatement();
        String SQLDelete = "delete from 'users' where NickID = ?";
        String SQLInsert = " insert into 'users'('NickID','Password') values (?,?)";
        String SQLUpdateConnection = "update 'users' set Connection = ? where NickID = ?";
        String SQLUpdatePassword = "update 'users' set Password = ? where NickID = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(SQLInsert);
        preparedStatement.setString(1,"Admin");
        preparedStatement.setString(2,"test");
        preparedStatement.setBoolean(3,1>0);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        
    }
}
