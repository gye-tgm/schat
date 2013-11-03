package test;

import com.data.ChatMessage;
import com.data.User;
import com.networking.SChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Tests the SChatClient implementation.
 *
 * @author Gary Ye
 */
public class SChatClientTest {
    public static void main(String[] args) {
        User me = null;
        SChatClient client = null;
        try {
            me = new User(Integer.parseInt(args[0]), args[1]);
        } catch (Exception e) {
            System.err.println("usage: java SChatClientTest <id> <user name> (<host name> <port>)");
            System.exit(1);
        }

        String hostName = SChatClient.SERVER_NAME;
        int portNumber = SChatClient.PORT_ADDRESS;

        if(args.length == 4){
            hostName = args[2];
            portNumber = Integer.parseInt(args[3]);
        }

        try {
            client = new SChatClient(hostName, portNumber, me);
        } catch (IOException e) {
            System.err.println("Could not connect to the server successfully.");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                String[] messageSplit = line.split(" ");
                try {
                    int id = Integer.parseInt(messageSplit[0]);
                    StringBuilder message = new StringBuilder("");
                    for (int i = 1; i < messageSplit.length; i++) {
                        message.append(messageSplit[i]);
                        message.append(" \n".charAt(i + 1 == messageSplit.length ? 1 : 0));
                    }
                    ChatMessage chatMessage = new ChatMessage(me, new User(id), message.toString(), Calendar.getInstance());
                    client.sendMessage(chatMessage);
                } catch (Exception e) {
                    System.err.println("usage: <id> <message>");
                }
            }
            in.close();
        } catch (IOException e) {
            System.err.println("STDIN I/O Exception");
            System.exit(1);
        }
    }
}
