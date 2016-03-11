# Data Maven Plugin

Plugin for [Apache Maven](https://maven.apache.org/) for
generating data archives.

**Status**: Early prototype.

This plugin adds support for making a Maven project with
`<packaging>data</packaging>`, where resources within its
`data/` folder are directly added to the output, a `.data.zip` file.

The archive is a valid
[Research Object bundle](https://w3id.org/bundle), a specialization of ZIP
that adds a manifest for provenance and annotations.

The archive artifact is deployed as a `data.zip`, meaning it can be used
as a `<dependency>` in other Maven projects.

Using this plugin for data publishing mean you benefit from the Maven
ecosystem of plugins for your data preparation and release process,
e.g. unit testing, sha1 checksums, Maven repositories and reproducible builds.

Further development of this plugin aims to capture provenance
(e.g. for downloaded and generated data files) and to propagate attributions
and annotations from upstream data sources.



## Usage

This plugin is not yet available from a Maven repository.
First build the plugin source code using:

    mvn clean install


Then from a new Maven project which will publish data, modify its `pom.xml`
to include `<packaging>` and `<plugin>` as below:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

  <groupId>com.example.data</groupId>
  <artifactId>example-data</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>data</packaging>

    <build>
      <plugins>
        <plugin>
          <groupId>no.s11.dataplugin</groupId>
        	<artifactId>data-maven-plugin</artifactId>
        	<version>0.0.6</version>
          <extensions>true</extensions>
        </plugin>
      </plugins>
    </build>
    <pluginRepositories>
  		<pluginRepository>
  			<id>bintray-stain-maven</id>
  			<name>bintray-plugins</name>
  			<url>http://dl.bintray.com/stain/maven</url>
  			<snapshots>
  				<enabled>false</enabled>
  			</snapshots>
  		</pluginRepository>
  	</pluginRepositories>
</project>
```

**Note**: The `<version>` in the example above might not be current, see the
[latest release](https://github.com/stain/data-maven-plugin/releases) for the
updated version number to use, or the `pom.xml` of the
[data-maven-plugin source code source code](https://github.com/stain/data-maven-plugin/releases)
if you are using a `-SNAPSHOT` build.


Build the data project with `mvn clean package`, or
install with `mvn clean install`, which will add the `data.zip`
archive artifact to your local Maven repository.

## License

[Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

(c) 2015 University of Manchester

Author: [Stian Soiland-Reyes](http://orcid.org/0000-0001-9842-9718)


## Contribute

Feel free to contribute [pull requests](https://github.com/stain/data-maven-plugin/pulls),
or raise an [issue](https://github.com/stain/data-maven-plugin/issues) for any
questions or bugs.




## Using data archives

One great advantage when creating data archives with Maven is that you can use existing
Maven support and infrastructure, e.g. deploy the archive to an
[Artifactory](https://www.jfrog.com/open-source/) Maven repository, and
at the same time get the benefits for your data that has already proved
useful for software, like:

* Checksums on download (Maven uses md5 and sha1)
* Multiple repositories for fallback
* Mirroring (e.g. Artifactory can publish to [Bintray](https://bintray.com/)
* [Semantic Versioning](http://semver.org/)
* Reproducible data builds with a `pom.xml`
* Release process with [Maven Release plugin](http://maven.apache.org/maven-release/maven-release-plugin/)
* Dependencies (data and software)
* Unit testing of data


### As dependency

Once a data archive has been installed or deployed to a Maven repository,
other Maven projects can use it as a `<dependency>`:

```xml
<dependency>
  <groupId>com.example.data</groupId>
  <artifactId>example-data</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <type>data.zip</type>
</dependency>
```

(A `<repository>` setting might also be needed.)

The archived data should then be available as `data/example-data/`
on the project's classpath. You do not need to have the `data-maven-plugin`
to use the artifacts, as the `.data.zip` files can be accessed as
JARs.

### From the command line

```
mvn dependency:get -DrepoUrl=http://data.openphacts.org/artifactory/data/ -DgroupId=org.openphacts.data  \
   -DartifactId=ops-rsc-surechembl-linksets -Dversion=LATEST -Dpackaging=data.zip -Ddest=/tmp/$$.zip ;
unzip /tmp/$$.zip ; rm /tmp/$$.zip
```

## Extracting

As a ZIP file, you can extract the content of a `data.zip` file with regular
decompression tools like [unzip](http://www.info-zip.org/UnZip.html),
[jar](https://docs.oracle.com/javase/tutorial/deployment/jar/unpack.html) or
[7-zip](http://www.7-zip.org/).



### Extracting on OS X

Note that the built-in OS X support for ZIP files (Archive Utilllity)
has a bug in that it detects the `mimetype` value of `application/vnd.wf4ever.robundle+zip`
and believe that the the archive is not a ZIP file, and therefore tries to compress the
ZIP file instead of decompressing it.  To work around
this, either `unzip` in a Terminal session, or open the `.data.zip` file
with an alternative tool like [StuffIt Expander](http://my.smithmicro.com/stuffit-expander-mac.html)


## Creating data archives

Data archives should be created as separate Maven projects or modules that
only contain and prepare the data. These projects should be of the type
`<packaging>data</packaging>`, which requires `<extensions>true</extensions>`
when listing the `data-maven-plugin`.

It is possible, but not recommended, to use the `data:archive` goal from
within regular `jar` projects.

### Data folders

Create a folder `data/` to contain the data files that
should be archived. These files will be copied directly to the archive, bypassing
`target/classes`. Archiving supports large files (e.g. > 2 GiB) provided
sufficient disk space is available.

Resources from `src/main/resources/data` are also added to the archive, as the plugin
also adds the content of `target/classes/data`. This means this plugin can
be combined with plugins like [wagon-maven-plugin](http://www.mojohaus.org/wagon-maven-plugin/)
to archive external and generated resources.

### Goals

For project of type `data`, the goal `data:archive` is bound to the `compile` stage
(rather than `package`), meaning that the generated `data.zip`
is available for use by the `test` phase.

This goal can also be used manually from normal Java projects.
(**TODO**: Document such usage)


### Data archive structure

The archived resources will be added under
the target path
`data/${project.artifactId}/` within the ZIP file,
(e.g. `data/bigfile.csv` in the _example-data_ project
archived as `data/example-data/bigfile.csv`).

This means that that multiple data ZIP files can be on the classpath
concurrently as long as their `artifactId`s are unique, which is
best practice for Maven artifacts.

Note that files from `src/main/resources/data` and friends are also added to this
target path instead of at the root.

The only files outside this data folder are the [Research Object Bundle](https://w3id.org/bundle)
metadata, which include the files `mimetype` and `.ro/manifest.json`.



### Configuration

This example shows the default configuration for the plugin. To customize,
add a overriding `<configuration>` to your data project's `<plugin>` section with
the properties you need to change.

```xml
<configuration>
  <dataArchive>${project.build.directory}/${project.build.finalName}.data.zip"</dataArchive>
  <dataDirectory>data</dataDirectory>
  <targetPath>data/${project.artifactId}</targetPath>
</configuration>
```

- `<dataArchive>` is the artifact filename that will be generated, e.g.
  `target/example-data-0.0.3-SNAPSHOT.data.zip`
- `<dataDirectory>` is the data directory to add, by default `data/`
- `<targetPath>` is the folder path within the ZIP archive where data
   is stored, e.g. `data/example-data/`. Use `/` to add to the root.


## Plugin development

To run the [integration tests](src/it), run as

    mvn clean install -Pintegration

The generated test projects will be under `target/it`.
