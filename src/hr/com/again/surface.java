package hr.com.again;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.List;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class surface extends View implements OnTouchListener{
	
	Paint paint = new Paint();
	List<Point> points = new ArrayList<Point>();
	Bitmap entryImage;
	int WIDTH = 180;
	int HEIGHT = 195;
	boolean canDraw = false;
	boolean isUp = false;
	  protected Sample sample;
	  private Context ctx;
	  /**
	   * Specifies the left boundary of the cropping rectangle.
	   */
	  protected int downSampleLeft;
	  
	  CountDownTimer cdt = null;

	  /**
	   * Specifies the right boundary of the cropping rectangle.
	   */
	  protected int downSampleRight;

	  /**
	   * Specifies the top boundary of the cropping rectangle.
	   */
	  protected int downSampleTop;

	  /**
	   * Specifies the bottom boundary of the cropping rectangle.
	   */
	  protected int downSampleBottom;

	  /**
	   * The downsample ratio for x.
	   */
	  protected double ratioX;

	  /**
	   * The downsample ratio for y
	   */
	  protected double ratioY;

	  /**
	   * The pixel map of what the user has drawn. Used to downsample it.
	   */
	  protected int pixelMap[];
	  
	 

	public surface(Context context) {
		super(context);
		try
		{
			ctx = context;
			setFocusable(true);
	        setFocusableInTouchMode(true);
	
	        this.setOnTouchListener(this);
	        paint.setColor(Color.BLACK);
	        paint.setAntiAlias(true);
	        entryImage = Bitmap.createBitmap(WIDTH, HEIGHT,Bitmap.Config.ARGB_8888 );
			// TODO Auto-generated constructor stub
		}
		catch(Exception e)
		{
			Log.e("constr",""+e);
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		try
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN | event.getAction() == MotionEvent.ACTION_MOVE)
			{
				Point point = new Point();
		        point.x = event.getX();
		        point.y = event.getY();
		        int x = (int)point.x;
		        int y = (int)point.y;
		        points.add(point);
		        
		        if( ( x < entryImage.getWidth() ) && ( y < entryImage.getHeight() ) )
		        {
		        	entryImage.setPixel(x,y, Color.BLACK);
		        }
		        invalidate();
		        Log.d("SaveP", "point: " + point);
		        
		        if(cdt!=null)
		        	cdt.cancel();
		        return true;
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				//Toast.makeText(ctx, "Action is up", Toast.LENGTH_SHORT).show();
				//Main m = new Main();
				//((Main) ctx).trainF();
				//for(int i=0;i<20000000;i++);
				
				cdt = new CountDownTimer(1000, 500) {

				     public void onTick(long millisUntilFinished) {
				         Log.i("Counter","seconds remaining: " + millisUntilFinished / 1000);
				     }

				     public void onFinish() {
				    	 ((Main) ctx).recogF();
				     }
				  }.start();
				 
				
				
				return true;
			}
			
			return false;
		}
		catch(Exception e)
		{
			Log.e("Touch",""+e);
			return false;
		}
	}
	
	@Override
    public void dispatchDraw(Canvas canvas) {
		try
		{
			
			paint.setColor(Color.WHITE);
		 	canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
		 	
		 	paint.setColor(Color.BLACK);
		 	
		 	
			canvas.drawRect(downSampleLeft, downSampleTop, downSampleRight - downSampleLeft,
			 	        downSampleBottom - downSampleTop,paint);
			canvas.drawBitmap(entryImage, 0, 0, paint);
			
		}
		catch(Exception e)
		{
			Log.e("Draw",""+e);
		}
    }
	
	public void setSample(Sample s)
	  {
	    sample = s;
	  }
	
	public Sample getSample()
	  {
	    return sample;
	  }
	
	public void downSample() 
	{
	   try
	    {
		   
		  int w = entryImage.getWidth();
		  int h = entryImage.getHeight();
		  Integer wide = new Integer(w);
		  Integer high = new Integer(h);
		  pixelMap = new int[w * h];
	      
		  entryImage.getPixels(pixelMap, 0, w, 0, 0, w, h);
		  
		  
		  findBounds(w, h);
		  
	      Log.d("dimension:wid,hig",wide.toString()+high.toString());
	      SampleData data = sample.getData();

	      ratioX = (double) (downSampleRight - downSampleLeft)
	          / (double) data.getWidth();
	      ratioY = (double) (downSampleBottom - downSampleTop)
	          / (double) data.getHeight();

	      for (int y = 0; y < data.getHeight(); y++)
	      {
	        for (int x = 0; x < data.getWidth(); x++)
	        {
	          if (downSampleQuadrant(x, y))
	            data.setData(x, y, true);
	          else
	            data.setData(x, y, false);
	        }
	      }
	      Log.d("AfterDown","Sample");
	      for (int y = 0; y < data.getHeight(); y++)
	      {
	        for (int x = 0; x < data.getWidth(); x++)
	        {
	        	Log.d("data x y",""+x+y+data.getData(x, y));
	        }
	      }
	      
	    }
	    catch (Exception e)
	    {
	    	Log.e("down",""+e);
	    }
	}
	
	protected boolean downSampleQuadrant(int x, int y)
	{
	    int w = entryImage.getWidth();
	    int startX = (int) (downSampleLeft + (x * ratioX));
	    int startY = (int) (downSampleTop + (y * ratioY));
	    int endX = (int) (startX + ratioX);
	    int endY = (int) (startY + ratioY);

	    for (int yy = startY; yy <= endY; yy++)
	    {
	      for (int xx = startX; xx <= endX; xx++)
	      {
	        int loc = xx + (yy * w);

	        
	        if (pixelMap[loc] != 0)
	          return true;
	      }
	    }

	    return false;
	}
	
	protected boolean hLineClear(int y)
	{
	    int w = entryImage.getWidth();
	    for (int i = 0; i < w; i++)
	    {
	      
	      if (pixelMap[(y * w) + i] != 0)
	        return false;
	    }
	    return true;
	}
	
	protected boolean vLineClear(int x)
	{
	    int w = entryImage.getWidth();
	    int h = entryImage.getHeight();
	    for (int i = 0; i < h; i++)
	    {
	      
	      if (pixelMap[(i * w) + x] != 0)
	        return false;
	    }
	    return true;
	}
	
	protected void findBounds(int w, int h)
	  {
	    // top line
	    for (int y = 0; y < h; y++)
	    {
	      if (!hLineClear(y))
	      {
	        downSampleTop = y;
	        break;
	      }

	    }
	    // bottom line
	    for (int y = h - 1; y >= 0; y--)
	    {
	      if (!hLineClear(y))
	      {
	        downSampleBottom = y;
	        break;
	      }
	    }
	    // left line
	    for (int x = 0; x < w; x++)
	    {
	      if (!vLineClear(x))
	      {
	        downSampleLeft = x;
	        break;
	      }
	    }

	    // right line
	    for (int x = w - 1; x >= 0; x--)
	    {
	      if (!vLineClear(x))
	      {
	        downSampleRight = x;
	        break;
	      }
	    }
	  }
	
	public void clear()
	{
		try
		{
		entryImage = null;
		entryImage = Bitmap.createBitmap(WIDTH, HEIGHT,Bitmap.Config.ARGB_8888 );
		this.downSampleBottom = this.downSampleTop = this.downSampleLeft = this.downSampleRight = 0;
		points = new ArrayList<Point>();
		}
		catch(Exception ex)
		{
			Log.e("Clearing",""+ex);
		}
		//paint.setColor(Color.RED);
	}
	

}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}