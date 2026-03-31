import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileGrabber {
    JFileChooser fileChooser = new JFileChooser();
    File selectedFile;

    private TreeMap<String, Integer> keyWords = new TreeMap<>();
    private TreeSet<String> stopWords = new TreeSet<>();

    //FIXME: have this eventually return a list or maybe even return an object
    public void GetFileFromUser(){
        boolean done = false;

        do {

            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);//set the current directory for the file chooser

            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){//open the chooser
                try {
                    selectedFile = fileChooser.getSelectedFile();
                    done = true;
                }catch (Exception e){
                    e.printStackTrace();
                    done = true;
                }
            }
            if(!done){
                System.out.println("No file chosen! Please choose a file before moving forward.");
            }
        }while(!done);
    }

    public TreeSet ReadFileAsStopWords(){
        try {
            Path file = selectedFile.toPath();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.toLowerCase();
                stopWords.add(line);
            }
            scanner.close();
        } catch (IOException e) {e.printStackTrace();}

        for(String word : stopWords){
            System.out.println(word);//just for debugging
        }

        return stopWords;
    }

    public TreeMap ReadFileKeyWords(TreeSet stopWordSet){
        keyWords.clear();
        try{
            Path file = selectedFile.toPath();

            InputStream in = null;
            BufferedReader reader = null;

            try {//attempt to set up the buffered reader and input
                in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                reader =
                        new BufferedReader(new InputStreamReader(in));
            } catch (Exception e) {
                System.out.println(e);
            }

            int line = 0;
            String rec;

            if(reader != null){
                while (reader.ready()) {
                    rec = reader.readLine();
                    rec = rec.toLowerCase();
                    String[] words = rec.split("\\W+");//split the string into words

                    for(int i = 0; i < words.length; i++){

                        words[i] = words[i].toLowerCase();

                        if(!stopWordSet.contains(words[i]) &&
                                !words[i].isBlank() &&
                                words[i].length() > 1 &&
                                words[i].matches("[a-zA-Z]+")
                        ){//make sure the word isn't blank, isn't a stop word, isnt a single letter, and isnt a number
                            int count = keyWords.getOrDefault(words[i], 0);
                            keyWords.put(words[i], count + 1);
                        }
                    }

                    line++;
                }
                reader.close();
                System.out.println("File read");

            }
        } catch (IOException e) {e.printStackTrace();}
        return keyWords;
    }

    public File GetFile(){
        return selectedFile;
    }

    public String GetFilePath(){
        return selectedFile.getAbsolutePath();
    }
}
