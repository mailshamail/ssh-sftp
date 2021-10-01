package ru.mailshamail.donos;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CheckBox ver, putin, pidr, zaebal;
    Button button;
    EditText fioText, adresText ,oplata;
    String fio, adres;
    ArrayList<CheckBox> checkArray;
    Thread thread;
    TextFile textFile;
    File file;

    int money;

    boolean connect = false;
    boolean potok = false;

    boolean bver = false;
    boolean bPutin = false;
    boolean bPidr = false;
    boolean bZaebal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ver = (CheckBox) findViewById(R.id.checkBox);
        putin = (CheckBox) findViewById(R.id.checkBox2);
        pidr = (CheckBox) findViewById(R.id.checkBox3);
        zaebal = (CheckBox) findViewById(R.id.checkBox4);
        button = (Button)findViewById(R.id.s);
        fioText = (EditText)findViewById(R.id.FIO);
        adresText = (EditText)findViewById(R.id.Adres);
        oplata = (EditText)findViewById(R.id.money);


        checkArray = new ArrayList<>();
        checkArray.add(ver);
        checkArray.add(putin);
        checkArray.add(pidr);
        checkArray.add(zaebal);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int count = 0;
                ArrayList<String> s = new ArrayList<>();

                for(CheckBox check : checkArray)
                {
                    if(check.isChecked())
                    {
                        s.add(check.getText().toString());
                        count++;
                    }
                }
                System.out.println(count);


                fio = fioText.getText().toString();
                adres = adresText.getText().toString();

                if(!oplata.getText().toString().equals(""))
                {
                    money = Integer.parseInt(oplata.getText().toString());
                }else {
                    Toast.makeText(MainActivity.this, "Введите оплату", Toast.LENGTH_SHORT).show();
                }


                connectServer();

                if(!connect) {
                    Toast.makeText(MainActivity.this, "Connected to server", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error connected to server", Toast.LENGTH_SHORT).show();
                }

                createfile();

                textFile = new TextFile(getFile());

                textFile.createText("ФИО преступника: " + fio + "\n" + "\n" + "Адрес: " + adres + " \n" + "\n" +
                        "Преступления (" + count + ")" + ":" + "\n" + textFile.ConverToString(s) + "\n" + "Оплата заказа: " + money);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(thread != null) {
            if (thread.isAlive()) {
                thread.stop();
                potok = false;
                connect = false;
                System.out.println("thread stop");
            }
        }
    }

    private void connectServer()
    {
        if(!potok) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Server server = new Server();
                    potok = true;

                    try {
                        if (!connect) {
                            server.Connect();
                            connect = true;

                            server.Createfile(getFile().getAbsolutePath(), "/home/mailshamail/donos");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    public void createfile() {
        //int s=1;

        if (fio != null) {

            File path = new File(Environment.getExternalStorageDirectory() + "/donos");

            if (!path.exists()) {
                path.mkdirs();
                System.out.println("Создание папки...");
            }

            file = new File(path, "/" + fio + ".txt");

            try {
                System.out.println("FILE: " + file.getAbsolutePath() + "  : " + file.createNewFile());
                Toast.makeText(MainActivity.this, "file: " + file.getName() + " created", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                Toast.makeText(MainActivity.this, "error: " + file.getName() + " created", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public File getFile(){
        return file;
    }
}
