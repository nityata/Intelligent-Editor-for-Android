package hr.com.again;

import android.content.Context;

public class Sample 
{
	
	SampleData data;
	int WIDTH = 120 , HEIGHT = 130;
	
	
	void setData(SampleData data)
	{
	    this.data = data;
	}
	
	SampleData getData()
	{
	    return data;
	}
	Sample(Context context ,int width, int height)
	{
		
	    data = new SampleData(' ', width, height);
	}
	
}
