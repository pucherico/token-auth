package com.claroy.token;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {
 
  private static final SecurityUtil singleton = new SecurityUtil();
 
  private SecureRandom random = new SecureRandom();

  public static SecurityUtil getInstance() {
    return singleton;
  }

  private SecurityUtil() {}
  
  public String generarAleatorio() {
    byte[] bytes = new byte[16];
    random.nextBytes(bytes);
    return HexaUtil.toHex(bytes);
  }

  public String autenticarMensaje(String secretKey, String message)
    throws UnsupportedEncodingException, GeneralSecurityException {
    SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF8"), "HmacMD5");
    Mac mac = Mac.getInstance(key.getAlgorithm());
    mac.init(key);
    return HexaUtil.toHex(mac.doFinal(message.getBytes("UTF8")));
  }
}