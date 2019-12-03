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
public class OLDAccountActionServlet extends HttpServlet {
    
CreditCard card;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "/CardTrans.jsp";
        String msg = "", chgDesc="";
        //CreditCard card;
        int acctno;
        double transamt;
        
        try {
            
            String path = getServletContext().getRealPath("/WEB-INF/") + "\\";
            card = (CreditCard) request.getSession().getAttribute("card");
            String action = request.getParameter("actiontype");
            if (card == null && 
                    !action.equalsIgnoreCase("NEW") && 
                    !action.equalsIgnoreCase("EXISTING")) {
                msg += "Action attempt on unopened card.<br>";
            } else {
                if (action.equalsIgnoreCase("NEW")) {
                    card = new CreditCard(path);
                    if (card.getErrorStatus()) {
                        msg += "Account open error: " + 
                                card.getErrorMessage() + "<br>";
                    } else {
                        msg += card.getActionMsg() + "<br>";
                    }
                }
                if (action.equalsIgnoreCase("EXISTING")) {
                    try {
                        acctno = Integer.parseInt(request.getParameter("account"));
                        card = new CreditCard(acctno, path);
                        msg += actionMsg("Account re-open error:");
                    } catch (Exception e) {
                        msg += "Problem loading account number. <br>";
                    }
                }
                if (action.equalsIgnoreCase("HISTORY")) {
                    url = "/History.jsp";
                }
                if (action.equalsIgnoreCase("CHARGE")) {
                    try {
                        double chgamt = Double.parseDouble(request.getParameter("cAmt"));
                        String chgdesc = request.getParameter("cDesc");
                        card.setCharge(chgamt, chgdesc);
                        msg += actionMsg("Charge error: ");
                    } catch (Exception e) {
                        msg += "Invalid charge or description input.<br>";
                    }
                }
                if (action.equalsIgnoreCase("PAYMENT")) {
                    try {
                        double payment = Double.parseDouble(request.getParameter("pAmt"));
                        card.setPayment(payment);
                        msg += actionMsg("Payment error: ");
                    } catch (Exception e) {
                        msg += "Invalid payment amount.<br>";
                    }
                }
                if (action.equalsIgnoreCase("INCREASE")) {
                    try {
                        double creditIncrease = Double.parseDouble(request.getParameter("cIncrease"));
                        card.setCreditIncrease(creditIncrease);
                        msg += actionMsg("Credit increase error: ");
                    } catch (Exception e) {
                        msg += "Invalid credit increase amount.<br>";
                    }
                }
                if (action.equalsIgnoreCase("INTEREST")) {
                    try {
                        double intChg = Double.parseDouble(request.getParameter("iRate"));
                        card.setInterestCharge(intChg);
                        msg += actionMsg("Interest charge error: ");
                    } catch (Exception e) {
                        msg += "Invalid interest charge amount.<br>";
                    }
                }
                request.getSession().setAttribute("card", card);
                Cookie acct = 
                        new Cookie("acct", String.valueOf(card.getAccountId()));
                acct.setPath("/");
                acct.setMaxAge(60*2);
                response.addCookie(acct);
            }
            
        } catch (Exception e) {
            msg += "Servlet error: " + e.getMessage() + "<br>";
        }
        
        request.setAttribute("msg", msg);
        RequestDispatcher disp = getServletContext().getRequestDispatcher(url);
        disp.forward(request,response);
        
    }
    
    private String actionMsg(String msgPrefix) {
        String msg = "";
        if (card.getErrorStatus()) {
            msg += msgPrefix + card.getErrorMessage() + "<br>";
        } else {
            msg += card.getActionMsg() + "<br>";
        }
        return msg;
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
