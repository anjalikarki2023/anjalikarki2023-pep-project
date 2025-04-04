package Controller;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
//import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import Util.ConnectionUtil;
import DAO.AccountDAO;
import DAO.MessageDAO;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
   private final MessageService messageService;
   public void SetUp() throws InterruptedException{
    ConnectionUtil.resetTestDatabase();
    //SocialMediaController() =socialMediaController.startAPI();
   //webClient =HttpClient.newHttpClient();
   }

    public SocialMediaController() {
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
      // System.out.println("Loading!!!!!");
        app.post("/register", this::NewregisterHandle);
        app.post("/login", this::UserLoginHandle);
        app.post("/messages", this::CreateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::DeleteMessage);
        app.patch("/messages/{message_id}", this::UpdatedMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void NewregisterHandle(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();  
            Account account = objectMapper.readValue(context.body(), Account.class);
    
            Account registeredAccount = accountService.NewregisterHandle(account);
    
            if (registeredAccount == null) {
                context.status(400).result(""); 
            } else {
                context.status(200).json(registeredAccount); 
            }
        } catch (Exception e) {
            context.status(400).result(""); 
        }
          
    }
      private void UserLoginHandle(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(context.body(), Account.class);

            Account loginAccount = accountService.UserLogin(account.getUsername(), account.getPassword());

            if (loginAccount != null) {
                context.status(200).json(loginAccount);
            } else {
                context.status(401).result("");
            }
        } catch (Exception e) {
            context.status(400).result("");
        }
      
    }

    private void CreateMessage(Context context) throws JsonProcessingException{
      
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Message message = objectMapper.readValue(context.body(), Message.class);
            Message createdMessage = messageService.CreateMessage(message);

            if (createdMessage != null) {
                context.status(200).json(createdMessage);
            } else {
                context.status(400).result("");
            }
        } catch (Exception e) {
            context.status(400);
        }
    }

    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessage();
        context.status(200).json(messages);
    }

    private void getMessageByID(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageByID(messageId);

            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(200);
            }
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID format.");
        }
    }

    private void DeleteMessage(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message deleted = messageService.DeleteMessage(messageId);

            if (deleted !=null) {
                context.status(200).json(deleted);
            } else {
                context.status(200).result("");
            }
        } catch (NumberFormatException e) {
            context.status(400);
        }
    }

    private void UpdatedMessage(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = objectMapper.readValue(context.body(), Message.class);
            
            Message updatedMessage = messageService.UpdatedMessage(messageId, message.getMessage_text());

            if (updatedMessage != null) {
                context.status(200).json(updatedMessage);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
            context.status(400).json("Invalid request format.");
        }
    }

    private void getMessagesByUser(Context context) {
        
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getMessageByUser(accountId);
            context.json(messages);
        } 
    
}

