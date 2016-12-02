package com.scnu.zhou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 16/11/20.
 */
public class RadarView extends View {

    // 默认长宽，在设置wrap_content时使用
    private int mWidth = 600;
    private int mHeight = 600;

    private int centerX = 0;
    private int centerY = 0;

    private float radius = 0f;  // 网格最大半径

    private List<RadarData> mData = new ArrayList<>();
    private int count = 0;
    double angle = 0;

    private Paint linePaint;  // 雷达线画笔
    private Paint textPaint;  // 文本画笔
    private Paint valuePaint;  // 区域画笔

    private int regionColor = Color.parseColor("#2680ef");

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            regionColor = a.getColor(R.styleable.RadarView_regionColor, Color.parseColor("#2680ef"));
            a.recycle();
        }

        initPaint();
    }


    // 初始化画笔
    private void initPaint(){

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#666666"));

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.parseColor("#333333"));
        textPaint.setTextSize(18);

        valuePaint = new Paint();
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setColor(regionColor);
    }

    // 测量View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        }
        else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }
        else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        centerX = w/2;
        centerY = h/2;
        radius = Math.min(w, h)/2 * 0.75f;

        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.translate(centerX, centerY);
        //canvas.scale(1, -1);   // y轴翻转

        if (count > 0) {
            drawPolygon(canvas);
            drawLine(canvas);
            drawText(canvas);
            drawRegion(canvas);
        }
    }


    /**
     * 绘制正多边形
     */
    private void drawPolygon(Canvas canvas){

        Path path = new Path();
        float r = radius / (count-1);    // 蜘蛛丝之间的间距

        for (int i=1; i<count; i++){

            float curR = r * i;
            path.reset();

            for (int j=0; j<count; j++){

                if (j == 0){
                    path.moveTo(centerX + curR, centerY);
                }
                else{
                    // 根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, linePaint);
        }
    }


    /**
     * 绘制直线
     */
    private void drawLine(Canvas canvas){

        Path path = new Path();

        for (int i=0; i<count; i++){

            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x, y);

            canvas.drawPath(path, linePaint);
        }
    }


    /**
     * 绘制文本
     */
    private void drawText(Canvas canvas){

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        for (int i=0; i<count; i++){

            float x = (float) (centerX + (radius + fontHeight/2) * Math.cos(angle * i));
            float y = (float) (centerY + (radius + fontHeight/2) * Math.sin(angle * i));

            if(angle*i>=0 && angle*i<=Math.PI/2){//第4象限
                canvas.drawText(mData.get(i).getDescription(), x, y + 5, textPaint);
            }else if(angle*i>=3*Math.PI/2 && angle*i<=Math.PI*2){//第3象限
                canvas.drawText(mData.get(i).getDescription(), x, y + 5, textPaint);
            }else if(angle*i>Math.PI/2 && angle*i<=Math.PI){//第2象限
                float dis = textPaint.measureText(mData.get(i).getDescription());//文本长度
                canvas.drawText(mData.get(i).getDescription(), x-dis, y + 5, textPaint);
            }else if(angle*i>=Math.PI && angle*i<3*Math.PI/2){//第1象限
                float dis = textPaint.measureText(mData.get(i).getDescription());//文本长度
                canvas.drawText(mData.get(i).getDescription(), x-dis, y + 5, textPaint);
            }
        }
    }


    /**
     * 绘制区域
     * @param canvas
     */
    private void drawRegion(Canvas canvas){
        Path path = new Path();
        valuePaint.setAlpha(255);
        for(int i=0; i<count; i++){
            double percent = mData.get(i).getValue()/mData.get(i).getMaxValue();
            float x = (float) (centerX + radius*Math.cos(angle*i)*percent);
            float y = (float) (centerY + radius*Math.sin(angle*i)*percent);
            if(i==0){
                path.moveTo(x, centerY);
            }else{
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 8, valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }


    public void setRadarData(List<RadarData> mData){

        this.mData = mData;
        this.count = mData.size();
        this.angle = 2 * Math.PI / count;

        postInvalidate();
    }


    /**
     * 数据模型
     */
    public static class RadarData{

        private float value;
        private float maxValue;
        private String description;

        public RadarData(float value, float maxValue, String description){

            this.value = value;
            this.maxValue = maxValue;
            this.description = description;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(float maxValue) {
            this.maxValue = maxValue;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
