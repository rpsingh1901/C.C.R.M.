package edu.ccrm.domain;

public enum Semester {
    SPRING(1), SUMMER(2), FALL(3);
    private final int order;
    Semester(int order) { this.order = order; }
    public int getOrder() { return order; }
}
