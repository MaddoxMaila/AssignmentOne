package nfsascard;


public class Card {

    private long cardNumber, cardPin;
    private String cardOwner;
    double cardBalance;
    private FileIO file = new FileIO();

    double money;

    // Constructor For Doing Transactions
    Card(){

        money = 0;

    }
    // Constructor For Creating A New Card
    Card(int cardPin, String cardOwner){

        this.cardPin = cardPin;
        this.cardOwner = cardOwner;

        this.cardBalance = 0; // When New Card Is Created, Balance == 0

        this.cardNumber = this.file.getLastCardNumber() + 1;


    } // End Of Class Constructor

    public void printCard(long cardNumber){

        this.file.statement(cardNumber);

    } // End Of PrintCard

    // Method To Deposit Funds Into The Card
    public void deposit(double money, long cardNumber){

        this.file.saveTransactions(1, money, cardNumber);

    } // End Of Deposit

    // Method To Withdraw Funds Out Of The Card
    public void withdraw(double money, long cardNumber){

        this.file.saveTransactions(2, money, cardNumber);

    } // End Of withdraw

    public void create(Card card){

        this.file.save(card);

    } // End Of Commit

    public void setCardBalance(double cardBalance) {
        this.cardBalance = cardBalance;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public void setCardPin(int cardPin) {
        this.cardPin = cardPin;
    }

    public double getCardBalance() {
        return cardBalance;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public long getCardPin() {
        return cardPin;
    }

    public String getCardOwner() {
        return cardOwner;
    }

}
