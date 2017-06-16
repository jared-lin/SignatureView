package com.newland.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SignatureView extends View {

	/**
	 * 	特征码字体
	 */
	private Typeface tf;
	/**
	 * 特征码画笔
	 */
	private Paint strpaint;
	// 特征码大小
	private int strsize;
	// 签字路径
	private Path path;
	// 特征码
	private String str;
	// 特征码字体高度
	private int strh;
	// 特征码字体长度
	private int strw;
	// 文本上升沿
	private int as;
	// 路径画笔
	private Paint paint;
	// 画布大小
	private int width;
	private int height;
	// 缩放因子
	private float scale;

	// 控制onmesure中部分代码执行一次
	private boolean first = true;
	// 动态生成的界面大小
	private float nwidth, nheight;

	public SignatureView(Context context) {
		super(context);
		initpamas();
	}

	public SignatureView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initpamas();
	}


	private void initpamas() {
		scale = 1;
		path = new Path();
		strsize = 32;
		strpaint = new Paint();
		strpaint.setColor(Color.BLACK);

		if(tf != null)
			strpaint.setTypeface(tf);

		strpaint.setTextSize(strsize);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(4);
        setBackgroundResource(android.R.color.white);
		setDrawingCacheBackgroundColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != str) {
			canvas.drawText(str, (width - strw) / 2, (height - strh) / 2 - as,
					strpaint);
		}
		canvas.drawPath(path, paint);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(event.getX(), event.getY());
			path.moveTo(event.getX(), event.getY());
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			invalidate();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		if (first) {
			boolean in = false;
			first = false;
			float a = 0.5f;
			float b = 1 / 3.0f;
			float indeed = height / (float) width;
			if (indeed >= b && indeed <= a) {
				in = true;
			}
			int scaletypex = 237;
			int scaletypey = 79;

			if (!in) {
				if (indeed > a) {
					nwidth = width;
					nheight = width / 2;
					scale = scaletypex / nwidth;

				} else if (indeed < b) {
					nheight = height;
					nwidth = height * 3;
					scale = scaletypey / nheight;
					if (nwidth * scale < scaletypex) {
						scale = scaletypex / nwidth;
					}
				}
			} else {
				nwidth = width;
				nheight = height;
				scale = scaletypex / nwidth;
				if (nheight * scale < scaletypey) {
					scale = scaletypey / nheight;
				}
			}

		}
		this.setMeasuredDimension((int) width, (int) height);
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
		FontMetrics fm = strpaint.getFontMetrics();
		strh = (int) (fm.descent - fm.ascent);
		strw = (int) strpaint.measureText(str);
		as = (int) fm.ascent;
	}

	public int getStrsize() {
		return strsize;
	}

	public void setStrsize(int strsize) {
		this.strsize = strsize;
	}

	private Bitmap zoomBitmap(Bitmap bmp) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);
		return newbmp;
	}

	public Bitmap getBitmap() {
		this.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
		this.setDrawingCacheEnabled(false);
		return zoomBitmap(bmp);
	}

	/**
	 * 获取带特征码的签名图
	 * @param str
	 * @return
	 */
	public Bitmap getMarkBitmap(String str) {
		FontMetrics fm = strpaint.getFontMetrics();
		strh = (int) (fm.descent - fm.ascent);
		strw = (int) strpaint.measureText(str);
		as = (int) fm.ascent;
		Bitmap bmp = getBitmap();
		int bw = bmp.getWidth();
		int bh = bmp.getHeight();
		Bitmap nbmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                Config.ARGB_8888);
		Canvas c = new Canvas(nbmp);
		c.drawBitmap(bmp, 0, 0, new Paint());
		c.save();
		c.drawText(str, (bw - strw) / 2, (bh - strh) / 2 - as, strpaint);
		return nbmp;
	}

	/**
	 * 获取原始签名图
	 * @return
	 */
	public Bitmap getOriginalBitmap() {

        return getBitmap();

	}

	/**
	 * 获取签名路径
	 * @return
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * 设置签名路径
	 * @return
	 */
	public void setPath(Path path) {
		this.path = path;
	}
	/**
	 * 判断签名版是否有字迹
	 * @return
	 */
	public boolean checkSignPath(){
		
		return !path.isEmpty();
	}


	/**
	 * 获取特征码画笔
	 * @return
	 */
	public Paint getCodePaint(){
		return strpaint;
	}

	/**
	 * 设置特征码字体
	 * @param tf
	 */
	public void setTf(Typeface tf) {
		this.tf = tf;
	}


	/**
	 * 清除画布
	 */
	public void clear() {
		// path = new Path();
		path.reset();
		invalidate();
	}

	/**
	 * 获取绘制签名画笔
	 * @return
	 */
	public Paint getSignatuePaint(){
		return paint;
	}
	
}
