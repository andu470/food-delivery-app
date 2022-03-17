package presentation;

import business.DeliveryService;
import business.User;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Andrei Rotaru
 * This class connects the logical part of the application with the Graphical Interface
 */

public class Controller {
    private HomeGUI homeGUI;
    private AdminGUI adminGUI;
    private EmployeeGUI employeeGUI;
    private ClientGUI clientGUI;
    private DeliveryService deliveryService;

    private ArrayList<User> users = new ArrayList<>();

    public void start(){
        deliveryService = new DeliveryService();
        homeGUI = new HomeGUI();
        adminGUI = new AdminGUI(homeGUI, deliveryService);
        employeeGUI = new EmployeeGUI(homeGUI);
        clientGUI = new ClientGUI(homeGUI, deliveryService);

        deliveryService.addEmployeeGUIObserver(employeeGUI);

        homeGUI.setVisible(true);

        activateButtons();
    }

    public void activateButtons(){
        homeGUI.addAdminButtonActionListener(e -> {
            homeGUI.setVisible(false);
            adminGUI.setVisible(true);
        });

        homeGUI.addEmployeeButtonActionListener(e -> {
            //homeGUI.setVisible(false);
            employeeGUI.setVisible(true);
        });

        homeGUI.addLoginButtonActionListener(e -> {
            boolean client = false;
            for(User user : users) {
                if(homeGUI.getUsernameTextField().equals(user.getUsername()) &&
                        homeGUI.getPassTextField().equals(user.getPassword())){
                    client = true;
                }
            }
            if(client) {
                homeGUI.setVisible(false);
                clientGUI.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(null,"No user found!");
            }
        });

        homeGUI.addRegisterButtonActionListener(e -> {
            if(!homeGUI.getUsernameTextField().equals("") && !homeGUI.getPassTextField().equals("")) {
                users.add(new User(users.size(),homeGUI.getUsernameTextField(), homeGUI.getPassTextField()));
                JOptionPane.showMessageDialog(null, "User created!");
            }
            else {
                JOptionPane.showMessageDialog(null, "Wrong input!");
            }
        });
    }
}
