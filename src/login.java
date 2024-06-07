import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Fri May 17 10:50:57 CST 2024
 */



/**
 * @author 翁梓翔
 */
public class login extends JDialog {
    public login(Window owner) {
        super(owner);
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("db");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        nameTextField = new JTextField();
        passwordTextField = new JTextField();
        loginButton = new JButton();
        registerButton = new JButton();
        titleLabel = new JLabel();
        veriCodeTextField = new JTextField();
        forgetLabel = new JLabel();

        //======== this ========
        setBackground(Color.black);
        setUndecorated(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBackground(Color.black);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBackground(new Color(0x15171e));

                //---- nameTextField ----
                nameTextField.setForeground(Color.white);
                nameTextField.setBackground(new Color(0x15171e));
                nameTextField.setToolTipText("\u8bf7\u8f93\u5165\u7528\u6237\u540d");
                nameTextField.setBorder(LineBorder.createGrayLineBorder());

                //---- passwordTextField ----
                passwordTextField.setForeground(Color.white);
                passwordTextField.setToolTipText("\u8bf7\u8f93\u5165\u5bc6\u7801");
                passwordTextField.setBackground(new Color(0x15171e));
                passwordTextField.setBorder(LineBorder.createGrayLineBorder());

                //---- loginButton ----
                loginButton.setText(bundle.getString("login.loginButton.text"));
                loginButton.setForeground(Color.white);
                loginButton.setBackground(new Color(0x0074e0));

                //---- registerButton ----
                registerButton.setText(bundle.getString("login.registerButton.text"));
                registerButton.setForeground(Color.white);
                registerButton.setBackground(new Color(0x0074e0));
                registerButton.setIcon(null);
                registerButton.setBorderPainted(false);

                //---- titleLabel ----
                titleLabel.setFont(new Font("\u534e\u6587\u65b0\u9b4f", Font.PLAIN, 29));
                titleLabel.setText(bundle.getString("login.titleLabel.text"));
                titleLabel.setForeground(Color.white);

                //---- veriCodeTextField ----
                veriCodeTextField.setForeground(Color.white);
                veriCodeTextField.setBackground(new Color(0x15171e));
                veriCodeTextField.setToolTipText("\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801");
                veriCodeTextField.setBorder(LineBorder.createGrayLineBorder());

                //---- forgetLabel ----
                forgetLabel.setText(bundle.getString("login.forgetLabel.text"));
                forgetLabel.setFont(forgetLabel.getFont().deriveFont(Font.PLAIN));
                forgetLabel.setForeground(new Color(0x3399ff));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(passwordTextField, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
                                .addComponent(veriCodeTextField, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(41, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addContainerGap(63, Short.MAX_VALUE)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(loginButton)
                                            .addGap(70, 70, 70)
                                            .addComponent(registerButton))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addGap(83, 83, 83)
                                            .addComponent(forgetLabel)))
                                    .addGap(60, 60, 60))
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                    .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                    .addGap(110, 110, 110))))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(55, 55, 55)
                            .addComponent(passwordTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                            .addComponent(veriCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(49, 49, 49)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginButton)
                                .addComponent(registerButton))
                            .addGap(32, 32, 32)
                            .addComponent(forgetLabel)
                            .addGap(31, 31, 31))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTextField nameTextField;
    private JTextField passwordTextField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel;
    private JTextField veriCodeTextField;
    private JLabel forgetLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
