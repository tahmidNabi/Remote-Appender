import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * This class represents the object client for a
 * distributed object of class CallbackServerImpl,
 * which implements the remote interface
 * CallbackServerInterface.  It also accepts callback
 * from the server.
 *
 * @author M. L. Liu
 */

public class CallbackClient extends JFrame {

    JTextArea textArea = new JTextArea();
    CallbackClientInterface callbackObj;
    CallbackServerInterface callbackServer;

    public CallbackServerInterface getCallbackServer() {
        return callbackServer;
    }

    public void setCallbackServer(CallbackServerInterface callbackServer) {
        this.callbackServer = callbackServer;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public CallbackClient() {

        initUI();
    }

    public final void initUI() {

        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");

        menubar.add(file);
        setJMenuBar(menubar);


        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // ImageIcon exit = new ImageIcon("exit.png");
        JButton registerButton = new JButton("Register");
        //   registerButton.setBorder(new EmptyBorder(0, 0, 0, 0));


        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.append("Ok");
                try {


                    String registryURL = "rmi://localhost:" + 1099 + "/callback";
                    // find the remote object and cast it to an
                    //   interface object
                    callbackServer = (CallbackServerInterface) Naming.lookup(registryURL);
                    System.out.println("Lookup completed ");
                    System.out.println("Server said " + callbackServer.sayHello());
                    callbackObj = new CallbackClientImpl();
                    callbackObj.setTextArea(textArea);
                    // register for callback
                    callbackServer.registerForCallback(callbackObj);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        toolbar.add(registerButton);

        JButton unRegisterButton = new JButton("Unregister");
        //   registerButton.setBorder(new EmptyBorder(0, 0, 0, 0));


        unRegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    callbackServer.unregisterForCallback(callbackObj);
                } catch (RemoteException e1) {
                    throw new RuntimeException(e1);
                }
                System.out.println("Unregistered for callback.");

            }
        });
        toolbar.add(unRegisterButton);


        add(toolbar, BorderLayout.NORTH);
        /*
        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(10, 5, 5, 5));

        ImageIcon select = new ImageIcon("drive.png");
        ImageIcon freehand = new ImageIcon("computer.png");
        ImageIcon shapeed = new ImageIcon("printer.png");

        JButton selectb = new JButton(select);
        selectb.setBorder(new EmptyBorder(3, 0, 3, 0));

        JButton freehandb = new JButton(freehand);
        freehandb.setBorder(new EmptyBorder(3, 0, 3, 0));
        JButton shapeedb = new JButton(shapeed);
        shapeedb.setBorder(new EmptyBorder(3, 0, 3, 0));

        vertical.add(selectb);
        vertical.add(freehandb);
        vertical.add(shapeedb);

        add(vertical, BorderLayout.WEST);
          */
        add(getTextArea(), BorderLayout.CENTER);

/*        JLabel statusbar = new JLabel(" Statusbar");
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        add(statusbar, BorderLayout.SOUTH);*/

        setSize(350, 300);
        setTitle("BorderLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


    }

    public static void main(String args[]) {
        try {
            final CallbackClient callbackClient = new CallbackClient();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    callbackClient.setVisible(true);
                }
            });
        } // end try
        catch (Exception e) {
            System.out.println(
                    "Exception in CallbackClient: " + e);
        }


    } //end main
}//end class
