package com.example.albader.wirelesscontrol;


import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnDiconnect, btnForward, btnBackward, btnLeft, btnRight, btnStop, btnbulb;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    boolean headlightOn = true;


    int countForward, countBackward, countLeft, countRight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_main);

        //call the widgtes

        btnDiconnect = (Button)findViewById(R.id.but_disc);
        btnForward = (Button)findViewById(R.id.but_forward);
        btnBackward = (Button)findViewById(R.id.but_backward);
        btnRight = (Button)findViewById(R.id.but_right);
        btnLeft = (Button)findViewById(R.id.but_left);
        btnStop = (Button)findViewById(R.id.but_stop);
        btnbulb = (Button)findViewById(R.id.but_bulb);




        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth

        /*----------------------------FORWARD---------------------------*/
        btnForward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // set all other counts to zero, reset all other
                countBackward = 0;
                countLeft = 0;
                countRight = 0;


                countForward++;

                // Turn off all other buttons
                btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_0));
                btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_0));
                btnRight.setBackground(getResources().getDrawable(R.drawable.direction_0));
                btnStop.setBackground(getResources().getDrawable(R.drawable.stopoff));

                // Change the image of the buttons and assign speed
                if(countForward == 1) {
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_1));
                    forward();      //method to turn on
                }
                else if(countForward == 2) {
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_2));
                    forward();
                }
                else if(countForward == 3){
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_3));
                    forward();
                }
                else if(countForward == 4){
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_2));
                    forward();
                }
                else if(countForward == 5){
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_1));
                    forward();
                }
                else{
                    btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_0));
                    btnStop.setBackground(getResources().getDrawable(R.drawable.stopon));
                    countForward = 0;
                    stop();
                }


            }
        });

        /*----------------------------STOP---------------------------*/
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Reset count
                countLeft = 0;
                countRight = 0;
                countBackward = 0;
                countForward = 0;

                // Turn off all other buttons
                btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_0));
                btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_0));
                btnRight.setBackground(getResources().getDrawable(R.drawable.direction_0));
                btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_0));
                btnStop.setBackground(getResources().getDrawable(R.drawable.stopon));


                stop();   //stop all rotation

            }
        });

         /*----------------------------BACKWARD---------------------------*/
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // set all other counts to zero, reset all other
                countForward = 0;
                countLeft = 0;
                countRight = 0;

                countBackward++;

                // Turn off all other buttons
                btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_0));
                btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_0));
                btnRight.setBackground(getResources().getDrawable(R.drawable.direction_0));
                btnStop.setBackground(getResources().getDrawable(R.drawable.stopoff));

                if(countBackward == 1) {
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_1));
                    backward();
                }
                else if(countBackward == 2) {
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_2));
                    backward();
                }
                else if(countBackward == 3){
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_3));
                    backward();
                }
                else if(countBackward == 4){
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_2));
                    backward();
                }
                else if(countBackward == 5){
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_1));
                    backward();
                }
                else{
                    btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_0));
                    btnStop.setBackground(getResources().getDrawable(R.drawable.stopon));
                    stop();
                    countBackward = 0;
                }
            }
        });

         /*----------------------------RIGHT---------------------------*/

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // set all other counts to zero, reset all other
                countBackward = 0;
                countLeft = 0;
                countForward = 0;

                countRight++;

                // Turn off all other buttons
                btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_0));
                btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_0));
                btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_0));
                btnStop.setBackground(getResources().getDrawable(R.drawable.stopoff));

                if(countRight == 1) {
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_1));
                    right();
                }
                else if(countRight == 2) {
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_2));
                    right();
                }
                else if(countRight == 3){
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_3));
                    right();
                }
                else if(countRight == 4){
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_2));
                    right();
                }
                else if(countRight == 5){
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_1));
                    right();
                }
                else{
                    btnRight.setBackground(getResources().getDrawable(R.drawable.direction_0));
                    btnStop.setBackground(getResources().getDrawable(R.drawable.stopon));
                    stop();
                    countRight = 0;
                }
            }
        });

         /*----------------------------LEFT---------------------------*/
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // set all other counts to zero, reset all other
                countBackward = 0;
                countForward = 0;
                countRight = 0;

                countLeft++;

                // Turn off all other buttons
                btnForward.setBackground(getResources().getDrawable(R.drawable.direction_up_0));
                btnBackward.setBackground(getResources().getDrawable(R.drawable.direction_down_0));
                btnRight.setBackground(getResources().getDrawable(R.drawable.direction_0));
                btnStop.setBackground(getResources().getDrawable(R.drawable.stopoff));

                if(countLeft == 1) {
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_1));
                    left();
                }
                else if(countLeft == 2) {
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_2));
                    left();
                }
                else if(countLeft == 3){
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_3));
                    left();
                }
                else if(countLeft == 4){
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_2));
                    left();
                }
                else if(countLeft == 5){
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_1));
                    left();
                }
                else{
                    btnLeft.setBackground(getResources().getDrawable(R.drawable.direction_left_0));
                    btnStop.setBackground(getResources().getDrawable(R.drawable.stopon));
                    stop();
                    countLeft = 0;
                }
            }
        });



        /*----------------------------DISCONNECT---------------------------*/
        btnDiconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
                btnDiconnect.setBackground(getResources().getDrawable(R.drawable.stop));
            }
        });

         /*----------------------------HEADLIGHTS---------------------------*/
        btnbulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (headlightOn == true) {
                    headlights();   //method to turn off
                    headlightOn = false;
                    btnbulb.setBackground(getResources().getDrawable(R.drawable.ledon));
                }

                else {
                    headlightsOff();
                    headlightOn = true;
                    btnbulb.setBackground(getResources().getDrawable(R.drawable.ledoff));
                }
            }
        });

//
    }

    private void headlights()
    {
        if (btSocket!=null)
        {
            try
            {

                btSocket.getOutputStream().write('D');
            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }

    private void headlightsOff()
    {
        if (btSocket!=null)
        {
            try
            {

                btSocket.getOutputStream().write('E');
            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.getOutputStream().write('0');
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void stop()
    {
        if (btSocket!=null)
        {
            try
            {

                btSocket.getOutputStream().write('0');
            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }


    // Forward Speed
    private void forward()
    {
        if (btSocket!=null)
        {
            try
            {
                // setting forward speed transmit codes
                if (countForward == 1) {
                    btSocket.getOutputStream().write('1');
                }
                else if(countForward == 2){
                    btSocket.getOutputStream().write('2');
                }
                else if(countForward == 3){
                    btSocket.getOutputStream().write('3');
                }
                else if(countForward == 4){
                    btSocket.getOutputStream().write('2');
                }
                else if(countForward == 5){
                    btSocket.getOutputStream().write('1');
                }
                else{
                    btSocket.getOutputStream().write('0');
                }

            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }



    private void backward()
    {
        if (btSocket!=null)
        {
            try
            {
                if(countBackward == 1) {
                    btSocket.getOutputStream().write('4');
                }
                else if(countBackward == 2){
                    btSocket.getOutputStream().write('5');
                }
                else if(countBackward == 3){
                    btSocket.getOutputStream().write('6');
                }
                else if(countBackward == 4){
                    btSocket.getOutputStream().write('5');
                }
                else if(countBackward == 5){
                    btSocket.getOutputStream().write('4');
                }
                else{
                    btSocket.getOutputStream().write('0');
                }
            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }

    private void right()
    {
        if (btSocket!=null)
        {
            try
            {
                if(countRight == 1){
                    btSocket.getOutputStream().write('7');
                }
                else if(countRight == 2){
                    btSocket.getOutputStream().write('8');
                }
                else if(countRight == 3){
                    btSocket.getOutputStream().write('9');
                }
                else if(countRight == 4){
                    btSocket.getOutputStream().write('8');
                }
                else if(countRight == 5){
                    btSocket.getOutputStream().write('7');
                }
                else{
                    btSocket.getOutputStream().write('0');
                }

            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }

    private void left()
    {
        if (btSocket!=null)
        {
            try
            {
                if (countLeft == 1) {
                    btSocket.getOutputStream().write('A');
                }
                else if(countLeft == 2){
                    btSocket.getOutputStream().write('B');
                }
                else if(countLeft == 3){
                    btSocket.getOutputStream().write('C');
                }
                else if(countLeft == 4){
                    btSocket.getOutputStream().write('B');
                }
                else if(countLeft == 5){
                    btSocket.getOutputStream().write('A');
                }
                else{
                    btSocket.getOutputStream().write('0');
                }

            }
            catch (IOException e)
            {
                msg("Error");

            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
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

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    //get the mobile bluetooth device
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    //connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    //create a SPP connection
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
