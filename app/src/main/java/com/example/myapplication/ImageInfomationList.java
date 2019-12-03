package com.example.myapplication;

public class ImageInfomationList {

    private int backGroundColor; //배경색
    private int figure; //도형
    private int figureColor; //도형색

    ImageInfomationList(int backGroundColor,int figure,int figureColor){
        this.backGroundColor=backGroundColor;
        this.figure=figure;
        this.figureColor=figureColor;
    }

    public int getBackGroundColor(){
        return backGroundColor;
    }

    public int getFigure(){
        return figure;
    }

    public int getFigureColor(){
        return figureColor;
    }
    //어차피 값 바꿀일없으니 안쓰임 혹시모르니 걍 만들어놓음
    public void setBackGroundColor(int backGroundColor){
        this.backGroundColor=backGroundColor;
    }
    public void setFigure(int figure){
        this.figure=figure;
    }
    public void setFigureColor(int figureColor){
        this.figureColor=figureColor;
    }

}
