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
import java.nio.file.StandardCopyOption;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.taverna.robundle.Bundle;
import org.apache.taverna.robundle.Bundles;

/**
 * Mojo for archiving data as a Research Object
 */
@Mojo(name = "archive", defaultPhase = LifecyclePhase.COMPILE)
public class ArchiveMojo extends AbstractConfiguredMojo {

	private Bundle openBundle() throws IOException {
		Path bundlePath = researchObject.toPath();
		if (Files.exists(bundlePath)) {
			return Bundles.openBundle(bundlePath);
		}
		Files.createDirectories(bundlePath.getParent());
		return Bundles.createBundle(bundlePath);
	}
	
	public void execute() throws MojoExecutionException {
		getLog().info("Archiving as Research Object");
		try (Bundle bundle = openBundle()) {
			Path toDir = bundle.getPath(targetPath);
			Files.createDirectories(toDir);
			
			archive(dataDirectory, toDir);
			archive(new File(buildOutput, "data"), toDir);
			
			// TODO: Downloads?
			
		} catch (IOException e) {
			throw new MojoExecutionException("Can't write to " + researchObject + ": " + e.getMessage(), e);
		}
		projectHelper.attachArtifact(project, "data.zip", researchObject);
		
	}

	private void archive(File fromDir, Path toDir) throws IOException {
		Path fromPath =  fromDir.toPath();			
		if (Files.isDirectory(fromPath)) {
			getLog().debug("Archiving " + fromPath  + " to " + toDir);
			Bundles.copyRecursively(fromPath, toDir, 
					StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
		}
	}


}
