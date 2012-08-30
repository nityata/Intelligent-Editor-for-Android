package hr.com.again;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



public class Main extends Activity implements OnClickListener{
	
	
    
	/*static final String[] keywords = new String[] {
		  "abstract", "assert", "boolean", "break", "byte",
		  "case", "catch", "char", "class", "const",
		  "continue", "default", "do", "double", "else",
		  "enum", "extends", "final", "finally", "float",
		  "for", "goto", "if", "implements", "import",
		  "instanceOf", "int", "interface", "long", "native",
		  "new", "package", "private", "protected", "public",
		  "return", "short", "static", "super", "switch",
		  "synchronized", "this", "throw", "throws", "transient",
		  "try", "void", "volatile", "while", "Congo"		
		  };*/
	
	ArrayList<String> keywords = new ArrayList<String>();
	private FrameLayout mainFrame;
	surface s;
	Sample sample;
	private Button addB;
	private Button clearB;
	private Button clearFile;
	private Button addLine;
	private Button addSemi;
	private Button newFile;
	private Button saveFile;
	private Button loadFile;
	private Button capsOn;
	private Button capsOff;
	private Button symOn;
	private Button numOn;
	CharSequence[] myData;
	String fileName="";
	private boolean isUp;
	private Button enterB;
	private TextView MyConsole;
	private Vector<SampleData> sampleList = new Vector<SampleData>();
	private TextView program;
	private AutoCompleteTextView lettersWritten;
	public static final String PREFS_NAME = "settings";
	private KohonenNetwork net;
	private String letter="";
	
	/**
	   * The down sample width for the application.
	   */
	
	static final int DOWNSAMPLE_WIDTH = 5;

	  /**
	   * The down sample height for the application.
	   */
	  static final int DOWNSAMPLE_HEIGHT = 5;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        try
        {
        	isUp = false;
        	preload("small.txt");
	        trainF();
	        
	        
	        initialize();
	        /** Setting up the canvases for Entry(surface) and Sample */
        
	        mainFrame = (FrameLayout)findViewById(R.id.mainFrame);
	        s = new surface(this);
	        mainFrame.addView(s);
	        sample = new Sample(this,DOWNSAMPLE_WIDTH,DOWNSAMPLE_HEIGHT);
	        s.setSample(sample);
	        
	        
	        lettersWritten = (AutoCompleteTextView) findViewById(R.id.autocomplete_keywords);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, keywords);
	        lettersWritten.setAdapter(adapter);
	        
	        program = (TextView)findViewById(R.id.prog);
	        
	        /** Setting up the Buttons and their Listeners */
	        
	        addB = (Button)findViewById(R.id.add);
	        addB.setOnClickListener(this);
	        
	        clearB = (Button)findViewById(R.id.clear);
	        clearB.setOnClickListener(this);
	        
	        MyConsole = (TextView)findViewById(R.id.console);
	        
	        newFile = (Button)findViewById(R.id.newFile);
	        newFile.setOnClickListener(this);
	        
	        saveFile = (Button)findViewById(R.id.saveFile);
	        saveFile.setOnClickListener(this);
	        
	        loadFile = (Button)findViewById(R.id.loadFile);
	        loadFile.setOnClickListener(this);
	     
	        enterB = (Button)findViewById(R.id.enter);
	        enterB.setOnClickListener(this);
	        
	        addLine = (Button)findViewById(R.id.newLine);
	        addLine.setOnClickListener(this);
	        
	        clearFile = (Button)findViewById(R.id.clearFile);
	        clearFile.setOnClickListener(this);
	        
	        addSemi = (Button)findViewById(R.id.semiColon);
	        addSemi.setOnClickListener(this);
	        
	        capsOn = (Button)findViewById(R.id.capsOn);
	        capsOn.setOnClickListener(this);
	        
	        capsOff = (Button)findViewById(R.id.capsOff);
	        capsOff.setOnClickListener(this);
	        
	        symOn = (Button)findViewById(R.id.symOn);
	        symOn.setOnClickListener(this);
	        
	        numOn = (Button)findViewById(R.id.numOn);
	        numOn.setOnClickListener(this);
    
       }
        catch(Exception ex)
        {
        	Log.e("in on create",""+ex);
        }
    }
    
    public void initialize()
    {
    	keywords.add("abstract");  //Adding  elements to Arraylist		
    	keywords.add("assert");
    	keywords.add("boolean");
    	keywords.add("break");
    	keywords.add("byte");
    	keywords.add("case");
    	keywords.add("catch");
    	keywords.add("char");  //Adding  elements to Arraylist		
    	keywords.add("class");
    	keywords.add("const");
    	keywords.add("continue");
    	keywords.add("default");
    	keywords.add("do");
    	keywords.add("double");
    	keywords.add("else");  //Adding  elements to Arraylist		
    	keywords.add("enum");
    	keywords.add("extends");
    	keywords.add("final");
    	keywords.add("finally");
    	keywords.add("float");
    	keywords.add("for");
    	keywords.add("goto");  //Adding  elements to Arraylist		
    	keywords.add("if");
    	keywords.add("implements");
    	keywords.add("import");
    	keywords.add("instanceOf");
    	keywords.add("int");
    	keywords.add("interface");
    	keywords.add("long");  //Adding  elements to Arraylist		
    	keywords.add("native");
    	keywords.add("new");
    	keywords.add("package");
    	keywords.add("private");
    	keywords.add("protected");
    	keywords.add("public");
    	keywords.add("return");  //Adding  elements to Arraylist		
    	keywords.add("short");
    	keywords.add("static");
    	keywords.add("super");
    	keywords.add("switch");
    	keywords.add("synchronized");
    	keywords.add("this");
    	keywords.add("throw");  //Adding  elements to Arraylist		
    	keywords.add("throws");
    	keywords.add("transient");
    	keywords.add("try");
    	keywords.add("void");
    	keywords.add("volatile");
    	keywords.add("while");
    }
    
    public void onResume()
    {
    	super.onResume();
    	try
    	{
	    	
    		if(isUp == true)
    			preload("big.txt");
    		else
    			preload("small.txt");
	    	trainF();
    	}
    	catch(Exception e)
    	{
    		Log.e("In resume:",""+e);
    	}
    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==addB) // on clicking the add button
		{
			addF();
		}
		
		else if(v==clearB) // on clicking the clear button
		{
			clearE();
		}
		
		else if(v==enterB) // on clicking the clear button
		{
			transferF();
		}
		
		else if(v == addLine)
		{
			if(fileName == "")
			{
				MyConsole.setText("Please create or upload file");
				return;
			}
			program.append("\n");
		}
		
		else if(v == clearFile)
		{
			if(fileName == "")
			{
				MyConsole.setText("Please create or upload file");
				return;
			}
			program.setText(" ");
		}
		
		else if(v == addSemi)
		{
			if(fileName == "")
			{
				MyConsole.setText("Please create or upload file");
				return;
			}
			program.append(";");
		}
		
		
		else if(v == capsOn)
		{
			isUp = true;
			preload("big.txt");
			trainF();
			//Toast.makeText(this, "Continue", Toast.LENGTH_SHORT).show();
		}
		
		else if(v == capsOff)
		{
			isUp = false;
			preload("small.txt");
			trainF();
		}
		
		else if(v == numOn)
		{
			final CharSequence[] items = {"1", "2", "3","4","5","6","7","8","9","0"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Digits");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        /*Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
			        String prog = program.getText().toString();
			        program.setText(prog+" "+items[item]);*/
			    	if(fileName == "")
					{
						MyConsole.setText("Please create or upload file");
						return;
					}
			    	program.append(items[item]);
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		else if(v == symOn)
		{
			final CharSequence[] items = {":", "&", "*","#","@","?","(",")","{","}"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Symbols");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        /*Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
			        String prog = program.getText().toString();
			        program.setText(prog+" "+items[item]);*/
			    	if(fileName == "")
					{
						MyConsole.setText("Please create or upload file");
						return;
					}
			    	program.append(items[item]);
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		else if(v == saveFile)
		{
			// take contents from program and write it to the file given by name.txt
			if(!(fileName.equals("")))
			{
				String toStore = program.getText().toString();
				File file = new File(Environment.getExternalStorageDirectory()+"/programs/"+fileName);
				try {
		            FileOutputStream f = new FileOutputStream(file);
		            PrintWriter pw = new PrintWriter(f);
		            pw.print(toStore);
		            pw.flush();
		            pw.close();
		            f.close();
		            MyConsole.setText("File has been saved");
				}
				catch(Exception e)
				{
					Log.e("saveFile",""+e);
				}
			}
			else
			{
				MyConsole.setText("fileName = empty");
			}
		}
		
		else if(v == newFile)
		{
			// ask user for name - dialog and save that file with name in download folder
			
			program.setText("");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			alert.setTitle("Create File");
			alert.setMessage("Enter file name");
	
			// Set an EditText view to get user input
			final EditText input = new EditText(Main.this);
			alert.setView(input);
	
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  fileName = input.getText().toString();
				  MyConsole.setText("Creating "+fileName);
				  try { // catches IOException below
						File dir = Environment.getExternalStorageDirectory();
						FileWriter writer = new FileWriter(new File(dir+"/programs/", fileName));
						writer.append(" ");
						//writer.append("Hello\n");
						//writer.append("Etc...\n");
						writer.flush();
						writer.close();
						MyConsole.setText("File: "+fileName+ " created");
					}
					catch(Exception e)
					{
						Log.e("New",""+e);
					}
				  dialog.dismiss();
				}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
					  //s.cdt.start();
					  dialog.dismiss();
				  }
				});
		
				alert.show();
				
				
		}
		
		else if(v == loadFile)
		{
			String name;
			int count = 0;
			program.setText("");
			File sdCardRoot = Environment.getExternalStorageDirectory();
			File yourDir = new File(sdCardRoot, "/programs");
			for (File f : yourDir.listFiles()) {
			    if (f.isFile())
			    {
			        count++;
			    }
			       
			}
			
			myData = new CharSequence[count];
			count = 0;
			
			for (File f : yourDir.listFiles()) {
			    if (f.isFile())
			    {
			        name = f.getName();
			        myData[count] = name;
			        count++;
			    }
			        
			}
			// add files to dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Files");
			builder.setItems(myData, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	fileName = myData[item].toString();
			    	load(fileName);
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
			
		}
		
	}
	

	
	public void load(String name)
	{
		FileInputStream fileIS;
    	try{
    		   File f = new File(Environment.getExternalStorageDirectory()+"/programs/"+fileName);
    		 
    		   fileIS = new FileInputStream(f);
    		 
    		   BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
    		 
    		   String line = new String();
    		   
    		   while((line = buf.readLine())!= null){
    			  program.append(line+"\n");
    		   }
    		  
    	}
    	catch(Exception e)
    	{
    		Log.e("load",""+e);
    	}
	}
	
	/** 
	 * Function to add the letters to the sampleList Vector
	 * Letter drawn on the canvas and entered in the edittext box are extracted.
	 */
	
	public void transferF()
	{
		if(fileName == "")
		{
			MyConsole.setText("Please create or upload file");
			return;
		}
		String toBePut = lettersWritten.getText().toString();
		addColor(toBePut);
	}
	
	public void addColor(String input)
	{
		SpannableString str;
		if(search(input))
		{
			str = SpannableString.valueOf(input);
			str.setSpan(new ForegroundColorSpan(0xff0099ff), 0, input.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			program.append(str);
			program.append(" ");
			lettersWritten.setText("");
			return;
		}
		program.append(input+" ");
		lettersWritten.setText("");
	}
	
	public boolean search(String searchStr)
	{
		boolean found = false;
		Iterator<String>  iter = keywords.iterator();
		String curItem="";
		int pos=0;

		while ( iter.hasNext() == true ) {
			pos=pos+1;
			curItem =(String) iter .next();
			if (curItem.equalsIgnoreCase(searchStr)  ) {
				found = true;
				break;
			}
		}
		if ( found == false ) {
			pos=0;
			return false;
		}
		if (pos!=0)
		 {
			return true;
		 }
		else
		 {
			return false;
		 }
	}
	
	public void addF()
	{
		try
		{
			s.cdt.cancel();
			if(s.points.isEmpty())
				{
					MyConsole.setText("Please Enter a letter to add");
					Log.e("List Empty","Nothing to add");
					return;
				}
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
	
			alert.setTitle("Add a Letter");
			alert.setMessage("Enter a letter");
	
			// Set an EditText view to get user input
			final EditText input = new EditText(Main.this);
			alert.setView(input);
	
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  letter = input.getText().toString();
			  MyConsole.setText("Hold on for a minute!");
			  addBase();
			  if(isUp)
				  saveF("big.txt");
			  else
				  saveF("small.txt");
			  clearF();
			  if(isUp)
				  preload("big.txt");
			  else
				  preload("small.txt");
			  trainF();
			  MyConsole.setText("Continue");
			  dialog.dismiss();
			  }
			});
	
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
				  s.cdt.start();
				  dialog.dismiss();
			  }
			});
	
			alert.show();
		}
		catch(Exception ex)
		{
			Log.e("Add",""+ex);
		}
	}
	
	public void addBase()
	{
		int i;
		if (letter.length() > 1)
	    {
	      Log.d("AddLetter","Enter only one letter.");
	      return;
	    }

	    if (letter.length() < 1)
	    {
	      Log.d("AddLetter","Enter a letter to add.");
	      return;
	    }
		s.downSample();
		
		final SampleData sampleData = (SampleData) sample.getData().clone();
	    sampleData.setLetter(letter.charAt(0));

	    for (i = 0; i < sampleList.size(); i++)
	    {
	      final SampleData str = (SampleData) sampleList.elementAt(i);
	      

	      if (str.compareTo(sampleData) > 0)
	      {
	        sampleList.insertElementAt(sampleData, i);
	        
	      
	        return;
	      }
		  
	    }
	    
	    sampleList.insertElementAt(sampleData, sampleList.size());
	    
	    
	    s.clear();
	    
		
		Log.d("Add","It has been clicked");
	}
	
	/**
	 * Function to clear the drawing area.
	 */
	
	public void clearF()
	{
		try
		{
			s.clear();
			s.requestFocus();
			sample.getData().clear();
			
			lettersWritten.requestFocus();
			Log.d("Clear","It has been clicked");
		}
		catch(Exception ex)
		{
			Log.e("Clear",""+ex);
		}
	}
	
	
	public void clearE()
	{
		try
		{
			s.clear();
			s.requestFocus();
			sample.getData().clear();
			lettersWritten.setText("");
			lettersWritten.requestFocus();
			Log.d("Clear","It has been clicked");
		}
		catch(Exception ex)
		{
			Log.e("Clear",""+ex);
		}
	}
	
	/**
	 * Function to train the neural network based on the contents of sampleList
	 */
	
	public void trainF()
	{
		try
	    {
		  Log.d("training","Train has been clicked");
	      int inputNeuron = Main.DOWNSAMPLE_HEIGHT
	          * Main.DOWNSAMPLE_WIDTH;
	      int outputNeuron = sampleList.size();

	      TrainingSet set = new TrainingSet(inputNeuron, outputNeuron);
	      set.setTrainingSetCount(sampleList.size());

	      for (int t = 0; t < sampleList.size(); t++)
	      {
	        int idx = 0;
	        SampleData ds = (SampleData) sampleList.elementAt(t);
	        for (int y = 0; y < ds.getHeight(); y++)
	        {
	          for (int x = 0; x < ds.getWidth(); x++)
	          {
	            set.setInput(t, idx++, ds.getData(x, y) ? .5 : -.5);
	          }
	        }
	      }

	      net = new KohonenNetwork(inputNeuron, outputNeuron, this);
	      net.setTrainingSet(set);
	      net.learn();
	      MyConsole.setText("Trained. Ready to recognize.");
	    } catch (Exception e)
	    {
	      Log.e("Training:" , ""+e);

	    }
	}
	
	/**
	 * Function to save the contents of the sampleList in the file sampleData.txt
	 * 
	 */
	
	public void saveF(String fileName)
	{
		File file = new File(Environment.getExternalStorageDirectory()+"/"+fileName);
		try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i < sampleList.size(); i++) {
				final SampleData ds = (SampleData) sampleList.elementAt(i);
				System.out.println(ds.getLetter() + ":");
				pw.print(ds.getLetter() + ":");
				for (int y = 0; y < ds.getHeight(); y++) {
					for (int x = 0; x < ds.getWidth(); x++) {
		
						pw.print(ds.getData(x, y) ? "1" : "0");
					}
				}
				
				pw.println();
			}
            
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            
            Log.i("Saving", "******* File not found. Did you" +
                            " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
            Log.e("Saving",""+e);
        } catch (IOException e) {
           
        	Log.e("Saving",""+e);
        }
        catch(Exception ex)
        {
        	Log.e("Saving",""+ex);
        }
		Log.d("Save","It has been clicked");
	}
	
	
	/**
	 * Function to recognize the letter drawn in the canvas
	 */
	
	public void recogF()
	{
		
		if (net == null)
	    {
	      Log.d("Recognizing","I need to be trained first!");
	      return;
	    }
	    s.downSample();

	    double input[] = new double[5 * 5];
	    int idx = 0;
	    SampleData ds = sample.getData();
	    for (int y = 0; y < ds.getHeight(); y++)
	    {
	      for (int x = 0; x < ds.getWidth(); x++)
	      {
	        input[idx++] = ds.getData(x, y) ? .5 : -.5;
	      }
	    }

	    double normfac[] = new double[1];
	    double synth[] = new double[1];

	    int best = net.winner(input, normfac, synth);
	    char map[] = mapNeurons();
	   
	    String letters = null;
		letters = lettersWritten.getText().toString().trim();
		lettersWritten.setText(letters +  map[best]);
	   
	    
		Log.d("Recognize","It has been clicked");
		clearF();
	}
	
	char[] mapNeurons()
	  {

	    char map[] = new char[sampleList.size()];
	    double normfac[] = new double[1];
	    double synth[] = new double[1];

	    for (int i = 0; i < map.length; i++)
	      map[i] = '?';
	    for (int i = 0; i < sampleList.size(); i++)
	    {
	      double input[] = new double[5 * 5];
	      int idx = 0;
	      SampleData ds = (SampleData) sampleList.elementAt(i);
	      for (int y = 0; y < ds.getHeight(); y++)
	      {
	        for (int x = 0; x < ds.getWidth(); x++)
	        {
	          input[idx++] = ds.getData(x, y) ? .5 : -.5;
	        }
	      }

	      int best = net.winner(input, normfac, synth);
	      map[best] = ds.getLetter();
	    }
	    return map;
	  }
	
	/**
	 * Fucntion to load the contents from file sampleData.txt into sampleList
	 */
	
	public void preload(String fileName)
	{
	    	FileInputStream fileIS;
	    	try{
	    		checkExternalMedia();
	    		 
	    		   File f = new File(Environment.getExternalStorageDirectory()+"/"+fileName);
	    		 
	    		   fileIS = new FileInputStream(f);
	    		 
	    		   BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
	    		 
	    		   String line = new String();
	    		   
	    		   int i = 0;
	    		   sampleList.clear();
	    		 
	    		   while((line = buf.readLine())!= null){
	    		 
	    			   final SampleData ds = new SampleData(line.charAt(0),
	   						Main.DOWNSAMPLE_WIDTH, Main.DOWNSAMPLE_HEIGHT);
	   				sampleList.insertElementAt(ds, i++);
	   				int idx = 2;
	   				for (int y = 0; y < ds.getHeight(); y++) {
	   					for (int x = 0; x < ds.getWidth(); x++) {
	   						ds.setData(x, y, line.charAt(idx++) == '1');
	   					}
	   				}
	    			   Log.d("Preload","\n"+line);
	    		   }
	    		 
	    		} catch (FileNotFoundException e) {
	    		 
	    			Log.e("lineE1: ",""+e);
	    		 
	    		} catch (IOException e){
	    		 
	    			Log.e("lineE2: ",""+e);
	    		}
	    		catch(Exception e)
	    		{
	    			Log.e("lineE3: ",""+e);
	    			e.printStackTrace();
	    		}
	}
	
	/**
	 * Function to check if there are permissions to write and read from external storage
	 */
	
	private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }  
        Log.d("Checking","\n\nExternal Media: readable="
                +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
       
    }
}