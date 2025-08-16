import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import java.io.*;

public class Client implements ActionListener {
    JTextField text;
    JPanel x;
    static Box vertical = Box.createVerticalBox();
    JFrame f = new JFrame();
    static DataOutputStream dout;
    static Socket cl;
    static DataInputStream din;

    public Client() {
        f.setLayout(null);

      
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(0, 50, 0));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

      
        ImageIcon i1 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/3.png");
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        p1.add(back);

    
        ImageIcon i4 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/man.png");
        Image i5 = i4.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 40, 40);
        p1.add(profile);

    
        ImageIcon i7 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/vd.png");
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

  
        ImageIcon a = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/ph.png");
        Image b = a.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel call = new JLabel(new ImageIcon(b));
        call.setBounds(350, 20, 25, 25);
        p1.add(call);

   
        ImageIcon d = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/mr.png");
        Image e = d.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel more = new JLabel(new ImageIcon(e));
        more.setBounds(400, 20, 25, 25);
        p1.add(more);

  
        JLabel name = new JLabel("Ashish");
        name.setBounds(90, 15, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        p1.add(name);

        JLabel status = new JLabel("Online");
        status.setBounds(90, 35, 100, 20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
        p1.add(status);

   
        x = new JPanel();
        x.setBounds(5, 75, 440, 570);
        x.setLayout(new BorderLayout());
        f.add(x);

    
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(0, 50, 0));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);

        startClient();
    }

    public void startClient() {
        new Thread(() -> {
            try {
                cl = new Socket("127.0.0.1", 8888);
                din = new DataInputStream(cl.getInputStream());
                dout = new DataOutputStream(cl.getOutputStream());

                while (true) {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));

                    x.setLayout(new BorderLayout());
                    x.add(vertical, BorderLayout.PAGE_START);
                    f.validate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText().trim();
            if (out.isEmpty()) return;

            if (dout != null) {
                dout.writeUTF(out);

                JPanel p2 = formatLabel(out);
                JPanel right = new JPanel(new BorderLayout());
                right.add(p2, BorderLayout.LINE_END);
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));

                x.removeAll();
                x.add(vertical, BorderLayout.NORTH);
                x.revalidate();
                x.repaint();
            } else {
                JOptionPane.showMessageDialog(f, "Server not connected yet!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(Color.BLACK);
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
