
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: tahmid
 * Date: 5/7/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static void main(String[] args) {

        //logger.addAppender(new RemoteAppender());

        PatternLayout layout= new PatternLayout("%-5p %d (%c:%L) - %m%n");


        Logger l = Logger.getLogger("test");

        RemoteAppender app = new RemoteAppender(layout);

        l.addAppender(app);

        l.warn("first");
        l.warn("second");
        l.warn("third");

        l.trace("fourth shouldn't be printed");

        for (String str : app.messagelist) {
            System.out.println("***" + str);
        }

        String str = JOptionPane.showInputDialog("Enter something");

        l.info("got" + str);

    }
}
