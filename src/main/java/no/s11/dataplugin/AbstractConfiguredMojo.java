package no.s11.dataplugin;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class AbstractConfiguredMojo extends AbstractMojo {

	@Component
	protected MavenProjectHelper projectHelper; 
	
	@Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
	protected File buildOutput;

	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}.data.zip", property = "researchObject", required = true)
	protected File researchObject;

	@Parameter(defaultValue = "data", property = "dataDirectory", required = true)
	protected File dataDirectory;

	@Parameter(defaultValue = "data/${project.artifactId}", property = "targetPath", required = true)
	protected String targetPath;

	@Parameter(defaultValue = "${session}", readonly = true)
	protected MavenSession session;

	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;
	
	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	protected File outputDirectory;

	
}
