package com.claroy.token;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class TokenManager {
 
  private static final int    MAX_PENDING_SIGNATURES = 100;
  private static TokenManager singleton = new TokenManager();
  
  private final SignatureEntry[] signatures = new SignatureEntry[MAX_PENDING_SIGNATURES];
  private int index = 0;
  
  private Map<String, TokenData> tokenStore = new java.util.HashMap<String, TokenData>();
 
  private TokenManager() {}
  
  public static TokenManager getInstance() {
    return singleton;
  }
  
  protected synchronized void addSignatureEntry(SignatureEntry s) {
    signatures[index] = s;
    index = (index + 1) % MAX_PENDING_SIGNATURES;
  }
  
  protected synchronized SignatureEntry findSignatureEntry(String signature) {
    SignatureEntry found = null;
    for(int i = 0; i < signatures.length; i++) {
      if (signatures[i] != null) // signatures puede tener nulos inicialmente mientras no se completa todo el array
        if (signature.equals(signatures[i].getSignature())) {
          found = signatures[i];
          break;
        }
    }
    return found;
  }
  
  public String generarAleatorio() {
    return SecurityUtil.getInstance().generarAleatorio();
  }
  
  public String createSignature(String apikey, TokenData data, String aleat)
  throws GeneralSecurityException, UnsupportedEncodingException {
    return SecurityUtil.getInstance().autenticarMensaje(
            apikey, data.toSecureString() + aleat);
  }
  
  public synchronized void addToken(String token, TokenData data) {
    tokenStore.put(token, data);
  }
  
  public synchronized TokenData removeToken(String token) {
    return tokenStore.remove(token);
  }
}