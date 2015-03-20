package replace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by htrembl2 on 3/19/15.
 */
public final class FileUtils {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private FileUtils() {}

    public static String readFile(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream in = new FileInputStream(file);
        try {
            in.read(buffer);
        }
        finally {
            in.close();
        }
        return new String(buffer, UTF_8);
    }

    public static void writeFile(File file, String string) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            out.write(string.getBytes(UTF_8));
        }
        finally {
            out.close();
        }
    }
}
