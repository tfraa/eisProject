package it.unipd.dei.eis.fra;

import org.junit.Ignore;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test della classe usata per l'analisi degli articoli salvati
 * @see Analyze
 */
public class AnalyzeTest {

    /**
     * Controllo comportamento in caso non sia presente la Stoplist.txt
     * @see Analyze
     */
    @Ignore
    @Test
    public void testNoStoplist(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Analyze a = new Analyze();
        a.extractTerms("ArticoliGuardian");
        String capturedOutput = outputStream.toString();
        assertTrue(capturedOutput.contains("Errore nel caricamento della stoplist."));
    }

    /**
     * Test del comportamento in caso di richiesta di analisi degli articoli senza aver prima scaricato nulla
     * @see Analyze
     */
    @Test
    public void testNoFolder(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Analyze a = new Analyze();
        a.extractTerms("Articoli");
        String capturedOutput = outputStream.toString();
        assertTrue(capturedOutput.contains("La cartella potrebbe non ancora essere stata creata."));
    }

    /**
     * Test del corretto uso della stoplist e del corretto calcolo delle parole più frequenti
     * @see Analyze
     */
    @Test
    public void testArticle() throws IOException {
        String folderName = "ArticoliTest";
        List<String> corpoArticoliTest = new ArrayList<>();
        corpoArticoliTest.add("This is a test. A test of an article. Awesome.");
        corpoArticoliTest.add("This is test number.");
        DownloadGuardian d = new DownloadGuardian();
        d.storeArticles(corpoArticoliTest, folderName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Analyze a = new Analyze();
        a.extractTerms(folderName);
        String capturedOutput = outputStream.toString();
        assertEquals("Le parole più frequenti sono:\r\ntest - 2\r\nawesome - 1\r\nnumber - 1\r\narticle - 1\r\n", capturedOutput);
    }
}
