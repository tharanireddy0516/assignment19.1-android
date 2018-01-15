package com.example.tharani.weatherapp;
/*import is libraries imported for writing the code
* AppCompatActivity is base class for activities
* Bundle handles the orientation of the activity
*/
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    /*onCreate is the first method in the life cycle of an activity
    savedInstance passes data to super class,data is pull to store state of application
  * setContentView is used to set layout for the activity
  *R is a resource and it is auto generate file
  * activity_main assign an integer value*/
    // Declaring variables
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private String TAG = MainActivity.class.getSimpleName();
    String location_name;
    String[] weatherStr;
    String coordStr;
    int temp_min;
    int temp_max;
    int humidity;
    long sunrise, sunset;
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initializing objects by ID
        textView1 =  findViewById(R.id.location);
        textView2 =  findViewById(R.id.coordinate);
        textView3 = findViewById(R.id.weather);
        textView4 =  findViewById(R.id.temperature);
        textView5 =  findViewById(R.id.humidity);
        textView6 =  findViewById(R.id.sunrise);
        textView7 =  findViewById(R.id.sunset);

        contactList = new ArrayList<>();
        new GetWeather().execute();//getsWeather using Get method
    }
    /** creating private class GETWeather which extending AsyncTask
     *  AsyncTask is an abstract class provided by Android which gives us the liberty to perform heavy tasks in the background and
     *  keep the UI thread light thus making the application more responsive.
     *  onPreExecute:This method contains the code which is executed before the background processing starts
     *  doInBackground: This method contains the code which needs to be executed in background.
     *  In this method we can send results multiple times to the UI thread by publishProgress() method*/
    private class GetWeather extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {//using Protected method
            super.onPreExecute();// super() can be used to invoke immediate parent class constructor
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
             /*A toast is a view containing a quick little message
                LENGTH_LONG Show the view or text notification for a long period of time*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {//once we use void cannot return any method

            String url = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
            // representing a Uniform Resource Locator, a pointer to a "resource" on the World Wide Web,taking API link to display weather information
            Weather_Handler weatherHandler = new Weather_Handler();//creating new Weather_Handler

            String jsonStr = weatherHandler.makeServiceCall(url);
            weatherStr = new String[2];//creating new string for weatherStr
            Log.e(TAG, "Response from url: " + jsonStr);
            /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/

            if (jsonStr != null) {//taking if statement to check weather the jsonStr is null or not
                try {//try is followed by catch
                    JSONObject jsonObject = new JSONObject(jsonStr);//creating new jsonObject
                    JSONObject jsonObject_coord = jsonObject.getJSONObject("coord");//gets JSONObject
                    /*taking location_name,lon,lat using string*/
                    location_name = jsonObject.getString("name");

                    String lon = jsonObject_coord.getString("lon");
                    String lat = jsonObject_coord.getString("lat");
                    coordStr = "Lon : " + lon + ", " + "Lat : " + lat;
                    //creating new object for weather
                    JSONArray weathers = jsonObject.getJSONArray("weather");
                    int temp;//initializing temp using interger
                    String weath_description = "";//here we can write weath_description

                    for (int i = 0; i < weathers.length(); i++) {//taking for loop
                        JSONObject weath = weathers.getJSONObject(i);//using get method to get weathers

                        weath_description = weath.getString("description");//here taking description
                    }
                    /**Here getInt retrieve an int value from the preferences */
                    weatherStr[0] = weath_description;//here taking weatherStr to 0
                    JSONObject main = jsonObject.getJSONObject("main");
                    temp = main.getInt("temp");
                    weatherStr[1] = "Avg.Temp : " + Integer.toString(temp) + "F";
                   //here converting WeatherStr to integer to string using Integer.toString
                    JSONObject jsonObject_temp = jsonObject.getJSONObject("main");
                    /*taking integer values to store Temp_min,temp_max,humidity using get method */
                    temp_min = jsonObject_temp.getInt("temp_min");
                    temp_max = jsonObject_temp.getInt("temp_max");
                    humidity = jsonObject_temp.getInt("humidity");
                    //creating new object jsonObject_sun
                    JSONObject jsonObject_sun = jsonObject.getJSONObject("sys");
                    /*taking values of sunrise and sunset using get method*/
                    sunrise = jsonObject_sun.getLong("sunrise");
                    sunset = jsonObject_sun.getLong("sunset");
                } catch (final JSONException e) {//if the errors not handled in try block then we will use catch block
                    //here declaring JSON as final that can be changed
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    /*here taking Log.e method to write logs and displaying
                * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs*/
                    runOnUiThread(new Runnable() {
                        //runOnUiThread() is launching a new background thread in Runnable state which be waiting for other resources from the operating system such as processor.
                        @Override
                        public void run() {//thread is in run state
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            /*A toast is a view containing a quick little message
                LENGTH_LONG Show the view or text notification for a long period of time*/
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                /*here taking Log.e method to write logs and displaying
                        * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs*/
                runOnUiThread(new Runnable() { //runOnUiThread() is launching a new background thread in Runnable state
                    @Override
                    public void run() {//thread is in run state
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                         /*A toast is a view containing a quick little message
                LENGTH_LONG Show the view or text notification for a long period of time*/
                    }
                });
            }

            return null;//returning null
        }

        @Override
        /**AsyncTask enables proper and easy use of the UI thread. This class allows you to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
        * onPostExecute invoked on the UI thread after the background computation finishes. The result of the background computation is passed to this step as a parameter.*/
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);// super() can be used to invoke immediate parent class constructor
            textView1.setText(location_name.toUpperCase());// when the button is clicked call setText() on the label and pass it the text from the text field.
            textView2.setText(coordStr);
            textView3.setText(weatherStr[0] + "\n" + weatherStr[1]);
            textView4.setText("MIN " + Integer.toString(temp_min) + " F , MAX " +
                    Integer.toString(temp_max) + " F");
            //converting integer to string of humidity
            textView5.setText(Integer.toString(humidity));
            /**A TimeUnit represents time durations at a given unit of granularity and provides utility methods to convert across units, and to perform timing and delay operations in these units
             * MILLISECONDS Time unit representing one thousandth of a second.
             * taking toHours,toMinutes,toSeconds to sunrise*/
            textView6.setText(String.format("%d Hr,%d min, %d sec",
                    (TimeUnit.MILLISECONDS.toHours(sunrise) / 24 > 12) ?
                            TimeUnit.MILLISECONDS.toHours(sunrise) / 24 - 12 :
                            TimeUnit.MILLISECONDS.toHours(sunrise) / 24,
                    TimeUnit.MILLISECONDS.toMinutes(sunrise) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sunrise)),
                    TimeUnit.MILLISECONDS.toSeconds(sunrise) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sunrise))));
            /*setting time format of month day and year as %d Hr,%d min, %d sec*/
            /**A TimeUnit represents time durations at a given unit of granularity and provides utility methods to convert across units, and to perform timing and delay operations in these units
             * MILLISECONDS Time unit representing one thousandth of a second.
             * taking another textview toHours,toMinutes,toSeconds to sunset*/
            textView7.setText(String.format("%d Hr,%d min, %d sec",
                    (TimeUnit.MILLISECONDS.toHours(sunset) / 24 > 12) ?
                            TimeUnit.MILLISECONDS.toHours(sunset) / 24 - 12
                            : TimeUnit.MILLISECONDS.toHours(sunset) / 24,
                    TimeUnit.MILLISECONDS.toMinutes(sunset) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sunset)),
                    TimeUnit.MILLISECONDS.toSeconds(sunset) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sunset))));

        }
    }
}
