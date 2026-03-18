package org.example;

class Chess {
    String name;
    int weight;
    int side;
    int location;
    boolean isOpened = false;

    Chess(String name, int weight, int side, int location) {
        this.name = name;
        this.weight = weight;
        this.side = side;
        this.location = location;
    }

    public String toString() {
        return isOpened ? " " + name : " X ";
    }
}