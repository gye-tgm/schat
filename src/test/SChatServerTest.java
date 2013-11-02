package test;

import server.SChatServer;

/**
 * SChat Server tester
 * @author Gary Ye
 */
public class SChatServerTest {
    public static void main(String[] args) {
        try {
            new SChatServer(Integer.parseInt(args[0]));
        } catch (Exception e) {
            System.err.println("usage: java SChatServerTest <port>");
        }
    }
}
