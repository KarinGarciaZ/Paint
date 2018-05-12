package com.example.oscar.androidpaint;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    private int color = Color.BLACK;
    private int thickness = 6;
    private int thicknessBefore;
    private int colorBefore;
    private boolean eraseInAction = false;
    private Lienzo background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ConstraintLayout myLayout = (ConstraintLayout)findViewById(R.id.myLayout);
        background = new Lienzo(this);
        myLayout.addView(background);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.brush:
                if (eraseInAction) afterErase();
                return true;
            case R.id.red:
                if (eraseInAction) afterErase();
                color = Color.RED;
                return true;
            case R.id.green:
                if (eraseInAction) afterErase();
                color = Color.GREEN;
                return true;
            case R.id.black:
                if (eraseInAction) afterErase();
                color = Color.BLACK;
                return true;
            case R.id.blue:
                if (eraseInAction) afterErase();
                color = Color.BLUE;
                return true;
            case R.id.white:
                if (eraseInAction) afterErase();
                color = Color.WHITE;
                return true;
            case R.id.thickness1:
                if (eraseInAction) afterErase();
                thickness = 1;
                return true;
            case R.id.thickness2:
                if (eraseInAction) afterErase();
                thickness = 2;
                return true;
            case R.id.thickness4:
                if (eraseInAction) afterErase();
                thickness = 4;
                return true;
            case R.id.thickness6:
                if (eraseInAction) afterErase();
                thickness = 6;
                return true;
            case R.id.thickness8:
                if (eraseInAction) afterErase();
                thickness = 8;
                return true;
            case R.id.thickness10:
                if (eraseInAction) afterErase();
                thickness = 10;
                return true;
            case R.id.eraserSmall:
                beforeErase();
                thickness = 10;
                return true;
            case R.id.eraserMedium:
                beforeErase();
                thickness = 30;
                return true;
            case R.id.eraserLarge:
                beforeErase();
                thickness = 70;
                return true;
            case R.id.save:
                showNameDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNameDialog(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Ingrese el nombre de la imagen:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogo.setView(input);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (!validateIfFileAlreadyExists(name))
                    save(name);
                else
                    Toast.makeText(Main2Activity.this, "Nombre de imagen existente, asigna otro.", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.setCancelable(true);
        dialogo.show();
    }

    public boolean validateIfFileAlreadyExists(String name){
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Lienzos/");
        File[] listOfFiles = folder.listFiles();

        String[] myArray;
        myArray = new String[listOfFiles.length];
        for (int x = 0; x < myArray.length; x++)
            if (listOfFiles[x].getName().equals(name+".png"))
                return true;
        return false;
    }

    public void save(String name){
        try{
            background.setDrawingCacheEnabled(true);
            Bitmap bitmap = background.getDrawingCache();
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Lienzos/"+name+".png");

            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();
        } catch(Exception e){
            Toast.makeText(this, "La imagen no se guardó", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finish();
    }

    public void afterErase(){
        thickness = thicknessBefore;
        color = colorBefore;
        eraseInAction = false;
    }

    public void beforeErase(){
        thicknessBefore = thickness;
        colorBefore = color;
        color = Color.WHITE;
        eraseInAction = true;
    }

    class Lienzo extends View {
        float x = 50;
        float y = 50;
        Path path;
        Paint brush;
        Paint canvasPaint;
        private Canvas canvasDraw;
        Bitmap canvasBitmap;

        public Lienzo(Context context) {
            super(context);
            setupDrawing();
        }

        public void setupDrawing(){
            path = new Path();
            brush = new Paint();
            canvasPaint = new Paint();
            brush.setColor(color);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeWidth(thickness);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
            canvasDraw = new Canvas(canvasBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRGB(255,255,255);
            canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
            canvas.drawPath(path, brush);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            x = event.getX();
            y = event.getY();
            brush.setColor(color);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeWidth(thickness);
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                path.moveTo(x,y);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE){
                path.lineTo(x,y);
            }
            if (event.getAction() == MotionEvent.ACTION_UP){
                path.lineTo(x,y);
                canvasDraw.drawPath(path, brush);
                path.reset();
            }
            invalidate();
            return true;
        }
    }
}
