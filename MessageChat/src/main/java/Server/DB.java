package Server;

import java.sql.*;

public class DB {
    private static Connection connection;

    DB() {
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection==null){
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Server/serverClients.db");
        }
        return connection;
    }

    public static MessageDB chekAuthorization(String Nick, String Pass) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(SQLComand.SQLSelectNickId.getComand());
        preparedStatement.setString(1,"Nick");
        ResultSet resultSet = preparedStatement.executeQuery();


        MessageDB messageDB;
        if (resultSet.next()){
            if (resultSet.getString("Password").equals(Pass)){
                if (resultSet.getBoolean("Connection")){
                    messageDB = new MessageDB("Клиент с таким именем уже подключён", false);
                } else {
                    updateConnection(resultSet.getString("NickID"),1==1);
                    messageDB = new MessageDB("Пароль указан верно", true);
                }
            } else {
                messageDB = new MessageDB("Не верный пароль", false);
            }
        }
        else{
            messageDB = new MessageDB("Нет клиента с таким именем", false);}

        return messageDB;
    }

    public static void updateConnection(String nick, boolean connection) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(SQLComand.SQLUpdateConnection.getComand());
        preparedStatement.setString(2,nick);
        preparedStatement.setBoolean(1,connection);
        preparedStatement.executeUpdate();

    }
    public static MessageDB updateNick(String oldNick, String newNick) throws ClassNotFoundException, SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(SQLComand.SQLUpdateNick.getComand());
        preparedStatement.setString(1,newNick);
        preparedStatement.setString(2,oldNick);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return new MessageDB("Пользователь с таким именем уже существует",false);
        }
        return new MessageDB("Ваше имя изменено на "+newNick,true);



    }

    public static void addClientDB(String nick, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(SQLComand.SQLInsert.getComand());
        preparedStatement.setString(1,nick);
        preparedStatement.setString(2,password);
        preparedStatement.executeUpdate();
    }




}
