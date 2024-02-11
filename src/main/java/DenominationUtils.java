//import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Slf4j

public class DenominationUtils {

    public static void main(String[] args){

        //long[] denomArray = {100, 50, 25, 10, 5};

        long[] denomArray = new long[args.length-1];
        Arrays.fill(denomArray,0);
        List<DenomBreakdown> solution;

        if(args.length < 2) {
            System.out.println("Usage: java DenominationUtils <amount> <CoinValueMax .. CoinValueMin>");
            return;
        }
        float amount = Float.parseFloat(args[0]);

        // Copy string array of coin value arguments to denomArray
        for ( int i = 1 ; i < args.length; i++) {
            denomArray[i-1] = Long.parseLong(args[i]);
        }

        solution = calculateFixedDenoms(amount,denomArray);

        System.out.println("Coin denominations for amount $" + amount/100 + " are: ");
        System.out.println("Coins " + solution );

    }

    // Minimum combination of coin values for given amount
    public static List<DenomBreakdown> calculateFixedDenoms(float floatAmount, long[] longDenoms) {

        List<DenomBreakdown> solution;

        // Convert float to int
        int amount = (int) floatAmount;

        int[] denominations = new int[longDenoms.length];

        // Loop through all longs and convert to int
        for(int i = 0; i < longDenoms.length; i++)
        {
            // Cast to int and put it to the int array
            denominations[i] = (int) longDenoms[i];
        }

        solution = calculateFixedDenomsMethod(amount, denominations);
        return solution;
    }

    // Minimum combinations of coin values for given amount
    public static List<DenomBreakdown> calculateFixedDenomsMethod (int amount, int[] denoms) {

        List<DenomBreakdown> denomList = new ArrayList<>();

        int amountCopy = amount;               // Working copy of amount
        int[] quantities = new int[10];        // Up to 10 coin values allowed (make larger if needed)
        int lengthOfDenoms = denoms.length;    // Number of coin values passed in through main
        int i;                                 // loop counter
        int remainder = 0;                     // remainder after integer divide of amount/denom

        for (i = 0; i < lengthOfDenoms; i++) {
            quantities[i] = amountCopy / denoms[i];  // Test if the amount can be integer divided by the currnet denom
            remainder = amountCopy % denoms[i];      // Remainder of integer divide

            if (remainder == 0) {                    // If the division has 0 remainder, we have a solution
               // Done if remainder = 0
               break;
            }
            // If remainder > 0 and the amount is > (qty * denom value),
            // subtract the qty * denom value and repeat loop with next denom in the list
           if ((quantities[i] > 0) && (amountCopy > (quantities[i] * denoms[i]))) {
              amountCopy = amountCopy - (quantities[i] * denoms[i]);
            }
        }
        // Next Check for incomplete solutions:
        //   If the remainder > 0, then the solution was not fully evaluated.
        //   There are two possible causes for this:
        //     1.  There is no solution possible given the available coin denominations.
        //     2.  An incorrect solution was arrived at because the algorithm uses the largest available
        //         denomination first to complete the order.  For example, an amount of 30 won't be
        //         solved correctly if the available denominations are only 10, 25, 50.
        //         The algorithm generates a result of 25 and a remainder of 5, however three 10's
        //         should have been the correct result.
        //
        //  If amount/(smallest coin denomination) is divisible with a remainder of 0, there is a possible solution
        //  else, there is no solution possible.
        //
        //  Pseudocode to resolve issue:
        //  Start by subtracting the smallest denomination from the amount and rerun the algorithm
        //  starting with the largest denomination.
        //          amount = amount - smallest denom
        //          quantity of smallest denom = 1
        //          rerun algorithm to find solution starting with the highest denomination
        //  If remainder = 0, then all is ok and return results
        //     else
        //          amount = amount - smallest demon
        //          quantity of smallest denom = quantity of smallest denom + 1
        //  If remainder = 0, then all is ok and return results
        //     else
        //          Repeat above until remainder = 0
        //  Done

        if (remainder > 0) {
            Arrays.fill(quantities, 0);         // Initialize the denum qty array to 0
            amountCopy = amount;                // Get working copy of amount
            quantities[lengthOfDenoms - 1]++;   // Initialize smallest denom counter to 1

            // Subtract the smallest denom from the amount.  This is the new starting point.
            amountCopy = amountCopy - denoms[lengthOfDenoms - 1];
            do {
                // Start algorithm again with amount = amount - smallest denom
                // Loop through all the possible denom values for a solution
                for (i = 0; i < lengthOfDenoms; i++) {

                    // Make sure to include the initial qty offset (1) for the smallest denom
                    if (i == lengthOfDenoms - 1) {
                        quantities[i] = quantities[i] + (amountCopy / denoms[i]);
                    }  else {
                        quantities[i] = amountCopy / denoms[i];
                    }

                    remainder = amountCopy % denoms[i];   // Calculate the remainder for this coin value

                    if (remainder == 0) {
                        // Done if remainder = 0
                        break;
                    }

                    if ((quantities[i] > 0) && (amountCopy > (quantities[i] * denoms[i]))) {
                        amountCopy = amountCopy - (quantities[i] * denoms[i]);
                    }

                }
                if (remainder > 0) {

                    // Not done yet if there is still a remainder,
                    // Redo the algorithm with another min coin denom subtracted from the amount
                    int qtyTemp = ++quantities[lengthOfDenoms-1]; // Save a working copy of the min demon qty + 1
                    Arrays.fill(quantities,0);                    // Clear qty array and start again
                    quantities[lengthOfDenoms - 1] = qtyTemp;     // Restore min coin denom qty
                    amountCopy = amount - qtyTemp * denoms[lengthOfDenoms - 1];  // Subtract another min coin from the amount
                }
            } while (remainder > 0);
        }

        // Add up the calculated coin values and compare to the amount
        // If the sum of the coins > amount, there was no solution possible for this combination
        int calculatedCoins = 0;
        for (i = 0; i < lengthOfDenoms; i++){
            calculatedCoins += (quantities[i] * denoms[i]);
        }

        if (calculatedCoins > amount){
            // If no solution, return empty list
                return denomList;
            } else {
                // Populate the DenomBreakdown list with the results
                for (i = 0; i < lengthOfDenoms; i++) {
                    if (quantities[i] > 0) {
                        DenomBreakdown current = new DenomBreakdown((float) denoms[i] / 100, quantities[i]);
                        denomList.add(current);
                    }
                }
            }
        return denomList;
    }
}

