package com.appers.ayvaz.thetravelingsalesman.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by D on 009 02 09.
 */
public class LoginUtils{

    private static final byte[] SALT = {
            (byte) 0x04, (byte) 0x08, (byte) 0x15, (byte) 0x16,
            (byte) 0x17, (byte) 0x2a, (byte) 0x10, (byte) 0x12,
    };

    private static final String SECRET = "secret";
    private static final String DEBUG_TAG = "LoginUtils";
    private static final String ALGORITHM = "DES/ECB/PKCS5Padding";
    private static final String PBE = "PBEWithMD5AndDES";
    public static final String PREF_NAME = "loginInfomation";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_LOCKED = "locked";
    public static final String KEY_FIRST_TIME = "firstTime";
    public static final String TIME_LEFT = "time_left";
    public static final long MAX_TIME_LEFT = 15 * 1000;
    public static final int MAX_QUESTION_CHAR = 64;
    public static final String KEY_QUESTION = "securityQuestion";
    public static final String KEY_ANSWER = "answer";
    public static final int MIN_ANSWER_CHAR = 5;
    public static final int MAX_ANSWER_CHAR = 24;


    public static String encrypt(String password) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET.toCharArray()));
            Cipher pbeCipher = Cipher.getInstance(PBE);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return Base64.encodeToString(pbeCipher.doFinal(password.getBytes()), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*private static String encodeBASE64(byte[] data) {
        return new String(data, "UTF-8");
    }*/

    public static String decrypt(String string) {
        try {
            byte[] encrypted = Base64.decode(string, Base64.DEFAULT);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET.toCharArray()));
            Cipher pbeCipher = Cipher.getInstance(PBE);
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean isNameValid(String name) {

        return TextUtils.isEmpty(name) || name.length() < 32;
    }

    public static boolean isPasswordValid(String password) {

        return password.length() >= 4 && password.length() <= 16;
    }






}
