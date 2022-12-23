package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 17/05/2018.
 */

import com.example.baktiar.myapplication.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class CallSoap {
    public final String SOAP_ACTION = "http://tempuri.org/GetJSON";

    public final String OPERATION_NAME = "GetJSON";

    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";


    //Setting untuk TBM atau TAA, pakai SOAP ADDRESS = "http://114.30.80.154/wcf/GetData.asmx"; digunakan untuk internet gunakan ip public
    // jika mau disetting local set ke 192.168.60.1 atau 192.168.57.1
    //Jika untuk retur  SOAP_ADDRESS = "http://192.168.60.1/wcf/GetData1.asmx";
    //UNTUK WCF yang lama

    //  public  final String SOAP_ADDRESS = "http://192.168.60.1/wcf/GetData.asmx";
//  public  final String SOAP_ADDRESS = "http://192.168.57.1/wcf/GetData.asmx";
    public final String SOAP_ADDRESS = "http://114.30.80.154/wcf/GetData.asmx";

    //public final String SOAP_ADDRESS = "http://192.168.60.90/test1/PLC.asmx";
//    public final String SOAP_ADDRESS = "http://192.168.60.90/webservice/GetData.asmx";
//public  final String SOAP_ADDRESS = "http://192.168.60.218/wcf/GetData.asmx";
//String mystring = getResources().getString(R.string.mystring);
    public CallSoap() {

    }

    public String Call(String strAddress, String strbTBM, String strQuery) {
        Element[] header = new Element[1];
        header[0] = new Element().createElement("http://tempuri.org/", "UserDetails");

        Element username = new Element().createElement("http://tempuri.org/", "userName");
        username.addChild(Node.TEXT, "MyAndroid");
        header[0].addChild(Node.ELEMENT, username);

        Element password = new Element().createElement("http://tempuri.org/", "password");
        password.addChild(Node.TEXT, "MyAndroid`12");
        header[0].addChild(Node.ELEMENT, password);

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
//        pi.setName("bTBM");
//        //Setting untuk TBM atau TAA, jika TBM true & jika TAA false
//        //Jika untuk retur value dirubah menjadi strbTBM jika tripilar/triarta diganti true atau false
////        pi.setValue(true);
//        pi.setValue(strbTBM);
//        pi.setType(Boolean.class);
//        request.addProperty(pi);
        //// jangan LUPA UBAH    FirebaseMessaging.getInstance().unsubscribeFromTopic("TAA1"); pada BASEACTIVITY

//
        pi.setName("strCompany");
        //Setting untuk TBM atau TAA atau TPI, jika MOBI
        //Jika untuk retur value dirubah menjadi strbTBM jika tripilar/triarta diganti true atau false
//        pi.setValue(strbTBM);
        pi.setValue("TAA");
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("strQuery");
        pi.setValue(strQuery);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = new Element[1];


        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.headerOut = header;
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        String responseJSON;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        System.out.println("ResponseTest = " + response.toString());
        return response.toString();
    }

    public String Register(String strCompany, String strQuery) {
        Element[] header = new Element[1];
        header[0] = new Element().createElement("http://tempuri.org/", "UserDetails");

        Element username = new Element().createElement("http://tempuri.org/", "userName");
        username.addChild(Node.TEXT, "MyAndroid");
        header[0].addChild(Node.ELEMENT, username);

        Element password = new Element().createElement("http://tempuri.org/", "password");
        password.addChild(Node.TEXT, "MyAndroid`12");
        header[0].addChild(Node.ELEMENT, password);

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
//        pi.setName("bTBM");
//        //Setting untuk TBM atau TAA, jika TBM true & jika TAA false
//        //Jika untuk retur value dirubah menjadi strbTBM jika tripilar/triarta diganti true atau false
////        pi.setValue(true);
//        pi.setValue(strbTBM);
//        pi.setType(Boolean.class);
//        request.addProperty(pi);
        //// jangan LUPA UBAH    FirebaseMessaging.getInstance().unsubscribeFromTopic("TAA1"); pada BASEACTIVITY

//
        pi.setName("strCompany");
        //Setting untuk TBM atau TAA atau TPI, jika MOBI
        //Jika untuk retur value dirubah menjadi strbTBM jika tripilar/triarta diganti true atau false
//        pi.setValue(strbTBM);
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("strQuery");
        pi.setValue(strQuery);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = new Element[1];


        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.headerOut = header;
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        String responseJSON;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        System.out.println("ResponseTest = " + response.toString());
        return response.toString();
    }

    public String LoadDataRegister() {
        SoapObject request = new SoapObject("http://tempuri.org/", "Register");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        String responseJSON;
        try {

            httpTransport.call("http://tempuri.org/Register", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        System.out.println("KRISNA77 "+ response.toString());
        return response.toString();
    }

    public String LoadLaporanPenjualan(String strCompany, String strRole, String strArea) {
        SoapObject request = new SoapObject("http://tempuri.org/", "LoadLaporanPenjualan");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/LoadLaporanPenjualan", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        return response.toString();
    }

    public String ViewLaporanPenjualan(String strCompany, String strRole, String strArea, String strJenisData, String strJenisSatuan, String strPPN, String strDist, Integer iGudang, String strGudang, String strBulanAwal, String strBulanAKhir, String strTahun, String strPenjualanTBM) {
        SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualan");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strJenisData");
        pi.setValue(strJenisData);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strJenisSatuan");
        pi.setValue(strJenisSatuan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPPN");
        pi.setValue(strPPN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strDist");
        pi.setValue(strDist);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("iGudang");
        pi.setValue(iGudang);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strGudang");
        pi.setValue(strGudang);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strBulanAwal");
        pi.setValue(strBulanAwal);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strBulanAKhir");
        pi.setValue(strBulanAKhir);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTahun");
        pi.setValue(strTahun);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPenjualanTBM");
        pi.setValue(strPenjualanTBM);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/ViewLaporanPenjualan", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        return response.toString();
    }

    public String ViewLaporanPenjualanPie(String strCompany, String strRole, String strArea,  String strJenisSatuan, Integer iGudang, String strGudang, String strBulan, String strTahun, String strPenjualanTBM, String strSales) {
        SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanPie");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strSatuan");
        pi.setValue(strJenisSatuan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("iGudang");
        pi.setValue(iGudang);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strGudang");
        pi.setValue(strGudang);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strBulan");
        pi.setValue(strBulan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTahun");
        pi.setValue(strTahun);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPenjualanTBM");
        pi.setValue(strPenjualanTBM);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strSales");
        pi.setValue(strSales);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/ViewLaporanPenjualanPie", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        return response.toString();
    }

    public String LoadLaporanPenjualanDist(String strCompany, String strRole, String strArea) {
        SoapObject request = new SoapObject("http://tempuri.org/", "LoadLaporanPenjualanDist");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/LoadLaporanPenjualanDist", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        System.out.println("KRISNA78 "+ response.toString());
        return response.toString();
    }

    public String ViewLaporanPenjualanDist(String strCompany, String strRole, String strArea, String strJenisData, String strJenisSatuan, String strPPN, Integer iGudang, String strGudang, String strTglAwal, String strTglAkhir, String strFlagTgl, String strPenjualanTBM, String strSales, String strProv ) {
        SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanDist");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strJenisData");
        pi.setValue(strJenisData);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strJenisSatuan");
        pi.setValue(strJenisSatuan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPPN");
        pi.setValue(strPPN);
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("iGudang");
        pi.setValue(iGudang);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strGudang");
        pi.setValue(strGudang);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTglAwal");
        pi.setValue(strTglAwal);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTglAkhir");
        pi.setValue(strTglAkhir);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strFlagTgl");
        pi.setValue(strFlagTgl);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPenjualanTBM");
        pi.setValue(strPenjualanTBM);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strSales");
        pi.setValue(strSales);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strProv");
        pi.setValue(strProv);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/ViewLaporanPenjualanDist", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        System.out.println("KRISNA79 "+ response.toString());
        return response.toString();
    }

    public String ViewLaporanPenjualanDistPie(String strCompany, String strRole, String strArea,  String strSatuan, Integer iGudang, String strGudang, String strTglAwal, String strTglAkhir, String strFlagTgl, String strPenjualanTBM, String strSales, String strCity, String strFlagParam, String strParam ) {
        SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanDistPie");

        PropertyInfo pi = new PropertyInfo();
        pi.setName("strCompany");
        pi.setValue(strCompany);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strRole");
        pi.setValue(strRole);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strArea");
        pi.setValue(strArea);
        pi.setType(String.class);
        request.addProperty(pi);



        pi = new PropertyInfo();
        pi.setName("strSatuan");
        pi.setValue(strSatuan);
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("iGudang");
        pi.setValue(iGudang);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strGudang");
        pi.setValue(strGudang);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTglAwal");
        pi.setValue(strTglAwal);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strTglAkhir");
        pi.setValue(strTglAkhir);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strFlagTgl");
        pi.setValue(strFlagTgl);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strPenjualanTBM");
        pi.setValue(strPenjualanTBM);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strSales");
        pi.setValue(strSales);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strCity");
        pi.setValue(strCity);
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("strFlagParam");
        pi.setValue(strFlagParam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("strParam");
        pi.setValue(strParam);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.bodyOut = request;

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;
        try {
            httpTransport.call("http://tempuri.org/ViewLaporanPenjualanDistPie", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }

        return response.toString();
    }

}



