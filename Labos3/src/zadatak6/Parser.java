package zadatak6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private List<Point> vertices;
    private List<ArrayList<Point>> polygons;
    private List<List<Integer>> order;


    public List<Point> parse(String string) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(new File(string)));
        List<String> verticesList = br.lines().filter(l -> l.startsWith("v")).collect(Collectors.toList());

        List<Point> vertices = new ArrayList<>();
        for( String v : verticesList) {
            String[] parts = v.split(" ");
            Point p = new Point(Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
            vertices.add(p);
        }
        return vertices;
    }

    public void parsePolygon (String string) throws FileNotFoundException {
        BufferedReader br1 = new BufferedReader(new FileReader(new File(string)));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(string)));
        List<String> verticesList = br1.lines().filter(l -> l.startsWith("v")).collect(Collectors.toList());
        List<String> polygonList = br2.lines().filter(l -> l.startsWith("f")).collect(Collectors.toList());

        this.vertices = new ArrayList<>();
        for( String v : verticesList) {
            String[] parts = v.split(" ");
            Point p = new Point(Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
            vertices.add(p);
        }
        this.polygons = new ArrayList<>();
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
            ArrayList<Point> l = new ArrayList<>();
            l.add(vertices.get(v1-1));
            l.add(vertices.get(v2-1));
            l.add(vertices.get(v3-1));

            polygons.add(l);
            order.add(v);
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
}
