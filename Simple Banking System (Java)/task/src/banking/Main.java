package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        String dbFileName = null;
        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equals(args[i]) && i + 1 < args.length) {
                dbFileName = args[i + 1];
                break;
            }
        }
        if (dbFileName == null) {
            System.out.println("Please, specify file name with argument -fileName");
            return;
        }

        DatabaseManager dbManager = new DatabaseManager(dbFileName);

        Scanner scanner = new Scanner(System.in);

        Map<String, CardGenerator> accounts = new HashMap<>();
        int input = 1;
        while (input != 0) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            input = scanner.nextInt();

            switch (input) {
                case 1:
                    System.out.println();
                    System.out.println("Your card has been created");
                    // ...
                    CardGenerator newAccount = new CardGenerator();
                    newAccount.createCardNumber();
                    String newCardNumber = newAccount.getCardNumber();
                    String newPin = newAccount.generate_PinCode();

                    System.out.println("Your card number:");
                    System.out.println(newCardNumber);
                    System.out.println("Your card PIN:");
                    System.out.println(newPin);

                    dbManager.insertCard(newCardNumber, newPin);

                    System.out.println();
                    break;

                case 2:
                    System.out.println();
                    System.out.println("Enter your card number:");
                    String cardNumber = scanner.next();
                    System.out.println("Enter your PIN:");
                    String pin = scanner.next();


                    CardGenerator account = dbManager.findCard(cardNumber, pin);

                    if (account == null) {
                        System.out.println();
                        System.out.println("Wrong card number or PIN!");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("You have successfully logged in!");
                        System.out.println();

                    }
                    break;
                case 0:
                    System.out.println("\nBye!");
                    input = 0;
                    break;
            }
        }
    }
}

class CardGenerator {
    private final int[] BIN = {4, 0, 0, 0, 0, 0};
    private final Random random = new Random();
    private String cardNumber;
    private String pinCode = null;

    public String getCardNumber() {
        return cardNumber;
    }

    public void createCardNumber() {
        this.cardNumber = generateCardNumber();
    }

    public String getPinCode() {
        if (pinCode == null) {
            generate_PinCode();
        }
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }


    private int[] generate_AccountIdentifier() {
        int[] accountIdentifier_array = new int[9];
        for (int i = 0; i < accountIdentifier_array.length; i++) {
            accountIdentifier_array[i] = random.nextInt(10);
        }
        return accountIdentifier_array;
    }

    public String generate_PinCode() {
        int number = random.nextInt(10000);
        pinCode = String.format("%04d", number);
        return pinCode;
    }

    private int generate_CheckSum(int[] cardNumber) {
        int summarize = 0;
        for (int i = cardNumber.length - 1; i >= 0; i--) {
            int digit = cardNumber[i];
            if ((cardNumber.length - 1 - i) % 2 == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            summarize += digit;
        }
        return (10 - (summarize % 10)) % 10;
    }

    private String generateCardNumber() {
        String cardNumber = "";
        int[] accountIdentifier = this.generate_AccountIdentifier();

        int len1 = BIN.length;
        int len2 = accountIdentifier.length;

        int[] cardNumberWithoutChecksum = new int[len1 + len2];
        System.arraycopy(BIN, 0, cardNumberWithoutChecksum, 0, len1);
        System.arraycopy(accountIdentifier, 0, cardNumberWithoutChecksum, len1, len2);

        int checksum = generate_CheckSum(cardNumberWithoutChecksum);
        for (int number : cardNumberWithoutChecksum) {
            cardNumber += number;
        }
        return cardNumber += checksum;
    }
}