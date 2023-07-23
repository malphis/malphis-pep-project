package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

    public Message deleteMessage(int messageId) {
        Message messageToDelete = messageDAO.getMessageById(messageId);

        if (messageToDelete != null) {
            boolean isDeleted = messageDAO.deleteMessage(messageId);

            if (isDeleted) {
                return messageToDelete;
            }
        }

        return null;
    }

    public Message updateMessage(int messageId, String newMessageText) {
        return messageDAO.updateMessage(messageId, newMessageText);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
}
