package com.claroy.token;

public abstract class Keystore {
 
  private static Keystore singleton = new PropertyKeystore();
 
  public static Keystore getInstance() {
    return singleton;
  }
  
  public abstract String findKeyForClient(String idClient);
}