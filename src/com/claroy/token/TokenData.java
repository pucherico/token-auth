package com.claroy.token;

import javax.servlet.http.HttpServletRequest;
 
public interface TokenData {
 
  /**
   * Procesa los parámetros de la request y los almacena en la instancia que 
   * implementa este interfaz para posteriormente recuperar los estos datos 
   * cuando el usuario envíe la autorización mediante token
   */
  public void processTokenRequest(HttpServletRequest req);
  
  /**
   * Devuelve una cadena de caracteres con información única para el estado del objeto.
   * Deberá intentar que sea única, para ello, todos los atributos se separarán por un caracter
   * no existente en la cadena (i.e. '#').
   * : Ejemplo, los valores ("XX", "YY") y ("XXY", "Y") tendrán representación única textual
   * si los separamos por '#'.
   */
  public String toSecureString();
}