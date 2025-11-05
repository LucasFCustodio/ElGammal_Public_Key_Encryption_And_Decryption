package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class ElGammal {
    //private variables
    private BigInteger largePrime; //large prime number
    private BigInteger generator; //generator
    private BigInteger privateKey; //private key
    private BigInteger b_a; //public key component

    //constructor
    public ElGammal(Scanner scan) {
        System.out.println("Welcome to the ElGammal Public Key Encryption System!");

        // Step 1 - Find p_A, the large prime number
        System.out.println("Would you like to generate a 64-bit or a 128-bit large prime number?");
        int bitNum = scan.nextInt();
        scan.nextLine(); // Consume the newline character
        MillerRabinTest2 millerRabin = new MillerRabinTest2();
        largePrime = millerRabin.findPrime(bitNum);
        // --STEP 1 COMPLETE--

        // Step 2 - Find the generator alpha_A
        // Use 2 for simplicity
        generator = new BigInteger("2");
        // --STEP 2 COMPLETE--

        //Step 3 - Generate the private key d_A as a new random number in the range [1, p_A-2]
        BigInteger maxRange = largePrime.subtract(new BigInteger("2"));
        privateKey = getRandomBigIntInRange(new BigInteger("2"), maxRange);
        //--STEP 3 COMPLETE--

        //Step 4 - Compute b_A = alpha_A^(d_A) mod p_A
        b_a = SquareAndMultiply.squareAndMultiply(generator, privateKey, largePrime);
        //--STEP 4 COMPLETE--

        //Step 5 - Display the public key
        System.out.println("Public Key (p_A, alpha_A, b_A): (" + largePrime + ", " + generator + ", " + b_a + ")");
        System.out.println("Private Key d_A: " + privateKey);
    }

    //helper methods
    public BigInteger[] encryption(BigInteger message) {
        BigInteger maxRange = largePrime.subtract(new BigInteger("2"));
        BigInteger k;
        BigInteger r;
        BigInteger t;
        k = getRandomBigIntInRange(new BigInteger("2"), maxRange); //just to ensure k is less than p_A-2
        r = generator.modPow(k, largePrime);
        t = (b_a.modPow(k, largePrime).multiply(message)).mod(largePrime);
        return new BigInteger[]{r, t};
    }

    public BigInteger decryption(BigInteger r, BigInteger t) {
        BigInteger s = r.modPow(privateKey, largePrime);
        BigInteger sInverse = s.modInverse(largePrime);
        BigInteger message = (t.multiply(sInverse)).mod(largePrime);
        return message;
    }

    /**
     * NEW: Helper function to generate a random BigInteger in a specified range.
     * Need this for both the private key (d_A) and the ephemeral key (k).
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @return A random BigInteger in the range [min, max].
     */
    private BigInteger getRandomBigIntInRange(BigInteger min, BigInteger max) {
        SecureRandom rand = new SecureRandom();
        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        BigInteger result;
        
        // Keep trying until we get a number in the valid range
        // This is more efficient than .mod() for non-power-of-2 ranges
        do {
            result = new BigInteger(range.bitLength(), rand);
        } while (result.compareTo(range) >= 0);
        
        return result.add(min); // Add the minimum value back
    }
    
    /**
     * NEW: Getter for Main.java to use for message validation.
     */
    public BigInteger getLargePrime() {
        return largePrime;
    }
}
