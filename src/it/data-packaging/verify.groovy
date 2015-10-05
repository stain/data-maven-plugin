import org.apache.taverna.robundle.Bundle;
import org.apache.taverna.robundle.Bundles;
import java.nio.file.Files;
import java.nio.file.Path;


File zip = new File( basedir, "target/dataplugin-example-0.0.1-SNAPSHOT.bundle.zip" );
assert zip.isFile();


Bundle bundle = Bundles.openBundleReadOnly(zip.toPath());
assert Files.isRegularFile(bundle.getPath("data/dataplugin-example/test.txt"));
assert Files.isRegularFile(bundle.getPath("data/dataplugin-example/other.txt"));
bundle.close();
