import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class TagExtractorFrame extends JFrame{
    JPanel mainPnl;
    JPanel topPnl;
    JPanel midPnl;
    JPanel botPnl;

    JButton selectFileBtn;
    JButton selectStop;
    JTextArea fileNameTxt;

    JTextArea tagsTxt;
    JScrollPane tagsScrollTxt;

    JButton saveTagsBtn;

    private static ArrayList<String> stopWords = new ArrayList<>();
    private static ArrayList<String> wordsList = new ArrayList<>();
    private static Map<String, Integer> wordsMap = new HashMap<>();

    public TagExtractorFrame(){
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createTopPnl();
        mainPnl.add(topPnl, BorderLayout.NORTH);

        createMidPnl();
        mainPnl.add(midPnl, BorderLayout.CENTER);

        createBotPnl();
        mainPnl.add(botPnl, BorderLayout.SOUTH);

        add(mainPnl);
        setSize(500,600);
        setVisible(true);
    }

    public void createTopPnl() {
        topPnl = new JPanel();
        topPnl.setLayout(new GridLayout(3, 1));

        selectStop = new JButton("Select Stop Words");
        selectStop.addActionListener((ActionEvent e) ->
                {
                    getStopWords();
                }
        );

        selectFileBtn = new JButton("Select File");
        selectFileBtn.addActionListener((ActionEvent e) ->
            {
                getWordList();
                sortWordsList();
                printWords();
            }
        );
        fileNameTxt = new JTextArea("", 1, 100);
        fileNameTxt.setEditable(false);

        topPnl.add(selectStop);
        topPnl.add(selectFileBtn);
        topPnl.add(fileNameTxt);
    }

    public void createMidPnl(){
        midPnl = new JPanel();
        tagsTxt = new JTextArea("", 21, 42);
        tagsScrollTxt = new JScrollPane(tagsTxt);

        midPnl.add(tagsScrollTxt);
    }

    public void createBotPnl() {
        botPnl = new JPanel();
        saveTagsBtn = new JButton("Save Data");

        botPnl.add(saveTagsBtn);
    }

    public void getWordList() {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";

        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();
                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));
                fileNameTxt.append(chooser.getSelectedFile().getName());

                // Finally we can read the file LOL!
                int line = 0;
                while(reader.ready())
                {
                    rec = reader.readLine();
                    line++;
                    String result = rec.replaceAll("[^\\p{L}\\p{Z}]","");
                    result = result.toLowerCase();
                    String[] Split = result.split("\\s+");

                    for (int x = 0; x <= Split.length-1; x++){
                        wordsList.add(Split[x]);
                    }
                    // echo to screen
//                    System.out.printf("\nLine %4d %-60s ", line, rec);
//                    tagsTxt.append(line + " " + rec + "\n");
//                    tagsTxt.append(wordsList.get(0));
                }
                reader.close(); // must close the file to seal it and flush buffer
                System.out.println("\n\nData file read!");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!!!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void getStopWords(){
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";

        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();
                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));

                // Finally we can read the file LOL!
                int line = 0;
                while(reader.ready())
                {
                    rec = reader.readLine();
                    line++;

                    stopWords.add(rec);
                    // echo to screen
//                    System.out.printf("\nLine %4d %-60s ", line, rec);
//                    tagsTxt.append(line + " " + rec + "\n");
                }
                reader.close(); // must close the file to seal it and flush buffer
                System.out.println("\n\nData file read!");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!!!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void sortWordsList(){
        for (int k = 0; k <= wordsList.size()-1; k++){
            if(wordsMap.containsKey(wordsList.get(k)))
                wordsMap.replace(wordsList.get(k), wordsMap.get(wordsList.get(k)), (wordsMap.get(wordsList.get(k)))+1);
            else
                wordsMap.put(wordsList.get(k),1);
        }

        for(int i = 0; i <= stopWords.size()-1; i++){
            if (wordsMap.containsKey(stopWords.get(i)))
                wordsMap.remove(stopWords.get(i));
        }
    }

    public void printWords() {
        for(String key: wordsMap.keySet()){
            tagsTxt.append(key + "\t" + "\t"+ "\t" + wordsMap.get(key) + "\n");
        }
    }
}

