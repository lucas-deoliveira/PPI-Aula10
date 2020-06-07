package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class SRecebe implements Runnable {
    private Socket socket;
    private ArrayList<Socket> listaUsuario;
    private Vector<String> nomeUsuario;
    private Map<String, Socket> map;


    public SRecebe(Socket s, ArrayList<Socket> listaUsuario, Vector<String> nomeUsuario, Map<String, Socket> map) {
        this.socket = s;
        this.listaUsuario = listaUsuario;
        this.nomeUsuario = nomeUsuario;
        this.map = map;
    }

    public void run() {
        try {
            BufferedReader brIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String s = brIn.readLine();
                String[] strs = s.split(",,");
                String info = strs[0];
                String line = strs[1];

                String name = "";
                if (strs.length == 3)
                    name = strs[2];

                if (info.equals("1")) {
                    TelaS.console.append("Nova mensagem ---->> " + line + "\r\n");
                    TelaS.console.setCaretPosition(TelaS.console.getText().length());
                    new SEnvia(listaUsuario, line, "1", "");
                } else if (info.equals("2")) {
                    if (!nomeUsuario.contains(line)) {
                        TelaS.console.append("Novo login requisitado ---->> " + line + "\r\n");
                        TelaS.console.setCaretPosition(TelaS.console.getText().length());
                        nomeUsuario.add(line);
                        map.put(line, socket);
                        TelaS.user.setListData(nomeUsuario);
                        new SEnvia(listaUsuario, nomeUsuario, "2", line);
                    } else {
                        TelaS.console.append("Login duplicado ---->> " + line + "\r\n");
                        TelaS.console.setCaretPosition(TelaS.console.getText().length());
                        listaUsuario.remove(socket);
                        new SEnvia(socket, "", "4");
                    }
                } else if (info.equals("3")) {
                    TelaS.console.append("Saiu ---->> " + line + "\r\n");
                    TelaS.console.setCaretPosition(TelaS.console.getText().length());
                    nomeUsuario.remove(line);
                    listaUsuario.remove(socket);
                    map.remove(line);
                    TelaS.user.setListData(nomeUsuario);
                    new SEnvia(listaUsuario, nomeUsuario, "3", line);
                    socket.close();
                    break;
                } else if (info.equals("4")) {
                    TelaS.console.append("Nova mensagem privada ---->> " + line + "\r\n");
                    TelaS.console.setCaretPosition(TelaS.console.getText().length());
                    if (map.containsKey(name))
                        new SEnvia(map.get(name), line, "6");
                    else
                        new SEnvia(socket, "", "7");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}