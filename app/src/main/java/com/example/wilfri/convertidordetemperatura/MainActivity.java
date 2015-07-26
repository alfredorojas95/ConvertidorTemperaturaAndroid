package com.example.wilfri.convertidordetemperatura;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends ActionBarActivity {
    EditText editCelsius;
    TextView txtResult;
    Button btnEnviar;
    //declaramos variables String
    String celsius, fahren;
    String TAG="";

    //variables del WS
    String NAMESPACE = "http://www.w3schools.com/webservices/";
    String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    String METHOD_NAME = "CelsiusToFahrenheit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //obtenemos referencia de los elementos
        editCelsius= (EditText)findViewById(R.id.editCelsius);
        txtResult=(TextView)findViewById(R.id.tv_result);
        btnEnviar=(Button)findViewById(R.id.btnEnviar);

        //evento boton al hacer click

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validar que no venga vacio
                if (editCelsius.getText().length() != 0 && editCelsius.getText().toString() != "") {
                    //recuperar desde edittex
                    celsius = editCelsius.getText().toString();
                    //Creacion de instancia AsyncCallWS
                    AsyncCallWS task = new AsyncCallWS();
                    //ejecuta
                    task.execute();

                } else {
                    txtResult.setText("Ingresar ° Celsius");
                }
            }


        });


    }

    public void getFahrenheit(String celsius) {
        //Crea el request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Crea un objeto con los datos que van a ser enviados
        PropertyInfo celsiusPI = new PropertyInfo();
        //Set Name
        celsiusPI.setName("Celsius");
        //Set Value
        celsiusPI.setValue(celsius);
        //Set tipo de dato
        celsiusPI.setType(double.class);
        //Agrega el objeto al request
        request.addProperty(celsiusPI);
        //Crea envelope (sobre)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object (salida objeto)
        envelope.setOutputSoapObject(request);
        //Crea HTTP call object (transporte)
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            //Invocar el Servicio
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Respuesta
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Asigna valor a la variable
            fahren = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Crea tarea Asincronica (2 plano)
    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celsius);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            txtResult.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            txtResult.setText("Calculando...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}