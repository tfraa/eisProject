package it.unipd.dei.eis.fra;
import java.util.Scanner;

public class App{
    public static void main(String[] args) {
        do {
            // Selezione dell'azione da eseguire
            System.out.println("Seleziona una delle opzioni e digita la tua scelta: \n Solo download (DL) \n Solo estrazione termini (EL) \n Download ed estrazione termini (DLEL) \n Fine Programma (Q) \n");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("Q")){ System.exit(0); }
            while(!(input.equalsIgnoreCase("DL") || input.equalsIgnoreCase("EL") || input.equalsIgnoreCase("DLEL"))){
                System.out.println("Azione non valida, digitare nuovamente la propria scelta: ");
                input = scanner.nextLine();
            }
            String action = input;

            // Selezione della sorgente
            System.out.println("Seleziona la sorgente su cui eseguire l'operazione: \n Guardian (G) \n New York Times (NYT)");
            scanner = new Scanner(System.in);
            input = scanner.nextLine();
            while(!(input.equalsIgnoreCase("G") || input.equalsIgnoreCase("NYT"))){
                System.out.println("Sorgente non valida, digitare nuovamente la propria scelta: ");
                input = scanner.nextLine();
            }
            String source = input;

            if(action.equalsIgnoreCase("DL")){
                if(source.equalsIgnoreCase("G")){
                    System.out.println("Scrivi la tua query di ricerca, oppure usa quella di default (\" Nuclear Power \") digitando D");
                    scanner = new Scanner(System.in);
                    input = scanner.nextLine();
                    if(input.equalsIgnoreCase("D")){
                        DownloadGuardian d = new DownloadGuardian();
                        d.downloadAndStore();
                    }else{
                        DownloadGuardian d = new DownloadGuardian(input);
                        d.downloadAndStore();
                    }
                }else{
                    DownloadNYT d = new DownloadNYT();
                    d.downloadAndStore();
                }
            }else if(action.equalsIgnoreCase("EL")){
                if(source.equalsIgnoreCase("G")){
                    // Analyse a = new Analyse();
                    // a.path (?)
                    Analyze a = new Analyze();
                    a.extractTerms("ArticoliGuardian");
                }else{
                    Analyze a = new Analyze();
                    a.extractTerms("ArticoliNYT");
                }
            }else if(action.equalsIgnoreCase("DLEL")){
                if(source.equalsIgnoreCase("G")){
                    System.out.println("Scrivi la tua query di ricerca, oppure usa quella di default (\" Nuclear Power \") digitando D");
                    scanner = new Scanner(System.in);
                    input = scanner.nextLine();
                    if(input.equalsIgnoreCase("D")){
                        DownloadGuardian d = new DownloadGuardian();
                        d.downloadAndStore();
                    }else{
                        DownloadGuardian d = new DownloadGuardian(input);
                        d.downloadAndStore();
                    }
                    Analyze a = new Analyze();
                    a.extractTerms("ArticoliGuardian");
                }else{
                    DownloadNYT d = new DownloadNYT();
                    d.downloadAndStore();
                    Analyze a = new Analyze();
                    a.extractTerms("ArticoliNYT");
                }
            }
            System.out.println("Cos'altro si vuole fare?"+"\n");
        } while(true);
    }
}