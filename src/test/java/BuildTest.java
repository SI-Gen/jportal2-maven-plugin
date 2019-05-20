
import co.za.bbd.servicebus.JportalPlugin;
import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.List;

public class BuildTest extends TestCase {

    private String sourcePath;
    private List<String> generators = new ArrayList<>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        sourcePath = this.getClass().getResource(".").getPath();
    }

    public void testFullBuild() throws MojoExecutionException {

        generators.add(String.format("JavaJCCode:%s", sourcePath));
        generators.add(String.format("MSSqlDDL:%s", sourcePath));

        JportalPlugin plugin = new JportalPlugin(sourcePath, generators);
        plugin.execute();
    }
}
