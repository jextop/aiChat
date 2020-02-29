package com.walle;

import com.walle.audio.ChatUtil;
import com.walle.audio.RecordHelper;
import com.walle.audio.TimeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private static final long MS_DURATION = 5000;
    private static final String RECORD = "      请讲话";
    private static final String PLAY = "      ---";
    private static boolean isChatting = false;

    private static JButton recordBtn;
    private static JLabel recordLbl;
    private static TimeListener playerListener;
    private static TimeListener recorderListener;

    static {
        recordLbl = new JLabel(PLAY);
        recordBtn = new JButton("开始聊天");

        recordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (App.class) {
                    isChatting = !isChatting;
                    recordBtn.setText(isChatting ? "结束聊天" : "开始聊天");

                    RecordHelper recordHelper = RecordHelper.getInst();
                    if (isChatting) {
                        recordHelper.record(recorderListener, MS_DURATION);
                    } else {
                        recordHelper.stop();
                    }
                }
            }
        });

        recorderListener = new TimeListener() {
            @Override
            public void timeUpdated(long seconds) {
                recordLbl.setText(String.format("%s(%d)", RECORD, MS_DURATION / 1000 - seconds));
            }

            @Override
            public void stopped(long seconds) {
                recordLbl.setText(PLAY);
                if (isChatting) {
                    ChatUtil.chat(playerListener);
                }
            }
        };

        playerListener = new TimeListener() {
            @Override
            public void timeUpdated(long seconds) {
            }

            @Override
            public void stopped(long seconds) {
                synchronized (App.class) {
                    if (isChatting) {
                        recordLbl.setText(RECORD);
                        RecordHelper recordHelper = RecordHelper.getInst();
                        recordHelper.record(recorderListener, MS_DURATION);
                    }
                }
            }
        };
    }

    public static void main(String[] args) {
        // create frame
        final JFrame frame = new JFrame("Walle - AI语音聊天");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // create panel
        JPanel panel = new JPanel();
        panel.add(Box.createVerticalStrut(30));
        Box verticalBox = Box.createVerticalBox();
        panel.add(verticalBox);
        verticalBox.add(recordBtn);
        verticalBox.add(recordLbl);

        // Show panel
        frame.setContentPane(panel);
        frame.setVisible(true);

        frame.getRootPane().setDefaultButton(recordBtn);
        recordBtn.doClick();
    }
}
