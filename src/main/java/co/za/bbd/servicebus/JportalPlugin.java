package co.za.bbd.servicebus;

import bbd.jportal2.JPortal2Arguments;
import bbd.jportal2.ProjectCompiler;
import bbd.jportal2.ProjectCompilerBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Mojo(name = "jportal", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class JportalPlugin
        extends AbstractMojo {

    public JportalPlugin() {}

    private static final Logger logger = LoggerFactory.getLogger(JportalPlugin.class);

    public JportalPlugin(String sourcePath, List<String> generators) {
        this.sourcePath = sourcePath;
        this.generators = generators;
    } //For testing purposes, this won't actually be called by Maven

    @Parameter(property = "jportal.sourcePath", required = true)
    private String sourcePath;

    @Parameter(property = "jportal.generators", required = true)
    private List<String> generators;

    @Parameter(property = "jportal.compilerFlags")
    private List<String> compilerFlags = Collections.emptyList();

    @Parameter(property = "jportal.additionalArguments")
    private String additionalArguments;


    public void execute() throws MojoExecutionException {

        try {

            JPortal2Arguments arguments = new JPortal2Arguments();
            arguments.setInputDirs(Collections.singletonList(sourcePath));
            arguments.setFlags(compilerFlags);
            arguments.setBuiltinSIProcessors(generators);

            logger.info("JPortal2 is configured with the following arguments:");
            logger.info(arguments.toString());

            ProjectCompiler pj = ProjectCompilerBuilder.build(arguments, additionalArguments);

            logger.info("JPortal2 generating sources...");
            pj.compileAll();
            logger.info("JPortal2 completed successfully");


        } catch (Exception e) {
            throw new MojoExecutionException("JPortal2 compilation unsuccessful", e);
        }
    }
}
