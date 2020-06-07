package user;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class UserRecebe implements Runnable {
    private Socket s;

    public UserRecebe(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader brIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                String s = brIn.readLine();
                String[] strs = s.split("\\.");
                String info = strs[0];
                String name = "", line = "";
                if (strs.length == 2)
                    line = strs[1];
                else if (strs.length == 3) {
                    line = strs[1];
                    name = strs[2];
                }

                if (info.equals("1")) {
                    TelaUser.textMessage.append(line + "\r\n");
                    TelaUser.textMessage.setCaretPosition(TelaUser.textMessage.getText().length());
                } else if (info.equals("2") || info.equals("3")) {
                    if (info.equals("2")) {
                        TelaUser.textMessage.append("(Alerta) " + name + " entrou!" + "\r\n");
                        TelaUser.textMessage.setCaretPosition(TelaUser.textMessage.getText().length());
                    } else {
                        TelaUser.textMessage.append("(Alerta) " + name + " saiu!" + "\r\n");
                        TelaUser.textMessage.setCaretPosition(TelaUser.textMessage.getText().length());
                    }
                    String list = line.substring(1, line.length() - 1);
                    String[] data = list.split(",");
                    TelaUser.user.clearSelection();
                    TelaUser.user.setListData(data);
                } else if (info.equals("4")) {
                    TelaUser.connect.setText("entrar");
                    TelaUser.sair.setText("sair");
                    TelaUser.socket.close();
                    TelaUser.socket = null;
                    JOptionPane.showMessageDialog(TelaUser.window, "Alguém já está usando esse nome de usuário");
                    break;
                } else if (info.equals("5")) {
                    TelaUser.connect.setText("entrou");
                    TelaUser.sair.setText("saiu");
                    TelaUser.socket.close();
                    TelaUser.socket = null;
                    break;
                } else if (info.equals("6")) {
                    TelaUser.textMessage.append("(Mensagem privada) " + line + "\r\n");
                    TelaUser.textMessage.setCaretPosition(TelaUser.textMessage.getText().length());
                } else if (info.equals("7")) {
                    JOptionPane.showMessageDialog(TelaUser.window, "Esse usuário não está online");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(TelaUser.window, "O usuário saiu");
        }
    }
}