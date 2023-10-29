import java.io.*;
import java.net.*;
import java.util.*;

public class server1 {
    public static void main(String[] args) throws Exception{
        String receive;
        String send;

        System.out.println("Waiting for client machine");
        ServerSocket ss = new ServerSocket(2224);
        Socket s = ss.accept();
        
        System.out.println("New client is pop up");
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("Server : ");
            send = sc.nextLine();

            PrintStream pr = new PrintStream(s.getOutputStream());
            pr.println("Server : " +send);

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            receive = br.readLine();
            System.out.println(receive);
        }
    }
}
