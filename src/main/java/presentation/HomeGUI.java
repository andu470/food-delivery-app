package presentation;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Andrei Rotaru
 * This class represents the "home" GUI of the application, being a login/register page for the users
 */

public class HomeGUI extends JFrame {
    private final JLabel usernameLabel;
    private final JTextField usernameTextField;

    private final JLabel passLabel;
    private final JTextField passTextField;

    private final JButton employeeButton;
    private final JButton adminButton;
    private final JButton loginButton;
    private final JButton registerButton;

    public HomeGUI(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(300, 100, 700, 500);
        this.getContentPane().setLayout(null);
        this.setTitle("Home");

        usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(250,150,70,30);
        getContentPane().add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(320,150,120,30);
        getContentPane().add(usernameTextField);

        passLabel = new JLabel("Password: ");
        passLabel.setBounds(250,180,70,30);
        getContentPane().add(passLabel);

        passTextField = new JTextField();
        passTextField.setBounds(320,180,120,30);
        getContentPane().add(passTextField);

        loginButton = new JButton("Login");
        loginButton.setBounds(320,220,90,30);
        getContentPane().add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(320,260,90,30);
        getContentPane().add(registerButton);

        employeeButton = new JButton("Employee");
        employeeButton.setBounds(500,350,100,30);
        getContentPane().add(employeeButton);

        adminButton = new JButton("Admin");
        adminButton.setBounds(150,350,100,30);
        getContentPane().add(adminButton);
    }

    public String getUsernameTextField() {
        return usernameTextField.getText();
    }

    public String getPassTextField() {
        return passTextField.getText();
    }

    public void addAdminButtonActionListener(ActionListener actionListener) {
        adminButton.addActionListener(actionListener);
    }

    public void addEmployeeButtonActionListener(ActionListener actionListener) {
        employeeButton.addActionListener(actionListener);
    }

    public void addRegisterButtonActionListener(ActionListener actionListener) {
        registerButton.addActionListener(actionListener);
    }

    public void addLoginButtonActionListener(ActionListener actionListener) {
        loginButton.addActionListener(actionListener);
    }
}
