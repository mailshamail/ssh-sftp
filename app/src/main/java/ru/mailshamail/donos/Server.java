package ru.mailshamail.donos;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import ru.mailshamail.donos.lib.SFTP;
import ru.mailshamail.donos.lib.SSH;

public class Server {

    private SSH ssh;
    private SFTP sftp;
    private Session session;

    public Server()
    {
        ssh = new SSH("nixub.ddns.net", "mailshamail", "FA5af3d9aw3F3", 32168);
    }

    public void Connect() throws JSchException
    {
        System.out.println("Connect...");
        session = ssh.OpenSession();
    }

    public void Createfile(String file, String serverFile) throws JSchException {
        SFTP sftp = new SFTP(session);

        sftp.UploadFile(file, serverFile);
    }
}
