package mchorse.mappet.client.gui.utils.text.undo;

import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mappet.client.gui.utils.text.utils.Cursor;
import mchorse.mclib.utils.undo.IUndo;

public class TextEditUndo implements IUndo<GuiMultiTextElement>
{
    public String text;
    public Cursor cursor = new Cursor(-1, 0);
    public Cursor selection = new Cursor(-1, 0);

    public String postText = "";
    public Cursor postCursor = new Cursor(-1, 0);
    public Cursor postSelection = new Cursor(-1, 0);

    public boolean ready;

    public TextEditUndo(String text, Cursor cursor, Cursor selection)
    {
        this.text = text;
        this.cursor.copy(cursor);
        this.selection.copy(selection);
    }

    public void post(String postText, Cursor postCursor, Cursor postSelection)
    {
        this.postText = postText;
        this.postCursor.copy(postCursor);
        this.postSelection.copy(postSelection);
    }

    public TextEditUndo ready()
    {
        this.ready = true;

        return this;
    }

    @Override
    public IUndo<GuiMultiTextElement> noMerging()
    {
        return this;
    }

    @Override
    public boolean isMergeable(IUndo<GuiMultiTextElement> undo)
    {
        return false;
    }

    @Override
    public void merge(IUndo<GuiMultiTextElement> undo)
    {}

    @Override
    public void undo(GuiMultiTextElement element)
    {
        element.cursor.copy(this.postCursor);
        element.selection.copy(this.postSelection);

        UndoType type = this.getType();

        if (type == UndoType.REPLACE || type == UndoType.INSERT)
        {
            element.selectTextful(this.postText, true);
            element.deleteSelection();
        }

        if (type == UndoType.REPLACE || type == UndoType.DELETE)
        {
            element.writeString(this.text);
        }

        element.cursor.copy(this.cursor);
        element.selection.copy(this.selection);
    }

    @Override
    public void redo(GuiMultiTextElement element)
    {
        element.cursor.copy(this.cursor);
        element.selection.copy(this.selection);

        UndoType type = this.getType();

        if (type == UndoType.REPLACE || type == UndoType.DELETE)
        {
            if (element.cursor.isGreater(element.selection))
            {
                element.swapSelection();
            }

            /* Handle delete key deletion */
            boolean backspace = this.cursor.line == this.postCursor.line && this.cursor.offset == this.postCursor.offset;

            for (int i = 0; i < this.text.length(); i++)
            {
                if (backspace)
                {
                    element.moveCursor(1, 0);
                }

                element.deleteCharacter();
            }
        }

        if (type == UndoType.REPLACE || type == UndoType.INSERT)
        {
            element.writeString(this.postText);
        }

        element.cursor.copy(this.postCursor);
        element.selection.copy(this.postSelection);
    }

    public UndoType getType()
    {
        if (!this.text.isEmpty() && this.postText.isEmpty())
        {
            return UndoType.DELETE;
        }
        else if (this.text.isEmpty() && !this.postText.isEmpty())
        {
            return UndoType.INSERT;
        }

        return UndoType.REPLACE;
    }

    public static enum UndoType
    {
        REPLACE, DELETE, INSERT
    }
}