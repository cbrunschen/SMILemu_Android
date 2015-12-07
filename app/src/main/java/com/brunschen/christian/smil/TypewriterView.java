package com.brunschen.christian.smil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypewriterView extends TextView {
  
  private Paint paint = new Paint();
  private Path path = new Path();
  private PointF headPosition = new PointF();
  private HeadPositionListener headPositionListener;

  public TypewriterView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public TypewriterView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TypewriterView(Context context) {
    super(context);
    init();
  }
  
  private void init() {
    paint.setColor(0xbb000000);
    paint.setStyle(Style.FILL);
  }

  public HeadPositionListener getHeadPositionListener() {
    return headPositionListener;
  }

  public void setHeadPositionListener(HeadPositionListener headPositionListener) {
    this.headPositionListener = headPositionListener;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    if (widthMode == MeasureSpec.UNSPECIFIED) {
      setMeasuredDimension(getMeasuredWidth() + 10, getMeasuredHeight());
    } else if (widthMode == MeasureSpec.AT_MOST) {
      int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
      int width = Math.min(maxWidth, getMeasuredWidth() + 10);
      setMeasuredDimension(width, getMeasuredHeight());
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    canvas.translate(getPaddingLeft(), getPaddingTop());
    Layout layout = getLayout();
    if (layout != null) {
      int i = layout.getLineCount() - 1;
      float bottom = layout.getLineBottom(i);
      float right = layout.getLineRight(i);
      float h = bottom - layout.getLineTop(i);
      float h2 = (float) (h / 2);
      float w = (float) (3 * h / 8);
      float x = right;
      canvas.translate(x, bottom - h2);

      path.reset();
      path.moveTo(0.0f, 0.0f);
      path.lineTo(w, h2);
      path.lineTo(-w, h2);
      path.close();

      canvas.drawPath(path, paint);
      
      if (right != headPosition.x || bottom != headPosition.y) {
        headPosition.x = right;
        headPosition.y = bottom;
        if (headPositionListener != null) {
          headPositionListener.onHeadPosition(right, bottom);
        }
      }

      canvas.restore();
    }
  }
  
  public PointF getHeadPosition() {
    return headPosition;
  }
  
  public interface HeadPositionListener {
    void onHeadPosition(float x, float y);
  }
}
