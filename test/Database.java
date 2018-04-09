import com.collaboration.*;
import com.collaboration.Trash.Client;
import com.collaboration.Trash.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


class Database {


    @Test
    void create() throws IOException {
        //ServerSocket Server=new ServerSocket(13345);
        //Socket client = new Socket("localhost", 13345);
        //MonoThreadClientHandler test= new MonoThreadClientHandler(Server.accept());
        MonoThreadClientHandler.createProject("");


    }

    @Test
    void test() throws IOException, InterruptedException {
//        new MultiThreadServer();
        ClientForAndroid test=new ClientForAndroid(new Socket("localhost", 15233));
        System.out.println();
//        test.run();
//        test.createProj();
      //  test.close();
    }

}
