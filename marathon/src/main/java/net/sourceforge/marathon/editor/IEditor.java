package net.sourceforge.marathon.editor;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public interface IEditor {

    public interface IGutterListener extends EventListener {
        public ImageIcon getIconAtLine(int line);

        public void gutterDoubleClickedAt(int line);
    }

    public static final int FIND_NEXT = 1;
    public static final int FIND_PREV = 2;

    public static final int FIND_FAILED = 1;
    public static final int FIND_WRAPPED = 2;
    public static final int FIND_SUCCESS = 3;

    public void setStatusBar(IStatusBar statusBar);

    public void startInserting();

    public void stopInserting();

    public void insertScript(String script);

    public void addKeyBinding(String keyBinding, ActionListener action);

    public void highlightLine(int line);

    public boolean isEditable();

    public int getSelectionStart();

    public int getSelectionEnd();

    public void undo();

    public void redo();

    public void cut();

    public void copy();

    public void paste();

    public boolean canUndo();

    public boolean canRedo();

    public void clearUndo();

    public void setDirty(boolean b);

    public boolean isDirty();

    public void addCaretListener(CaretListener listener);

    public void refresh();

    public void addContentChangeListener(IContentChangeListener l);

    public int getCaretLine();

    public void setCaretLine(int line);

    public Component getComponent();

    public void closeSearch();

    public int find(String searchText, boolean bForward, boolean bAllLines, boolean bCaseSensitive, boolean bWrapSearch,
            boolean bWholeWord, boolean bRegex);

    abstract int replaceFind(String searchText, String replaceText, boolean bForward, boolean bAllLines, boolean bCaseSensitive,
            boolean bWrapSearch, boolean bWholeWord, boolean bRegex);

    abstract void replace(String searchText, String replaceText, boolean bForward, boolean bAllLines, boolean bCaseSensitive,
            boolean bWrapSearch, boolean bWholeWord, boolean bRegex);

    public void replaceAll(String searchText, String replaceText, boolean bCaseSensitive, boolean bWholeWord, boolean bRegex);

    public void find(int findPrev);

    public void showSearchDialog(ISearchDialog dialog);

    public void addGutterListener(IGutterListener provider);

    public Object getData(String key);

    public void setData(String key, Object fileHandler);

    public void setCaretPosition(int position);

    public int getCaretPosition();

    public String getText();

    public void setText(String code);

    public void setMode(String string);

    public void setEnabled(boolean b);

    public int getLineOfOffset(int selectionStart) throws BadLocationException;

    public int getLineStartOffset(int startLine) throws BadLocationException;

    public int getLineEndOffset(int endLine) throws BadLocationException;

    public void setFocus();

    public void setMenuItems(JMenuItem[] menuItems);

    public void toggleInsertMode();

    public void setEditable(boolean b);
};
