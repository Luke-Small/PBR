package com.example.pbr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class YieldView extends View {


    //circle and text colors
    private int gaugeCol, meterCol, labelCol;
    private int meterVal = 0;
    //label text
    private String yieldText;
    //paint for drawing custom view
    private Paint gaugePaint;
    private Paint meterPaint;
    private Paint textPaint;

    private Path pathEmpty;
    private Path pathFull;
    //circle and text colors
    private int circleCol;
    //label text
    private String circleText;
    //paint for drawing custom view
    private Paint circlePaint;

    public YieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //paint object for drawing in onDraw
        gaugePaint = new Paint();
        meterPaint = new Paint();
        textPaint = new Paint();

        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.YieldView, 0, 0);

        try {     //get the text and colors specified using the names in attrs.xml
            yieldText = a.getString(R.styleable.YieldView_yieldLabel);
            gaugeCol = a.getInteger(R.styleable.YieldView_gaugeColor, 0);//0 is default
            meterCol = a.getInteger(R.styleable.YieldView_meterColor, 0);

        //get the attributes specified in attrs.xml using the name we included
        //paint object for drawing in onDraw
        circlePaint = new Paint();;
        } finally {
            a.recycle();
        }
        init();
    }

    /*
    * Method is called on Initialization of class, used to set all the attributes of the paint objects.
    *
     */
    private void init(){
          //

    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF oval = new RectF();
        pathEmpty = new Path();
        pathFull = new Path();
        //draw the View
        //get half of the width and height as we are working with a circle
        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;
        //get the radius as half of the width or height, whichever is smaller
        //subtract ten so that it has some space around it
        /*

        int radius = 0;
        if (viewWidthHalf > viewHeightHalf)
            radius = viewHeightHalf - 10;
        else
            radius = viewWidthHalf - 10;

            */
//attributes for the empty arc gauge
        gaugePaint.setStyle(Style.STROKE);
        gaugePaint.setStrokeWidth(45);
        gaugePaint.setAntiAlias(true);
        gaugePaint.setColor(gaugeCol);
//attributes for percentage text
        textPaint.setColor(meterCol);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(80);

        meterPaint.setStyle(Style.STROKE);
        meterPaint.setStrokeWidth(45);
        meterPaint.setAntiAlias(true);
        meterPaint.setColor(meterCol);

        //draw the text (percentage in the middle)
        canvas.drawText(yieldText, viewWidthHalf, viewHeightHalf, textPaint);
        //draw the empty meter which is an arc
        drawArc(canvas, oval);
    }

    private void drawArc(Canvas canvas, RectF oval){
        pathFull.reset();
        float width = (float)getWidth();
        float height = (float)getHeight();
        float center_x, center_y;
        float radius = 0;
        int sweep;

        if (width > height){
            radius = height/2;
        }else{
            radius = width/4;
        }
        center_x = width/2;
        center_y = height/2;

        oval.set(center_x - radius,
                center_y - radius + 50,
                center_x + radius,
                center_y+350);

        pathEmpty.addArc(oval, -210, 240);
        pathFull.addArc(oval, -210, meterVal);
        canvas.drawPath(pathEmpty, gaugePaint);
        canvas.drawPath(pathFull, meterPaint);
    }
  //getter and setter methods
    public int getGaugeColor(){
        return gaugeCol;
    }
    public int getMeterCol(){
        return meterCol;
    }
    public int getLabelColor(){
        return labelCol;
    }
    public String getLabelText(){
        return yieldText;
    }

    public void setMeterVal(double newMeterVal){
        meterVal = (int)newMeterVal;
        invalidate();
        requestLayout();
    }
    public void setGaugeColor(int newColor){
        //update the instance variable
        gaugeCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public int getCircleColor(){
        return circleCol;
    }

    public void setCircleColor(int newColor){
        //update the instance variable
        circleCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }
    public void setMeterCol(int newColor){
        meterCol=newColor;
        invalidate();
        requestLayout();
    }
    public void setLabelColor(int newColor){
        //update the instance variable
        labelCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public void setLabelText(String newLabel){
        //update the instance variable

        yieldText=newLabel;

        circleText=newLabel;

        //redraw the view
        invalidate();
        requestLayout();
    }

    /*
    private RectF getOval(Canvas canvas, float factor) {

        RectF oval;
        final int canvasWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        final int canvasHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();

        if (canvasHeight*2 >= canvasWidth) {
            oval = new RectF(10, 10, canvasWidth*factor, canvasWidth*factor);
        } else {
            oval = new RectF(10, 10, canvasHeight*2*factor, canvasHeight*2*factor);
        }

        oval.offset((canvasWidth-oval.width())/2 + getPaddingLeft(), (canvasHeight*2-oval.height())/2 + getPaddingTop());

        return oval;
    }*/

}
