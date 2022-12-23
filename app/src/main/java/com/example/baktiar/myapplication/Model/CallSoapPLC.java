package com.example.baktiar.myapplication.Model;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * Created by TRIPILAR on 26/10/2018.
 */

public class CallSoapPLC {
    //    public final String SOAP_ACTION = "http://tempuri.org/GetJSON";
    public final String SOAP_ACTION = "http://tempuri.org/GetDataTaa1";

    //public  final String OPERATION_NAME = "GetJSON";
    public final String OPERATION_NAME = "GetDataTaa1";

    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";


    //Setting untuk TBM atau TAA, pakai SOAP ADDRESS = "http://114.30.80.154/wcf/GetData1.asmx";
    //Jika untuk retur  SOAP_ADDRESS = "http://192.168.60.1/wcf/GetData1.asmx";

    //        public  final String SOAP_ADDRESS = "http://114.30.80.154/wcf/GetData1.asmx";
//    public  final String SOAP_ADDRESS = "http://192.168.60.1/WCFRetur/WebService1.asmx";
    //public  final String SOAP_ADDRESS = "http://114.30.80.154/WCFRetur/WebService1.asmx";

    //public  final String SOAP_ADDRESS = "http://192.168.60.1/wcf/GetData1.asmx";
    //    public  final String SOAP_ADDRESS = "http://192.168.57.1/wcf/GetData1.asmx";

        public  final String SOAP_ADDRESS = "http://114.30.80.154/wcf/PLC.asmx";
//    public  final String SOAP_ADDRESS = "http://192.168.60.1/wcf/PLC.asmx";
//    public final String SOAP_ADDRESS = "http://192.168.60.90/test1/PLC.asmx";
//public final String SOAP_ADDRESS = "http://192.168.60.90/webservice/PLC.asmx";
    public CallSoapPLC() {
    }

    public String CallPLC(String strQueryPLC, String strQueryPLC2, String strQuery) {

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();


        pi = new PropertyInfo();
        pi.setName("QueryPLC");
        pi.setValue(strQueryPLC);
        pi.setType(String.class);
        request.addProperty(pi);


        pi = new PropertyInfo();
        pi.setName("QueryPLC2");
        pi.setValue(strQueryPLC2);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Query");
        pi.setValue(strQuery);
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
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception)

        {
            exception.printStackTrace();
            response = exception.toString();
        }
        // System.out.println("ResponseTestPLC = " + response.toString());
        return response.toString();
    }
}
