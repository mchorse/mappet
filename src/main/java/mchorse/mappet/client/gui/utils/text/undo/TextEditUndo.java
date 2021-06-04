package mchorse.mappet.client.gui.utils.text.undo;

import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mappet.client.gui.utils.text.utils.Cursor;
import mchorse.mclib.utils.undo.IUndo;

public class TextEditUndo implements IUndo<GuiMultiTextElement>
{
    /**
     * Text that was present before undo
     */
    public String text;

    /* Cursor and selection before a text operation */
    public Cursor cursor = new Cursor(-1, 0);
    public Cursor selection = new Cursor(-1, 0);

    /**
     * Text that will be added
     */
    public String postText = "";

    /* Cursor and selection after a text operation */
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
        if (undo instanceof TextEditUndo)
        {
            TextEditUndo text = (TextEditUndo) undo;

            if (this.getType() == text.getType() && text.getType() != UndoType.REPLACE)
            {
                if (this.getType() == UndoType.INSERT)
                {
                    if (this.postCursor.line != text.postCursor.line)
                    {
                        return false;
                    }

                    if (text.cursor.offset != this.cursor.offset + this.postText.length())
                    {
                        return false;
                    }
                }

                if (this.getType() == UndoType.DELETE)
                {
                    if (this.postCursor.line != text.postCursor.line)
                    {
                        return false;
                    }

                    if (this.isBackspace() && text.isBackspace())
                    {
                        if (!this.cursor.isEqualTo(text.cursor))
                        {
                            return false;
                        }
                    }
                    else if (text.cursor.offset != this.cursor.offset - this.text.length())
                    {
                        return false;
                    }
                }

                return this.isBackspace() == text.isBackspace() && !text.wasSelecting();
            }
        }

        return false;
    }

    @Override
    public void merge(IUndo<GuiMultiTextElement> undo)
    {
        TextEditUndo text = (TextEditUndo) undo;

        if (text.getType() == UndoType.INSERT)
        {
            this.mergeInsert(text);
        }
        else if (text.getType() == UndoType.DELETE)
        {
            this.mergeDelete(text);
        }
    }

    private void mergeInsert(TextEditUndo text)
    {
        this.postCursor.copy(text.postCursor);
        this.postSelection.copy(text.postSelection);
        this.postText += text.postText;
    }

    private void mergeDelete(TextEditUndo text)
    {
        if (this.isBackspace())
        {
            this.postCursor.copy(text.postCursor);
            this.postSelection.copy(text.postSelection);
            this.text += text.text;
        }
        else
        {
            this.postCursor.copy(text.postCursor);
            this.postSelection.copy(text.postSelection);
            this.text = text.text + this.text;
        }
    }

    @Override
    public void undo(GuiMultiTextElement element)
    {
        element.cursor.copy(this.cursor);
        element.selection.copy(this.selection);

        UndoType type = this.getType();

        if (type == UndoType.REPLACE || type == UndoType.INSERT)
        {
            if (element.selection.isThisLessTo(element.cursor))
            {
                element.swapSelection();
            }

            element.selectTextful(this.postText, false);
            element.deleteSelection();
        }

        if (type == UndoType.REPLACE || type == UndoType.DELETE)
        {
            if (!this.wasSelecting())
            {
                element.cursor.copy(this.postCursor);
                element.selection.copy(this.postSelection);
            }

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
            if (element.cursor.isThisLessTo(element.selection))
            {
                element.swapSelection();
            }

            /* Handle delete key deletion */
            boolean backspace = this.isBackspace();

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

    public boolean isBackspace()
    {
        return this.getType() == UndoType.DELETE && this.cursor.isEqualTo(this.postCursor);
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

    public boolean wasSelecting()
    {
        return !this.selection.isEmpty();
    }

    public static enum UndoType
    {
        REPLACE, DELETE, INSERT
    }
}