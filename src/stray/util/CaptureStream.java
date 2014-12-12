package stray.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class CaptureStream extends OutputStream {

	private StringBuilder buffer;
	private Consumer consumer;
	private PrintStream old;

	public CaptureStream(Consumer consumer, PrintStream old) {
		buffer = new StringBuilder(256);
		this.old = old;
		this.consumer = consumer;
	}

	 @Override
     public void write(int b) throws IOException {
         char c = (char) b;
         String value = Character.toString(c);
         buffer.append(value);
         if (value.equals("\n")) {
             consumer.appendText(buffer.toString());
             buffer.delete(0, buffer.length());
         }
         old.print(c);
     }  

	public static interface Consumer {

		public void appendText(String s);
	}
}
