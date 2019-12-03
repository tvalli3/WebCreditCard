
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<script src="ajax.js" type="text/javascript"></script>
<script src="history.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
    function pageAction(action)
    {
        document.card.actiontype.value=action;
        if (ajax && action === 'history') {
            ajax.open('get','AccountAction?actiontype=history');
            ajax.send(null);
        } else {
            document.card.submit();
        }
    }
</script>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Credit Card</title>
    </head>
    <h1>Credit Card Simulator</h1><br>
    <body bgcolor="tan">
        <form action="AccountAction" name="card" id="card" method="post">
            <table border="0">
            <tr>
            <td>Account ID:</td>
            <td><input type="text" name="account" id="account" 
                       value="${empty card.accountId ? cookie.acct.value : card.accountId}"/></td>
            <td><input type="submit" value="New Account" onclick="pageAction('new')"></td>
            <td><input type="submit" value="Existing" onclick="pageAction('existing')"></td>
            </table>
            <hr/>         
            <table>
                <tr>
                    <td>Charge Amt:</td>
                    <td><input type="text" name="cAmt" id="cAmt" size="15"></td>
                    <td width="50" align="right">Desc:</td>
                    <td><input type="text" name="cDesc" id="cDesc" size="40"></td>
                    <td><input type="button" name="charge" id="charge" value="Post Charge" onclick="pageAction('charge')"</td>
                </tr>
                <tr>
                    <td>Payment Amt:</td>
                    <td><input type="text" name="pAmt" id="pAmt" size="15"></td>
                    <td></td>
                    <td><input type="button" name="payment" id="payment" value="Post Payment" onclick="pageAction('payment')"</td>
                </tr>
                <tr>
                    <td>Credit Increase:</td>
                    <td><input type="text" name="cIncrease" id="cIncrease" size="15"></td>
                    <td></td>
                    <td><input type="button" name="Increase" id="Increase" value="Credit Increae" onclick="pageAction('increase')"</td>
                </tr>
                <tr>
                    <td>Interest Rate:</td>
                    <td><input type="text" name="iRate" id="iRate" size="15"></td>
                    <td></td>
                    <td><input type="button" name="interest" id="interest" value="Interest Charge" onclick="pageAction('interest')"</td>
                    <td><input type="button" name="history" id="history" value="Display History" onclick="pageAction('history')">
                </tr>
            </table><br>
            <br>
            <hr><br>
            <table border="1">
                <tr>
                    <td>Credit Limit:</td>
                    <td><input type="text" name="climit" id="climit" readonly
                               value="<fmt:formatNumber value="${card.creditLimit}" type="currency"/>"/></td>
                </tr>
                <tr>
                    <td>Credit Balance Due:</td>
                    <td><input type="text" name="cbal" id="cbal" readonly
                               value="<fmt:formatNumber value="${card.outstandingBal}" type="currency"/>"/></td>
                </tr>
                <tr>
                    <td>Credit Available Credit:</td>
                    <td><input type="text" name="availablecr" id="availablecr" readonly
                               value="<fmt:formatNumber value="${card.availableCr}" type="currency"/>"/></td>
                </tr>
            </table>
            <input type="hidden" name="actiontype" id="actiontype" value="">
        </form>
        <br>
        ${msg}
        <br>
        <div id="results"></div>
        
        <%
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c: cookies) {
        %>
                    <%= c.getName() %> = <%= c.getValue() %><br>
        <%
                }
            }
        %>
    </body>
</html>
