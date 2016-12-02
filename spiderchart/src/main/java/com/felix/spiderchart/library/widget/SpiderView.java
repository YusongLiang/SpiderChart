package com.felix.spiderchart.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.felix.spiderchart.library.R;
import com.felix.spiderchart.library.entity.DataPoint;
import com.felix.spiderchart.library.entity.SpiderDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 蛛网图控件
 *
 * @author Felix
 */
public class SpiderView extends View {

    private int[] mOverlayColors;
    private Paint mWebPaint;
    private TextPaint mAxisTextPaint;
    private TextPaint mUnitTextPaint;
    private Paint mMarkerContentPaint;
    private Paint mMarkerBorderPaint;

    private float mUnitTextSize;
    private float mLineWidth;
    private float mMarkerContentWidth;
    private float mMarkerBorderWidth;
    private int mTextColor;
    private int mWebColor;
    private int mMarkerContentColor;
    private int mMarkerBorderColor;

    private String mPattern = "0";
    private SpiderDataSet mDataSet;
    private float mUnitLength = 50;
    private float mUnitValue = 1;
    private int mSideNum;
    private int mCycleNum = 6;
    private String[] mItemNames;

    public SpiderView(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecation")
    public SpiderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpiderView);
        mWebColor = array.getColor(R.styleable.SpiderView_contentColor, Color.BLACK);
        mLineWidth = array.getDimension(R.styleable.SpiderView_lineWidth, 2);
        mTextColor = array.getColor(R.styleable.SpiderView_textColor, Color.BLACK);
        mUnitTextSize = array.getDimension(R.styleable.SpiderView_unitTextSize, 25);
        mMarkerBorderColor = array.getColor(R.styleable.SpiderView_markerBorderColor, Color.WHITE);
        mMarkerBorderWidth = array.getDimension(R.styleable.SpiderView_markerBorderWidth, 2);
        mMarkerContentColor = array.getColor(R.styleable.SpiderView_markerContentColor, Color.BLUE);
        mMarkerContentWidth = array.getDimension(R.styleable.SpiderView_markerContentWidth, 5);
        mOverlayColors = new int[4];
        mOverlayColors[0] = getResources().getColor(R.color.overlayColorA);
        mOverlayColors[1] = getResources().getColor(R.color.overlayColorB);
        mOverlayColors[2] = getResources().getColor(R.color.overlayColorC);
        mOverlayColors[3] = getResources().getColor(R.color.overlayColorD);
        initPaints();
        array.recycle();
    }

    private void initPaints() {
        mWebPaint = new Paint();
        mWebPaint.setAntiAlias(true);
        mWebPaint.setColor(mWebColor);
        mWebPaint.setStrokeWidth(mLineWidth);
        mWebPaint.setStyle(Paint.Style.STROKE);

        mAxisTextPaint = new TextPaint();
        mAxisTextPaint.setAntiAlias(true);
        mAxisTextPaint.setColor(mTextColor);
        mAxisTextPaint.setTextSize(mUnitTextSize);
        mAxisTextPaint.setStyle(Paint.Style.FILL);

        mUnitTextPaint = new TextPaint();
        mUnitTextPaint.setAntiAlias(true);
        mUnitTextPaint.setTextSize(mUnitTextSize);
        mUnitTextPaint.setColor(mTextColor);
        mUnitTextPaint.setStyle(Paint.Style.FILL);
        mUnitTextPaint.setTextAlign(Paint.Align.CENTER);

        mMarkerContentPaint = new Paint();
        mMarkerContentPaint.setAntiAlias(true);
        mMarkerContentPaint.setStyle(Paint.Style.FILL);
        mMarkerContentPaint.setColor(mMarkerContentColor);

        mMarkerBorderPaint = new Paint();
        mMarkerBorderPaint.setAntiAlias(true);
        mMarkerBorderPaint.setStyle(Paint.Style.FILL);
        mMarkerBorderPaint.setColor(mMarkerBorderColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        if (mDataSet == null) {
            canvas.drawText("当前数据为空", 0, 0, mUnitTextPaint);
            return;
        }
        initData();
        canvas.drawPath(getWebCyclePath(), mWebPaint);
        canvas.drawPath(getWebLinePath(), mWebPaint);
        drawWebText(canvas);
        drawSamples(canvas);
    }

    private void drawWebText(Canvas canvas) {
        float lengthY = 0;
        for (int i = 0; i <= mCycleNum; i++) {
            float axis = i * mUnitValue;
            DecimalFormat format = new DecimalFormat(mPattern);
            String axisValue = format.format(axis);
            canvas.drawText(axisValue, 0, -lengthY, mAxisTextPaint);
            lengthY += mUnitLength;
        }
        float degree = 360f / mSideNum;
        for (int j = 0; j < mSideNum; j++) {
            float currX = (float) (lengthY * Math.sin(Math.toRadians(j * degree)));
            float currY = (float) (-lengthY * Math.cos(Math.toRadians(j * degree)));
            canvas.drawText(mItemNames[j], currX, currY, mUnitTextPaint);
        }
    }

    /**
     * 初始化图表数据
     */
    private void initData() {
        mItemNames = mDataSet.getItems();
        mSideNum = mItemNames.length;
    }

    /**
     * 获取蛛网圈路径
     *
     * @return {@link Path} 路径对象
     */
    private Path getWebCyclePath() {
        Path path = new Path();
        float degree = 360f / mSideNum;
        float lengthY = 0;
        for (int j = 0; j < mCycleNum; j++) {
            lengthY += mUnitLength;
            Path cyclePath = new Path();
            for (int i = 0; i < mSideNum; i++) {
                float currX = (float) (lengthY * Math.sin(Math.toRadians(i * degree)));
                float currY = (float) (-lengthY * Math.cos(Math.toRadians(i * degree)));
                if (i == 0) cyclePath.moveTo(currX, currY);
                else cyclePath.lineTo(currX, currY);
            }
            cyclePath.close();
            path.addPath(cyclePath);
        }
        return path;
    }

    /**
     * 获取蛛网线路径
     *
     * @return {@link Path} 路径对象
     */
    private Path getWebLinePath() {
        Path path = new Path();
        float lengthLine = mUnitLength * mCycleNum + mUnitLength / 2;
        float degree = 360f / mSideNum;
        for (int i = 0; i < mSideNum; i++) {
            float currX = (float) (lengthLine * Math.sin(Math.toRadians(i * degree)));
            float currY = (float) (-lengthLine * Math.cos(Math.toRadians(i * degree)));
            path.moveTo(0, 0);
            path.lineTo(currX, currY);
        }
        path.close();
        return path;
    }

    /**
     * 绘制样本
     *
     * @param canvas {@link Canvas} 画板对象
     */
    private void drawSamples(Canvas canvas) {
        Path path = new Path();
        List<TreeMap<String, Double>> samples = mDataSet.getSampleList();
        for (int i = 0; i < samples.size(); i++) {
            Map<String, Double> sample = samples.get(i);
            drawSample(sample, i, canvas);
        }
    }

    /**
     * 绘制单条样本
     *
     * @param sample      存储在SpiderView中的{@link Map},存储了一条样本数据
     * @param sampleIndex 样本序号
     * @param canvas      {@link Canvas} 画板对象
     */
    private void drawSample(Map<String, Double> sample, int sampleIndex, Canvas canvas) {
        Paint samplePaint = new Paint();
        samplePaint.setAntiAlias(true);
        samplePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        samplePaint.setColor(mOverlayColors[sampleIndex % mOverlayColors.length]);
        samplePaint.setAlpha(128);
        Set<String> items = sample.keySet();
        String[] itemArray = items.toArray(new String[items.size()]);
        Path path = new Path();
        List<DataPoint> points = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            int index = 0;
            for (int j = 0; j < mItemNames.length; j++) {
                if (mItemNames[j].equals(itemArray[i])) {
                    index = j;
                    break;
                }
            }
            double value = sample.get(itemArray[i]) * mUnitLength / mUnitValue;
            float currX = (float) (value * Math.sin(Math.toRadians(index * 360f / mSideNum)));
            float currY = (float) (-value * Math.cos(Math.toRadians(index * 360f / mSideNum)));
            DataPoint point = new DataPoint(currX, currY);
            point.setValue(sample.get(itemArray[i]));
            points.add(point);
            if (i == 0) path.moveTo(currX, currY);
            else path.lineTo(currX, currY);
        }
        path.close();
        canvas.drawPath(path, samplePaint);
        samplePaint.setAlpha(255);
        samplePaint.setStyle(Paint.Style.STROKE);
        samplePaint.setStrokeWidth(3);
        canvas.drawPath(path, samplePaint);
        drawMarkers(points, canvas);
    }

    /**
     * 绘制标记点
     *
     * @param points 标记点集合
     * @param canvas {@link Canvas} 画板对象
     */
    private void drawMarkers(List<DataPoint> points, Canvas canvas) {
        float offSetX = 20;
        float offSetY = 20;
        for (DataPoint point : points) {
            canvas.drawCircle(point.getX(), point.getY(), mMarkerContentWidth + mMarkerBorderWidth, mMarkerBorderPaint);
            canvas.drawCircle(point.getX(), point.getY(), mMarkerContentWidth, mMarkerContentPaint);
            canvas.drawText(String.valueOf(point.getValue()), point.getX() + offSetX, point.getY() + offSetY, mUnitTextPaint);
        }
    }

    public SpiderDataSet getDataSet() {
        return mDataSet;
    }

    public void setDataSet(SpiderDataSet dataSet) {
        mDataSet = dataSet;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public float getUnitTextSize() {
        return mUnitTextSize;
    }

    public void setUnitTextSize(float unitTextSize) {
        mUnitTextSize = unitTextSize;
        invalidate();
    }

    public int getContentColor() {
        return mWebColor;
    }

    public void setContentColor(int contentColor) {
        mWebColor = contentColor;
        invalidate();
    }

    public float getUnitValue() {
        return mUnitValue;
    }

    public void setUnitValue(float unitValue) {
        mUnitValue = unitValue;
        invalidate();
    }

    public float getUnitLength() {
        return mUnitLength;
    }

    public void setUnitLength(float unitLength) {
        mUnitLength = unitLength;
        invalidate();
    }

    public String getPattern() {
        return mPattern;
    }

    public void setPattern(String pattern) {
        mPattern = pattern;
        invalidate();
    }
}