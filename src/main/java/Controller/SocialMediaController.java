package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getOneMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context context) {
        AccountService accountService = new AccountService();
        Account account = context.bodyAsClass(Account.class);
        
        //Check if Username not blank and password >= 4 char (400)
        if (account.getUsername() == null || 
            account.getPassword() == null || account.getPassword().length() < 4) {
            context.status(400).json("");
            return;
        }

        //Check if username already exists (400)
        Account existingAccount = accountService.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            context.status(400).json("");
            return;
        }

        //Create Account (200)
        Account createdAccount = accountService.createAccount(account);
        if (createdAccount != null) {
            context.status(200).json(createdAccount);
        }
        else {
            context.status(400).json("");
        }


    }

    private void loginHandler(Context context) {
        AccountService accountService = new AccountService();
        Account account = context.bodyAsClass(Account.class);

        Account existingAccount = accountService.getAccountByUsername(account.getUsername());

        //Check for account and password |Fail/Unauthorized (401) Success (200)
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            context.status(401).json("");
            return;
        }

        context.status(200).json(existingAccount);
    }
 
    private void createMessageHandler(Context context) {
        MessageService messageService = new MessageService();
        Message message = context.bodyAsClass(Message.class);

        //Text not null, under 255 char, posted by existing user|Client Error (400) Success (200)
        if (message.getMessage_text() == null || 
            message.getMessage_text().length() > 254) {
            context.status(400).json("");
            return;
        }

        AccountService accountService = new AccountService();
        Account postedByAccount = accountService.getAccountById(message.getPosted_by());
        if (postedByAccount == null) {
            context.status(400).json("");
            return;
        }

        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            context.status(200).json(createdMessage);
        }
        else {
            context.status(400).json("");
        }

    }

    private void getAllMessagesHandler(Context context) {
        MessageService messageService = new MessageService();
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);

    }

    private void getOneMessageHandler(Context context) {
        MessageService messageService = new MessageService();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.status(200).json(message);
        }
        else {
            context.status(200).json("");
        }
    }

    private void deleteMessageHandler(Context context) {
        MessageService messageService = new MessageService();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);

        if (deletedMessage != null) {
            context.status(200).json(deletedMessage);
        } else {
            context.status(200).json("");
        }

    }

    private void updateMessageHandler (Context context) {
        MessageService messageService = new MessageService();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message messageToUpdate = messageService.getMessageById(messageId);

        if (messageToUpdate == null) {
            context.status(400).json("");
            return;
        }

        Message updatedMessage = context.bodyAsClass(Message.class);

        //check if blank or over 255 char
        if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().trim().isEmpty() ||
            updatedMessage.getMessage_text().length() > 254) {
            context.status(400).json("");
            return;
        }

        messageToUpdate.setMessage_text(updatedMessage.getMessage_text());

        Message savedMessage = messageService.updateMessage(messageId, messageToUpdate.getMessage_text());

        if (savedMessage != null) {
            context.status(200).json(savedMessage);
        } 
        else {
            context.status(400).json("");
        }

    }

    private void getAllMessagesFromUserHandler(Context context) {
        MessageService messageService = new MessageService();
        int accountId = Integer.parseInt (context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);

        context.status(200).json(messages);
    }

}