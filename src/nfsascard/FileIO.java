package nfsascard;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class FileIO {

    private Card card;
    private FileWriter saveCard;
    private FileReader printCard;
    private JSONObject obj;
    private JSONParser parser;
    private String fileName = "C:\\Users\\Maddox\\IdeaProjects\\NSFASCard\\src\\nfsascard\\cards.json";


    // Method To Find The Card, Makes It Easier To Perform Tasks On The A Card
    private  boolean findCard(JSONArray objectArray,long cardNumber){

        boolean found = false;
        this.obj = new JSONObject();

        for(int i = 0; i < objectArray.size(); i ++){

            this.obj = (JSONObject) objectArray.get(i); // Holds The Card On Current Iteration

            if((long) obj.get("id") == (long) cardNumber){
                
                // Card Found On The System
                
                found = true;
                break;

            }else{
                
                // Card Was Not Found On The System
                
                found = false;

            }

        }
        return found;
    } // End Of findCard()
    
    /*
    
      This Method Has A Bug
      Check getLastCardNumber-Patch Branch For The Bug Fix
      
    */
     /*
        
          To Generate Unique Card Numbers For Each And Every Card That Is Created
          
          Must First Check For Highest Card Number From All Cards In The System
          
          Then Increment That Highest Card Number To Be The Next Card Number For The NExt Card Created
          
          
        
        */

    public long getLastCardNumber(){

        long cardNumber = 20191000; // Default Card Number If There Are No Cards In The System, i.e When The First Card Is Created

        try {

            this.parser = new JSONParser();
            this.printCard = new FileReader(this.fileName);

            JSONArray cardList = (JSONArray) this.parser.parse(this.printCard);
            
            // Check If There Are Crds In The System
               if(cardList.isEmpty()){
                   
                   // No Cards In The System

                   return cardNumber; // Return The Default Card Number

               }else{
                   
                   // There Are Cards In The System

                   JSONObject lastCard = (JSONObject) cardList.get(cardList.size() - 1); // To Get The Last Json Object

                   cardNumber = (long) lastCard.get("id");

                   return cardNumber;

               }

        }catch (IOException io){

            io.printStackTrace();

        }catch (ParseException pe){

            pe.printStackTrace();

        }
        return cardNumber;
    }


    // Method To Keep Track Of Each And Every Transaction Happening Concerning A Card

    private void trackTransactions(JSONObject object, String type, double funds){

        JSONArray transactionList;
        JSONObject transactions = new JSONObject();

        if(object.containsKey("transactions")){

            transactionList = (JSONArray) object.get("transactions");

        }else{

            transactionList = new JSONArray();

            object.put("transactions", transactionList);

        }

        LocalDateTime DateTime = LocalDateTime.now();

        String transactionTime = DateTime.format(DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss"));

        transactions.put("transaction_type", type);
        transactions.put("funds", funds);
        transactions.put("date_time", transactionTime);

        transactionList.add(transactions);


    } // trackTransactions()
    
    /*
      
      saveTransactions() Method is used To Save every transaction associated with a card to file
      
      It Handles Both Deposits & Withdrawals
    
    */

    public void saveTransactions(int Context, double funds, long cardNumber){

        /*
        * Context : gives context on which type transaction is happening
        *
        *    Context = 1 : Depositing
        *    Context = 2 : Withdrawing
        *
        * Funds : The Amount Of Money Concerning The Transaction That Is Taking Place
        *
        * CardNumber : Used To Find The Card Amongst Other Cards
        *
        * */

        try {

            this.parser = new JSONParser();

            this.printCard = new FileReader(this.fileName);

            JSONArray dataList = (JSONArray) this.parser.parse(this.printCard); // Save The File Contents Into A JSONArray Object

            // Check If The Card Number Matches To A Card That Has Already Been Created By Calling The findCard Method
              if(this.findCard(dataList, cardNumber)){ 
                  
                  // Card Was Found

                  dataList.remove(this.obj); // Remove The Card From System To Make Changes To The Card

                  double balance = 0.0;
                  String type = "";

                  if(Context == 1){ // For Depositing

                       balance = (double) this.obj.get("balance") + funds; // Increment The Current Amount With The Funds That Were Just Entered By The User
                       type = "Deposit";

                  }else if(Context == 2){ // For Withdrawing

                      // Check If There Are Enough Funds To Withdraw
                      if((double) this.obj.get("balance") - funds > 0){
                          
                          // There Are Suffiecient Funds On The Card

                          balance = (double) this.obj.get("balance") - funds; // Decrement The Current Amount With The Funds That Were Just Entered By User
                          type = "Withdrawal";

                      }else{
                          
                          // There Are Insufficient Funds On The Card 

                          System.out.println("#### ---- Insufficient Funds, Unable To Perform Transaction ---- ####");
                          return;

                      } // End Of Funds If

                  } // End Of Context If
                  
                  
                  // Using The Card Saved In this.obj, Make Changes To It
                  
                  this.obj.replace("balance", balance);

                  this.trackTransactions(this.obj, type, funds);
                  
                  // Save The Updated Card To All Cards
                  
                  dataList.add(this.obj);
                  
                  // Saves Card To File
                  
                  this.saveCard = new FileWriter(this.fileName);

                  this.saveCard.write(dataList.toJSONString());
                  this.saveCard.flush();

                  System.out.println(type + " : R" + funds);


              }else{
                  
                  // Card Was Not Found

                  System.out.println("\n ##### ---- Could Not Find Card Using The Card Id, Deposit Unsuccessful ---- #####");

              } // End Of Card Check If

        }catch (FileNotFoundException fn){

            fn.printStackTrace();

        }catch (IOException io){

            io.printStackTrace();

        }catch (ParseException pe){

            pe.printStackTrace();

        }

    }

    // Method To Read Card
    public void statement(long cardNumber){

        /*
        * Data In File Is In Json Array,
        * To Print A Certain's Card Details
        * We Supply The CardNumber Then Loop Through The Objects Until A CardNumber Match Is True
        */
        try {

            this.parser = new JSONParser();

            this.printCard = new FileReader(this.fileName);

            JSONArray dataList = (JSONArray) this.parser.parse(this.printCard);

               if(this.findCard(dataList, cardNumber)){ // findCard Returns True;

                   System.out.println("\n        ****************************\n        *        Statement         *\n        ****************************");

                   System.out.println("Card Owner        : " + this.obj.get("name"));
                   System.out.println("Available Balance : " + this.obj.get("balance"));
                   System.out.println("Card Id           : " + this.obj.get("id"));
                   System.out.println("\n        ****************************\n        *        Transactions      *\n        ****************************");

                   // Check If Card Has Already Made Transactions
                     if(this.obj.containsKey("transactions")){ // Already Has Made Transactions!

                         /*
                         * Since My Transactions Are In An Json Array
                         * Loop Through The Json Array To Extract
                         * */
                         JSONArray transList = (JSONArray) this.obj.get("transactions");

                         System.out.println("Transaction Type  | Transaction Amount  | Transaction Date Time");

                           transList.forEach(transaction -> {

                               JSONObject object = (JSONObject) transaction;

                               System.out.println(object.get("transaction_type") + " \t\t  | R" + object.get("funds").toString().trim() + " \t\t\t    |  " + object.get("date_time"));


                           });

                     }else{ // No Transactions

                         System.out.println("\nNo Transactions To Show, Either **" + this.obj.get("name") + "** Is Broke Or Card Is Newly Created");

                     }

               }else{ // findCard Returns False

                   System.out.println("Sorry, Unable To Find Card With Card Id : " + cardNumber);

               }



        }catch (IOException io){

            io.printStackTrace();

        }catch (ParseException pe){

            pe.printStackTrace();

        }

    } // End Of statement()


    // Method To Write The Card Object Into File
    public void save(Card card){

        this.card = card;

        // Try-Catch For Exception Handling
        try{

            JSONParser parser = new JSONParser();

            /*
            * To Save New Card Into The File,
            * First Get The Data In The File, Then Append To The That Data
            *
            * Data In File Is Json Format For Easy Adding And Deleting Of The File Contents
            * Each Card Is Saved As Json Object Inside A Json Array == [{},{}]
            */

            this.printCard = new FileReader(this.fileName);

               if(this.printCard.ready()){

                   // Create An Array JSON Object To Hold Card Details
                   JSONArray cardList = (JSONArray) parser.parse(this.printCard); // parse() accepts FileReader Object

                   // Create JSON Object To Hold Card Details
                   JSONObject cardData = new JSONObject();
                   cardData.put("name", this.card.getCardOwner());
                   cardData.put("id", this.card.getCardNumber());
                   cardData.put("balance", this.card.getCardBalance());
                   cardData.put("pin", this.card.getCardPin());

                   // Add The JSON Object With Card Details To The Array JSON Object
                   cardList.add(cardData);


                     this.saveCard = new FileWriter(this.fileName);

                     // Add The Array JSON back Into File
                     this.saveCard.write(cardList.toJSONString());
                     this.saveCard.flush();

                     System.out.println("\n        ****************************\n        *        Card Created      *\n        ****************************");

                     System.out.println("\nCard Number : " + this.card.getCardNumber() + "\n\nUse The Above Card Number To Perform Transactions Like\n - Deposits\n - Withdrawals\n - Printing Statements");

               }else{

                   System.out.println("#### ---- Was Unable To Create/ Issue A New Card, System Error ---- ####");

               } // End Of ready() If

        }catch (IOException io){

            io.printStackTrace();

        }catch (ParseException pe){

            pe.printStackTrace();

        }

    } //End Of save()

} // End Of Class
