import com.collaboration.*;
import com.collaboration.Trash.Client;
import com.collaboration.Trash.Server;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import org.junit.jupiter.api.Test;
import sun.security.pkcs11.wrapper.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


class Database {


    @Test
    void create() throws IOException, SQLException {
        //ServerSocket Server=new ServerSocket(13345);
        //Socket client = new Socket("localhost", 13345);
        //MonoThreadClientHandler test= new MonoThreadClientHandler(Server.accept());
        MonoThreadClientHandler.createProject("");


    }




    @Test
    void test() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 15233);
        ClientForAndroid client= new ClientForAndroid(socket);
        Thread.sleep(20);
        System.out.println(client.isConnected());
        client.sendMessage("test");
        client.sendMessage("Client test");
        System.out.println(client.getmServerMessage());

    }

}
