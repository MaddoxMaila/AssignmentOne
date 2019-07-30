package nfsascard;

import java.util.Scanner;

public class Main {

    public static Scanner input = new Scanner(System.in);
    public static Card mycard = new Card();
    public static int Context = 0;
    public static double money;
    public static int cardNumber;

    public static void main(String[] args){



        while (true){

            System.out.println("\n        ****************************\n        *        NSFAS Bank        *\n        ****************************");
            System.out.println("1. Create New Card \n2. Deposit \n3. Withdraw \n4. Statement \n0. Exit\n");

            System.out.print("Option : ");
            Context = input.nextInt();

            if(Context == 1){

                pressOne();

            }else if(Context == 2){

                pressTwo();

            }else if(Context == 3){

                pressThree();

            }else if(Context == 4){

                pressFour();

            }else if(Context == 0){

                System.out.println("************** PEACE OUT **************");
                break;

            }else{

                break;

            } // End Of Context If

            System.out.println("\n\n\n");

        } // End Of While Loop

    }
    public static void pressFour(){

        System.out.println("\n        ****************************\n        *        STATEMENT         *\n        ****************************\n");

        System.out.print("Enter Card Number : ");
        cardNumber = input.nextInt();

        mycard.printCard(cardNumber);

    }
    public static void pressThree(){

        System.out.println("\n        ****************************\n        *        WITHDRAW          *\n        ****************************\n");

        System.out.print("Enter Card Number : ");
        cardNumber = input.nextInt();

        System.out.print("Funds To Withdraw : R");
        money = input.nextDouble();

        mycard.withdraw(money, cardNumber);

    }

    public static void pressTwo(){

        System.out.println("\n        ****************************\n        *        DEPOSIT           *\n        ****************************\n");


        System.out.print("Enter Card Number : ");
        cardNumber = input.nextInt();

        System.out.print("Funds To Deposit : R");
        money = input.nextDouble();

        mycard.deposit(money, cardNumber);

    }
    public static void pressOne(){

        String Name;
        int Pin;

        System.out.println("\n        ****************************\n        *        CREATE CARD      *\n        ****************************\n");

        System.out.print("Enter Name & Surname : ");
        input.nextLine();
        Name = input.nextLine();

        System.out.print("\nEnter Card Pin : ");
        Pin = input.nextInt();

           Card card = new Card(Pin, Name);

           card.create(card);

    }


}
