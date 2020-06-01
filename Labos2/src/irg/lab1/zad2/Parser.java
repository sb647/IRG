package irg.lab1.zad2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private List<PointDouble> vertices;
    private List<ArrayList<PointDouble>>  polygons;


    public void parse(String string) throws FileNotFoundException {
        BufferedReader br1 = new BufferedReader(new FileReader(new File(string)));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(string)));
        List<String> verticesList = br1.lines().filter(l -> l.startsWith("v")).collect(Collectors.toList());
        List<String> polygonList = br2.lines().filter(l -> l.startsWith("f")).collect(Collectors.toList());

        this.vertices = new ArrayList<>();
        for( String v : verticesList) {
            String[] parts = v.split(" ");
            PointDouble p = new PointDouble(Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
            vertices.add(p);
        }
        this.polygons = new ArrayList<>();
        for( String f : polygonList) {
            String[] parts = f.split(" ");
            int v1 = Integer.valueOf(parts[1]);
            int v2 = Integer.valueOf(parts[2]);
            int v3 = Integer.valueOf(parts[3]);

            ArrayList<PointDouble> l = new ArrayList<>();
            l.add(vertices.get(v1-1));
            l.add(vertices.get(v2-1));
            l.add(vertices.get(v3-1));

            polygons.add(l);
       }
    }

    public List<PointDouble> getVertices() {
        return vertices;
    }

    public List<ArrayList<PointDouble>> getPolygons() {
        return polygons;
    }
}