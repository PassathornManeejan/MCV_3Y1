package app.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog {
    private JTextField userField;
    private String result;

    public LoginDialog(Frame owner) {
        super(owner, "Login (username only)", true);
        initUI();
    }

    private void initUI() {
        userField = new JTextField(15);
        var ok = new JButton("Login");
        var cancel = new JButton("Cancel");

        ok.addActionListener((ActionEvent e) -> {
            result = userField.getText().trim();
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username (e.g., anan, bua, chai)");
                return;
            }
            dispose();
        });
        cancel.addActionListener(e -> { result = null; dispose(); });

        var p = new JPanel(new GridBagLayout());
        var c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.LINE_END;
        p.add(new JLabel("Username:"), c);
        c.gridx=1; c.gridy=0; c.anchor=GridBagConstraints.LINE_START; c.fill=GridBagConstraints.HORIZONTAL; c.weightx=1.0;
        p.add(userField, c);

        var btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(ok); btns.add(cancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(btns, BorderLayout.SOUTH);
        pack();
    }

    public static String prompt(Frame owner) {
        var d = new LoginDialog(owner);
        d.setLocationRelativeTo(owner);
        d.setVisible(true);
        return d.result;
    }
}
