package mchorse.mappet.client.gui.scripts.utils;

import mchorse.mappet.client.gui.utils.text.TextLine;

import java.util.List;

public class HighlightedTextLine extends TextLine
{
    public List<TextSegment> segments;

    public HighlightedTextLine(String text)
    {
        super(text);
    }

    public void resetSegments()
    {
        this.segments = null;
    }

    public void setSegments(List<TextSegment> segments)
    {
        this.segments = segments;
    }
}