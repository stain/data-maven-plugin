/*
 * Copyright 2015 University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.s11.dataplugin;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Mojo for adding data/ resources
 *
 */
@Mojo(name = "add", defaultPhase = LifecyclePhase.INITIALIZE)
public class AddDataMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	private File outputDirectory;

	@Parameter(defaultValue = "data", property="dataDir", required=true)
	private File dataDirectory;

	@Parameter(defaultValue="data", property="targetPath", required=true)
	private String targetPath;

    @Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession session;

    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject project;


	public void execute() throws MojoExecutionException {
		if (! dataDirectory.isDirectory()) {
			getLog().debug("Not a directory: " + dataDirectory);
			return;
		}
		getLog().info("Adding data from: " + dataDirectory);
		Resource resource = new Resource();
		resource.setDirectory(dataDirectory.getPath());
		resource.setTargetPath(targetPath);
		project.addResource(resource);
	}
}
