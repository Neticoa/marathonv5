package net.sourceforge.marathon.javaagent.css;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.marathon.javaagent.IJavaElement;

public class IdFilter implements SelectorFilter {

    private String id;

    public IdFilter(String id) {
        this.id = id;
    }

    @Override public String toString() {
        char[] cs = id.toCharArray();
        boolean needQuotes = false;
        for (char c : cs) {
            needQuotes = needQuotes || !Character.isJavaIdentifierPart(c);
        }
        if (needQuotes)
            return "#\"" + id + "\"";
        return "#" + id;
    }

    @Override public List<IJavaElement> match(IJavaElement je) {
        if (id.equals(je.getAttribute("name")))
            return Arrays.asList(je);
        return new ArrayList<IJavaElement>();
    }
}
