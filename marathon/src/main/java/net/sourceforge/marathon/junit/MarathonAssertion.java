package net.sourceforge.marathon.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.AssertionFailedError;
import net.sourceforge.marathon.runtime.api.Failure;

public class MarathonAssertion extends AssertionFailedError {
    private static final long serialVersionUID = 1L;
    private Failure[] failures;

    public MarathonAssertion(Failure[] failures, String testName) {
        super(failures != null && failures.length == 1 ? failures[0].getMessage() : "Multiple Failures");
        this.failures = failures;
    }

    public void printStackTrace() {
        super.printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(new PrintWriter(s));
    }

    public void printStackTrace(PrintWriter s) {
        StringWriter output;
        PrintWriter writer = new PrintWriter(output = new StringWriter());
        super.printStackTrace(writer);
        BufferedReader reader = new BufferedReader(new StringReader(output.toString()));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.println(line);
        try {
            if (failures != null) {
                for (int i = 0; i < failures.length; i++) {
                    s.println("\tFailure: " + failures[i].getMessage());
                    if (failures[i].getTraceback().length > 0)
                        s.println("\tat " + failures[i].getTraceback()[0].functionName + "("
                                + getRelativeFileName(failures[i].getTraceback()[0].fileName) + ":"
                                + failures[i].getTraceback()[0].lineNumber + ")");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            s.println(line);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRelativeFileName(String fileName) throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        fileName = new File(fileName).getCanonicalPath();
        if (fileName.startsWith(currentDir)) {
            fileName = fileName.substring(currentDir.length() + 1);
            if (fileName.equals(""))
                fileName = ".";
        }
        return fileName.replace('\\', '/');
    }

    public Failure[] getFailures() {
        return failures;
    }
}
