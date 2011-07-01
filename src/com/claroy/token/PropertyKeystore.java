package com.claroy.token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyKeystore extends Keystore {
 
  private Properties properties;
 
  protected PropertyKeystore() {
    super();
  }
  
  @Override
  public String findKeyForClient(String idClient) {
    if (properties == null) {
      this.properties = new Properties();
      try {
        properties.load(getClass().getResourceAsStream("/META-INF/apikeys.properties"));
        String externalKeystore = properties.getProperty("keystore");
        if (externalKeystore != null) {
          properties.load(new FileReader(new File(externalKeystore)));
        }
      } catch (IOException ex) {
        Logger.getLogger(PropertyKeystore.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return properties.getProperty(idClient);
  }
}