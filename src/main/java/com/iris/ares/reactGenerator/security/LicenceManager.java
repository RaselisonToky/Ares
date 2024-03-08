package com.iris.ares.reactGenerator.security;

import com.iris.ares.reactGenerator.security.exception.LicenceInvalideException;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;

import static com.iris.ares.reactGenerator.generator.CRUDGenerator.generateCRUDPages;
import static com.iris.ares.reactGenerator.generator.CSSGenerator.generateCSSFile;
import static com.iris.ares.reactGenerator.react_handler.ReactProjectHandler.createReactProject;


/**
 * LicenceManager
 * Manages licence validation and initialization of the application.
 */
public class LicenceManager {
    private final Properties licences;
    private PrivateKey privateKey;


    /**
     * Constructor
     * Initializes LicenceManager by loading licence properties and private key.
     */
    public LicenceManager() {
        this.licences = new Properties();
        try (InputStream inputStream = LicenceManager.class.getResourceAsStream("/licence.properties")) {
            licences.load(inputStream);
            String privateKeyString = licences.getProperty("privateKey"); // Utilisation de privateKey
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes); // Utilisation de PKCS8EncodedKeySpec
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = keyFactory.generatePrivate(keySpec); // Utilisation de generatePrivate
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Decrypts the given encrypted data using the private key.
     *
     * @param encryptedData The encrypted data to decrypt.
     * @return The decrypted data.
     * @throws Exception If decryption fails.
     */
    private String decrypt(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // Utilisation de privateKey
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }


    /**
     * Checks if the licence associated with the given username is valid.
     *
     * @param username   The username associated with the licence.
     * @param licenceKey The licence key to validate.
     * @return true if the licence is valid, false otherwise.
     * @throws Exception If licence validation fails.
     */
    public boolean isLicenceValid(String username, String licenceKey) throws Exception {
        String storedEncryptedLicenceKey = licences.getProperty(username);
        if (storedEncryptedLicenceKey != null) {
            String decryptedLicenceKey = decrypt(storedEncryptedLicenceKey);
            return decryptedLicenceKey.equals(licenceKey);
        }
        return false;
    }



    /**
     * Initializes the application after validating the licence.
     *
     * @throws LicenceInvalideException If the licence is invalid.
     * @throws Exception                If an error occurs during initialization.
     */
    public void init() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username : ");
        String username = scanner.nextLine();
        System.out.print("Licence key : ");
        String licenceKey = scanner.nextLine();
        if (!isLicenceValid(username, licenceKey)) {
            throw new LicenceInvalideException("Invalid licence. Access denied.");
        } else {
            createReactProject();
            generateCRUDPages();
            generateCSSFile();
        }

    }
}
