package Service;

import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;
    private final AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO =new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message CreateMessage(Message message) {
        if (message == null ||
        message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() ||
        message.getMessage_text().length() > 255) {
       
        return null; 
    }
   
    if (!isUserExists(message.getPosted_by())) {
        return null; 
    }
return messageDAO.CreateMessage(message);


    }
    public boolean isUserExists(int userId){
        return accountDAO.getAccountById(userId) !=null;
    }

    public List<Message> getAllMessage() {
        return messageDAO.getAllMessage();
    }

    public Message getMessageByID(int messageId) {
        return messageDAO.getMessageBYID(messageId);
    }

    public Message DeleteMessage(int messageId) {
        Message message = messageDAO.getMessageBYID(messageId);
        if(message!=null){
            boolean success = messageDAO.DeletedMessage(messageId);
            return success ? message : null;

        }
        return null;
    }

    public Message UpdatedMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null; 
        }
        Message existMessage = messageDAO.getMessageBYID(messageId);
        if (existMessage == null){
            return null;
        }
        return messageDAO.UpdatedMessage(messageId, newMessageText);
    }

    public List<Message> getMessageByUser(int accountID) {
        return messageDAO.getMessageByUser(accountID);
    }
}
