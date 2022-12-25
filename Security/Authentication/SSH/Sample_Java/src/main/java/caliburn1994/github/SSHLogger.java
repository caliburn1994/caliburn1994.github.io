package caliburn1994.github;

import com.jcraft.jsch.Logger;

import java.util.Hashtable;

public class SSHLogger implements com.jcraft.jsch.Logger {
    static Hashtable<Integer,String> name = new Hashtable<>();

    static {
        name.put(Logger.DEBUG, "DEBUG: ");
        name.put(Logger.INFO, "INFO: ");
        name.put(Logger.WARN, "WARN: ");
        name.put(Logger.ERROR, "ERROR: ");
        name.put(Logger.FATAL, "FATAL: ");
    }

    public boolean isEnabled(int level) {
        return true;
    }

    public void log(int level, String message) {
        System.err.print(name.get(level));
        System.err.println(message);
    }
}