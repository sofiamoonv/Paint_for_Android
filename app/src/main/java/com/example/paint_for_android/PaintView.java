package com.example.paint_for_android;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintView extends View {
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint;
    private List<PointF> currentPath;
    private List<List<PointF>> paths;
    private Map<List<PointF>, Integer> pathColors; // Mapa para asociar colores con trazos
    private int currentColor;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);

        paths = new ArrayList<>();
        pathColors = new HashMap<>();
        currentPath = new ArrayList<>();
        currentColor = Color.BLACK;

        bitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

        // Dibujar cada trazo con su respectivo color
        for (List<PointF> path : paths) {
            paint.setColor(pathColors.get(path));
            for (int i = 0; i < path.size() - 1; i++) {
                PointF pointStart = path.get(i);
                PointF pointEnd = path.get(i + 1);
                canvas.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, paint);
            }
        }

        // Dibujar trazo actual (si existe)
        if (!currentPath.isEmpty()) {
            paint.setColor(currentColor);
            for (int i = 0; i < currentPath.size() - 1; i++) {
                PointF pointStart = currentPath.get(i);
                PointF pointEnd = currentPath.get(i + 1);
                canvas.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, paint);
            }
        }
    }

    private void touchStart(float x, float y) {
        currentPath = new ArrayList<>();
        currentPath.add(new PointF(x, y));
    }

    private void touchMove(float x, float y) {
        currentPath.add(new PointF(x, y));
        invalidate();
    }

    private void touchUp() {
        if (!currentPath.isEmpty()) {
            paths.add(currentPath);
            pathColors.put(currentPath, currentColor);
        }
        currentPath = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void clear() {
        paths.clear();
        pathColors.clear();
        invalidate();
    }

    public void setColor(int color) {
        currentColor = color;
    }

    public Bitmap save() {
        return bitmap;
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    public void undo() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }
}


