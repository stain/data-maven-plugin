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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.taverna.robundle.Bundle;
import org.apache.taverna.robundle.Bundles;

/**
 * Mojo for archiving data as a Research Object
 */
@Mojo(name = "archive", defaultPhase = LifecyclePhase.PACKAGE)
public class ArchiveMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	private File outputDirectory;

	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}.bundle.zip", property = "researchObject", required = true)
	private File researchObject;

	@Parameter(defaultValue = "data", property = "dataDir", required = true)
	private File dataDirectory;

	@Parameter(defaultValue = "data/${project.artifactId}", property = "targetPath", required = true)
	private String targetPath;

	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession session;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException {
		getLog().info("Archiving as Research Object");
		Bundle bundle = openBundle();
		
		try {
			bundle.close();
		} catch (IOException e) {
			throw new MojoExecutionException("Can't write researchObject " + researchObject, e);
		}
		project.getArtifact().setFile(researchObject);
		
	}

	private Bundle openBundle() throws MojoExecutionException {
		Path bundlePath = researchObject.toPath();
		try {
			if (Files.exists(bundlePath)) {
				return Bundles.openBundle(bundlePath);
			}
			return Bundles.createBundle(bundlePath);
		} catch (IOException e) {
			throw new MojoExecutionException("Can't open researchObject " + researchObject, e);
		}
	}

}
