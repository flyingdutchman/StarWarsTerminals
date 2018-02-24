package codes.flyingdutchman.swt;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

public class Test {

    final static List<String> holder = new LinkedList<>();

    private static boolean b;

    public static void main(String... args) throws Exception {

        b=false;

        final JFrame frame = new JFrame("Test");

        final JTextField field = new JTextField("Enter some int + press enter");
        frame.add(field);
        field.addActionListener(e -> {
                synchronized (holder) {
                    if(b) {
                        holder.add(field.getText());
                        holder.notify();
                    }
                }
                frame.dispose();

    });

        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE)
                    pwa();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static void pwa() {

        b = true;

        Thread thread = new Thread(() -> {
            synchronized (holder) {

                // wait for input from field
                while (holder.isEmpty()) {
                    try {
                        holder.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String nextInt = holder.remove(0);
                System.out.println(nextInt);
                //....
            }
        });

        thread.start();

        b = false;
    }
}
