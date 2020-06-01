package zadatak7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private List<Point> vertices = new ArrayList<>();
    private List<ArrayList<Point>> polygons = new ArrayList<>();
    private List<List<Integer>> order;
    private Point viewPoint;
    private Point direction;

    public void parse(String string) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(string)));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(string)));
        List<String> polygonList = br2.lines().filter(l -> l.startsWith("f")).collect(Collectors.toList());

        String line;
        while((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            if(parts[0].equals("O") || parts[0].equals("G") || parts[0].equals("v")) {
                Point point = new Point(Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
                switch (parts[0]) {
                    case "O" : this.viewPoint = point; break;
                    case "G" : this.direction = point; break;
                    case "v" : this.vertices.add(point); break;
                }
            }
        }
        this.order = new ArrayList<>();
        for( String f : polygonList) {
            List<Integer> v = new ArrayList<>();
            String[] parts = f.split(" ");
            int v1 = Integer.valueOf(parts[1]);
            v.add(v1);
            int v2 = Integer.valueOf(parts[2]);
            v.add(v2);
            int v3 = Integer.valueOf(parts[3]);
            v.add(v3);

            this.order.add(v);

            ArrayList<Point> l = new ArrayList<>();
            l.add(vertices.get(v1-1));
            l.add(vertices.get(v2-1));
            l.add(vertices.get(v3-1));

            polygons.add(l);
        }
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public List<ArrayList<Point>> getPolygons() {
        return polygons;
    }

    public List<List<Integer>> getOrder() {
        return order;
    }

    public Point getViewPoint() {
        return viewPoint;
    }

    public void setViewPoint(Point viewPoint) {
        this.viewPoint = viewPoint;
    }

    public Point getDirection() {
        return direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }
}
