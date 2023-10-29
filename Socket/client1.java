import java.net.*;
import java.util.*;
import java.io.*;

public class client1 {
    public static void main(String[] args) throws Exception{
        String recieve;
        String send;

        Scanner sc = new Scanner(System.in);
        Socket s = new Socket("localhost", 2225);
        System.out.println("Request sent");

        while(true){
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            recieve = br.readLine();
            System.out.println(recieve);

            PrintStream pr = new PrintStream(s.getOutputStream());
            System.out.println("Client : ");

            send = sc.nextLine();
            pr.println("Client " + send);

            if(send == "exit"){
                s.close();
            }
        }
    }
}
