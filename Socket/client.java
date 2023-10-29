import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {

	public static void main(String args[]) {
		String recmsg;
		String sendmsg;

		Scanner input = new Scanner(System.in);
		try {

			// Making conenction with server
			Socket client = new Socket("localhost", 2225);
			System.out.println("Request sent successfully");
			// boolean b = true;

			while (true) {
				// server msg receive!
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				recmsg = br.readLine();
				System.out.println(recmsg);

				// Client msg sending to server!
				PrintStream ps = new PrintStream(client.getOutputStream());
				// pw.println(args[0]);
				System.out.print("Client: ");
				sendmsg = input.nextLine();
				ps.println("Client: " + sendmsg);

				if (sendmsg.equals("exit")) {
					// b = false;
					client.close();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}