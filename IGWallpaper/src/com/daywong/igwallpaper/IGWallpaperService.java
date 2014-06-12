package com.daywong.igwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.daywong.igwallpaper.model.BaseApplication;
import com.daywong.igwallpaper.model.ImageLooper;
import com.daywong.igwallpaper.model.ImageWall;

/*
 * Main Program for IG Photowall
 * */
public class IGWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	private class MyWallpaperEngine extends Engine {
		private static final String TAG = "Service";
		private static final long DRAW_INTERVAL = 10000;
		private BaseApplication app = (BaseApplication) getApplicationContext();
		private int width;
		private int height;
		private boolean visible = true;
		private boolean touchEnabled = true;
		
//		private ImageLooper imgloop;
		private ImageLooper imgloop;
		Context mContext;
		private final Handler autoUpdateHandle = new Handler();
		private final Runnable autoUpdateRunner = new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "Auto Update runs");
//				autoUpdateHandle.postDelayed(autoUpdateRunner, 1000);
			}
		};

		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private final Runnable errorRunner = new Runnable() {
			@Override
			public void run() {
				drawErrorMessage();
			}
		};

		public MyWallpaperEngine() {
			Log.d(TAG, "MyWallpaperEngine created");
			mContext = IGWallpaperService.this.getApplicationContext();
			WallpaperManager mWM = WallpaperManager.getInstance(mContext);
			width = mWM.getDesiredMinimumWidth();
			height = mWM.getDesiredMinimumHeight();
			if (BaseApplication.cachePhotoCount(app.getPref_wallStyle()) > 1) {
				imgloop = new ImageLooper(width, height, getApplicationContext());
//				imgloop = new ImageWall();
				handler.post(drawRunner);
				autoUpdateHandle.postDelayed(autoUpdateRunner, 100);
			} else {
				handler.post(errorRunner);
			}
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			this.width = width;
			this.height = height;
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
//			 if (touchEnabled) {
//			 float x = event.getX();
//			 float y = event.getY();
//			 SurfaceHolder holder = getSurfaceHolder();
//			 Canvas canvas = null;
//			 try {
////				canvas = holder.lockCanvas();
//				if (canvas != null) {
//			 		imgloop.onTouch(x,y,canvas);
//				 }
//			 } finally {
//			 if (canvas != null)
////			 holder.unlockCanvasAndPost(canvas);
//			 }
//			 super.onTouchEvent(event);
//			 }
		}

		private void drawErrorMessage() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					Paint p = new Paint();
					p.setTextSize(80);
					p.setColor(Color.WHITE);
					canvas.drawText((String) getResources().getText(R.string.app_name),0, canvas.getHeight() / 2 - 300, p);
					p.setTextSize(30);
					canvas.drawText((String) getResources().getText(R.string.no_image_mesage1), 0,canvas.getHeight() / 2, p);
					p.setColor(Color.CYAN);
					canvas.drawText((String) getResources().getText(R.string.no_image_mesage2), 0,canvas.getHeight() / 2 + 30, p);
					p.setColor(Color.GREEN);
					canvas.drawText((String) getResources().getText(R.string.no_image_mesage3), 0,canvas.getHeight() / 2 + 60, p);
					p.setColor(Color.YELLOW);
					canvas.drawText((String) getResources().getText(R.string.no_image_mesage4), 0,canvas.getHeight() / 2 + 90, p);

					handler.removeCallbacks(drawRunner);
					if (visible) {
						handler.postDelayed(drawRunner, 2000);
					}
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			handler.removeCallbacks(drawRunner);
		}

		private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					imgloop.drawPhotos(canvas);
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, DRAW_INTERVAL);
			}
		}
	}
}