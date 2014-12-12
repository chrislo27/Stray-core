package stray.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.badlogic.gdx.Gdx;

public class Logger extends com.badlogic.gdx.utils.Logger {

	public Logger(String tag) {
		super(tag);
	}

	public Logger(String tag, int lvl) {
		super(tag, lvl);
	}

	private SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");

	private String getDateStamp() {
		return "[" + dateformat.format(Calendar.getInstance().getTime()) + "]";
	}
	

	@Override
	public void debug(String message) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.DEBUG) {
			System.out.println(getDateStamp() + " [DEBUG] "
					+ message);
		}
	}
	
	@Override
	public void debug(String message, Exception exception) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [INFO] "
					+ message);
			exception.printStackTrace(System.out);
		}
	}

	@Override
	public void info(String message) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.INFO) {
			System.out.println(getDateStamp() + " [INFO] "
					+ message);
		}
	}
	
	@Override
	public void info(String message, Exception exception) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [INFO] "
					+ message);
			exception.printStackTrace(System.out);
		}
	}

	@Override
	public void error(String message) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [ERROR]"
					+ message);
		}
	}

	@Override
	public void error(String message, Throwable exception) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [ERROR]"
					+ message);
			exception.printStackTrace(System.out);
		}
	}
	
	public void warn(String message) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [WARN] "
					+ message);
		}
	}

	public void warn(String message, Throwable exception) {
		if (getLevel() >= com.badlogic.gdx.utils.Logger.ERROR) {
			System.out.println(getDateStamp() + " [WARN] "
					+ message);
			exception.printStackTrace(System.out);
		}
	}
}
