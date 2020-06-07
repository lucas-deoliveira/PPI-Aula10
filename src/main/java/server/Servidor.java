package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map;

import javax.swing.JOptionPane;

public class Servidor implements Runnable {
    private int porta;
    public static ArrayList<Socket> listaUsuario = null;
    public static Vector<String> nomeUsuario = null;
    public static Map<String, Socket> map = null;
    public static ServerSocket ss = null;
    public static boolean flag = true;

    public Servidor(int porta) throws IOException {
        this.porta = porta;
    }

    public void run() {
        Socket s = null;
        listaUsuario = new ArrayList<Socket>();
        nomeUsuario = new Vector<String>();
        map = new HashMap<String, Socket>();

        System.out.println("Server on!");

        try {
            ss = new ServerSocket(porta);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (flag) {
            try {
                s = ss.accept();
                listaUsuario.add(s);
                new Thread(new SRecebe(s, listaUsuario, nomeUsuario, map)).start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(TelaS.window, "Server off！");
            }
        }
    }
}