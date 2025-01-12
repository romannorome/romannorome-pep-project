package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO{

    public boolean doesUsernameExist(String username){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT COUNT(*) FROM account WHERE username=?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(1) > 0;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));  
                }
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account validateLogin(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT username, password, account_id FROM account WHERE username = ? AND password = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){ 
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password")
                );
            }

           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}