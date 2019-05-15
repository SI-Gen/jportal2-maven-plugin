
import co.za.bbd.servicebus.JportalPlugin;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

public class BuildTest extends TestCase {

    private String projectFile;
    private String jportalPath;
    private String sourcePath;
    private String buildPath;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Class testClass = this.getClass();

        //TODO: Test outdated
        projectFile = testClass.getResource("AnyDBTest.xml").getPath();
        jportalPath = testClass.getResource("jportal2-1.0.jar").getPath();
        sourcePath = projectFile.replace("AnyDBTest.xml", "");
        buildPath = sourcePath;

        // Replacements cater for most used drive letters
        projectFile = projectFile.replaceAll("/([CDEFGHIJOXYZ]):/", "$1:/");
        jportalPath = jportalPath.replaceAll("/([CDEFGHIJOXYZ]):/", "$1:/");
        sourcePath = sourcePath.replaceAll("/([CDEFGHIJOXYZ]):/", "$1:/");
        buildPath = buildPath.replaceAll("/([CDEFGHIJOXYZ]):/", "$1:/");

    }

    public void testEffectiveGeneration() throws IOException {
        JportalPlugin plugin = new JportalPlugin();
        String effectivePath = plugin.generateEffectiveAnyDBProjectFile(projectFile, sourcePath,
                buildPath, buildPath);
        String newContent = FileUtils.fileRead(new File(effectivePath));
        assertFalse(newContent.contains("${sourcePath}") || newContent.contains("${buildPath}"));
    }

    public void testFullBuild() throws MojoExecutionException {
        JportalPlugin plugin = new JportalPlugin(sourcePath, buildPath, jportalPath,
                projectFile);
        plugin.execute();
    }
}
