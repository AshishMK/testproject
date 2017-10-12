package test.com.mygapplication.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class  UnCaughtException implements Thread.UncaughtExceptionHandler {
//	Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));
	//	super.onCreate(savedInstanceState);
private Thread.UncaughtExceptionHandler defaultUEH;

@Nullable
private Context app = null;

public UnCaughtException(Context app) {
this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
this.app = app;
    

}
private void addInformation(@NonNull StringBuilder message) {
	Context context= app.getApplicationContext();
    message.append("Locale: ").append(Locale.getDefault()).append('\n');
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo pi;
      pi = pm.getPackageInfo(context.getPackageName(), 0);
      message.append("trVersion: ").append(pi.versionName).append('\n');
      message.append("Package: ").append(pi.packageName).append('\n');
    } catch (Exception e) {
      Log.e("CustomExceptionHandler", "Error", e);
      message.append("Could not get Version information for ").append(
          context.getPackageName());
    }
    message.append("Phone Model: ").append(android.os.Build.MODEL).append(
        '\n');
    message.append("Android Version: ").append(
        android.os.Build.VERSION.RELEASE).append('\n');
    message.append("Board: ").append(android.os.Build.BOARD).append('\n');
    message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
    message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
    message.append("Host: ").append(android.os.Build.HOST).append('\n');
    message.append("ID: ").append(android.os.Build.ID).append('\n');
    message.append("Model: ").append(android.os.Build.MODEL).append('\n');
    message.append("mmProduct: ").append(android.os.Build.PRODUCT).append(
        '\n');
    message.append("Type: ").append(android.os.Build.TYPE).append('\n');
  }

public void uncaughtException(@NonNull Thread t, @NonNull Throwable e)
{   
	StringBuilder reports = new StringBuilder();
    addInformation(reports);
StackTraceElement[] arr = e.getStackTrace();
String throwableE =t.toString();
String report = e.toString()+"\n\n";
report += "--------- Stack trace ---------\n\n"+throwableE;
for (int i=0; i<arr.length; i++)
{
report += "    "+arr[i].toString()+"\n";
}
report += "-------------------------------\n\n";

// If the exception was thrown in a background thread inside
// AsyncTask, then the actual exception can be found with getCause
report += "--------- Cause ---------\n\n";
Throwable cause = e.getCause();
if(cause != null) {
report += cause.toString() + "\n\n";
arr = cause.getStackTrace();
for (int i=0; i<arr.length; i++)
{
report += "    "+arr[i].toString()+"\n";
}
}
report += "-------------------------------\n\n";
report +=e.getMessage();
report += "-------------------------------\n\n";
reports.append('\n').append(report);


//System.exit(0);
sendReport(reports.toString());
send_mail(reports);

// context.startActivity(Intent.createChooser(sendIntent, "Error Report"));

/*try {
	 String dirname = Environment.getExternalStorageDirectory()
	 			+ "/";
	 File output = new File(dirname);
     if ((!output.mkdirs()) && (!output.exists()))
     {
   	       }
     FileOutputStream fout = new FileOutputStream(new File(dirname+ "stack.trace"));
	OutputStreamWriter osw = new OutputStreamWriter(fout);
	osw.write(reports.toString());
	osw.flush();
	osw.close();
FileOutputStream trace = app.openFileOutput(
"stack.trace", Context.MODE_PRIVATE);
trace.write(reports.toString().getBytes());
trace.close();
} catch(IOException ioe) {
// ...
}*/
defaultUEH.uncaughtException(t, e);

}
void send_mail(@NonNull final StringBuilder reports){
	  new Thread(){
          @Override
          public void run() {
              Looper.prepare();
              try{
	Intent sendIntent = new Intent(Intent.ACTION_SEND);
	String subject = "Your App crashed! Fix it!";
	/*StringBuilder body = new StringBuilder("Yoddle");
	body.append('\n').append('\n');
	body.append(reports).append('\n').append('\n');*/
	//  sendIntent.setType("text/plain");
	sendIntent.setType("message/rfc822");
	sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "ashish.nautiyal102@gmail.com" });
	sendIntent.putExtra(Intent.EXTRA_TEXT, reports.toString());
	sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
	sendIntent.setType("message/rfc822");
	app.startActivity(sendIntent);
	//System.exit(0);
    Looper.loop();}
              catch (Exception e) {
				// TODO: handle exception
            	  System.out.println("eex "+e.getMessage());
			}
}
}.start();  
}
boolean suc=false;
boolean sendReport(@NonNull final String report){
	
	  new Thread(){
          @Override
          public void run() {
              Looper.prepare();
              try{
            	  String dirname = Environment.getExternalStorageDirectory()
          	 			+ "/";
          	 File output = new File(dirname);
               if ((!output.mkdirs()) && (!output.exists()))
               {
             	       }
               FileOutputStream fout = new FileOutputStream(new File(dirname+ "diary_stack.trace"));
          	OutputStreamWriter osw = new OutputStreamWriter(fout);
          	osw.write(report.toString());
          	osw.flush();
          	osw.close();
	  Looper.loop();}
              catch (Exception e) {
				// TODO: handle exception
            	  System.out.println("eex "+e.getMessage());
			}
              }
          }.start();  
	suc=false;
          return suc;
}
}