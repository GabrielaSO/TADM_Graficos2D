package com.example.giso.tadm_graficos2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

/**
 * Created by giso on 24/04/18.
 */

public class Clock extends View {
    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0; // Tamaño de la fuente
    private int numeralSpacing = 0; //Espacio entre numeros
    private int handTruncation, hourHandTruncation = 0; //manecillas
    private int radius = 0; //radio
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private Rect rect = new Rect();

    public Clock(Context context) {
        super(context);
    }

    public Clock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Clock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClock();
        }

        drawCenter(canvas); //Centro de las manecillas
        drawNumeral(canvas); // Los numeros
        drawHands(canvas); // Manecillas
        postInvalidateDelayed(500);
        invalidate();
    }

    //Metodo para marcar posiciones de las manecillas
    private void drawHand(Canvas canvas, double loc, boolean isHour) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    //Metodo para pintar las manecillas (min, seg)
    private void drawHands(Canvas canvas) {
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas, (hour + c.get(Calendar.MINUTE) / 60) * 5f, true);
        drawHand(canvas, c.get(Calendar.MINUTE), false);
    }

    //Metodo para pintar los numeros (color y posicion)
    private void drawNumeral(Canvas canvas) {
        paint.setTextSize(30); // tamaño de los numeros
        paint.setColor(getResources().getColor(android.R.color.black)); // Color

        for (int number : numbers) {
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    //Metodo para pintar el centro del reloj
    private void drawCenter(Canvas canvas) {
        paint.setColor(getResources().getColor(android.R.color.white)); // Color
        paint.setStyle(Paint.Style.FILL); // FILL es relleno STROKE es vacio
        canvas.drawCircle(width / 2, height / 2, 12, paint); //posicion
    }
}

