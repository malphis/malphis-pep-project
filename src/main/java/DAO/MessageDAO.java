package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1);
                    message.setMessage_id(messageId);
                    return message;
                }
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public boolean deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message updateMessage(int messageId, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                String selectSql = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, messageId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int messageIdFromDB = resultSet.getInt("message_id");
                    int postedByFromDB = resultSet.getInt("posted_by");
                    String messageTextFromDB = resultSet.getString("message_text");
                    long timePostedEpochFromDB = resultSet.getLong("time_posted_epoch");

                    return new Message(messageIdFromDB, postedByFromDB, messageTextFromDB, timePostedEpochFromDB);
                }
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
