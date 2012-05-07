import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * This class implements the remote interface
 * CallbackServerInterface.
 *
 * @author M. L. Liu
 */

public class CallbackServerImpl extends UnicastRemoteObject
        implements CallbackServerInterface {

    private Vector clientList;


    public CallbackServerImpl() throws RemoteException {
        super();
        clientList = new Vector();
    }

    public String sayHello()
            throws RemoteException {
        return ("hello");
    }

    public synchronized void registerForCallback(CallbackClientInterface callbackClientObject) throws RemoteException {
        // store the callback object into the vector
        if (!(clientList.contains(callbackClientObject))) {
            clientList.addElement(callbackClientObject);
            System.out.println("Registered new client ");
            //doCallbacks();
        } // end if
    }

// This remote method allows an object client to
// cancel its registration for callback
// @param id is an ID for the client; to be used by
// the server to uniquely identify the registered client.
    public synchronized void unregisterForCallback(CallbackClientInterface callbackClientObject) throws RemoteException {

        if (clientList.removeElement(callbackClientObject)) {
            System.out.println("Unregistered client ");
        } else {
            System.out.println("unregister: clientwasn't registered.");
        }
    }

    public synchronized void doCallbacks(String message) throws RemoteException {
        // make callback to each registered client
        System.out.println("**************************************\n" + "Callbacks initiated ---");
        for (int i = 0; i < clientList.size(); i++) {
            //System.out.println("doing " + i + "-th callback\n");
            // convert the vector object to a callback object
            CallbackClientInterface nextClient = (CallbackClientInterface) clientList.elementAt(i);
            // invoke the callback method
            nextClient.notifyMe(message);
        }// end for
        //System.out.println("********************************\n" + "Server completed callbacks ---");
    } // doCallbacks

}// end CallbackServerImpl class
