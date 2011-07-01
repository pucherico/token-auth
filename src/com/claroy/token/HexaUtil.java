package com.claroy.token;

public final class HexaUtil {
 
  private static final char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7',
                                      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  private static final String hextring = "0123456789abcdef";
 
  private HexaUtil() {
  }
  
  public static String toHex(byte[] src) {
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < src.length; i++) {
      int low = src[i] & 0x0f;
      int hi = (src[i] >>> 4) & 0x0f;
      sb.append(hexa[hi]).append(hexa[low]);
    }
    return sb.toString();
  }
  
  public static byte[] toBytes(String src) {
    
    char[] source = src.toLowerCase().toCharArray();
    byte[] dest;
    
    if ((source.length % 2) != 0) {
      dest = null; // Error, no hay un n�mero par de caracteres
    } else {
      dest = new byte[source.length / 2];
      int j;
      for (int i = j = 0; i < source.length; i += 2, j++) {
        char hi = source[i];
        char low = source[i + 1];
        dest[j] = (byte)((hextring.indexOf(hi) << 4) | hextring.indexOf(low));
      }
    }
    return dest;
  }
}