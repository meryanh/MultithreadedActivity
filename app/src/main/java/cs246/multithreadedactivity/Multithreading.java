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
import java.io.FileOutputStream;
import java.io.InputStreamReader;


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

    private class LoadFile extends AsyncTask<String, Void, Void> {

        String[] stringArray;

        @Override
        protected Void doInBackground(String... params) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("numbers.txt")));
                String inputString;
                StringBuffer stringBuffer = new StringBuffer();
                int i = 1;
                while ((inputString = inputReader.readLine()) != null) {
                    stringBuffer.append(inputString + "\n");
                    progressBar.setProgress(i++);
                    Thread.sleep(250);
                }
                stringArray = (stringBuffer.toString()).split("\\r?\\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_list_item_1, stringArray);
            ListView myList = (ListView)findViewById(R.id.listView);
            myList.setAdapter(adapter);
            adapter.addAll();
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
        try {
            new LoadFile().execute("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void messageCreate(View view) {
        new CreateFile().execute("");
    }

}
