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

import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo for the initialization of <packaging>data</packaging>
 *
 */
@Mojo(name = "dataInit", defaultPhase = LifecyclePhase.VALIDATE)
public class DataInitMojo extends AbstractConfiguredMojo {

	private static final String ADDITIONAL_CLASSPATH = "maven.test.additionalClasspath";

	public void execute() throws MojoExecutionException {
		// Add the dataArchive to test classpath
		Properties props = project.getProperties();
		String classpath = dataArchive.getAbsolutePath();
		if (props.containsKey(ADDITIONAL_CLASSPATH)) {
			classpath += "," + props.getProperty(ADDITIONAL_CLASSPATH);
		}
		props.put(ADDITIONAL_CLASSPATH, classpath);
	}
}
