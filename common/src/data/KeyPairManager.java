package data;

import crypto.Cryptography;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Gary Ye
 * @version 12/1/13
 */
public class KeyPairManager {
    public static void outputBytesToFile(byte[] bytes, String filename){
        try(FileOutputStream fos = new FileOutputStream(filename)){
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static byte[] readBytesFromFile(String filename){
        Path path = Paths.get(filename);
        byte[] data = null;
        try {
            if(Files.exists(path))
                data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static PublicKey readPublicKey(String publicKeyFile){
        return Cryptography.getPublicKeyFromBytes(readBytesFromFile(publicKeyFile));
    }
    public static PrivateKey readPrivateKey(String privateKeyFile){
        return Cryptography.getPrivateKeyFromBytes(readBytesFromFile(privateKeyFile));
    }
    public static KeyPair readKeyPair(String publicKeyFile, String privateKeyFile){
        return new KeyPair(readPublicKey(publicKeyFile), readPrivateKey(privateKeyFile));
    }
    public static void main(String[] args){
        KeyPair keyPair = Cryptography.gen_asymm_key();
        outputBytesToFile(keyPair.getPublic().getEncoded(), "public.key");
        outputBytesToFile(keyPair.getPrivate().getEncoded(), "private.key");
    }
}
