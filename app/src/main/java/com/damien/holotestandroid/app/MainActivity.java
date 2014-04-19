package com.damien.holotestandroid.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends Activity {

  private EditText etAdress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

    final EditText etAdress = (EditText) findViewById(R.id.etAdress);

    Button bOn = (Button) findViewById(R.id.bOn);
    bOn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new RequestTask().execute("http://"+etAdress.getText()+":3000/off");

      }
    });


    Button bOff = (Button) findViewById(R.id.bOff);
    bOff.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new RequestTask().execute("http://"+etAdress.getText()+":3000/on");

      }
    });
  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


  class RequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {
      HttpClient httpclient = new DefaultHttpClient();
      HttpResponse response;
      String responseString = null;
      try {

        URI uri1 = new URI(uri[0]);
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(uri1);
        response = httpclient.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          response.getEntity().writeTo(out);
          out.close();
          responseString = out.toString();
        } else{
          //Closes the connection.
          response.getEntity().getContent().close();
          throw new IOException(statusLine.getReasonPhrase());
        }
      } catch (ClientProtocolException e) {
        //TODO Handle problems..
      } catch (IOException e) {
        //TODO Handle problems..
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);


      Log.d("weather", "end erquest");
//      Log.d("weather", result);



      //Do anything with response..
    }
  }

}
