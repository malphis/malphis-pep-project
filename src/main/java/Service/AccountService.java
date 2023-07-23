package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account createAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            return null;
        }

        // Check if the username already exists
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);

    }
}
