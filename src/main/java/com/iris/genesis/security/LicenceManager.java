package com.iris.genesis.security;

import com.iris.genesis.generator.CRUDGenerator;
import com.iris.genesis.generator.CSSGenerator;
import com.iris.genesis.react_handler.ReactProjectHandler;
import com.iris.genesis.security.exception.LicenceInvalideException;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;

public class LicenceManager {
    private final Properties licences;
    private PrivateKey privateKey;

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

    private String decrypt(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // Utilisation de privateKey
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
    public boolean isLicenceValid(String username, String licenceKey) throws Exception {
        String storedEncryptedLicenceKey = licences.getProperty(username);
        if (storedEncryptedLicenceKey != null) {
            String decryptedLicenceKey = decrypt(storedEncryptedLicenceKey);
            return decryptedLicenceKey.equals(licenceKey);
        }
        return false;
    }

    public void init() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username : ");
        String username = scanner.nextLine();
        System.out.print("Licence key : ");
        String licenceKey = scanner.nextLine();
        if (!isLicenceValid(username, licenceKey)) {
            throw new LicenceInvalideException("Invalid licence. Access denied.");
        } else {
            ReactProjectHandler.createReactProject();
            CRUDGenerator.generateCRUDPages();
            CSSGenerator.generateCSSFile();
        }

    }
}
