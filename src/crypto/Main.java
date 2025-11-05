package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Call ElGammal class to generate keys
        Scanner scan = new Scanner(System.in);
        ElGammal elGammal = new ElGammal(scan);
        
        //get p_A from ElGammal class to check if final message is less than p_A
        BigInteger p_A = elGammal.getLargePrime();
        BigInteger finalMessage;

        while(true) {
            System.out.println("Enter a message to encrypt:");
            String message = scan.nextLine(); // No double nextLine needed

            // REVISED: Use UTF-8 for consistent encoding
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            finalMessage = new BigInteger(1, messageBytes);

            // REVISED: Critical check: M must be less than p_A
            if (finalMessage.compareTo(p_A) < 0) {
                break; // Message is good
            } else {
                System.out.println("Error: Message is too large for the key. Please enter a shorter message.");
            }
        }
        //Capture and print the returned ciphertext
        BigInteger[] ciphertext = elGammal.encryption(finalMessage);
        BigInteger plaintext = elGammal.decryption(ciphertext[0], ciphertext[1]);
        
        System.out.println("--- Encryption Complete ---");
        System.out.println("Ciphertext (r): " + ciphertext[0]);
        System.out.println("Ciphertext (t): " + ciphertext[1]);
        System.out.println();
        System.out.println("--- Decryption Process ---");
        System.out.println("Plaintext (s): " + plaintext);

        // REVISED: Close the one-and-only scanner at the very end.
        scan.close();
    }
}
