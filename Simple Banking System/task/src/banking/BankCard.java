package banking;

import java.security.SecureRandom;


public class BankCard {

    private String cardNumber;
    private String cardPin;
    private int cardBalance = 0;
    public BankCard() {

        SecureRandom random = new SecureRandom();
        final String BIN = "400000";
        cardNumber = BIN +
                String.format("%09d", random.nextInt(1000000000));
        cardNumber = cardNumber + checkSum(cardNumber);

        cardPin = String.format("%04d", random.nextInt(10000));

    }

    public static String checkSum(String cardNumber) {
        int[] number = new int[cardNumber.length()];
        int sum = 0;
        for(int i = 0; i < cardNumber.length() ; i++){
            number[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (i % 2 == 0) {
                if (number[i] * 2 > 9) {
                    number[i] = number[i] * 2 - 9;
                } else {
                    number[i] = number[i] * 2;
                }
            }
            sum = sum + number[i];
        }
        return String.valueOf((10 - (sum % 10) == 10) ? 0 : (10 - (sum % 10)));
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }
}