package net.sourceforge.marathon.javaagent.components;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import net.sourceforge.marathon.javaagent.IJavaElement;
import net.sourceforge.marathon.javaagent.JavaAgent;
import net.sourceforge.marathon.testhelpers.ComponentUtils;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import components.TableFilterDemo;

@Test public class JTableJavaElementTest extends JavaElementTest {

    private JavaAgent driver;
    protected JFrame frame;

    @BeforeMethod public void showDialog() throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
                frame = new JFrame(JTableJavaElementTest.class.getSimpleName());
                frame.setName("frame-" + JTableJavaElementTest.class.getSimpleName());
                frame.getContentPane().add(new TableFilterDemo(), BorderLayout.CENTER);
                frame.pack();
                JTable table = (JTable) ComponentUtils.findComponent(JTable.class, frame);
                table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                frame.setAlwaysOnTop(true);
                frame.setVisible(true);
            }
        });
        driver = new JavaAgent();
    }

    @AfterMethod public void disposeDriver() throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }

    public void selectNoCells() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        marathon_select(table, "");
        AssertJUnit.assertEquals("[]", table.getAttribute("selectedRows"));
        AssertJUnit.assertEquals("[]", table.getAttribute("selectedColumns"));
    }

    public void selectSingleRow() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        marathon_select(table, "[rows:[0], columns:[Sport]]");
        AssertJUnit.assertEquals("[0]", table.getAttribute("selectedRows"));
        AssertJUnit.assertEquals("[2]", table.getAttribute("selectedColumns"));
    }

    public void selectMultipleRows() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        marathon_select(table, "[rows:[0,2], columns:[Sport]]");
        AssertJUnit.assertEquals("[0, 2]", table.getAttribute("selectedRows"));
        AssertJUnit.assertEquals("[2]", table.getAttribute("selectedColumns"));
    }

    public void selectSingleRowByProperties() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        IJavaElement e = marathon_select_by_properties(table, "{0, Sport}", false);
        AssertJUnit.assertEquals("Snowboarding", e.getText());
    }

    public void editCellByProperties() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        IJavaElement e = marathon_select_by_properties(table, "{0, Sport}", false);
        AssertJUnit.assertEquals("Snowboarding", e.getText());
        e = marathon_select_by_properties(table, "{0, Sport}", true);
        marathon_select(e, "Knitting");
        e = marathon_select_by_properties(table, "{0, Sport}", false);
        AssertJUnit.assertEquals("Knitting", e.getText());
    }

    public void assertContent() throws Throwable {
        IJavaElement table = driver.findElementByTagName("table");
        String expected = "[[\"Kathy\",\"Smith\",\"Snowboarding\",\"5\",\"false\"],[\"John\",\"Doe\",\"Rowing\",\"3\",\"true\"],[\"Sue\",\"Black\",\"Knitting\",\"2\",\"false\"],[\"Jane\",\"White\",\"Speed reading\",\"20\",\"true\"],[\"Joe\",\"Brown\",\"Pool\",\"10\",\"false\"]]";
        AssertJUnit.assertEquals(expected, table.getAttribute("content"));
    }

}
