package DAO;

import Model.Account;
import Util.ConnectionUtil;



import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.sql.*;

public class AccountDAO {

    public AccountDAO(){}


    public Account NewAccount (Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{

            String sql = "INSERT INTO Account (username, password) VALUES (?,?)";
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(account.getUsername() != ""){
                preparedStatement.setString(1, account.getUsername());
    
            preparedStatement.setString(2, account.getPassword());
    
            int affectedRows = preparedStatement.executeUpdate();
    
            if (affectedRows>0){
    
          ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
    
          if(generatedKeys.next()){
    
            account.setAccount_id(generatedKeys.getInt(1));
    
    return account;
          }
           
            }
        }
        }
                
                catch(SQLException e){
    
                    System.out.println ("Cannot register: " + e.getMessage());
    }
    return null;
}

    public Account getAccountByUserNameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() && username != "") {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Cannot Find Account: " + e.getMessage());
        }
        return null;
    }
    public boolean userExist(String Username)throws SQLException{
        Connection connection = ConnectionUtil.getConnection();
         String sql = "SELECT * FROM account WHERE username =?";
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1,Username);
         ResultSet rs = preparedStatement.executeQuery();
         return rs.next();

    }
    public Account getAccountById (int accountId){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Cannot find account by ID: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
        return null;
    
    }
}