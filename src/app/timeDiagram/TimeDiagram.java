package app.timeDiagram;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import app.Simulator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.JFrame;



public class TimeDiagram extends JFrame {

    private static final long serialVersionUID = 1L;

    public TimeDiagram() {
        super("Waveform Graph");
    }

    public JFreeChart getTimeDiagram(Simulator simulator) {

        // Create dataset
        DefaultXYDataset dataset = new DefaultXYDataset();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        CoordinateInfo coordinateInfo = simulator.getCoordinateInfo();

        Color sColor = Color.green;
        Color bColor = Color.magenta;
        Color dColor = Color.cyan;
        Color rejColor = Color.red;

        int j = 0;

        for (Map.Entry<String, List<Point>> entry : coordinateInfo.getMapPoint().entrySet()) {
            double[][] waveform = new double[2][entry.getValue().size()];
            for (int i = 0; i < entry.getValue().size(); i++) {
                waveform[0][i] = entry.getValue().get(i).getX();
                waveform[1][i] = entry.getValue().get(i).getY();
            }

            String seriesKey = entry.getKey();
            if (seriesKey.startsWith("S")) {
                renderer.setSeriesPaint(j, sColor);
            } else if (seriesKey.startsWith("B")) {
                renderer.setSeriesPaint(j, bColor);
            } else if (seriesKey.startsWith("D")) {
                renderer.setSeriesPaint(j, dColor);
            } else if (seriesKey.equals("Rej")) {
                renderer.setSeriesPaint(j, rejColor);
            }

            dataset.addSeries(seriesKey, waveform);
            j++;
        }

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Time",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Set background color
        chart.setBackgroundPaint(Color.white);

        // Get plot
        XYPlot plot = (XYPlot) chart.getPlot();

        // Set range axis
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, coordinateInfo.getMapScale().get("S1") + 1);

        for (XYTextAnnotation text : coordinateInfo.getListText()) {
            plot.addAnnotation(text);
        }
        // Set renderer
        plot.setRenderer(renderer);

        plot.setDomainPannable(true);
        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1024, 780));

        // Add chart panel to frame
        setContentPane(chartPanel);
        return chart;
    }
}
