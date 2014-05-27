/**
 * 
 */
package net.castegaming.plugins.FPSCaste.util;

import java.util.Date;

/**
 * @author Brord
 *
 */
public class TimeUtil {
	/**
	 * returns -1 if the time cannot be transformed
	 * @return
	 */
	public static long getTimeMillis(String time){
		long date = 0;
		String[] splits = time.split(" ");
		try {
			for (String s : splits){
				if (s.length() > 1 && (s.endsWith("d") || s.endsWith("day") || s.endsWith("days"))){
					date += Integer.parseInt(s.substring(0, s.length()-1))*24*60*60*1000;
				} else if (s.length() > 1 && (s.endsWith("h") || s.endsWith("hour") || s.endsWith("hours"))){
					date += Integer.parseInt(s.substring(0, s.length()-1))*60*60*1000;
				} else if (s.length() > 1 && (s.endsWith("m") || s.endsWith("minute") || s.endsWith("minutes"))){
					date += Integer.parseInt(s.substring(0, s.length()-1))*60*1000;
				} else if (s.length() > 1 && (s.endsWith("s") || s.endsWith("second") || s.endsWith("seconds"))){
					date += Integer.parseInt(s.substring(0, s.length()-1))*1000;
				} else {
					throw new NumberFormatException();
				}
			}
			return date;
		} catch (NumberFormatException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * returns -1 if the time cannot be transformed
	 * @return
	 */
	public static Date getDateMillis(String time){
		return new Date(getTimeMillis(time));
	}
	
	public static String stampToReadable(long stamp){
		String time = "";
		int temp;
		if (stamp / (1000*60*60*24) > 0){
			temp = (int) (stamp / (1000*60*60*24));
			time += temp + temp == 1 ? "day " : " days ";
		}
		if ((stamp / (1000*60*60)) % 24 > 0){
			temp = (int) (stamp / (1000*60*60)) % 24;
			time += temp + temp == 1 ? "hour " : " hours ";
		}
		if ((stamp / (1000*60)) % 60 > 0){
			temp = (int) (stamp / (1000*60)) % 60;
			time += temp + temp == 1 ? "minute " : " minutes ";
		}
		if ((stamp / 1000) % 60 > 0){
			temp = (int) (stamp / 1000) % 60;
			time += temp + temp == 1 ? "second " : " seconds ";
		}
		
		if (time.equals("")) return "0 seconds...";
		return time.substring(0, time.length()-1);
	}
}
