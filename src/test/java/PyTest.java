import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by bbdnet1339 on 2016/06/24.
 */
public class PyTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testPythonIsWorking() throws Exception{
            String [] arguments = new String[]{
                    "python",
                    "--version"
            };

            ProcessBuilder pb = new ProcessBuilder(arguments);
            Process p = pb.start();

            int exitCode = p.waitFor();
            assertEquals(exitCode,0);
    }





}
