package ru.mailshamail.donos.lib;

import android.support.annotation.NonNull;

import com.jcraft.jsch.*;

import java.io.*;


public class SSH {
    private String HOST;
    private String PASSWORD;
    private String USER;
    private int PORT;
    private String KnownHostsFileName;
    private int ConnectedTime = 600;

    private JSch ssh;
    private Session session;
    private Channel channel;

    private void info(@NonNull String host, String user, String password, int port, String knownHostsFileName) {
        HOST = host;
        USER = user;
        PASSWORD = password;
        PORT = port;
        KnownHostsFileName = knownHostsFileName;

        ssh = new JSch();
    }

    public SSH(){}

    public SSH(@NonNull String host, String user, int port) {
        info(host, user, getPassword(), port, "no");
        System.err.println("HOST FILE NAME: no");
        ConnectedTime = 600;
    }

    public SSH(@NonNull String host, String user, String password, int port, String knownHostsFileName) {
        info(host, user, password, port, knownHostsFileName);
        ConnectedTime = 600;
    }

    public SSH(@NonNull String host, String user, String password, int port) {
        info(host, user, password, port, "no");
        System.err.println("HOST FILE NAME: no");
        ConnectedTime = 600;
    }

    public SSH(@NonNull String host, String user, String password, int port, int connectedTime) {
        info(host, user, password, port, "no");
        System.err.println("HOST FILE NAME: no");
        ConnectedTime = connectedTime;
    }

    public Session OpenSession() throws JSchException {
        Session session = ssh.getSession(USER, HOST, PORT);

        session.setPassword(PASSWORD);
        session.setTimeout(ConnectedTime);
        session.setConfig("StrictHostKeyChecking", KnownHostsFileName);
        session.connect();
        System.out.println("Session: " + "host: " + session.getHost() + " |connected: " + session.isConnected());

        this.session = session;

        return session;
    }

    public Channel OpenChannel(Session session, String type) throws JSchException {

        Channel channel = session.openChannel(type);
        channel.connect();
        System.out.println("Channel: " + "id: " + channel.getId() + " |host: " + session.getHost() + " |connected: " + channel.isConnected());


        this.channel = channel;

        return channel;
    }

    public String sendCommand(String command) {

        String resultCommand = "";
        try {
            ChannelExec channel = (ChannelExec) getSession().openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            int d = 0;

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    d++;
                    if (i < 0) break;
                    resultCommand = (d + ":" + new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    break;
                }
            }
            channel.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultCommand;
    }

    public void DisconnectSession(Session session) {
        if (session != null) {
            if (channel != null)
                getChannel().disconnect();
            session.disconnect();
        }
    }

    public void DisconnectChannel(Channel channel) {
        if (channel != null)
            session.disconnect();
    }




    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public Session getSession() {
        return session;
    }

    public Channel getChannel() {
        return channel;
    }



}




