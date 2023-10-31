import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

// =============================================================================================================================================================
//
// DO NOT CHANGE THIS CLASS
//
// recordmydesktop -x 1920 --width 1000 --height 1000
//
public class Viewer extends TimerTask
{
   // =========================================================================================================================================================
   private class Display extends JPanel
   {
      // ------------------------------------------------------------------------------------------------------------------------------------------------------
      @Override
      public Dimension getPreferredSize()
      {
         return new Dimension(FRAME_SIZE, FRAME_SIZE);
      }

      // ------------------------------------------------------------------------------------------------------------------------------------------------------
      @Override
      protected void paintComponent(final Graphics graphics)
      {
         super.paintComponent(graphics);

         for (Body body : _bodies)
         {
            String id = body.getID();

            List<Point> track;

            if (ENABLE_TRACK)
            {
               if (_tracks.containsKey(id))
               {
                  track = _tracks.get(id);
               }
               else
               {
                  track = new ArrayList<>();

                  _tracks.put(id, track);
               }
            }

            Body.Pair positionWorld = body.getPosition();

            Point positionScreen = convertFromWorldToScreenCoordinates(positionWorld.getX(), positionWorld.getY());

            double diameterWorld = body.getDiameter();

            int diameterScreen = (convertFromWorldToScreenCoordinates(diameterWorld, 0).x - (FRAME_SIZE / 2));

            int radiusScreen = (diameterScreen / 2);

            graphics.drawString(id, (positionScreen.x - radiusScreen), (positionScreen.y - radiusScreen));

            graphics.fillOval((positionScreen.x - radiusScreen), (positionScreen.y - radiusScreen), diameterScreen, diameterScreen);

            if (ENABLE_TRACK)
            {
               track.add(positionScreen);

               Point breadcrumbPrev = track.get(0);

               for (Point breadcrumb : track)
               {
                  graphics.drawLine(breadcrumbPrev.x, breadcrumbPrev.y, breadcrumb.x, breadcrumb.y);

                  breadcrumbPrev = breadcrumb;
               }
            }
         }

         int time = (int) _simulator.getTime();

         graphics.drawString(("Time: " + time), 10, 20);
      }
   }

   private static final double METERS_PER_KILOMETER = 1000;

   private static final double WORLD_EXTENT = (100000 * METERS_PER_KILOMETER); // meters

   private static final boolean ENABLE_TRACK = true;

   private final int FRAME_SIZE = 800;

   private final Display _display = new Display();

   private final Timer _timer = new Timer();

   private final double _zoomFactor;

   private final int _frameDelay;

   private final Body _bodies[];

   private final Simulator _simulator;

   private final Map<String, List<Point>> _tracks = new HashMap<>();

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public Viewer(final Simulator simulator, final double zoomFactor, final int frameDelay, final Body... bodies)
   {
      if (simulator == null)
      {
         throw new NullPointerException("simulator");
      }

      _simulator = simulator;

      if (zoomFactor <= 0)
      {
         throw new IllegalArgumentException("illegal zoom factor: " + zoomFactor);
      }

      _zoomFactor = zoomFactor;

      if (frameDelay < 1)
      {
         throw new IllegalArgumentException("illegal frame delay: " + frameDelay);
      }

      _frameDelay = frameDelay;

      if (bodies == null)
      {
         throw new NullPointerException("bodies");
      }

      if (bodies.length == 0)
      {
         throw new IllegalArgumentException("illegal body count: " + bodies.length);
      }

      _bodies = bodies;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   private Point convertFromWorldToScreenCoordinates(final double worldX, final double worldY)
   {
      double scaleFactor = ((FRAME_SIZE / WORLD_EXTENT) * _zoomFactor);

      double translation = (FRAME_SIZE / 2);

      int screenX = (int) Math.round((worldX * scaleFactor) + translation);
      int screenY = (int) Math.round((-worldY * scaleFactor) + translation);

      Point coordinatesScreen = new Point(screenX, screenY);

      return coordinatesScreen;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void launch()
   {
      JFrame frame = new JFrame("Task 3");

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.add(_display);
      frame.pack();
      frame.setVisible(true);

      _timer.scheduleAtFixedRate(this, 0, _frameDelay);
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   @Override
   public void run()
   {
     refresh();
   }
      
   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void refresh()
   {
      _display.repaint();
   }
}