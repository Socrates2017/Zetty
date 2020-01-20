package com.zrzhen.sqlgraph.util.poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;

/**
 * @author chenanlian
 */
public class Grid {
    private boolean show;
    // 对应Excel中的row,也可以理解为cells[i][j]的i
    private int row;
    // 对应Excel中的col,也可以理解为cells[i][j]的j
    private int col;
    // x坐标
    private int x;
    // y坐标
    private int y;
    private int width;
    private int height;
    private String text;
    private Font font;
    //= new Font("微软雅黑", Font.PLAIN, 12);
    private Color bgColor = null;
    private Color ftColor = null;

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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getBgColor() {
        return bgColor;
    }

    /**
     * 将poi.ss.usermodel.Color 转换成 java.awt.Color
     * <a href="http://home.cnblogs.com/u/309701/" target="_blank">@param</a>
     * color
     */
    public void setBgColor(org.apache.poi.ss.usermodel.Color color) {
        this.bgColor = poiColor2awtColor(color);
    }

    public void setBgColor(Color color) {
        this.bgColor = color;
    }

    public Color getFtColor() {
        return ftColor;
    }

    public void setFtColor(org.apache.poi.ss.usermodel.Color color) {
        this.ftColor = poiColor2awtColor(color);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(org.apache.poi.ss.usermodel.Font font) {
        if (font != null) {
            this.font = new Font(font.getFontName(), Font.BOLD, font.getFontHeight() / 20 + 2);
        }
    }

    private Color poiColor2awtColor(org.apache.poi.ss.usermodel.Color color) {
        Color awtColor = null;
        if (color instanceof XSSFColor) { // .xlsx
            XSSFColor xc = (XSSFColor) color;
            String rgbHex = xc.getARGBHex();
            if (rgbHex != null) {
                awtColor = new Color(Integer.parseInt(rgbHex.substring(2), 16));
            }
        } else if (color instanceof HSSFColor) { // .xls
            HSSFColor hc = (HSSFColor) color;
            short[] s = hc.getTriplet();
            if (s != null) {
                awtColor = new Color(s[0], s[1], s[2]);
            }
        }
        return awtColor;
    }
}

