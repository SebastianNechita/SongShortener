package sample;

import java.util.List;

public class Verse {
    int id = -1;
    List<String> lines;

    public Verse(List<String> lines) {
        this.lines = lines;
    }

    public void addLine(String s){
        lines.add(s);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        String s = "";
        for(String s2 : lines){
            s += s2 + "\n";
        }
        return s;
    }
}
