    package crypto;

    import java.math.BigInteger;

    public class SquareAndMultiply {

        //private variables
        
        //All the logic is in the helper methods, not the constructor

        //Helper method to perform square and multiply
        public static BigInteger squareAndMultiply(BigInteger base, BigInteger exponent, BigInteger modulus) {
            BigInteger result = BigInteger.ONE;
            String binaryExponent = exponent.toString(2); // Convert exponent to binary string

            for (int i = 0; i < binaryExponent.length(); i++) {
                result = result.multiply(result).mod(modulus); // Square step
                if (binaryExponent.charAt(i) == '1') {
                    result = result.multiply(base).mod(modulus); // Multiply step
                }
            }
            return result;
        }
    }