import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RemoteAppender extends WriterAppender {
    ArrayList<LoggingEvent> eventsList = new ArrayList();
    ArrayList<String> messagelist = new ArrayList<String>();
    public CallbackServerInterface rmiServer;

    public RemoteAppender(Layout layout) {
        this.layout = layout;


        try {
            startRegistry(1099);
            rmiServer = new CallbackServerImpl();
            String registryURL ="rmi://localhost:" + 1099 + "/callback";
            Naming.rebind(registryURL, rmiServer);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
   public void append(LoggingEvent event) {
        eventsList.add(event);
        messagelist.add(this.layout.format(event));
        try {
            rmiServer.doCallbacks(this.layout.format(event));
        }
        catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void close() {
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry =
                    LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
            // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            Registry registry =
                    LocateRegistry.createRegistry(RMIPortNum);
        }
    }



}
