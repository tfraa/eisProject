package it.unipd.dei.eis.fra;

import org.junit.Ignore;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Test specifici di DownloadNYT, mirati a controllare il corretto caricamento del file e il corretto parsing del file csv
 */
public class DownloadNYTTest {

    /**
     * Test che controlla che venga lanciata l'eccezione FileNotFoundException se il programma non riesce a caricare il file corrispondente
      */
    @Ignore
    @Test
    public void sourceNotFound(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        DownloadNYT d = new DownloadNYT();
        List<String> testList = d.downloadArticles();
        String capturedOutput = outputStream.toString();
        assertEquals("Errore. Non è stato possibile trovare o aprire il file della sorgente.\r\n", capturedOutput);
    }

    /**
     *  Test per vedere se il corpo dell'articolo viene salvato correttamente
     */
    @Test
    public void correctDownloadTest(){
        DownloadNYT d = new DownloadNYT();
        List<String> testLista = d.downloadArticles();
        assertEquals("To the Editor: The New York State Public Service Commission, the agency " +
                "charged with overseeing the restructuring of New York's electric industry, should not " +
                "rely on the Nuclear Regulatory Commission to monitor nuclear safety since that " +
                "commission was unable to ensure the safe operation of the Indian Point 2 nuclear " +
                "plant (news article, Sept. 1). Gov. George E. Pataki should require all relevant " +
                "state agencies to complete a full public review of nuclear safety before exposing " +
                "nuclear power to market competition or considering the sale of any of New York's six " +
                "commercial nuclear power plants. Just as we regret the hasty construction of nuclear " +
                "power plants, we will regret hasty electric utility deregulation. New York needs to " +
                "scrutinize what role, if any, nuclear power should have in the new competitive energy " +
                "marketplace. KYLE RABIN Albany, Sept. 1, 2000 The writer is nuclear energy project " +
                "director, Environmental Advocates.", testLista.get(7));
    }
}
