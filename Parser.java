/* I am a c++ and observerd the following 

file is not properly guarded. The getter/setter are synchronized, but no other access to the field file is synchronized. 
Not a massive issue at this point, but it should be better. Add more mutator methods and it starts becoming a big issue.

If file needs to be thread safe it should be a volatile variable (so it publishes across core/cache lines on change). 
Individual access to it should be properly synchronized.

String appending should be replaced by a StringBuilder that does a toString() at the end. 
The JVM will do this for you automatically, but not as efficiently as you want. You should not be doing

  String myString = "";
  myString += "hello";  // Don't put this in a loop

Input and output streams are never closed. Resource leak.

I'd put on a default constructor. But that's just me.

I'd probably read and write in blocks rather than individual characters.

There is duplicate code in the getContent()/getContentWithoutUnicode() which could be extracted ... not sure how I'd work it, but it irks me.

*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * This class is thread safe.
 */
public class Parser {
  private File file;
  public synchronized void setFile(File f) {
    file = f;
  }
  public synchronized File getFile() {
    return file;
  }
  public String getContent() throws IOException {
    FileInputStream i = new FileInputStream(file);
    String output = "";
    int data;
    while ((data = i.read()) > 0) {
      output += (char) data;
    }
    return output;
  }
  public String getContentWithoutUnicode() throws IOException {
    FileInputStream i = new FileInputStream(file);
    String output = "";
    int data;
    while ((data = i.read()) > 0) {
      if (data < 0x80) {
        output += (char) data;
      }
    }
    return output;
  }
  public void saveContent(String content) throws IOException {
    FileOutputStream o = new FileOutputStream(file);
    for (int i = 0; i < content.length(); i += 1) {
      o.write(content.charAt(i));
    }
  }
}
