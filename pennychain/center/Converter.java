package pennychain.center;

import java.util.Scanner;

public class Converter {

    public static char US_Dollar_Sign = 36;
    public static char British_Pound_Sign = 163;
    public static char Japanese_Yen_Sign = 165;
    public static char Euro_Sign = 8364;

    public static String US_Money = "Dollars";
    public static String British_Money = "Pounds";
    public static String Japan_Money = "Yen";
    public static String Europe_Money = "Euros";

    public static double exchange_Rate = 0;


    public static  void main(String args[]) 
    {
        displayCurrencyConverter();
    }
        
    
    public static void displayCurrencyConverter()
    {
        System.out.println("Please Select one of the Following for your currency choices: \n 1 - US dollars \n 2 - British Pounds \n 3 - Euros  \n 4 - Japanese Yen \n");
        System.out.println("Please choose the currency you want to convert from: ");
        Scanner user_selection = new Scanner(System.in);
        int user_currency_choice = user_selection.nextInt();

        String currentCurrency = null;
        switch(user_currency_choice) {
            case 1: currentCurrency = "US Dollars >> " + US_Dollar_Sign;  break;
            case 2: currentCurrency = "British Pounds >> " + British_Pound_Sign ; break;
            case 3: currentCurrency = "Euros >> " + Euro_Sign ; break;
            case 4: currentCurrency = "Japanese Yen >> " + Japanese_Yen_Sign; break;
            default:
                System.out.println("Please try again and choose an option from the list!");
                return;
        }

        System.out.println("Please choose the currency you want to convert to: ");
        int desiredCurrency = user_selection.nextInt();

        System.out.printf("Please enter the amount of money you want to convert in " + currentCurrency);
        double totalAmountToBeConverted = user_selection.nextDouble();

        if (user_currency_choice == desiredCurrency)
            System.out.println("These are the same currencies. No need to convert!");

        if (user_currency_choice == 1 && desiredCurrency == 3)
        {
            double US_Dollar_To_Euro_Rate = 0.81125;
            exchange_Rate = totalAmountToBeConverted * US_Dollar_To_Euro_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + "  converted from a rate of " + US_Dollar_To_Euro_Rate + " Dollars to %s = %.2f\n", US_Dollar_Sign, Europe_Money, exchange_Rate);
        }
        else if (user_currency_choice == 1 && desiredCurrency == 2){
            double US_Dollar_To_British_Pound_Rate = 0.71340;
            exchange_Rate = totalAmountToBeConverted * US_Dollar_To_British_Pound_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + "  converted from a rate of " + US_Dollar_To_British_Pound_Rate + " Dollars to %s = %.2f\n", US_Dollar_Sign, British_Money, exchange_Rate);
        }
        else if (user_currency_choice == 1 && desiredCurrency == 4){
            double US_Dollar_To_Japenese_Yen_Rate = 106.24;
            exchange_Rate = totalAmountToBeConverted * US_Dollar_To_Japenese_Yen_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + US_Dollar_To_Japenese_Yen_Rate + " Dollars to %s = %.2f\n", US_Dollar_Sign, Japan_Money, exchange_Rate);
        }
        if (user_currency_choice == 3 && desiredCurrency == 1)
        {
            double Euro_To_US_Dollar_Rate = 1.23207;
            exchange_Rate = totalAmountToBeConverted * Euro_To_US_Dollar_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Euro_To_US_Dollar_Rate + " Euros to %s = %.2f\n", Euro_Sign, US_Money, exchange_Rate);
        }
        else if (user_currency_choice == 3 && desiredCurrency == 2)
        {
            double Euro_To_British_Pound_Rate = 0.87885;
            exchange_Rate = totalAmountToBeConverted * Euro_To_British_Pound_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Euro_To_British_Pound_Rate + " Euros to %s = %.2f\n", Euro_Sign, British_Money, exchange_Rate);
        }
        else if (user_currency_choice == 3 && desiredCurrency == 4)
        {
            double Euro_To_Japenese_Yen_Rate = 130.88;
            exchange_Rate = totalAmountToBeConverted * Euro_To_Japenese_Yen_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Euro_To_Japenese_Yen_Rate + " Euros to %s = %.2f\n", Euro_Sign, Japan_Money, exchange_Rate);
        }
        if (user_currency_choice == 2 && desiredCurrency == 1)
        {
            double British_Pound_To_Us_Dollar_Rate = 1.40084;
            exchange_Rate = totalAmountToBeConverted * British_Pound_To_Us_Dollar_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + British_Pound_To_Us_Dollar_Rate + " Pounds to %s = %.2f\n", British_Pound_Sign, US_Money, exchange_Rate);
        }
        else if (user_currency_choice == 2 && desiredCurrency == 3)
        {
            double British_Pound_To_Euro_Rate = 1.13656;
            exchange_Rate = totalAmountToBeConverted * British_Pound_To_Euro_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + British_Pound_To_Euro_Rate + " Pounds to %s = %.2f\n", British_Pound_Sign, Europe_Money, exchange_Rate);
        }
        else if (user_currency_choice == 2 && desiredCurrency == 4)
        {
            double British_Pound_To_Japanese_Yen_Rate = 148.85;
            exchange_Rate = totalAmountToBeConverted * British_Pound_To_Japanese_Yen_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + British_Pound_To_Japanese_Yen_Rate + " Pounds to %s = %.2f\n", British_Pound_Sign, Japan_Money, exchange_Rate);
        }
        if (user_currency_choice == 4 && desiredCurrency == 1)
        {
            double Japanese_Yen_To_US_Dollar_Rate = 0.00941;
            exchange_Rate = totalAmountToBeConverted * Japanese_Yen_To_US_Dollar_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Japanese_Yen_To_US_Dollar_Rate + " Yen to %s = %.2f\n", (char)Japanese_Yen_Sign, US_Money, exchange_Rate);
        }
        else if (user_currency_choice == 4 && desiredCurrency == 3)
        {
            double Japanese_Yen_To_Euro_Rate = 0.00763;
            exchange_Rate = totalAmountToBeConverted * Japanese_Yen_To_Euro_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Japanese_Yen_To_Euro_Rate + " Yen to %s = %.2f\n", (char)Japanese_Yen_Sign, Europe_Money, exchange_Rate);
        }
        else if (user_currency_choice == 4 && desiredCurrency == 2)
        {
            double Japanese_Yen_To_British_Pound_Rate = 0.00671;
            exchange_Rate = totalAmountToBeConverted * Japanese_Yen_To_British_Pound_Rate;
            System.out.printf( "%s" + totalAmountToBeConverted + " converted from a rate of " + Japanese_Yen_To_British_Pound_Rate + " Yen to %s = %.2f\n", (char)Japanese_Yen_Sign, British_Money, exchange_Rate);
        }
    }
}
