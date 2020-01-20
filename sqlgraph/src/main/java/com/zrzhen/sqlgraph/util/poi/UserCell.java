package com.zrzhen.sqlgraph.util.poi;

import org.apache.poi.ss.usermodel.Cell;

import java.awt.*;

/**
 * @author chenanlian
 */
public class UserCell {
    private Cell cell;
    private int row;
    private int col;
    private boolean show;
    private String text = "";
    private Color color = null;

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
