package com.example.baktiar.myapplication.Model;

import android.app.AlertDialog;

import com.example.baktiar.myapplication.Control.PemakaianBarangActivity;
import com.example.baktiar.myapplication.Control.PerbandinganPemakianBahanActivity;

/**
 * Created by TRIPILAR on 26/10/2018.
 */

public class CallerPLC  extends Thread
{
    public CallSoapPLC cs;
    public String strQueryPLC,strPassword,strQuery,strQueryPLC2;
    public AlertDialog ad;
    public void run(){
        try{
            cs=new CallSoapPLC();
            String resp=cs.CallPLC( strQueryPLC,strQueryPLC2,strQuery);
            PerbandinganPemakianBahanActivity.rslt=resp;
            PemakaianBarangActivity.rslt=resp;

        }catch(Exception ex)
        {PerbandinganPemakianBahanActivity.rslt=ex.toString();
            PemakaianBarangActivity.rslt=ex.toString();

        }
    }
}
