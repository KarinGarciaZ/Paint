package com.example.oscar.androidpaint;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Main2Activity extends AppCompatActivity {

    private int color = Color.BLACK;
    private int thickness = 6;
    private File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ConstraintLayout myLayout = (ConstraintLayout)findViewById(R.id.myLayout);
        Lienzo background = new Lienzo(this);
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
            case R.id.red:
                color = Color.RED;
                return true;
            case R.id.green:
                color = Color.GREEN;
                return true;
            case R.id.black:
                color = Color.BLACK;
                return true;
            case R.id.blue:
                color = Color.BLUE;
                return true;
            case R.id.white:
                color = Color.WHITE;
                return true;
            case R.id.thickness1:
                thickness = 1;
                return true;
            case R.id.thickness2:
                thickness = 2;
                return true;
            case R.id.thickness4:
                thickness = 4;
                return true;
            case R.id.thickness6:
                thickness = 6;
                return true;
            case R.id.thickness8:
                thickness = 8;
                return true;
            case R.id.thickness10:
                thickness = 10;
                return true;
            case R.id.eraserSmall:
                thickness = 6;
                color = Color.TRANSPARENT;
                return true;
            case R.id.save:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
