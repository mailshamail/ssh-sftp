package ru.mailshamail.donos.lib;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class SFTP
{

    private Session session;
    //private Channel channel;
    //private ChannelSftp sftp = null;

    public SFTP(Session session)
    {
        this.session = session;
    }

    public void downloadFile(String file, String save) throws JSchException {

        if (session != null)
        {
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            try {

                sftpChannel.get(file, save);

                System.out.println("File download: " + sftpChannel.realpath(file));
                sftpChannel.disconnect();

            } catch (SftpException e) {
                e.printStackTrace();
            }

        }else{System.err.println("Session is closed");}
    }

    @SuppressWarnings("unchecked")
    public void DowndloadAllFiles(String localcDir, String ServerDir) throws JSchException, SftpException {
        if (session != null)
        {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            File files;

            Vector<ChannelSftp.LsEntry> allFile = sftp.ls(localcDir);

            int count = 0;

            for (ChannelSftp.LsEntry item : allFile)
            {
                if (!item.getAttrs().isDir())
                {
                    if (!(files = new File(localcDir + "/" + item.getFilename())).exists())
                    {
                        if(!files.getName().startsWith("."))
                        {
                            System.out.println("Download: " + item.getFilename());

                            sftp.get(localcDir + "/" + item.getFilename(), ServerDir);

                           // System.out.println("Download " + item.getFilename() + " COMPLETE");

                            count++;
                        }
                    }
                }
            }
            System.out.println(count);
            channel.disconnect();



        }
        else
            {System.err.println("Session is closed");}
    }

    public void UploadFile(String localFile, String serverDir) throws JSchException {

        if (session != null)
        {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();

            try {

                sftp.put(localFile, serverDir);
                System.out.println("File upload" );

                sftp.disconnect();

            } catch (SftpException e) {
                e.printStackTrace();
            }

        }
        else{System.err.println("Session is closed");}
    }

    public void UploadsAllFiles(String localDir, String serverDir) throws JSchException, SftpException {
        if (session != null)
        {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;


            File localFile = new File(localDir);
            File[] allFiles = localFile.listFiles();

            int count = 0;

            if (allFiles != null)
            {
                for(File f : allFiles)
                {
                    if(f.isFile())
                    {
                        if (!f.getName().startsWith("."))
                        {
                            System.out.println("Upload file: " + f.getName());

                            sftp.put(localDir + "/" + f.getName(), serverDir);
                            count++;
                        }
                    }
                }
            }
            System.out.println(count);
            channel.disconnect();
        }
        else{System.err.println("Session is closed");}
    }
}