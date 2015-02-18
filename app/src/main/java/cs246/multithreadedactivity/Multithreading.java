package cs246.multithreadedactivity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;


public class Multithreading extends ActionBarActivity {

    static Context context;

    private class CreateFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput("numbers.txt", Context.MODE_PRIVATE);
                for (Integer i = 1; i <= 10; i++) {
                    outputStream.write(i.toString().getBytes());
                    outputStream.write("\n".getBytes());
                    progressBar.setProgress(i);
                    Thread.sleep(250);
                }
                outputStream.close();
            } catch (Exception e) {}
            return null;
        }
    }

    private class LoadFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
            System.out.println("LoadFile started");
            String string = null;

            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("numbers.txt")));
                String inputString;
                StringBuffer stringBuffer = new StringBuffer();
                int i = 0;
                while ((inputString = inputReader.readLine()) != null) {
                    stringBuffer.append(inputString + "\n");
                    System.out.println(inputString);
                    progressBar.setProgress(i++);
                    Thread.sleep(250);
                }
                string  = (stringBuffer.toString());
                String[]  stringArray= string.split("\\r?\\n");
                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_list_item_1, stringArray);
                ListView myList = (ListView)findViewById(R.id.listView);
                myList.setAdapter(adapter);

            } catch (Exception e) {
                System.out.println("ERROR!");
                e.printStackTrace();
            }
            System.out.println(string);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithreading);
        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multithreading, menu);
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

    /** Called when the user clicks the Clear button */
    public void messageClear(View view) {
        String[]  stringArray= {""};
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_list_item_1, stringArray);
        ListView myList = (ListView)findViewById(R.id.listView);
        myList.setAdapter(adapter);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
    }

    /** Called when the user clicks the Load button */
    public void messageLoad(View view) {
            new LoadFile().execute("");
    }


    public void messageCreate(View view) {
        new CreateFile().execute("");
    }

}
