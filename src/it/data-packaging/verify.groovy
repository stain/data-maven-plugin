import org.apache.taverna.robundle.Bundle;
import org.apache.taverna.robundle.Bundles;
import java.nio.file.Files;
import java.nio.file.Path;


File zip = new File( basedir, "target/data-packaging-0.0.1-SNAPSHOT.data.zip" );
assert zip.isFile();


Bundle bundle = Bundles.openBundleReadOnly(zip.toPath());
assert Files.isRegularFile(bundle.getPath("data/data-packaging/test.txt"));
assert Files.isRegularFile(bundle.getPath("data/data-packaging/other.txt"));
assert !Files.exists(bundle.getPath("ignored.txt"));
assert !Files.exists(bundle.getPath("data/ignored.txt"));
assert !Files.exists(bundle.getPath("data/data-packaging/ignored.txt"));
bundle.close();
