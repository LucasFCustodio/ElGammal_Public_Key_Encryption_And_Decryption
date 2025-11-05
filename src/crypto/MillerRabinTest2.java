//** THIS IS THE FINAL VERSION OF THE CODE. THIS IS THE VERSION THAT SHOULD BE USED, AS IT IS THE UPDATED VERSION**/
package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabinTest2 {

    // NEW: The number of times to run the test for a single candidate.
    // 64 is a good number for strong certainty.
    // Your old code only ran this 1 time, with a=2.
    private static final int CERTAINTY = 64;

    /**
     * This is the "generate and test" loop you asked about.
     * It will keep getting new numbers until one passes the test.
     */
    public BigInteger findPrime(int numBits) {
        System.out.println("Searching for a " + numBits + "-bit prime...");
        
        // This loop will run indefinitely...
        while (true) {
            // 1. Generate a random candidate
            BigInteger primeCandidate = generateRandomOddNumber(numBits);
            
            // 2. Test it.
            // We pass it to our helper method, which is your original logic.
            if (isProbablyPrime(primeCandidate)) {
                // 3. If it's prime, we're done! Return it.
                System.out.println("Found probable prime: " + primeCandidate);
                return primeCandidate;
            }
            
            // 4. If it's composite, the loop repeats and tries a new number.
        }
    }

    /**
     * This method contains your original logic from the constructor.
     * I've wrapped it in the "Multiple Witness" loop.
     * @param n The prime candidate (your 'randomOddNum')
     * @return true if probably prime, false if composite
     */
    private boolean isProbablyPrime(BigInteger n) {
        
        // Simple checks: 2 and 3 are prime. Even numbers are not.
        if (n.equals(BigInteger.TWO) || n.equals(new BigInteger("3"))) return true;
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false; // Even
        
        //Step 1
        BigInteger randomOddMinusOne = n.subtract(BigInteger.ONE); // This is n-1
        int k = 0;
        
        BigInteger m = BigInteger.ZERO;

        int base = 2;
        BigInteger baseBigInt = BigInteger.valueOf(base);
        int exponent = 1;
        boolean remainderIsZero = true;
        BigInteger divisionResult = null;

        while (remainderIsZero) {
            if (randomOddMinusOne.mod(baseBigInt.pow(exponent)).equals(BigInteger.ZERO)) {
                divisionResult = randomOddMinusOne.divide(baseBigInt.pow(exponent));
                exponent++;
            } else {
                remainderIsZero = false;
                k = exponent - 1;
                m = divisionResult; // FIX: m is now a BigInteger
            }
        }
        
        // Run step 2 and 3 a specified amount of times, depending on the certainty we want to have
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < CERTAINTY; i++) {
            
            // Step 2 - Choose a random 'a' between 2 and n-2
            BigInteger nMinusTwo = n.subtract(BigInteger.TWO);
            // This creates a random 'a' in the range [2, n-2]
            BigInteger a = new BigInteger(n.bitLength(), rand).mod(nMinusTwo).add(BigInteger.TWO);
            
            // --- This is your "Step 3" logic, inside the new loop ---
            // Compute b = a^m mod n
            
            BigInteger b = SquareAndMultiply.squareAndMultiply(a, m, n); // This is a.pow(m).mod(n)
            
            // If b=1 OR b=n-1, the test passes *for this 'a'*.
            if (b.equals(BigInteger.ONE) || b.equals(randomOddMinusOne)) {
                continue; // Try the next random 'a'
            }

            // This is your inner loop logic, checking b = b^2 mod n
            int step;
            for (step = 1; step < k; step++) { // We loop k-1 times
                
                // This is your `b = b.pow(2).mod(randomOddNum)`
                b = b.pow(2).mod(n);

                // Your logic here was correct:
                if (b.equals(BigInteger.ONE)) {
                    return false; // Definitely composite
                }
                if (b.equals(randomOddMinusOne)) {
                    // This 'a' test passed. Break this inner loop
                    // and go to the next 'a' in the outer loop.
                    break;
                }
            }
            
            // FIX: This is the other missing piece.
            // If we finished the inner loop (step == k)
            // AND b *still* doesn't equal n-1, it's composite.
            if (step == k && !b.equals(randomOddMinusOne)) {
                return false; // Definitely composite
            }
        }

        // If the number survived all 64 rounds, it's probably prime.
        return true;
    }

    // --- This is your original helper method, untouched ---
    // (With one small addition to make the prime the full bit length)
    public static BigInteger generateRandomOddNumber(int numBits) {
        //The random number generator
        SecureRandom rand = new SecureRandom();
        //Generate the random number with the specified number of bits
        BigInteger randomNum = new BigInteger(numBits, rand);
        //Make sure the number is odd by setting the least significant bit (bit 0) to 1
        randomNum = randomNum.setBit(0);
        
        // NEW: Also set the top bit to 1. This guarantees your
        // 64-bit number is actually 64 bits long, not 62 or 63.
        return randomNum.setBit(numBits - 1);
    }

    // NEW: The main method is now clean.
    // It just creates an instance and calls findPrime().
    public static void main(String[] args) {
        int numBits = 64; // 64 is faster to test than 128
        
        MillerRabinTest2 mrt = new MillerRabinTest2();
        BigInteger prime = mrt.findPrime(numBits);
        
        System.out.println("Final prime found: " + prime);
    }
}