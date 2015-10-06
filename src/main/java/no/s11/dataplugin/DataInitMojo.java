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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo for the initialization of <packaging>data</packaging>
 *
 */
@Mojo(name = "dataInit", defaultPhase = LifecyclePhase.INITIALIZE)
public class DataInitMojo extends AbstractConfiguredMojo {
	

	public void execute() throws MojoExecutionException {
		
		try {
			project.getTestClasspathElements().add(dataArchive.getAbsolutePath());
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("Can't add to test classpath: " + dataArchive, e);
		}
		
		System.out.println("Plugins");
		System.out.println(project.getBuild().getPluginsAsMap().keySet()); 
		//project.getPlugin(pluginKey)
		
		 
		Artifact testArtifact = ArtifactUtils.copyArtifactSafe(project.getArtifact());
		testArtifact.setScope(Artifact.SCOPE_TEST);
		testArtifact.setFile(dataArchive);
		testArtifact.setResolved(true);
		testArtifact.selectVersion(project.getVersion());
		//testArtifact.setType()); ??? not available
		project.getArtifacts().add(project.getArtifact());
		
		
	}
}
