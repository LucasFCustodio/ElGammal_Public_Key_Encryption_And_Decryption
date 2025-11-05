package crypto;
import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabinTest {

    private int k = 0;
    private int m;
    int base = 2;
    BigInteger a;
    BigInteger b;
    boolean isPrime = false;
    
    public MillerRabinTest(int numBits) {
        while(isPrime == false) {
            //BigInteger randomOddNum = generateRandomOddNumber(numBits);
            //Test case
            BigInteger randomOddNum = new BigInteger("561");
            BigInteger randomOddMinusOne = randomOddNum.subtract(BigInteger.ONE);

            //Step 1 of the Test: Find k and m such that n-1 = 2^k * m
            BigInteger baseBigInt = BigInteger.valueOf(base);
            int exponent = 1;
            boolean remainderIsZero = true;
            BigInteger divisionResult = null;
            while (remainderIsZero) {
                if (randomOddMinusOne.mod(baseBigInt.pow(exponent)).equals(BigInteger.ZERO)) {
                    divisionResult = randomOddMinusOne.divide(baseBigInt.pow(exponent));
                    exponent++;
                }
                else {
                    remainderIsZero = false;
                    k = exponent - 1;
                    m = divisionResult.intValue();
                }
            }

            //Step 2 of the Test: Choose 'a' such that 1 < a < n-1
            a = BigInteger.valueOf(2);
        
            //Step 3 of the Test: Compute b = a^m mod n
            boolean remainderCheck = false;
            int step = 0;
            while (remainderCheck == false) {
                if (step == 0) {
                    b = a.pow(m).mod(randomOddNum);
                    System.out.println("Initial b value: " + b.toString());
                    if (b.equals(BigInteger.ONE)) {
                        isPrime = false;
                        System.out.println("RandomOddNum is composite.");
                        remainderCheck = true;
                    }
                    else if (b.equals(randomOddMinusOne)) {
                        isPrime = true;
                        System.out.println("RandomOddNum is probably prime.");
                        remainderCheck = true;
                    }
                    else {
                        step++;
                    }   
                }
                else {
                    b = b.pow(2).mod(randomOddNum);
                    System.out.println("Updated b value: " + b.toString());
                    if (b.equals(BigInteger.ONE)) {
                        isPrime = false;
                        System.out.println("RandomOddNum is composite.");
                        remainderCheck = true;
                    }
                    else if (b.equals(randomOddMinusOne)) {
                        isPrime = true;
                        System.out.println("RandomOddNum is probably prime.");
                        remainderCheck = true;
                    }
                    else {
                        step++;
                        if (step == k) {
                            remainderCheck = true;
                            isPrime = false;
                        }
                    }
                }
            }
        }
    }

    //method to generate a random odd number with specified bit length
    public static BigInteger generateRandomOddNumber(int numBits) {
        //The random number generator
        SecureRandom rand = new SecureRandom();
        //Generate the random number with the specified number of bits
        BigInteger randomNum = new BigInteger(numBits, rand);
        //Make sure the number is odd by setting the least significant bit (bit 0) to 1
        return randomNum.setBit(0);
    }

    public static void main(String[] args) {
        int numBits = 128; // Example bit length
        MillerRabinTest mrt = new MillerRabinTest(numBits);
    }
}