package com.example.tharani.weatherapp;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


//created class Weather_Handler
public class Weather_Handler {
    private static final String TAG= Weather_Handler.class.getSimpleName();
    //taking Weather_handler to private which can access within this class
    public Weather_Handler(){ // Creating constructor

    }
    /*making service call and requesting Url
    * representing a Uniform Resource Locator, a pointer to a "resource" on the World Wide Web*/
    public String makeServiceCall(String reqUrl) {
        String response = null;//taking string response to null
        try {// try block is used to enclose the code that might throw an exception
            URL url = new URL(reqUrl);//creating new url

            /*
             HttpURLConnection will follow up to five HTTP redirects.
             It will follow redirects from one origin server to another
             */
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
            //creating new BufferedInputStream object ,class is used to read information from stream
            response = convertStreamToString(inputStream);//converting stream to string
        }catch (MalformedURLException e){//. A try block is always followed by a catch block, which handles the exception that occur in catch block
            Log.e(TAG,"MalformedURLException: " + e.getMessage());
            /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
        }catch (ProtocolException e){//catches ProtocolException

            Log.e(TAG, "ProtocolException: " + e.getMessage());//taking log for ProtocolException
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());//taking log for IoException
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());//taking log for exception
        }
        return response;//returning response

    }
/** To convert the InputStream to String we use the BufferedReader.readLine()
 * method. We iterate until the BufferedReader return null which means
 * there's no more data to read. Each line will appended to a StringBuilder
 * * and returned as String*/
    private String convertStreamToString(InputStream inStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {// try block is used to enclose the code that might throw an exception
            while ((str = bufferedReader.readLine()) != null) {//taking While loop
                stringBuilder.append(str).append('\n');
                /*here read line is used to read line by line
                * The append method always adds these characters at the end of the existing character sequence, while the insert method adds the characters at a specified point.
                 *The StringBuilder class is used to create mutable string*/
            }
        } catch (IOException e) {//catches IOException
            e.printStackTrace();//where "e" is the exception. It prints the stack trace
        } finally {
            /*try block is followed by catch or finally block
            *  finally block is a block that is used to execute important code such as closing connection, stream etc*/
            try {
                inStream.close();//closes
            } catch (IOException e) {
                e.printStackTrace();// It prints several lines in the output console
            }
        }

        return stringBuilder.toString();//returning stringBuilder
    }
}
