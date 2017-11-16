package SPDownloader;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static SPTools.Extractor.getFile;
import static SPTools.Extractor.getJarURI;

class Downloader {

    static boolean isDownloading = false;


    static void downloader(CheckBox languageGerman, CheckBox languageEnglish, ArrayList<String> selectedEpisodes, TextArea console, SplitPane upperSplit) {
        int threadsInt = 1;

        //threadsInt = (int) threads.getSelectionModel().getSelectedItem();
        isDownloading = true;
        for (String selectedEpisode : selectedEpisodes) {
            new Thread(new DownloadRunnable(languageGerman, languageEnglish, selectedEpisode, console, upperSplit)).start();
        }
    }
}
