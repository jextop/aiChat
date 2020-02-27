package com.walle;

import com.walle.audio.ChatUtil;
import com.walle.audio.RecordHelper;
import com.walle.audio.TimeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) {
        // create frame
        final JFrame frame = new JFrame("Walle");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // create panel
        JPanel panel = new JPanel();
        Box verticalBox = Box.createVerticalBox();
        panel.add(verticalBox);

        // Create buttons
        final long msDuration = 5000;
        final String title = "AI语音聊天";
        final JButton recordBtn = new JButton(title);
        recordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RecordHelper recordHelper = RecordHelper.getInst();
                if (recordHelper.isRecording()) {
                    return;
                }

                recordBtn.setText(String.format("%s(%d)", title, msDuration / 1000));
                recordHelper.record(new TimeListener() {
                    @Override
                    public void timeUpdated(long seconds) {
                        recordBtn.setText(String.format("%s(%d)", title, msDuration / 1000 - seconds));
                    }

                    @Override
                    public void stopped(long seconds) {
                        recordBtn.setText(title);
                        ChatUtil.chat();
                    }
                }, msDuration);
            }
        });

        verticalBox.add(recordBtn);

        // Show panel
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
