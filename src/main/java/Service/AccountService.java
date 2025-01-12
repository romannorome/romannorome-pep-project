package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
         accountDAO = new AccountDAO();
    }
    public Account registerAccount(Account account){
        if(validateUsername(account.username) && validatePassword(account.password)){
            return accountDAO.createAccount(account);
        }
        else{
            return null;
        }
    }

    public Account login(String username, String password){
        return accountDAO.validateLogin(username, password);
    }

    private boolean validateUsername(String username){
        if(username.length() > 0 && !(accountDAO.doesUsernameExist(username))){
            return true;
        }
        else{
            return false;
        }

    }

    private boolean validatePassword(String password){
        if(password.length() >= 4){
            return true;
        }
        else{
            return false;
        }
        
    }

}
