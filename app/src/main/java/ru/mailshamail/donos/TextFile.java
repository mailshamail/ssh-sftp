package ru.mailshamail.donos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextFile {

    File file;
    FileWriter fw;

    public TextFile(File file)
    {
        this.file = file;

        try {
            fw = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String createText(String text)
    {
        try {
            fw.write(text);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String ConverToString(ArrayList<String> text)
    {
        StringBuilder builder = new StringBuilder();
        for(String s : text) {
            builder.append(s + "\n");
        }


        return builder.toString();

    }

}
