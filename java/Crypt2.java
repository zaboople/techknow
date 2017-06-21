package org.tmotte.klonk.io;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Crypt2 {

  // True constants:
  private final static int keySize=128;
  private final static int iterations=1024*1024;
  private final static Charset utf8=Charset.forName("UTF-8");

  private final static String pbeAlgorithm="PBEWithHmacSHA256AndAES_";
  private final static int paramsLength=104;

  // Things that are needed only once:
  private final static Base64.Encoder base64Encoder=Base64.getEncoder();
  private final static Base64.Decoder base64Decoder=Base64.getDecoder();


  public static String encryptToBase64(char[] pass, int keySize, CharSequence data) throws Exception {
    String algorithm=pbeAlgorithm+keySize;
    SecretKey myKey=getSecretKey(pass, algorithm);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, myKey);
    byte[] cipherText = cipher.doFinal(data.toString().getBytes(utf8));
    byte[] params = cipher.getParameters().getEncoded();
    if (params.length != paramsLength)
      throw new Exception("Cannot encrypt because params are wrong "+params.length);

    byte[] result=new byte[params.length + cipherText.length];
    System.arraycopy(params, 0, result, 0, params.length);
    System.arraycopy(cipherText, 0, result, params.length, cipherText.length);

    return base64Encoder.encodeToString(result);
  }

  public static String decryptFromBase64(char[] pass, int keySize, CharSequence base64Data) throws Exception {
    String algorithm=pbeAlgorithm+keySize;
    SecretKey myKey=getSecretKey(pass, algorithm);

    byte[] original=base64Decoder.decode(base64Data.toString().getBytes(utf8));
    byte[] params=new byte[paramsLength];
    byte[] encryptedBytes=new byte[original.length - params.length];

    System.arraycopy(original, 0, params, 0, params.length);
    System.arraycopy(original, params.length, encryptedBytes, 0, encryptedBytes.length);

    AlgorithmParameters algParams=AlgorithmParameters.getInstance(algorithm);
    algParams.init(params);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, myKey, algParams);
    return new String(cipher.doFinal(encryptedBytes), utf8);
  }

  public static SecretKey getSecretKey(char[] pass, String algorithm) throws Exception {
    byte[] salt=new byte[8];
    new SecureRandom().nextBytes(salt);
    return SecretKeyFactory.getInstance(algorithm)
      .generateSecret(
        new PBEKeySpec(pass, salt, iterations, keySize)
      );
  }


  //////////////
  // TESTING: //
  //////////////

  public static void main(String[] args) throws Exception {
    System.out.println("Args length "+args.length);
    if (args.length<2)
      System.err.println("Need a password, and some strings to pass thru");
    System.out
      .append("\n---\n")
      .append("Password=")
      .append(args[0])
      .append("\n");
    char[] pass=args[0].toString().toCharArray();
    for (int i=1; i<args.length; i++)
      debugRoundTrip(pass, args[i]);
    debugRoundTrip(pass, "Mashed\n+Potatoes+\nGravy");
  }
  private static void debugRoundTrip(char[] pass, CharSequence data) throws Exception {
    System.out.println("------------------");
    System.out.append("  --Data:\n  ").append(data).append("\n");
    String encrypted=encryptToBase64(pass, 128, data);
    System.out.append("  --Encrypted:\n  "+encrypted);
    System.out.println();
    System.out.println("  --Decrypted:\n  "+decryptFromBase64(pass, 128, encrypted));
  }
  private static void debugBytes(byte[] bytes) {
    for (byte b: bytes)
      System.out.print(String.format("%04x ", b));
  }

}