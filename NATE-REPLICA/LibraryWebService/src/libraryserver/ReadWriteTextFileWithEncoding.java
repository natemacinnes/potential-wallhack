package libraryserver;


/**
 * Enables easy logging of client and server activities to file and console
 *
 * @author Nathan MacInnes
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class ReadWriteTextFileWithEncoding {

	// PRIVATE 
	private final String fFileName;
	private final String fEncoding;
	private final String FIXED_TEXT = "But soft! what code in yonder program breaks?";

  /** Constructor. */
  public ReadWriteTextFileWithEncoding(String aFileName, String aEncoding){
    fEncoding = aEncoding;
    fFileName = aFileName;
  }
  
  /** Write fixed content to the given file. */
  void write() throws IOException  {
    log("Writing to file named " + fFileName + ". Encoding: " + fEncoding);
    Writer out = new OutputStreamWriter(new FileOutputStream(fFileName, true), fEncoding);
    try {
      out.write(FIXED_TEXT);
    } finally {
      out.close();
    }
  }
  /** Write string to a given file. */
  public void write(String message) throws IOException {
	 // log("Writing to file named " + fFileName + ". Encoding: " + fEncoding);
	  log(message);
	  
	  Writer out = new OutputStreamWriter(new FileOutputStream(fFileName, true), fEncoding);
	  try {
		  out.append(message + "\n");
	  } finally {
		  out.close();
	  }
  }
  /** Read the contents of the given file. */
  void read() throws IOException {
    log("Reading from file.");
    StringBuilder text = new StringBuilder();
    String NL = System.getProperty("line.separator");
    Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
    try {
      while (scanner.hasNextLine()){
        text.append(scanner.nextLine() + NL);
      }
    }
    finally{
      scanner.close();
    }
    log("Text read in: " + text);
  }
  
  private void log(String aMessage){
    System.out.println(aMessage);
  }
}

