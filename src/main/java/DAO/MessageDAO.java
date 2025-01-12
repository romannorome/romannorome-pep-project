package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));  
                }
                return message;

            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message";

            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

                return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, messageId);

            ResultSet rs = selectPs.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

                return message;
            }

            PreparedStatement deletePs = connection.prepareStatement(deleteSql);
            deletePs.setInt(1, messageId);

            deletePs.executeUpdate();


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByAccountId(int accountId){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                messages.add(message);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message updateMessage(int messageId, String newMessageText){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            String selectSql = "SELECT * FROM message WHERE message_id = ?";

            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, messageId);

            PreparedStatement updatePs = connection.prepareStatement(updateSql);
            updatePs.setString(1, newMessageText);
            updatePs.setInt(2, messageId);
            
            updatePs.executeUpdate();

            ResultSet rs = selectPs.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

                return message;
            }



        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
