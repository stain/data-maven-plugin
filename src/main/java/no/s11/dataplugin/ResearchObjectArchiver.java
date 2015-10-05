package no.s11.dataplugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.apache.taverna.robundle.Bundle;
import org.apache.taverna.robundle.Bundles;
import org.codehaus.plexus.archiver.AbstractArchiver;
import org.codehaus.plexus.archiver.ArchiveEntry;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.ResourceIterator;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

public class ResearchObjectArchiver extends AbstractArchiver {

	private Bundle bundle;
	
	@Override
	protected String getArchiveType() {
		return "bundle.zip";
	}

	@Override
	protected void close() throws IOException {
		if (bundle != null) {
			bundle.close();
		}
		
	}

	@Override
	protected void execute() throws ArchiverException, IOException {
		bundle = Bundles.createBundle(getDestFile().toPath());
		
		ResourceIterator resources = getResources();
		while (resources.hasNext()) {
			ArchiveEntry resource = resources.next();
			Path bundlePath = bundle.getPath(resource.getName());
			copy(resource.getResource(), bundlePath);
			
		}
		
	}

	private void copy(PlexusIoResource fromResource, Path toPath) throws IOException {
		if (fromResource.isDirectory()) {
			throw new ArchiverException("Unhandled: archiving of directory " + fromResource);
		}
		
		// Copy to bundle
		try (InputStream fromInputStream = fromResource.getContents()) {
			Files.copy(fromInputStream, toPath);
		}

		// Clone metadata
		long lastModified = fromResource.getLastModified();
		if (lastModified != PlexusIoResource.UNKNOWN_MODIFICATION_DATE) {
			FileTime fileTime = FileTime.fromMillis(lastModified);
			Files.setLastModifiedTime(toPath, fileTime);
			bundle.getManifest().getAggregation(toPath).setCreatedOn(fileTime);
		}
		
		recordProvenance(fromResource, toPath);
		
	}

	private void recordProvenance(PlexusIoResource fromResource, Path toPath) throws IOException {
		URL url = fromResource.getURL();
		if (url != null) {
			try {
				URI retrievedFrom = url.toURI();
				// TODO: Record as pav:retrievedFrom
			} catch (URISyntaxException e) {
			}
		}
		
		
	}

}
