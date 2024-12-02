package org.example.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Génère un mot de passe aléatoire sécurisé.
     *
     * @param length Longueur du mot de passe.
     * @return Un mot de passe sécurisé sous forme de chaîne.
     */
    public static String generateSecurePassword(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Simule la génération d'un code d'authentification à deux facteurs (2FA).
     *
     * @return Un code à 6 chiffres.
     */
    public static String generate2FACode() {
        int code = secureRandom.nextInt(1_000_000);
        return String.format("%06d", code);
    }

    /**
     * Vérifie si le code 2FA est valide.
     *
     * @param inputCode Le code saisi par l'utilisateur.
     * @param actualCode Le code attendu.
     * @return Vrai si le code est correct, sinon faux.
     */
    public static boolean validate2FACode(String inputCode, String actualCode) {
        return inputCode.equals(actualCode);
    }
}
