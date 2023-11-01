//=============================================================================================================================================================
//
// IMPLEMENT THIS CLASS HOWEVER YOU WANT, BUT DO NOT CHANGE THE SIGNATURES OF Analyzer(), addEntry(), or printResults(). YOU MUST USE addEntry() TO GET THE
// DATA; DO NOT ACCESS OTHER CLASSES IN THE PROJECT (UNLESS THEY ARE YOURS).
//

import java.util.ArrayList;

public class Analyzer {
   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   // private String _body; // Body
   // ArrayList<Body> _bodies;
   // private double _time; // Total time
   // private double _distance; // Total distance
   // private double prev_x; // Previous x value
   // private double prev_y; // Previous y value
   // private double _velocity;
   // private double[] _velocity_metrics;

   // Entry class
   public class Entry {
      private String _bodyID;
      private double _time;
      private double _distance;
      private double prev_x;
      private double prev_y;
      public double _velocity;
      public double prev_velocity;
      public double[] _velocity_set;
      public int _velocity_sum;
      public double _acceleration;
      public double[] _acceleration_set;
      // Add velocity and acceleration later

      // Entry constructor
      public Entry(String id, double time, double x, double y) {
         _bodyID = id;
         _time = time;
         prev_x = x;
         prev_y = y;
         _distance = 0.0;
         _velocity = 0.0;
         _velocity_set = new double[4];
         _velocity_sum = 0;
         prev_velocity = 0.0;
         _acceleration = 0.0;
         _acceleration_set = new double[4];
      }

      // Getter for Body ID
      public String getBodyId() {
         return _bodyID;
      }

      // Setters for fields
      public void setTime(double time) {
         _time = time;
      }

      public void setDistance(double x, double y) {
         _distance = Math.sqrt(Math.pow(Math.abs(prev_x - x), 2) + Math.pow(Math.abs(prev_y - y), 2));
         prev_x = x;
         prev_y = y;
      }

      public void setVelocity(double x, double y, double timeStep) {
         // double v_x = (prev_x - x) / timeStep;
         // double v_y = (prev_y - x) / timeStep;

         // _velocity = Math.sqrt(Math.pow(v_x, 2) + Math.pow(v_y, 2));

         double deltaSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

         double v_x = (deltaSpeed / _distance) * (x - prev_x);
         double v_y = (deltaSpeed / _distance) * (y - prev_y);

         if (_velocity < _velocity_set[0]) {
            _velocity_set[0] = _velocity; // Minimum Velocity
         }

         if (_velocity > _velocity_set[1]) {
            _velocity_set[1] = _velocity; // Maximum Velocity
         }

         _velocity_sum++;

         _velocity_set[2] = (_velocity_set[0] + _velocity_set[1]) / 2;

         _velocity_set[3] = Math.sqrt(Math.pow(_velocity - _velocity_set[2], 2) / _velocity_sum);
      }

      // public void setAcceleration() {
      // _acceleration = _velocity - prev_velocity / _time;

      // if (_acceleration < _acceleration_set[0]) {
      // _acceleration_set[0] = _acceleration;
      // }

      // if (_acceleration > _acceleration_set[1]) {
      // _acceleration_set[1] = _acceleration;
      // }

      // _acceleration_set[2] = (_acceleration_set[0] + _acceleration_set[1]) / 2;

      // }

      public void print() {
         System.out.println("---------------");
         System.out.println("ID: " + _bodyID);
         System.out.println("Time: " + _time);
         System.out.println("Distance: " + _distance);
         System.out.println("Velocity: ");
         System.out.println("   Min: " + _velocity_set[0]);
         System.out.println("   Max: " + _velocity_set[1]);
         System.out.println("   Avg: " + _velocity);
         System.out.println("   STD: " + _velocity_set[3]);
         System.out.println("Acceleration: ");
         System.out.println("   Min: " + _acceleration_set[0]);
         System.out.println("   Max: " + _acceleration_set[1]);
         System.out.println("   Avg: " + _acceleration);
         System.out.println("---------------");
      }
   }

   private ArrayList<Entry> entries = new ArrayList();

   public Analyzer() {
      // _bodies = new ArrayList<>();
      // _time = 0.0;
      // _body = "";
      // _distance = 0.0;
      // prev_x = 0.0;
      // prev_y = 0.0;
      // _velocity_metrics = new double[3];
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void addEntry(final long step, final double time, final String id, final double x, final double y) {
      System.out.println("<log>: " + step + "," + time + "," + id + "," + x + "," + y);

      // Checks if entry exsits
      boolean exsits = false;

      // If ID is not in entries, create new entry
      if (entries.size() == 0) {
         entries.add(new Entry(id, time, x, y));
      }

      for (int i = 0; i < entries.size(); i++) {
         if (entries.get(i).getBodyId() == id) {
            exsits = true;
            entries.get(i).setTime(time);
            entries.get(i).setDistance(x, y);
            entries.get(i).setVelocity(x, y, step);
            // entries.get(i).setAcceleration();
            break;
         }
      }

      if (exsits == false) {
         entries.add(new Entry(id, time, x, y));
      }

      // _body = id;
      // _time = time;
      // _distance = Math.sqrt(Math.pow(Math.abs(x - prev_x), 2) + Math.pow(Math.abs(y
      // - prev_y), 2));
      // prev_x = x;
      // prev_y = y;

      // _velocity = _distance / time;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void printResults() {
      System.out.println("<your metrics here for all bodies>");

      for (int i = 0; i < entries.size(); i++) {
         entries.get(i).print();
      }

      // System.out.println("Body: " + _body);
      // System.out.println("Time: " + _time);
      // System.out.println("Distance: " + _distance);
      // System.out.println("----- Velocity -----");
      // System.out.println("avg: " + _velocity);
   }
}
