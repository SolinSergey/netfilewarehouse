package ru.gb.service;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CryptService {
    public String getCryptString(String stringToCrypt) {
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(stringToCrypt.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
