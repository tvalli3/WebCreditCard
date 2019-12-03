package business;

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;

public class CreditCard {
    private int acctno = 0;
    private String path;
    private double climit;
    private double cbal;
    private String cerrmsg = "";  //error message (abnormal result)
    private String cmsg = "";    //action message (normal process result)
    private NumberFormat f = NumberFormat.getCurrencyInstance();

    public CreditCard(String path) {   //constructor for new account number
        this.climit = 0;
        this.cbal = 0.0;
        this.path = path;
        cerrmsg = "";
        cmsg = "";

        while (acctno == 0) {
            acctno = (int) (Math.random() * 1000000);
            try {       //for each new acct. no. generated see if that acct. no. file already exist
                BufferedReader in = new BufferedReader(new FileReader(
                        path + "CC" + acctno + ".txt"));
                in.close();
                acctno = 0;
            } catch (IOException e) {  //if a file does not exist the new acct. not is good
                this.climit = 1000;
                cerrmsg = writeStatus();
                if (cerrmsg.isEmpty()) {
                    cmsg = "Account " + acctno + " created.";
                    cerrmsg = writeLog(cmsg);
                }
            }
        }
        if (acctno == 0) {
            cerrmsg = cerrmsg + "Unable to genterate account number.";
        }
    }

    public CreditCard(int AccountNumber, String path) {  //constructor for existing account number
        cerrmsg = "";
        cmsg = "";
        this.path = path;
        try {
            acctno = AccountNumber;
            BufferedReader in = new BufferedReader(new FileReader(
                path + "CC" + AccountNumber + ".txt"));
            climit = Double.parseDouble(in.readLine());
            cbal = Double.parseDouble(in.readLine());
            in.close();
        } catch (NumberFormatException e) {
            cerrmsg = "Account " + acctno + ": CC File data format not correct.  " +e;
        } catch (FileNotFoundException e) {
            cerrmsg = "Account " + acctno + ": CC File could not be opened.  " +e;
        } catch (Exception e) {
            cerrmsg = "Account " + acctno + ": CC File could not be read.  " +e;
        }
        if (!cerrmsg.isEmpty()) {
            cbal = 0;
            climit = 0;
            this.path = "";
        }
    }

    public void setCharge(double a, String d) {
        cerrmsg = "";
        cmsg = "";

        if (a > climit - cbal) {
            cmsg = "Credit Card Charge: " + f.format(a) + " for " + d + " declined - over limit";
            cerrmsg = writeLog(cmsg);
        } else {
            cbal += a;
            cerrmsg = writeStatus();
            if (cerrmsg.isEmpty())
               cmsg = "Credit Card Charge: " + f.format(a) + " for " + d;
               cerrmsg = writeLog(cmsg);
        }
    }

    public void setPayment(double payment) {
        cerrmsg = "";
        cmsg = "";

        if (payment < 0) {
            cmsg = "Credit Card payment of: " + payment + " declined: must be positive value.";
            cerrmsg = writeLog(cmsg);
        } else {
            cbal -= payment;
            cmsg = ("Credit Card Payment: " + f.format(payment));
            cerrmsg = writeStatus();
            if (cerrmsg.isEmpty()) {
                cerrmsg = writeLog(cmsg);
            }
        }
    }

    public void setCreditIncrease(double r) {
        cerrmsg =  "";
        cmsg = "";

        if (r < 100) {
            cmsg = "Credit Increase of " + r + " declined: minimum increase is $100.";
            cerrmsg = writeLog(cmsg);
        } else {
            if (((int) (Math.random() * 10)) % 2 == 0) {   //random approvals
                r = r - (r % 100);  //increase are only granted in $100 increments.
                climit += r;
                cmsg = ("Credit Limit increased to " + f.format(climit) + ".");
                cerrmsg = writeStatus();
                if (cerrmsg.isEmpty()) {
                    cerrmsg = writeLog(cmsg);
                }
             } else {
                cmsg = "Credit Increase of " + r + " declined.";
                cerrmsg = writeLog(cmsg);
            }
        }
    }

    public void setInterestCharge(double r) {
        cerrmsg = "";
        cmsg = "";

        cmsg = ("Interest Charge of " + f.format((cbal * (r / 12))) +
                "\n applied for " + (r * 100) + "% APR.");
        cbal += cbal * (r / 12);
        cerrmsg = writeStatus();
        if (cerrmsg.isEmpty()) {
            cerrmsg = writeLog(cmsg);
        }
    }

    public ArrayList<String> getCreditHistory() {
        cerrmsg = "";
        cmsg = "";
        ArrayList<String> h = new ArrayList<String>();
        String s;

        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(this.path + "CCL" + acctno + ".txt"));
            s = in.readLine();
            while (s != null) {
                h.add(s);
                s = in.readLine();
            }
        } catch (IOException e) {
            h.clear();
            cerrmsg = "Error reading log history. " + e.getMessage();
        }
        return h;
    }

    private String writeStatus() {
        String result = "";
        try {
            PrintWriter out = new PrintWriter(new FileWriter(this.path + "CC" 
                    + acctno + ".txt"));
            out.println(climit);
            out.println(cbal);
            out.close();
        } catch (IOException e) {
            result = "Error writing status: " + e.getMessage();
        }
        return result;
    }

    private String writeLog(String desc) {
        String result = "";
        try {
            PrintWriter out = new PrintWriter(new FileWriter(this.path + "CCL" 
                    + acctno + ".txt", true));
            Calendar cal = Calendar.getInstance();
            DateFormat df = DateFormat.getInstance();
            String datestamp = df.format(cal.getTime());
            out.println(desc + "\t" + datestamp);
            out.close();
        } catch (IOException e) {
            result = "Error writing log: " + e.getMessage();
        }
        return result;
    }

    public int getAccountId() {
        return acctno;
    }

    public double getOutstandingBal() {
        return cbal;
    }

    public double getCreditLimit() {
        return climit;
    }

    public double getAvailableCr() {
        return climit - cbal;
    }
    public boolean getErrorStatus() {
        boolean eresult = false;
        if (!cerrmsg.isEmpty()) {
            eresult = true;
        }
        return eresult;
    }
    public String getErrorMessage() {
        return cerrmsg;
    }
    public String getActionMsg() {
        return cmsg;
    }
}
