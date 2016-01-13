package com.trapasoft.android.tension;

import android.app.Activity;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends Activity {

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicializar la referencia a XYPlot
        plot = (XYPlot) findViewById(R.id.plot);

        // crear arrays de puntos para ejemplo
        Number[] datosAlta = {123, 145, 151, 162, 180, 177, 156, 144, 124, 132};
        Number[] datosBaja = { 65,  67,  73,  67,  57,  67,  65,  62,  60, 61};
        String[] fechas = { "21/11/2015 07:34",
                            "21/11/2015 19:15",
                            "22/11/2015 08:20",
                            "22/11/2015 21:10",
                            "23/11/2015 06:12",
                            "23/11/2015 19:15",
                            "24/11/2015 06:23",
                            "24/11/2015 23:02",
                            "25/11/2015 08:12",
                            "25/11/2015 22:45"
                        };

        // crear array de numeros a partir de las fechas
        Number[] numFechas = new Number[datosAlta.length];
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // bucle que va convirtiendo cada fecha en su numero de milisegundos desde Epoch
        for (int i=0; i < datosAlta.length; i++) {
            Date date = null;

            try {
                date = format.parse(fechas[i]);
                numFechas[i] = date.getTime();
                Log.d("FECHAS: ", numFechas[i].toString());
                // prueba: volver a convertirlo con un simpledateformat
                Log.d("FECHAS:    ", new Date(numFechas[i].longValue()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        // convertir los arrays de puntos en series
        //public SimpleXYSeries(List<? extends Number> xVals,
        //        List<? extends Number> yVals,
        //        String title)
        XYSeries serieAlta = new SimpleXYSeries( Arrays.asList(numFechas),
                Arrays.asList(datosAlta),
                "Sist.");

        XYSeries serieBaja = new SimpleXYSeries(Arrays.asList(numFechas),
                Arrays.asList(datosBaja),
                 "Diast");

        // crear formateadores para usarlos para dibujar una serie con LineAndPointRenderer
        // y configurarlos a partir del xml:
        LineAndPointFormatter altaFormat = new LineAndPointFormatter();
        altaFormat.setPointLabelFormatter(new PointLabelFormatter());
        altaFormat.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter bajaFormat = new LineAndPointFormatter();
        bajaFormat.setPointLabelFormatter(new PointLabelFormatter());
        bajaFormat.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels_2);

        // agregar un efecto de "punteado" a la linea de la serie 2
        bajaFormat.getLinePaint().setPathEffect(
                new DashPathEffect(new float[]{
                        // usar siempre DP al especificar tamaños en pixels, para mantener
                        // la consistencia entre dispositivos
                        PixelUtils.dpToPix(20),
                        PixelUtils.dpToPix(15)}, 0));
        // añadir algo de suavizado a las lineas
        // ver: http://androidplot.com/smooth-curves-and-androidplot/
        altaFormat.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        bajaFormat.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // añadir las series al grafico
        plot.addSeries(serieAlta, altaFormat);
        plot.addSeries(serieBaja, bajaFormat);

        // reducir el numero de etiquetas por rango
        plot.setTicksPerRangeLabel(3);

        // crear un formato de tipo fecha para gestionar las etiquetas para las fechas/horas/minutos
        plot.setDomainValueFormat(new Format() {

            // el formato que se mostrara: "22/11/2015 08:20" --> "1122 08"
            private SimpleDateFormat formato = new SimpleDateFormat("MMdd HH");
            //private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            @Override
            public StringBuffer format(Object o, StringBuffer toAppendTo, FieldPosition pos) {
                // tenemos hasta minutos segundos, pero SimpleDateFormat espera milisegundos: multiplicamos por mil
                //long timestamp = ((Number) o).longValue() * 60 * 1000;
                // a ver si no hay que multiplicarlo por nada
                long timestamp = ((Number) o).longValue();
                Date date = new Date(timestamp);
                return formato.format(date,toAppendTo,pos);
            }

            @Override
            public Object parseObject(String s, ParsePosition pos) {
                return null;
            }
        });
        // rotar las etiquetas de dominio 45º para compactarlas horizontalmente
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }


}
