package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


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
                    System.out.println("Your card number:");
                    CardGenerator newAccount = new CardGenerator();
                    newAccount.createCardNumber();
                    System.out.println(newAccount.getCardNumber());
                    System.out.println("Your card PIN:");
                    newAccount.generate_PinCode();
                    System.out.println(newAccount.getPinCode());
                    accounts.put(newAccount.getCardNumber(), newAccount);
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Enter your card number:");
                    String cardNumber = scanner.next();
                    System.out.println("Enter your PIN:");
                    String pin = scanner.next();
                    CardGenerator account = accounts.get(cardNumber);
                    if (account == null || !account.getPinCode().equals(pin)) {
                        System.out.println();
                        System.out.println("Wrong card number or PIN!");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("You have successfully logged in!");
                        System.out.println();

                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("1. Balance");
                            System.out.println("2. Log out");
                            System.out.println("0. Exit");

                            input = scanner.nextInt();

                            switch (input) {
                                case 1:
                                    System.out.println("Balance: 0");
                                    break;
                                case 2:
                                    System.out.println();
                                    System.out.println("You have successfully logged out!");
                                    System.out.println();
                                    loggedIn = false;
                                    break;
                                case 0:
                                    System.out.println("\nBye!");
                                    loggedIn = false;
                                    input = 0;
                            }
                        }
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