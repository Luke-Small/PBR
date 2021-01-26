package com.example.pbr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
<<<<<<< HEAD
import android.graphics.Path;
import android.graphics.RectF;
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
import android.util.AttributeSet;
import android.view.View;

public class YieldView extends View {
<<<<<<< HEAD

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
=======
    //circle and text colors
    private int circleCol, labelCol;
    //label text
    private String circleText;
    //paint for drawing custom view
    private Paint circlePaint;
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16

    public YieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //paint object for drawing in onDraw
<<<<<<< HEAD
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
=======
        circlePaint = new Paint();
        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.YieldView, 0, 0);
        //paint object for drawing in onDraw
        circlePaint = new Paint();
        try {
            //get the text and colors specified using the names in attrs.xml
            circleText = a.getString(R.styleable.YieldView_circleLabel);
            circleCol = a.getInteger(R.styleable.YieldView_circleColor, 0);//0 is default
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
            labelCol = a.getInteger(R.styleable.YieldView_labelColor, 0);
        } finally {
            a.recycle();
        }
<<<<<<< HEAD
        init();
    }

    /*
    * Method is called on Initialization of class, used to set all the attributes of the paint objects.
    *
     */
    private void init(){
          //
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
    }

    @Override
    protected void onDraw(Canvas canvas) {
<<<<<<< HEAD
        RectF oval = new RectF();
        pathEmpty = new Path();
        pathFull = new Path();
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
        //draw the View
        //get half of the width and height as we are working with a circle
        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;
        //get the radius as half of the width or height, whichever is smaller
        //subtract ten so that it has some space around it
<<<<<<< HEAD
        /*
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
        int radius = 0;
        if (viewWidthHalf > viewHeightHalf)
            radius = viewHeightHalf - 10;
        else
            radius = viewWidthHalf - 10;
<<<<<<< HEAD
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
=======
        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);
        //set the paint color using the circle color specified
        circlePaint.setColor(circleCol);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
        //set the text color using the color specified
        circlePaint.setColor(labelCol);
        //set text properties
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        //draw the text using the string attribute and chosen properties
        canvas.drawText(circleText, viewWidthHalf, viewHeightHalf, circlePaint);
    }

    public int getCircleColor(){
        return circleCol;
    }

    public int getLabelColor(){
        return labelCol;
    }

    public String getLabelText(){
        return circleText;
    }

    public void setCircleColor(int newColor){
        //update the instance variable
        circleCol=newColor;
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
        //redraw the view
        invalidate();
        requestLayout();
    }
<<<<<<< HEAD
    public void setMeterCol(int newColor){
        meterCol=newColor;
        invalidate();
        requestLayout();
    }
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
    public void setLabelColor(int newColor){
        //update the instance variable
        labelCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public void setLabelText(String newLabel){
        //update the instance variable
<<<<<<< HEAD
        yieldText=newLabel;
=======
        circleText=newLabel;
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
        //redraw the view
        invalidate();
        requestLayout();
    }
<<<<<<< HEAD
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
=======
>>>>>>> 2f3a5adea85385ceb308f6494b8a33d5d01f5b16
}
