package com.example.activityfxml.Database;

import com.example.activityfxml.DashboardController;
import com.example.activityfxml.Entities.Note;
import com.example.activityfxml.Entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228fxml";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() {
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            String query = """
                CREATE TABLE IF NOT EXISTS users(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    password VARCHAR(50) NOT NULL,
                    firstname VARCHAR(50) NOT NULL,
                    lastname VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    constraint unique_username UNIQUE(username),
                    constraint unique_email UNIQUE(email)
                );
                """;
            statement.execute(query);

            query = """
                CREATE TABLE IF NOT EXISTS notes(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    userid INT NOT NULL,
                    note VARCHAR(1024) NOT NULL,
                    
                    createdAt TIMESTAMP DEFAULT NOW(),
                    updatedAt TIMESTAMP NULL,
                    
                    FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE
                );
                """;
            statement.execute(query);

            System.out.println("Connected to database");
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }

    public static void register(String username, String password, String firstname, String lastname, String email) throws SQLException{
        String query = """
                INSERT INTO users (
                    username,
                    password,
                    firstname,
                    lastname,
                    email)
                    VALUES (?, ?, ?, ?, ?)
                """;
        try(
            Connection c = getConnection();
            PreparedStatement statement = c.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS);)
        {

        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, firstname);
        statement.setString(4, lastname);
        statement.setString(5, email);
        statement.executeUpdate();
        }
    }

    public static Boolean login(String username, String password) throws SQLException {
        try (Connection c = getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next())return false;

            User.id = resultSet.getInt("id");
            User.firstname = resultSet.getString("firstname");
            User.lastname = resultSet.getString("lastname");
            User.email = resultSet.getString("email");
            User.username = resultSet.getString("username");
            return true;
        }
    }

    public static int updateProfile(String firstname, String lastname, String username, String email) throws SQLException {
        HashMap<String, String> update = new HashMap<>();
        if(!firstname.trim().isEmpty() && !firstname.equals(User.firstname)){
            update.put("firstname",firstname);
        }
        if(!lastname.trim().isEmpty() && !lastname.equals(User.lastname)){
            update.put("lastname", lastname);
        }
        if(!username.trim().isEmpty() && !username.equals(User.username)){
            update.put("username", username);
        }
        if(!email.trim().isEmpty() && !email.equals(User.email)){
            update.put("email",email);
        }

        if(update.isEmpty())return 0;

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE users SET ");

        for (HashMap.Entry<String, String> entry : update.entrySet()) {
            String key = entry.getKey();
            sb.append(key).append(" = ?, ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(" WHERE id = ?");

        Connection c = getConnection();

        PreparedStatement statement = c.prepareStatement(sb.toString());

        int index = 1;
        for (HashMap.Entry<String, String> entry : update.entrySet()) {
            String value = entry.getValue();
            statement.setString(index++, value);
        }
        statement.setInt(index, User.id);
        return statement.executeUpdate();
    }

    public static void deleteAccount() throws SQLException{
        try(Connection c = getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );
        ) {
            statement.setInt(1, User.id);
            statement.executeUpdate();
            User.id = null;
            User.firstname = null;
            User.lastname = null;
            User.username = null;
            User.email = null;
        }
    }

    public static void getNotes() throws SQLException{
        try(
            Connection c = getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT * FROM notes WHERE userid = ?"
            );)
        {
            statement.setInt(1, User.id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                DashboardController.notes.add(new Note(resultSet.getInt("id"), resultSet.getInt("userid"), resultSet.getString("note"), resultSet.getString("createdat"), resultSet.getString("updatedat")));
            }
        }
    }

    public static void createNote(String note) throws  SQLException{
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement(
                "INSERT INTO notes (userid, note) VALUES (?, ?)"
        );

        statement.setInt(1, User.id);
        statement.setString(2, note);
        statement.executeUpdate();
    }

    public static void deleteNote(int id) throws SQLException {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement(
                "DELETE FROM notes WHERE id = ?"
        );

        statement.setInt(1, id);
        statement.executeUpdate();
    }

    public static void updateNote(Note note, String newNote) throws SQLException{
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement(
                "UPDATE notes SET note = ? WHERE id = ?"
        );

        statement.setString(1, newNote);
        statement.setInt(2, note.id);
        statement.executeUpdate();
    }



    public static void main(String[] args) {
        getConnection();
    }
}