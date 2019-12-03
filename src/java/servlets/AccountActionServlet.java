package servlets;

import business.CreditCard;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tom Valli
 */

/* 
I experimented a little bit and changed a few things around from the lecture.
I changed the "msg" variable from String to StringBuilder partially as an
experiment and also because I read that it's more efficient than constantly
modifying an immutable String variable (I don't think it actually makes a 
difference here though).  I also changed the series of "if" statements to a 
switch for readability (although it might be more efficient as well, I don't 
really know.  I declared a few variables closer to their single uses as I 
understand that to be best-practice.  Lastly,  I moved some repeated code to 
the actionMsg() method. My original version of the code is in the
OLDAccountActionServlet class which I kept here as a fallback.
*/

public class AccountActionServlet extends HttpServlet {
    
CreditCard card;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "/CardTrans.jsp";
        StringBuilder msg = new StringBuilder();
        
        try {
            
            String path = getServletContext().getRealPath("/WEB-INF/") + "\\";
            card = (CreditCard) request.getSession().getAttribute("card");
            String action = request.getParameter("actiontype");
            if (card == null && 
                    !action.equalsIgnoreCase("NEW") && 
                    !action.equalsIgnoreCase("EXISTING")) {
                msg.append("Action attempt on unopened card.<br>");
            } else { //Here's where input is parsed and sent to the appropriate CreditCard setter for processing
                switch (action.toUpperCase()) {
                    case "NEW":
                        try {
                            card = new CreditCard(path);
                            msg.append(actionMsg("Account Open Error: "));
                        } catch (Exception e) {
                            msg.append("Invalid acct number input.<br>");
                        }
                        break;
                    case "EXISTING":
                        try{
                            int acctno = Integer.parseInt(request.getParameter("account"));
                            card = new CreditCard(acctno, path);
                            msg.append(actionMsg("Account re-open error:"));
                        } catch (Exception e) {
                            msg.append("Problem loading account number. <br>");
                        }
                        break;
                    case "HISTORY":
                        url = "History.jsp";
                        break;
                    case "CHARGE":
                        try {
                            double chgamt = Double.parseDouble(request.getParameter("cAmt"));
                            String chgdesc = request.getParameter("cDesc");
                            card.setCharge(chgamt, chgdesc);
                            msg.append(actionMsg("Charge error: "));
                        } catch (Exception e) {
                            msg.append("Invalid charge or description input.<br>");
                        }
                        break;
                    case "PAYMENT":
                        try {
                            double payment = Double.parseDouble(request.getParameter("pAmt"));
                            card.setPayment(payment);
                            msg.append(actionMsg("Payment error: "));
                        } catch (Exception e) {
                            msg.append("Invalid payment amount.<br>");
                        }
                        break;
                    case "INCREASE":
                        try {
                            double creditIncrease = Double.parseDouble(request.getParameter("cIncrease"));
                            card.setCreditIncrease(creditIncrease);
                            msg.append(actionMsg("Credit increase error: "));
                        } catch (Exception e) {
                            msg.append("Invalid credit increase amount.<br>");
                        }
                        break;
                    case "INTEREST":
                        try {
                            double intChg = Double.parseDouble(request.getParameter("iRate"));
                            card.setInterestCharge(intChg);
                            msg.append(actionMsg("Interest charge error: "));
                        } catch (Exception e) {
                            msg.append("Invalid interest charge amount.<br>");
                        }
                        break;
                }

                request.getSession().setAttribute("card", card);
                Cookie acct = 
                        new Cookie("acct", String.valueOf(card.getAccountId()));
                acct.setPath("/");
                acct.setMaxAge(60*2);
                response.addCookie(acct);
            }
            
        } catch (Exception e) {
            String errMsg = "Servlet error: " + e.getMessage() + "<br>";
            msg.append(errMsg);
        }
        
        request.setAttribute("msg", msg.toString());
        RequestDispatcher disp = getServletContext().getRequestDispatcher(url);
        disp.forward(request,response);
        
    }
    
    private String actionMsg(String msgPrefix) {
        String message;
        if (card.getErrorStatus()) {
            message = msgPrefix + card.getErrorMessage() + "<br>";
            
        } else {
            message = card.getActionMsg() + "<br>";
        }
        return message;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
