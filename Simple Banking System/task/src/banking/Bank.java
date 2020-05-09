package banking;

import java.util.Scanner;

import static banking.BankCard.checkSum;

public class Bank {
    Scanner scanner = new Scanner(System.in);
    DataBase dataBase;


    public Bank(String fileName) {
        this.dataBase = new DataBase(fileName);
    }

    public void init() {
        int choice;

        do {
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    exit();
                    break;
                case 1:
                    createCard();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    System.out.println("ERROR: Invalid Choice.");
            }
        } while (choice != 0);
    }

    public void createCard() {
        BankCard bankCard = new BankCard();

        System.out.println("\nYour Account has been created");
        System.out.println("Your card number:\n" + bankCard.getCardNumber());
        System.out.println("Your card PIN:\n" + bankCard.getCardPin() + "\n");

        dataBase.insert(bankCard.getCardNumber(), bankCard.getCardPin(), 0);
        //dataBase.selectAll();
    }

    private void logIntoAccount() {
        String inputCardNumber;
        String inputCardPin;
        BankCard currentCard;

        scanner.nextLine();
        System.out.println("\nEnter your card number:");
        inputCardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        inputCardPin = scanner.nextLine();
        currentCard = dataBase.selectCard(inputCardNumber, inputCardPin);
        if (currentCard.getCardNumber().equals(inputCardNumber) || currentCard.getCardPin().equals(inputCardPin)) {
            System.out.println("\nYou have successfully logged in!\n");
            operationAcc(currentCard);
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void operationAcc(BankCard currentCard) {
        int choice;

        do {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Transfer Money");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    exit();
                    break;
                case 1:
                    viewBalance(currentCard);
                    break;
                case 2:
                    addDeposit(currentCard);
                    break;
                case 3:
                    doTransfer(currentCard);
                    break;
                case 4:
                    closeAccount(currentCard);
                    dataBase.selectAll();
                    return;
                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                default:
                    System.out.println("ERROR: Invalid Choice.");
            }
        } while (choice != 0);

    }


    private void viewBalance(BankCard currentCard) {
        dataBase.getBalance(currentCard);
        System.out.println("\nBalance: " + dataBase.getBalance(currentCard) + "\n");
    }

    private void addDeposit(BankCard currentCard) {
        System.out.println("\nEnter Amount:");
        dataBase.addBalance(scanner.nextInt() + dataBase.getBalance(currentCard), currentCard);
    }

    private void doTransfer(BankCard currentCard) {
        String destCardNum;
        String destCheckSum;
        int transfer;
        BankCard destCard;
        scanner.nextLine();
        do {
            System.out.println("\nRecipients Card Number:");
            destCardNum = scanner.nextLine();

            destCheckSum = checkSum(destCardNum.substring(0, destCardNum.length() - 1));

            if (!String.valueOf(destCardNum.charAt(15)).equals(destCheckSum)) {
                System.out.println("Probably you made mistake in card number. Please try again!\n");
            }

        } while (!String.valueOf(destCardNum.charAt(15)).equals(destCheckSum));

        if(!destCardNum.equals(dataBase.selectCardNum(destCardNum))){
            System.out.println("\nSuch a card does not exist.\n");
            return;
        }

        if(destCardNum.equals(currentCard.getCardNumber())){
            System.out.println("\nYou can't transfer money to the same account!\n");
            return;
        }

        do{
            System.out.println("\nEnter amount to be transferred");
            transfer = scanner.nextInt();
            if(transfer > dataBase.getBalance(currentCard)) {
                System.out.println("\nBalance not available, try again with a valid amount.\n");
            }
        } while (transfer > dataBase.getBalance(currentCard));

        destCard = dataBase.selectDestCard(destCardNum);
        dataBase.addBalance(transfer + dataBase.getBalance(destCard), destCard);
        dataBase.addBalance(dataBase.getBalance(currentCard) - transfer, currentCard);
    }

    private void closeAccount(BankCard currentCard) {
        dataBase.closeAccount(currentCard);
        System.out.println("\nAccount " + currentCard.getCardNumber() + " has been deleted...");
    }

    private void exit() {
        System.out.println("\nThanks For Visiting!!");
        System.exit(0);
    }
}