package com.nikitayankov.grappstriangle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class TriangleView extends View {
    Point a;
    Point b;
    Point c;
    Path path = new Path();

    Paint fillPaint = new Paint();
    Paint pointPaint = new Paint();

    int mFillColor;
    Bitmap point;

    int maxWidth;
    int maxHeight;

    private GestureDetector mDetector;

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriangleView,
                0, 0);

        try {
            mFillColor = a.getColor(R.styleable.TriangleView_fillColor, Color.CYAN);

            point = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.TriangleView_drawablePoint, R.drawable.dot));
        } finally {
            a.recycle();
        }

        pointPaint.setAntiAlias(true);

        fillPaint.setColor(mFillColor);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        mDetector = new GestureDetector(TriangleView.this.getContext(), new mListener());
    }

    private Point[] getPoints() {
        return new Point[]{a, b, c};
    }

    public void setTriangle(Triangle triangle) {
        a = triangle.a;
        b = triangle.b;
        c = triangle.c;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.maxWidth = w;
        this.maxHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(a.getX(), a.getY());
        path.lineTo(b.getX(), b.getY());
        path.lineTo(c.getX(), c.getY());
        path.lineTo(a.getX(), a.getY());

        path.close();

        canvas.drawPath(path, fillPaint);

//        canvas.drawPoint(a.getX(), a.getY(), pointPaint);
//        canvas.drawPoint(b.getX(), b.getY(), pointPaint);
//        canvas.drawPoint(c.getX(), c.getY(), pointPaint);

        canvas.drawBitmap(point, a.getX() - point.getWidth() / 2, a.getY() - point.getHeight() / 2, pointPaint);
        canvas.drawBitmap(point, b.getX() - point.getWidth() / 2, b.getY() - point.getHeight() / 2, pointPaint);
        canvas.drawBitmap(point, c.getX() - point.getWidth() / 2, c.getY() - point.getHeight() / 2, pointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);

        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                result = true;
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                path = new Path();

                float x = event.getX();
                float y = event.getY();

                moveClosestPoint(x, y);
            }
        }
        return result;
    }

    public float calculateArea() {
        float a_x = a.getX();
        float a_y = a.getY();

        float b_x = b.getX();
        float b_y = b.getY();

        float c_x = c.getX();
        float c_y = c.getY();

        return Math.abs(a_x * (b_x - c_y) + b_x * (c_y - a_y) + c_x * (a_y - b_y)) * 0.5f;
    }

    private void moveClosestPoint(float x, float y) {
        Point closest = this.a;
        Point[] points = getPoints();
        float distance = Float.MAX_VALUE;

        for (int i = 0; i < points.length; i++) {
            float new_distance = (float) Math.sqrt(Math.pow(x - points[i].getX(), 2) + Math.pow(y - points[i].getY(), 2));

            if (new_distance < distance) {
                distance = new_distance;
                closest = points[i];
            }
        }

        if (x > maxWidth)
            x = maxWidth;
        if (x < 0)
            x = 0;

        if (y > maxHeight)
            y = maxHeight;
        if (y < 0)
            y = 0;

        closest.setX(x);
        closest.setY(y);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
