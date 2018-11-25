package Java;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * File:BomberConfigDialog
 */

/**
 * Lớp này tạo dialog để config key
 */
public class BomberConfigDialog extends JDialog
implements ActionListener {
    /** dữ liệu key tạm thời để thao tác */
    private int[][] keys = null;
    /** key đang set giá trị offset */
    private int[] keysBeingSet = { -1, -1 };
    /** đợi key flag */
    private boolean waitingForKey = false;
    /** nút cho phép người dùng set key */
    private JButton[][] buttons = null;
    /** textfield chứa key show */
    private JTextField[][] keyFields = null;

    /**
     * Dựng dialog.
     */
    public BomberConfigDialog(JFrame owner) {
        /** gọi lớp căn cứ constructor */
        super(owner, "Bomberman Keys Configuration", true);

        /** tạo key object tạm thời */
        keys = new int[4][5];
        /** set the object's data from the currently configurations */
        for (int i = 0; i < 4; i++) for (int j = 0; j < 5; j++)
            keys[i][j] = BomberKeyConfig.keys[i][j];

        /** tạo panel giữ config */
        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        /** panel cho mỗi config */
        JPanel[] panels = new JPanel[4];
        /** tạo text fields mảng */
        keyFields = new JTextField[4][];
        /** tạo mảng nút config */
        buttons = new JButton[4][5];
        for (int i = 0; i < 4; i++) {
            /** tạo mảng key fields */
            keyFields[i] = new JTextField[5];
            /** tạo 4 panel */
            setupPanel(i, centerPanel, panels[i], keyFields[i]);
        }

        /** tạo panel hiển thị help message */
        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        /** thiết lập border */
        helpPanel.setBorder(BorderFactory.createEtchedBorder());
        /** thêm nhãn */
        helpPanel.add(new JLabel("Click on the buttons to edit the keys.",
        JLabel.CENTER));
        /** thêm panel trợ giúp trên dialog */
        getContentPane().add(helpPanel, "North");
        /** thêm key setup panel ở trung tâm */
        getContentPane().add(centerPanel, "Center");
        /** tạo panel giữ nút */
        JPanel buttonsP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsP.setBorder(BorderFactory.createEtchedBorder());
        /** tạo nút save config */
        JButton saveButton = new JButton("Save Configurations");
        saveButton.addActionListener(this);
        buttonsP.add(saveButton);
        /** tạo nút đóng */
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonsP.add(closeButton);
        /** thêm panel trợ giúp dưới dialog */
        getContentPane().add(buttonsP, "South");

        /** set dialog không để người dùng resize */
        setResizable(false);
        /** tối thiểu dialog */
        pack();

        int x = owner.getLocation().x + (owner.getSize().width -
                getSize().width) / 2;
        int y = owner.getLocation().y + (owner.getSize().height -
                getSize().height) / 2;

        /** đặt dialog vào giữa */
        setLocation(x, y);
        /** show dialog */
        show();
    }

    /**
     * Tạo mỗi panel.
     */
    private void setupPanel(int pn, JPanel m, JPanel p, JTextField[] fields)
    {
        /** tạo panel */
        JPanel left = new JPanel(new GridLayout(5, 1));
        JPanel right = new JPanel(new GridLayout(5, 1));

        /** tạo nút và text fields */
        for (int i = 0; i < 5; i++) {
            /** tạo nút */
            buttons[pn][i] = new JButton();
            /** tạo text field */
            fields[i] = new JTextField(10);
            /** setup nút */
            switch (i) {
                case 0: buttons[pn][i].setText("Up"); break;
                case 1: buttons[pn][i].setText("Down"); break;
                case 2: buttons[pn][i].setText("Left"); break;
                case 3: buttons[pn][i].setText("Right"); break;
                case 4: buttons[pn][i].setText("Bomb"); break;
            }
            /** thêm nút xử lý */
            buttons[pn][i].addActionListener(this);
            /** setup text field từ dữ liệu */
            fields[i].setText(KeyEvent.getKeyText(keys[pn][i]));
            /** không thể edit text field */
            fields[i].setEditable(false);
            /** tạo nút trái */
            left.add(buttons[pn][i]);
            /** tạo nút phải */
            right.add(fields[i]);
        }

        /** tạo player's panel */
        p = new JPanel(new GridLayout(1, 2));
        /** set border */
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.
        createEtchedBorder(), "Player " + (pn + 1) + " Keys Configuration"));
        /** tạo nút và key cho panel */
        p.add(left);
        p.add(right);
        /** tạo panel cho master panel */
        m.add(p);
    }

    /**
     * Xử lý hành động
     */
    public void actionPerformed(ActionEvent evt) {
        /** nếu ân nút save */
        if (evt.getActionCommand().equals("Save Configurations")) {
            /** copy key lại */
            for (int i = 0; i < 4; i++) for (int j = 0; j < 5; j++)
                BomberKeyConfig.keys[i][j] = keys[i][j];
            /** viết file */
            BomberKeyConfig.writeFile();
            /** bỏ dialog */
            dispose();
        }
        /** nếu nút bị ấn loại bỏ dialog */
        else if (evt.getActionCommand().equals("Close")) dispose();
        /** nếu ấn nút khác */
        else {
            /** kiếm xem nút nào bị ấn */
            int i = 0, j = 0;
            boolean found = false;
            for (i = 0; i < 4; ++i) {
                for (j = 0; j < 5; ++j) {
                    /** nếu tìm được key thoát loop */
                    if (evt.getSource().equals(buttons[i][j])) found = true;
                    if (found) break;
                }
                if (found) break;
            }
            /** set keys being set indexes */
            keysBeingSet[0] = i;
            keysBeingSet[1] = j;
            /** setup get key loop */
            waitingForKey = true;
            /** create the get key dialog */
            new GetKeyDialog(this, "Press a key to be assigned...", true);
        }
    }

    /**
     * Lớp nhớ key mới.
     */
    private class GetKeyDialog extends JDialog {
        /** points to itself */
        private JDialog me = null;
        /**
         * Dựng dialog mới.
         */
        public GetKeyDialog(JDialog owner, String title, boolean modal) {
            /** call base class constructor */
            setTitle(title);
            setModal(modal);
            /** setup pointer to point to itself */
            me = this;

            /** add keyboard event handler */
            addKeyListener(new KeyAdapter() {
                /**
                 * Handles key pressed events.
                 * @param evt keyboard event
                 */
                public void keyPressed(KeyEvent evt) {
                    /** if it's waiting for a key */
                    if (waitingForKey) {
                        /** get index of key to set */
                        int i = keysBeingSet[0];
                        int j = keysBeingSet[1];
                        /** get the key pressed */
                        int newKey = evt.getKeyCode();
                        /** key used flag */
                        boolean keyUsed = false;
                        /** see if the key is used already or not */
                        for (int p = 0; p < 4; ++p) {
                            for (int k = 0; k < 5; ++k) {
                                /** if key is used already */
                                if (keys[p][k] == newKey) {
                                    /** if it isn't the key being set */
                                    if (!(p == i && j == k))
                                       /** set key used flag to true */
                                       keyUsed = true;
                                }
                                /** if key used flag is true, then exit loop */
                                if (keyUsed) break;
                            }
                            /** if key used flag is true, then exit loop */
                            if (keyUsed) break;
                        }
                        /** if key isn't used */
                        if (!keyUsed) {
                            /** copy new key */
                            keys[i][j] = newKey;
                            /** reset the key field */
                            keyFields[i][j].setText(
                            KeyEvent.getKeyText(keys[i][j]));
                            /** set waiting for key to false */
                            waitingForKey = false;
                            /** destroy the dialog */
                            dispose();
                        }
                        /** if key is used already */
                        else {
                            /** then show an error dialog */
                            /** create the dialog content */
                            JOptionPane pane = new JOptionPane(
                            "Key: [" + KeyEvent.getKeyText(newKey) +
                            "] is used already.  Pick a different key.");
                            /** setup the dialog controls */
                            pane.setOptionType(-JOptionPane.NO_OPTION);
                            pane.setMessageType(JOptionPane.ERROR_MESSAGE);
                            /** create the dialog */
                            JDialog dialog = pane.createDialog(me, "Error");
                            /** set it so user can't resize the dialog */
                            dialog.setResizable(false);
                            /** show the dialog */
                            dialog.show();
                        }
                    }
                }
            });

            /** set dialog không thể resize */
            setResizable(false);
            /** set dialog size */
            setSize(300, 0);

            int x = owner.getLocation().x + (owner.getSize().width -
            getSize().width) / 2;
            int y = owner.getLocation().y + (owner.getSize().width -
            getSize().height) / 2;
            /** center the dialog relative to the owner */
            setLocation(x, y);

            /** show dialog */
            show();
        }
    }
}
