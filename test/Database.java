import com.collaboration.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


class Database {


    @Test
    void create() throws IOException {
        //ServerSocket server=new ServerSocket(13345);
        //Socket client = new Socket("localhost", 13345);
        //MonoThreadClientHandler test= new MonoThreadClientHandler(server.accept());
        MonoThreadClientHandler.createProject("");


    }



}
