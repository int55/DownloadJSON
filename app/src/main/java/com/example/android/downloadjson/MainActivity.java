package com.example.android.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadJSONTask task = new DownloadJSONTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London&appid=eac25b280928bfb743b776a08bd28526");
    }

        //класс загружает данные json             AsyncTask<Входные данные, Промежуточные, Тип вовращаемых данных>
    private static class DownloadJSONTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }
            //метод doInBackground не имеет доступа к элемент графич инерфейса, назначить текст в textview или именить картинку
            // в imageview не получится
            //Доступ к элементам графического интерфайса имеет метод onPostExecute
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // Log.i("MyResult", s); //полученная json строка
                //сoздаем обьект из строки s
                try {
                    JSONObject jsonObject = new JSONObject(s);
//          //чтобы вывесли название города ключ name, сохраняем в строку
                    String nameCity = jsonObject.getString("name");
                    Log.i("MyResult", nameCity);
//          // чтобы получить температуру и давдение нужно вызвать метод getJSONObject ключ main, сохраняем в JSONObject
                       JSONObject mainJSON = jsonObject.getJSONObject("main");
                       String temp = mainJSON.getString("temp");
                       String pressure = mainJSON.getString("pressure");
                       Log.i("MyResult", temp + " " + pressure);
//          //чтобы получить погоду, описание , иконку и тд. Данные хранятся в массиве.
            // из jsonObject нужно получить JSONArray ключ "weather"
//                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
//                        Log.i("MyResultTT", jsonArray.toString());
//                    JSONObject weather = jsonArray.getJSONObject(0);
//                    Log.i("MyResultW", weather.toString()); // полученный обрезок weather
//                    String mainWeather = weather.getString("main");
//                    String description = weather.getString("description");
                    //Log.i("MyResult", mainWeather + " " + description);

                } catch (JSONException e) { // искл если передали пустую строку или строку кот невозможно преобразовать в JSONObject
                    e.printStackTrace();
                }
            } // onPostExecute----------------
        }
}
