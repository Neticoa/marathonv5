package net.sourceforge.marathon.javaagent.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import net.sourceforge.marathon.javaagent.AbstractJavaElement;
import net.sourceforge.marathon.javaagent.IJavaElement;
import net.sourceforge.marathon.javaagent.JavaAgent;
import net.sourceforge.marathon.javaagent.JavaTargetLocator.JWindow;
import net.sourceforge.marathon.javaagent.UnsupportedCommandException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JTableHeaderJavaElement extends AbstractJavaElement {

    private static final class PropertyPredicate implements Predicate {
        private final Properties p;

        private PropertyPredicate(Properties p) {
            this.p = p;
        }

        @Override public boolean isValid(JTableHeaderItemJavaElement e) {
            Enumeration<Object> keys = p.keys();
            while (keys.hasMoreElements()) {
                String object = (String) keys.nextElement();
                if (!p.getProperty(object).equals(e.getAttribute(object)))
                    return false;
            }
            return true;
        }
    }

    private static interface Predicate {
        public boolean isValid(JTableHeaderItemJavaElement e);
    }

    public JTableHeaderJavaElement(Component header, JavaAgent driver, JWindow window) {
        super(header, driver, window);
    }

    @Override public String _getText() {
        int nitems = getCount();
        JSONArray r = new JSONArray();
        for (int i = 0; i < nitems; i++)
            r.put(new JTableHeaderItemJavaElement(this, i)._getText());
        return r.toString();
    }

    @Override public List<IJavaElement> getByPseudoElement(String selector, Object[] params) {
        if (selector.equals("nth-item"))
            return Arrays.asList((IJavaElement) new JTableHeaderItemJavaElement(this, ((Integer) params[0]).intValue() - 1));
        else if (selector.equals("all-items")) {
            int nitems = getCount();
            List<IJavaElement> r = new ArrayList<IJavaElement>();
            for (int i = 0; i < nitems; i++) {
                r.add(new JTableHeaderItemJavaElement(this, i));
            }
            return r;
        } else if (selector.equals("select-by-properties")) {
            try {
                JSONObject o = new JSONObject((String) params[0]);
                return selectByProperties(new ArrayList<IJavaElement>(), o);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        throw new UnsupportedCommandException("JTableHeader does not support pseudoelement " + selector, null);
    }

    private List<IJavaElement> selectByProperties(ArrayList<IJavaElement> r, JSONObject o) throws Throwable {
        final Properties p;
        if (o.has("select")) {
            p = PropertyHelper.fromString(o.getString("select"), new String[][] { { "text" } });
        } else {
            p = PropertyHelper.asProperties(o);
        }
        return findmatch(r, new PropertyPredicate(p));
    }

    private List<IJavaElement> findmatch(ArrayList<IJavaElement> r, PropertyPredicate p) {
        JTableHeader header = (JTableHeader) component;
        TableColumnModel columnModel = header.getColumnModel();
        int col = columnModel.getColumnCount();
        for (int i = 0; i < col; i++) {
            JTableHeaderItemJavaElement e = new JTableHeaderItemJavaElement(this, i);
            if (p.isValid(e))
                r.add(e);
        }
        return r;
    }

    public int getCount() {
        JTableHeader header = (JTableHeader) getComponent();
        return header.getColumnModel().getColumnCount();
    }

    public String getContent() {
        return new JSONArray(getContent((JTableHeader) component)).toString();
    }

    public static String[][] getContent(JTableHeader component) {
        int columnCount = component.getColumnModel().getColumnCount();
        String[][] content = new String[1][columnCount];
        for (int i = 0; i < columnCount; i++) {
            content[0][i] = JTableHeaderItemJavaElement.getText(component, i);
        }
        return content;
    }
}
