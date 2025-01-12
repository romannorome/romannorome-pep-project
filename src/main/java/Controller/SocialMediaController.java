package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessagesById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);

        return app;
    }

    public void createMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();

        Message message = map.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);

        if(createdMessage != null){
            ctx.json(map.writeValueAsString(createdMessage));
        }
        else{
            ctx.status(400).result("");
        }

    }

    public void getAllMessages(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    public void getMessagesById(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200).result(""); // Return empty response for non-existing message
        }
    }
    
    public void deleteMessageById(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200).result(""); // Return empty response for non-existing message
        }
    }

    public void updateMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessageData = map.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(messageId, updatedMessageData.getMessage_text());

        if(updatedMessage != null){
            ctx.status(200).json(map.writeValueAsString(updatedMessage));
        }
        else{
            ctx.status(400).result("");
        }
    }

    public void getMessagesByAccountId(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId); 
        
        if (messages != null) {
            ctx.status(200).json(messages);
        } else {
            ctx.status(200).json(new ArrayList<>()); 
        }

    }

    public void registerAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();

        Account account= map.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.registerAccount(account);

        if(createdAccount != null){
            ctx.status(200).json(createdAccount);
        }
        else{
            ctx.status(400).result("");
        }

    }

    public void login(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Account loginRequest = map.readValue(ctx.body(), Account.class);

        Account account = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if(account != null){
            ctx.status(200).json(account);
        }
        else{
            ctx.status(401).result("");
        }

    }

}