package edu.ccrm.domain;

import java.util.Objects;

// Immutable value class example
public final class Name {
    private final String first;
    private final String middle;
    private final String last;

    public Name(String first, String middle, String last) {
        this.first = first == null ? "" : first;
        this.middle = middle == null ? "" : middle;
        this.last = last == null ? "" : last;
    }

    public static Name parseFull(String full) {
        if (full == null || full.isBlank()) return new Name("", "", "");
        String[] parts = full.trim().split("\\s+");
        if (parts.length == 1) return new Name(parts[0], "", "");
        if (parts.length == 2) return new Name(parts[0], "", parts[1]);
        StringBuilder mid = new StringBuilder();
        for (int i = 1; i < parts.length - 1; i++) { if (i>1) mid.append(' '); mid.append(parts[i]); }
        return new Name(parts[0], mid.toString(), parts[parts.length-1]);
    }

    public String first() { return first; }
    public String middle() { return middle; }
    public String last() { return last; }
    public String full() {
        StringBuilder sb = new StringBuilder();
        if (!first.isBlank()) sb.append(first);
        if (!middle.isBlank()) { if (sb.length()>0) sb.append(' '); sb.append(middle); }
        if (!last.isBlank()) { if (sb.length()>0) sb.append(' '); sb.append(last); }
        return sb.toString();
    }

    @Override public String toString() { return full(); }
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Name)) return false; Name n=(Name)o; return Objects.equals(first,n.first)&&Objects.equals(middle,n.middle)&&Objects.equals(last,n.last); }
    @Override public int hashCode() { return Objects.hash(first,middle,last); }
}
