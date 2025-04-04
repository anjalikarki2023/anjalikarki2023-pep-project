package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private final AccountDAO accountDAO;
    public AccountService()
    {
        this.accountDAO=new AccountDAO();

    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account NewregisterHandle(Account account) {
        if (account == null || 
            account.getUsername() == null ||account.getUsername().isBlank()||account.getPassword().isBlank()|| account.getUsername().trim().isEmpty() || 
            account.getPassword() == null || account.getPassword().length() < 4) {
            return null; 
        }
        
        //return null;
        return accountDAO.NewAccount(account);
    }

    public Account UserLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null; 
        }
        Account account = accountDAO.getAccountByUserNameAndPassword(username, password);
        return (account != null && account.getPassword().equals(password)) ? account : null;
    }
    public Account getAccountById(int accountId) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getAccountById(accountId);
    }
}
