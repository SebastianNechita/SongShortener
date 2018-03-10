package sample;

import java.util.List;

class Verse {
    private int id = -1;
    private List<String> lines;

    Verse(List<String> lines) {
        this.lines = lines;
    }

    void addLine(String s) {
        lines.add(s);
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String s2 : lines) {
            s.append(s2).append("\n");
        }
        return s.toString();
    }
}
