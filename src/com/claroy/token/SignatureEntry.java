package com.claroy.token;

public class SignatureEntry {
 
  private final String signature, idClient;
  private final TokenData data;
 
  public SignatureEntry(String signature, String idClient, TokenData data) {
    this.signature = signature;
    this.idClient = idClient;
    this.data = data;
  }

  public TokenData getData() {
    return data;
  }

  public String getIdClient() {
    return idClient;
  }

  public String getSignature() {
    return signature;
  }
}