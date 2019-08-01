package com.chaosbuffalo.mkultra.client.gui;

public class MKStackLayoutVertical extends MKLayout {

    private int currentY;

    public MKStackLayoutVertical(int x, int y, int width) {
        super(x, y, width, 0);
        currentY = y;
    }

    @Override
    public void setupLayoutStartState(){
        currentY = getY();
        currentY += getMarginTop();
    }

    @Override
    public void doLayout(MKWidget widget, int index){
        widget.setY(currentY);
        widget.setX(getX() + getMarginLeft() + (int) ((getWidth() - getMarginLeft() - getMarginRight()) * widget.getPosHintX()));
        widget.setWidth((int)((getWidth()  - getMarginRight() - getMarginLeft()) * widget.getSizeHintWidth()));
        currentY += widget.getHeight() + getPaddingBot() + getPaddingTop();
    }

    @Override
    public void addWidget(MKWidget widget){
        super.addWidget(widget);
        setHeight(getHeight() + widget.getHeight() + getPaddingTop() + getPaddingBot());
    }

    @Override
    public void removeWidget(MKWidget widget){
        super.removeWidget(widget);
        setHeight(getHeight() - widget.getHeight() - getPaddingTop() - getPaddingBot());
    }

    @Override
    public MKLayout setMarginTop(int value) {
        super.setMarginTop(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setMarginBot(int value) {
        super.setMarginBot(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setPaddingBot(int value) {
        super.setPaddingBot(value);
        recomputeChildren();
        return this;
    }

    @Override
    public MKLayout setPaddingTop(int value) {
        super.setPaddingTop(value);
        recomputeChildren();
        return this;
    }

    @Override
    public void recomputeChildren() {
        int height = getMarginTop();
        int i = 0;
        for (MKWidget child : children){
            height += child.getHeight();
            if (i != 0){
                height += getPaddingTop();
            }
            if (i != children.size() - 1){
                height += getPaddingBot();
            }
            i++;
        }
        height += getMarginBot();
        setHeight(height);
    }
}
