package it.unipd.dei.eis.fra;

import org.junit.Ignore;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test specifici di DownloadGuardian mirati a testare connessione, corretto parsing del JSON e costruzione degli articoli
 * @see DownloadGuardian
 */
public class DownloadGuardianTest {

    /**
     *  Test del comportamento di DownloadGuardian quando non c'è una connessione a internet attiva
     * @see DownloadGuardian
     */
    @Ignore
    @Test
    public void testNoConnection(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        DownloadGuardian d = new DownloadGuardian();
        List<String> testList = d.downloadArticles();
        String capturedOutput = outputStream.toString();
        assertEquals("Errore nell'apertura o lettura dall'InputStream della connessione. Controlla la tua connessione ad internet e di aver inserito la tua Guardian API Key.\r\n", capturedOutput);
    }

    /**
     *  Testa se il contenuto di un articolo è salvato correttamente
     * @see DownloadGuardian
     */
    @Test
    public void testCorrectArticle(){
        DownloadGuardian d = new DownloadGuardian();
        List<String> testList = d.downloadArticles(); // 561
        d.downloadAndStore();
        assertEquals("Government spending is always contentious, with every choice made " +
                "being highly political. For example, Labor has said it will not scrap " +
                "the controversial stage-three tax cuts. But what if they were scrapped? " +
                "What if there was an extra $243bn in the budget over the next 10 years? " +
                "What could be done? And similarly, what if we didn’t spend up to $58bn on" +
                " a nuclear submarine program over the next ten years, what else could we" +
                " spend it on? This interactive allows you to play treasurer by adding in" +
                " spendings and savings measures. If you would like to use your savings to" +
                " “pay down the debt” then simply save more than you spend.", testList.get(560));
    }

    /**
     *  Testa se il corretto numero di file vengono salvati quando il numero di articoli è maggiore di 1000
     * @see DownloadGuardian
     */
    @Test
    public void testCorrectNumberDownloaded1(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        // Uso Nuclear Power che so avere > 1000 articoli
        DownloadGuardian d = new DownloadGuardian();
        d.downloadAndStore();
        String capturedOutput = outputStream.toString();
        assertEquals("1000 articoli salvati correttamente.\r\n", capturedOutput);
    }

    /**
     *  Test se il corretto numero di file vengono salvati quando il numero di articoli è minore di 1000
     * @see DownloadGuardian
     */
    @Test
    public void testCorrectNumberDownloaded2(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        // Uso "supercali" che so avere 7 articoli
        DownloadGuardian d = new DownloadGuardian("supercali");
        d.downloadAndStore();
        String capturedOutput = outputStream.toString();
        assertEquals("7 articoli salvati correttamente.\r\n", capturedOutput);
    }

    /**
     *  Test del comportamento in una situazione in cui 0 articoli sono trovati, con una query nota per lo scopo
     * @see DownloadGuardian
     */
    @Test
    public void testNoArticles(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        // Uso "supercalifragilistichespiralidoso" che so avere 0 articoli
        DownloadGuardian d = new DownloadGuardian("Supercalifragilistichespiralidoso");
        d.downloadAndStore();
        String capturedOutput = outputStream.toString();
        assertEquals("Non è stato trovato nessun articolo del Guardian con questa query di ricerca\r\n0 articoli salvati.\r\n", capturedOutput);
    }

    /**
     *  Test del comportamento in caso di mancata aggiunta della API Guardian personale
     * @see DownloadGuardian
     */
    @Ignore
    @Test
    public void testNoAPI(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        DownloadGuardian d = new DownloadGuardian();
        List<String> testList = d.downloadArticles();
        String capturedOutput = outputStream.toString();
        assertEquals("Errore nell'apertura o lettura dall'InputStream della connessione. Controlla la tua connessione ad internet e di aver inserito la tua Guardian API Key.\r\n", capturedOutput);
    }
}
