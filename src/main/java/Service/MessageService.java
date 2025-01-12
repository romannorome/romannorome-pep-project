package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
         messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message){
        if(validateMessageText(message.message_text)){
            return messageDAO.createMessage(message);
        }
        else{
            return null;
        }
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String newMessageText){
        if(validateMessageText(newMessageText)){
            return messageDAO.updateMessage(messageId, newMessageText);
        }
        else{
            return null;
        }
    }

    public List<Message> getMessagesByAccountId(int accountId){
        return messageDAO.getMessagesByAccountId(accountId);
    }

    private boolean validateMessageText(String messageText){
        if(messageText.length() > 0 && messageText.length() <= 255){
            return true;
        }
        else{
            return false;
        }
    }
}
