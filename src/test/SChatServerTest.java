package test;

import server.SChatServer;

/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 11/2/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SChatServerTest {
    public static void main(String[] args){
        try{
            new SChatServer(Integer.parseInt(args[0]));
        }catch(Exception e){
            System.err.println("usage: java SChatServerTest <port>");
        }
    }
}
