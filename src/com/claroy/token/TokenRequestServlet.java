package com.claroy.token;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenRequestServlet extends HttpServlet {

  private String tokenDataClassName;
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    tokenDataClassName = config.getServletContext().getInitParameter("com.claroy.TokenDataClass");
  }

  protected void processRequest(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {
    try {
      // En cada petición solo deberá existir un único parámetro de los 3 siguientes
      String idClient = req.getParameter("id");
      String signatureParam = req.getParameter("signature");
      String tokenParam = req.getParameter("token");
      
      TokenManager tm = TokenManager.getInstance();
      if (idClient != null) {                                     // Paso 1 **** Petición de Token ****
        String apikey = findKeyForClient(idClient);
        if (apikey == null) {
          res.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        TokenData data = (TokenData) Class.forName(tokenDataClassName).newInstance();
        data.processTokenRequest(req);
        String aleat = tm.generarAleatorio();
        String signature = tm.createSignature(apikey, data, aleat);
        
        tm.addSignatureEntry(new SignatureEntry(signature, idClient, data));
        // Enviamos el código aleatorio al cliente
        res.addHeader("aleat", aleat);
        res.setStatus(HttpServletResponse.SC_OK);
      } else if ((idClient == null) && (signatureParam != null)) {// Paso 2 **** Autenticación de mensaje ****
        SignatureEntry entry = tm.findSignatureEntry(signatureParam);
        if (entry == null) {
          res.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        String token = tm.generarAleatorio();
        tm.addToken(token, entry.getData());
        // Enviamos el token de acceso al cliente
        res.addHeader("token", token);
        res.setStatus(HttpServletResponse.SC_OK);
      } else if (tokenParam != null) {                            // Paso 3 **** Petición con token del usuario ****
        TokenData data = tm.removeToken(tokenParam);
        if (data == null) {
          res.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        } else {
          processAuthorizedRequest(data, req, res);
        }
      } else {
        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(TokenRequestServlet.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(TokenRequestServlet.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(TokenRequestServlet.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    } catch (GeneralSecurityException ex) {
      Logger.getLogger(TokenRequestServlet.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    }
  }
  
  private String findKeyForClient(String idClient) {
    return Keystore.getInstance().findKeyForClient(idClient);
  }
  
  protected void processAuthorizedRequest(TokenData data, HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /** 
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /** 
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /** 
   * Returns a short description of the servlet.
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}