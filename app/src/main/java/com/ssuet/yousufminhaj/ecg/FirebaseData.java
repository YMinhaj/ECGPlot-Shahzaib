package com.ssuet.yousufminhaj.ecg;

/**
 * Created by Yousuf Minhaj on 12/15/2017.
 */

public class FirebaseData {
    private String fname,lname,email,pass;
    public FirebaseData(){}
    public FirebaseData(String fname,String lname,String email, String pass)
    {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.pass = pass;
    }
    public String getfname()
    {
        return fname;
    }
    public String getlname()
    {
        return lname;
    }
    public String getEmail()
    {return email;}
    public String getPass()
    {
        return pass;
    }

}
