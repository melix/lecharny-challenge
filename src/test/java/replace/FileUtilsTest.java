package replace;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void test() throws Exception {
        String expected = "aaaaéééé\n\rxxx";
        File file = folder.newFile();
        FileUtils.writeFile(file, expected);
        String actual = FileUtils.readFile(file);
        assertEquals(expected, actual);
    }
}