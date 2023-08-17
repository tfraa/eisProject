package it.unipd.dei.eis.fra;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test delle funzioni fornite dalla classe Download e poi ereditate e riutilizzate dalle varie sorgenti
 */
public class DownloadTest{

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     *  Test del salvataggio dell'articolo con il nome corretto, nel path corretto
     */
    @Test
    public void storeArticlesTest_correctSaveName() throws IOException{
        String folderName = "ArticoliTest";
        Path folderPath = temporaryFolder.newFolder(folderName).toPath();
        List<String> corpoArticoliTest = new ArrayList<>();
        corpoArticoliTest.add("Corpo articolo 1");

        Download download = new Download() {
            @Override
            protected List<String> downloadArticles() {
                return null;
            }
        };

        download.storeArticles(corpoArticoliTest, folderPath.toString());
        File folder = new File(folderPath.toString());
        File[] files = folder.listFiles();
        Path newSavedArticle = files[0].toPath();
        assertEquals(folderPath + "\\Articolo1.txt", newSavedArticle.toString());
        assertEquals(files.length, 1);
    }

    /**
     *  Test del salvataggio corretto del contenuto di un articolo tramite storeArticles
     * @throws IOException
     */
    @Test
    public void storeArticlesTest_correctSaveBody() throws IOException{
        String folderName = "ArticoliTest";
        Path folderPath = temporaryFolder.newFolder(folderName).toPath();
        List<String> corpoArticoliTest = new ArrayList<>();
        corpoArticoliTest.add("Corpo Articolo 1");

        Download download = new Download() {
            @Override
            protected List<String> downloadArticles() {
                return null;
            }
        };

        download.storeArticles(corpoArticoliTest, folderPath.toString());
        BufferedReader reader = new BufferedReader(new FileReader(folderPath + "/Articolo1.txt"));
        StringBuilder rdBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            rdBuilder.append(line);
        }
        reader.close();
        assertEquals(rdBuilder.toString(), "Corpo Articolo 1");
    }

    /**
     * Test che verifica che quando non è presente nessun articolo, non viene salvato nessun file
     *
     * @throws IOException
     */
    @Test
    public void storeArticlesTest_noArticles() throws IOException{
        String folderName = "ArticoliTest";
        Path folderPath = temporaryFolder.newFolder(folderName).toPath();
        List<String> corpoArticoliTest = new ArrayList<>();
        Download download = new Download() {
            @Override
            protected List<String> downloadArticles() {
                return null;
            }
        };
        download.storeArticles(corpoArticoliTest, folderPath.toString());
        File folder = new File(folderPath.toString());
        File[] files = folder.listFiles();
        assertEquals(files.length, 0);
        assertEquals(corpoArticoliTest.size(), 0);
    }

    /**
     * Test del funzionamento di getFolder se la cartella di destinazione non è ancora stata creata
     *
     * @throws IOException
     */
    @Test
    public void getFolderTest_newFolder() throws IOException{
        String folderName = "newFolder";
        Path folderPath = temporaryFolder.getRoot().toPath().resolve(folderName);
        assertTrue(Files.notExists(folderPath));
        Download download = new Download() {
            @Override
            protected List<String> downloadArticles() {
                return null;
            }
        };

        Path returnedFolderPath = download.getFolder(folderPath.toString());
        assertTrue(Files.exists(returnedFolderPath));
        assertTrue(returnedFolderPath.toFile().isDirectory());
        assertEquals(folderPath, returnedFolderPath);
    }

    /**
     * Test del funzionamento di getFolder se la cartella di destinazione esiste già
     *
     * @throws IOException
     */
    @Test
    public void getFolderTest_folderExists() throws IOException {
        String folderName = "existingFolder";
        Path existingFolderPath = temporaryFolder.newFolder(folderName).toPath();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Download download = new Download() {
            @Override
            protected List<String> downloadArticles() {
                return null;
            }
        };
        Path folderPath = download.getFolder(existingFolderPath.toString());
        assertNotNull(folderPath);
        assertEquals(existingFolderPath, folderPath);
        String capturedOutput = outputStream.toString();
        assertTrue(capturedOutput.isEmpty());
    }
}
