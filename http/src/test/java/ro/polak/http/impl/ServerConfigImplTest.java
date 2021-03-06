package ro.polak.http.impl;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ro.polak.http.ServerConfig;
import ro.polak.http.utilities.IOUtilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class ServerConfigImplTest {

    private String tempDir = System.getProperty("java.io.tmpdir") + "/";
    private File configFile;
    private File mimeFile;

    private static final String DEFAULT_CONFIG_DATA = "server.port=8090\n" +
            "server.servlets.extension=dddd\n" +
            "server.static.path=wwwx\n" +
            "server.static.directoryIndex=index.php,index.html\n" +
            "server.mimeType.defaultMimeType=mime/text\n" +
            "server.mimeType.filePath=mime.mime\n" +
            "server.maxThreads=3\n" +
            "server.keepAlive.enabled=true\n" +
            "server.errorDocument.404=error404.html\n" +
            "server.errorDocument.403=error403.html\n";

    public void writeFile(String configData) throws IOException {
        configFile = new File(tempDir + "httpd.properties");
        mimeFile = new File(tempDir + "mime.mime");
        try {
            mimeFile.createNewFile();
        } catch (IOException e) {
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(configData);
        } finally {
            if (writer != null) {
                IOUtilities.closeSilently(writer);
            }
        }
    }

    public void removeFile() throws IOException {
        if (configFile != null && configFile.exists() && !configFile.delete()) {
            throw new IOException("Unable to delete " + configFile.getAbsolutePath());
        }

        if (mimeFile != null && mimeFile.exists() && !mimeFile.delete()) {
            throw new IOException("Unable to delete " + mimeFile.getAbsolutePath());
        }
    }

    @Test
    public void shouldCreateFromPath() throws IOException {
        writeFile(DEFAULT_CONFIG_DATA + "DefaultMimeType mime/text\n");
        try {
            ServerConfig serverConfig = ServerConfigImpl.createFromPath(tempDir, tempDir);
            assertThat(serverConfig.getBasePath(), is(tempDir));
            assertThat(serverConfig.getDocumentRootPath(), is(tempDir + "wwwx"));
            assertThat(serverConfig.getServletMappedExtension(), is("dddd"));
            assertThat(serverConfig.getDirectoryIndex(), hasItem("index.php"));
            assertThat(serverConfig.getDirectoryIndex(), hasItem("index.html"));
            assertThat(serverConfig.getErrorDocument403Path(), is(tempDir + "error403.html"));
            assertThat(serverConfig.getErrorDocument404Path(), is(tempDir + "error404.html"));
            assertThat(serverConfig.getListenPort(), is(8090));
            assertThat(serverConfig.getMaxServerThreads(), is(3));
            assertThat(serverConfig.isKeepAlive(), is(true));
            assertThat(serverConfig.getMimeTypeMapping().getMimeTypeByExtension("ANY"), is("mime/text"));
        } finally {
            removeFile();
        }
    }

    @Test
    public void shouldSetDefaultMimeType() throws IOException {
        writeFile(DEFAULT_CONFIG_DATA);
        try {
            ServerConfig serverConfig = ServerConfigImpl.createFromPath(tempDir, tempDir);
            assertThat(serverConfig.getMimeTypeMapping().getMimeTypeByExtension("ANY"), is("mime/text"));
        } finally {
            removeFile();
        }
    }
}