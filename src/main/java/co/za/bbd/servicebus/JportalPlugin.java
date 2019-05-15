package co.za.bbd.servicebus;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Mojo(name = "jportal", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class JportalPlugin
        extends AbstractMojo {
    public JportalPlugin() {
    }

    public JportalPlugin(String sourcePath, String buildPath, String jportalPath,
                         String projectFile) {
        this.sourcePath = sourcePath;
        this.buildPath = buildPath;
        this.jportalPath = jportalPath;
        this.projectFile = projectFile;
        anyDbPathPostfix = ""; //For testing
    } //For testing purposes, this won't actually be called by Maven

    @Parameter(property = "jportal.sourcePath", required = true)
    private String sourcePath;

    @Parameter(property = "jportal.buildPath", required = true)
    private String buildPath;

    @Parameter(property = "jportal.jportalPath", required = true)
    private String jportalPath;

    @Parameter(property = "jportal.projectFile", required = true)
    private String projectFile;

    private String anyDbPathPostfix = "scripts/anydb/";
    //TODO: Change to maven dependency folder
    private String effectiveProjectFilePath;

    //TODO:This will become useless when we have an artifact repository
    private void copyResource(String resourceName, String path) throws IOException {
        URL inputUrl = getClass().getResource("/" + resourceName);
        File dest = new File(path + resourceName);

        FileUtils.copyURLToFile(inputUrl, dest);
    }

    private String inQuotes(String val) {
        return "\"" + val + "\"";
    }

    public String generateEffectiveAnyDBProjectFile(String projPath, String srcPath, String bldPath,
                                                    String outPath) throws IOException {
        String content = FileUtils.fileRead(new File(projPath));
        String replaced = content.replace("${sourcePath}", srcPath);
        replaced = replaced.replace("${buildPath}", bldPath);
        String effectivePath = outPath + "effectiveAnyDBProject.xml";
        File file = new File(effectivePath);
        file.getParentFile().mkdirs();
        FileUtils.fileWrite(effectivePath, replaced);
        return effectivePath;
    }

    public void execute() throws MojoExecutionException {
        try {
            effectiveProjectFilePath = generateEffectiveAnyDBProjectFile(projectFile, sourcePath,
                    buildPath, buildPath + anyDbPathPostfix);
        } catch (IOException e) {
            getLog().error("Could not generate effective project file.");
            throw new MojoExecutionException(e.getMessage());
        }

        String[] arguments = new String[]{
                "python",
                buildPath + anyDbPathPostfix + "anydbMake.py",
                "--buildPath",
                inQuotes(buildPath),
                "--sourcePath",
                inQuotes(sourcePath),
                "--jportal",
                inQuotes(jportalPath),
                "--verbose",
                effectiveProjectFilePath
        };

        getLog().info("Configured AnyDB Python with arguments:");
        for (String arg : arguments) {
            getLog().info(arg);
        }

        try {
            if (!new File(buildPath + anyDbPathPostfix + "anydbMake.py").exists())
                copyResource("anydbMake.py", buildPath + anyDbPathPostfix);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(arguments);
            Process p = pb.start();
            getLog().info("Running AnyDB Python: ");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            line = bfr.readLine();
            getLog().info(line);
            while ((line = bfr.readLine()) != null) {
                getLog().info(line);
            }

            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new MojoExecutionException("JPortal Build Unsuccessful.");
            }

            getLog().info("Exit Code : " + exitCode);

            p.destroy();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
