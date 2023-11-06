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
      private double total_distance;
      private double prev_x;
      private double prev_y;
      private double _velocity;
      private double[] _velocity_set;
      private int _velocity_entries;
      private double _acceleration;
      private double[] _acceleration_set;
      private int _acceleration_entries;
      // Add velocity and acceleration later

      // Entry constructor
      public Entry(String id, double time, double x, double y) {
         _bodyID = id;
         _time = time;
         prev_x = x;
         prev_y = y;
         _distance = 0.0;
         total_distance = 0.0;
         _velocity = 0.0;
         _velocity_set = new double[4];
         _velocity_set[0] = Double.MAX_VALUE;
         _velocity_set[1] = Double.MIN_VALUE;
         _velocity_entries = 0;
         _acceleration = 0.0;
         _acceleration_set = new double[4];
         _acceleration_set[0] = Double.MAX_VALUE;
         _acceleration_set[1] = Double.MIN_VALUE;
         _acceleration_entries = 0;
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
         total_distance += _distance;
         prev_x = x;
         prev_y = y;
      }

      public void setVelocity(double x, double y) {

         _velocity = total_distance / _time;

         _velocity_set[0] = Double.min(_velocity, _velocity_set[0]);
         _velocity_set[1] = Double.max(_velocity, _velocity_set[1]);

         _velocity_entries++;

         _velocity_set[2] = (Double.sum(_velocity_set[0], _velocity_set[1]) / 2);

         // _velocity_set[2] = _velocity_sum / _velocity_entries;

         _velocity_set[3] = Math.sqrt(Math.pow(_velocity - _velocity_set[2], 2) / _velocity_entries);
      }

      public void setAcceleration() {

         _acceleration = _velocity / _time;

         _acceleration_set[0] = Double.min(_acceleration, _acceleration_set[0]);
         _acceleration_set[1] = Double.max(_acceleration, _acceleration_set[1]);

         _acceleration_entries++;

         _acceleration_set[2] = (Double.sum(_acceleration_set[0], _acceleration_set[1]) / 2);

         _acceleration_set[3] = Math.sqrt(Math.pow(_acceleration - _acceleration_set[2], 2) / _acceleration_entries);

      }

      public void print() {
         System.out.println("---------------");
         System.out.println("ID: " + _bodyID);
         System.out.println("Time: " + _time);
         System.out.println("Distance: " + total_distance);
         System.out.println("Velocity: ");
         System.out.println("   Min: " + _velocity_set[0]);
         System.out.println("   Max: " + _velocity_set[1]);
         System.out.println("   Avg: " + _velocity_set[2]);
         System.out.println("   STD: " + _velocity_set[3]);
         System.out.println("Acceleration: ");
         System.out.println(" Min: " + _acceleration_set[0]);
         System.out.println(" Max: " + _acceleration_set[1]);
         System.out.println(" Avg: " + _acceleration_set[2]);
         System.out.println(" STD: " + _acceleration_set[3]);
         System.out.println("---------------");
      }
   } // Entry Class End

   private ArrayList<Entry> entries;

   public Analyzer() {
      entries = new ArrayList<>();
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
            entries.get(i).setTime(time);
            entries.get(i).setDistance(x, y);
            entries.get(i).setVelocity(x, y);
            entries.get(i).setAcceleration();
            return;
         }
      }

      if (!exsits) {
         entries.add(new Entry(id, time, x, y));
      }
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void printResults() {
      System.out.println("<your metrics here for all bodies>");

      for (int i = 0; i < entries.size(); i++) {
         entries.get(i).print();
      }
   }
}
