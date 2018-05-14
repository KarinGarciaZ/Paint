package com.example.oscar.androidpaint;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ListView lvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = (ListView)findViewById(R.id.lvList);
        final Intent intent = new Intent(this, Main2Activity.class);
        if(isExternalStorageWritable()) {
            createDirectory("Lienzos");
            getFiles();
        } else
            Toast.makeText(this,"Memoria externa no disponible", Toast.LENGTH_LONG).show();
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = lvList.getItemAtPosition(position).toString();
                toSecondActivity(name);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newOption)
            toSecondActivity("");
        else {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Android Paint 1.0");
            dialogo.setMessage("Integrantes:\nOscar Jovanny García Zepeda\nDiego Osvaldo Solorio Lara\nJosé Manuel Sandoval Chávez");
            dialogo.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogo.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toSecondActivity(String name){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void createDirectory(String name) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),name);
        if(!file.exists())
            file.mkdirs();
    }
    public void getFiles(){
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) , "Lienzos");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            String[] myArray;
            myArray = new String[listOfFiles.length];

            for (int x = 0; x < myArray.length; x++)
                if (listOfFiles[x].isFile())
                    myArray[x] = listOfFiles[x].getName();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myArray);
            lvList.setAdapter(adapter);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getFiles();
    }
}
