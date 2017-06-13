package com.nikitayankov.grappstriangle;

public class Triangle {
    Point a = new Point(0f,   100f);
    Point b = new Point(100f, 0f);
    Point c = new Point(200f, 100f);

    public Triangle() {

    }

    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Point[] getPoints() {
        return new Point[]{a, b, c};
    }
}
