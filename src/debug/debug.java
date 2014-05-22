package debug;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class debug {
	
	static long time;

	public static void notify(String code){
		System.out.println(compile(code));
	}
	public static void notify(int code){
		System.out.println(compile(Integer.toString(code)));
	}
	
	private static String compile(String code){
		if(code.equals("APP_STARTED"))time=System.currentTimeMillis();
		StackTraceElement[] trace = new Throwable().getStackTrace();
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		return("> "+trace[2].getClassName()+"::"+trace[2].getMethodName()+">"+code+" @ "+sdf.format(cal.getTime())+" -> t-minus("+(System.currentTimeMillis()-time)+")");
	}
}
