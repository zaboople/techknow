package org.tmotte.common.io;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

  private final static int keySize=128;

  private final static Charset utf8=Charset.forName("UTF-8");
  private final static String padding="AES/CBC/PKCS5PADDING";
  private final static Base64.Encoder base64Encoder=Base64.getEncoder();
  private final static Base64.Decoder base64Decoder=Base64.getDecoder();

  public static String encrypt(SecretKey secretKey, CharSequence data) throws Exception {
    byte[] ivBytes = new byte[keySize / 8];
    SecureRandom prng = new SecureRandom();
    prng.nextBytes(ivBytes);

    Cipher aesCipher = Cipher.getInstance(padding);
    aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
    byte[] encryptedBytes = aesCipher.doFinal(data.toString().getBytes(utf8));

    byte[] result=new byte[ivBytes.length + encryptedBytes.length];
    System.arraycopy(ivBytes, 0, result, 0, ivBytes.length);
    System.arraycopy(encryptedBytes, 0, result, ivBytes.length, encryptedBytes.length);
    return base64Encoder.encodeToString(result);
  }

  public static String decrypt(SecretKey secretKey, CharSequence base64Data) throws Exception {
    byte[] original=base64Decoder.decode(base64Data.toString().getBytes());
    byte[] ivBytes=new byte[keySize / 8];
    byte[] encryptedBytes=new byte[original.length - ivBytes.length];

    System.arraycopy(original, 0, ivBytes, 0, ivBytes.length);
    System.arraycopy(original, ivBytes.length, encryptedBytes, 0, encryptedBytes.length);

    Cipher aesCipher = Cipher.getInstance(padding);
    aesCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
    return new String(aesCipher.doFinal(encryptedBytes), utf8);
  }

  private static byte[] getBytes(CharSequence s) {
    if (s.length()==0)
      throw new IllegalArgumentException("0-length string");
    return s.toString().getBytes(utf8);
  }


  public static SecretKey getSecretKey(CharSequence s) {
    return getSecretKey(toKeyBytes(s));
  }

  public static SecretKey getSecretKey(byte[] variable) {
    return new SecretKeySpec(toKeyBytes(variable), "AES");
  }

  private static String toKeyBase64(CharSequence password) {
    return base64Encoder.encodeToString(toKeyBytes(getBytes(password)));
  }

  private static byte[] toKeyBytes(CharSequence password) {
    return toKeyBytes(getBytes(password));
  }

  private static byte[] toKeyBytes(byte[] inBytes) {
    byte[] result = new byte[keySize/8];
    int filled=0, used=0, maxUsed=0;
    while (filled < result.length || maxUsed < inBytes.length){
      if (filled==result.length)
        filled=0;
      if (used==inBytes.length){
        for (int i=0; i<inBytes.length; i++) inBytes[i]+=1;
        used=0;
      }
      result[filled]=(byte)(result[filled] ^ inBytes[used]);
      used++;
      filled++;
      maxUsed++;
    }
    return result;
  }


  //////////////
  // TESTING: //
  //////////////

  public static void main(String[] args) throws Exception {
    System.out.println("Args length "+args.length);
    SecretKey key=null;
    System.out.println("-----");
    byte[] salty={1, 2, 3, 4, 1, 2, 3, 4};
    debugBytes(salty);
    for (int i=0; i<args.length; i++){
      if (i==0) {
        System.out.println("Key "+args[i]);
        System.out.println(" ...Or "+toKeyBase64(args[i]));
        //        key=getSecretKey(args[i]);
        key=getSecretKey(args[i]);
        System.out.println("------------");
        String encrypted=encrypt(key, "yes\nno\nagain");
        System.out.println(encrypted);
        System.out.println(decrypt(key, encrypted));
      }
      else {
        System.out.println("------------");
        System.out.println(args[i]);
        String encrypted=encrypt(key, args[i]);
        System.out.println(encrypted);
        System.out.println(decrypt(key, encrypted));
      }
    }
  }
  private static void debugKey(CharSequence pass) {
    debugBytes(toKeyBytes(pass));
    System.out.println(" "+pass);
  }
  private static void debugBytes(byte[] bytes) {
    for (byte b: bytes)
      System.out.print(String.format("%04x ", b));
  }

}