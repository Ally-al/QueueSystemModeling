package app.timeDiagram;

import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.annotations.XYTextAnnotation;

import java.util.*;

@Getter
@Setter
public class CoordinateInfo {

    private final Map<String, List<Point>> mapPoint = new LinkedHashMap<>();
    private final Map<String, Integer> mapScale = new HashMap<>();
    private final List<XYTextAnnotation> listText = new ArrayList<>();

    public CoordinateInfo(int countSource, int countBuffer, int countDevice) {
        int maxY = countBuffer + countDevice + countSource;
        for (int i = 1; i <= countSource; i++) {
            String sourceId = "S" + i;
            mapPoint.put(sourceId, new LinkedList<>());
            mapScale.put(sourceId, maxY--);

            mapPoint.get(sourceId).add(new Point(0.0, mapScale.get(sourceId)));
        }
        for (int i = 1; i <= countBuffer; i++) {
            String bufferId = "B" + i;
            mapPoint.put(bufferId, new LinkedList<>());
            mapScale.put(bufferId, maxY--);

            mapPoint.get(bufferId).add(new Point(0.0, mapScale.get(bufferId)));
        }
        for (int i = 1; i <= countDevice; i++) {
            String deviceId = "D" + i;
            mapPoint.put(deviceId, new LinkedList<>());
            mapScale.put(deviceId, maxY--);

            mapPoint.get(deviceId).add(new Point(0.0, mapScale.get(deviceId)));
        }
        mapPoint.put("Rej", new LinkedList<>());
        mapScale.put("Rej", 0);

        mapPoint.get("Rej").add(new Point(0.0, 0.0));
    }

    public void addImpulse(String id, double x, String text) {
        int coorY = mapScale.get(id);
        mapPoint.get(id).add(new Point(x, coorY));
        mapPoint.get(id).add(new Point(x, coorY + 0.5));
        mapPoint.get(id).add(new Point(x, coorY));

        listText.add(new XYTextAnnotation(text, x + 0.02, coorY + 0.3));
    }

    public void upImpulse(String id, double x, String text) {
        int coorY = mapScale.get(id);
        mapPoint.get(id).add(new Point(x, coorY));
        mapPoint.get(id).add(new Point(x, coorY + 0.5));

        listText.add(new XYTextAnnotation(text, x + 0.02, coorY + 0.3));
    }

    public void downImpulse(String id, double x, String text) {
        int coorY = mapScale.get(id);
        mapPoint.get(id).add(new Point(x, coorY + 0.5));
        mapPoint.get(id).add(new Point(x, coorY ));

//        mapPoint.get(id).add(new Point(x, coorY + 0.5));
//        mapPoint.get(id).add(new Point(x, coorY));
        //listText.add(new XYTextAnnotation(text, x, coorY + 0.2));
    }
}
