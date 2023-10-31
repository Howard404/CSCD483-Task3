import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//=============================================================================================================================================================
//
// DO NOT CHANGE THIS CLASS EXCEPT FOR ENABLE_LOGGING
//
public class Simulator {
   private static final String LOG_FILESPEC = "task3.log";

   private static final boolean ENABLE_LOGGING = false; // you may change this

   private static boolean _isFirstUpdate = true;

   private final double _timeStep;

   private long _step = 0;

   private double _time = 0;

   private int _numBodies;

   private final Body _bodies[];

   private final Analyzer _analyzer;

   private boolean _hasCollision = false;

   private BufferedWriter _outfile;

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public Simulator(final Analyzer analyzer, final double timeStep, final Body... bodies) {
      if (analyzer == null) {
         throw new NullPointerException("analyzer");
      }

      _analyzer = analyzer;

      if (timeStep <= 0) {
         throw new IllegalArgumentException("illegal time step: " + timeStep);
      }

      _timeStep = timeStep;

      if (bodies == null) {
         throw new NullPointerException("bodies");
      }

      _numBodies = bodies.length;

      if (_numBodies == 0) {
         throw new IllegalArgumentException("illegal body count: " + _numBodies);
      }

      // xxx should check for duplicates

      _bodies = bodies;

      for (Body body : _bodies) {
         System.out.println(" ADDED BODY [" + body.getID() + "]");
      }

      try {
         _outfile = new BufferedWriter(new FileWriter(LOG_FILESPEC));
      } catch (IOException exception) {
         System.err.println("CANNOT CREATE LOG FILE: " + exception);

         System.exit(-1);
      }
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   private void doCollisionDetection() {
      for (int iBody1 = 0; iBody1 < _numBodies; iBody1++) {
         for (int iBody2 = 0; iBody2 < _numBodies; iBody2++) // not optimal
         {
            if (iBody1 != iBody2) {
               Body body1 = _bodies[iBody1];
               Body body2 = _bodies[iBody2];

               if (body1.canCollide() || body2.canCollide()) {
                  double distanceFromCenters = body1.calculateDistance(body2);

                  double distanceFromPerimeters = (distanceFromCenters - (body1.getDiameter() / 2)
                        - (body2.getDiameter() / 2));

                  boolean isCollision = (distanceFromPerimeters <= 0);

                  if (isCollision) {
                     System.out.println("COLLISION BETWEEN " + body1.getID() + " AND " + body2.getID());

                     _hasCollision = true;
                  }
               }
            }
         }
      }
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public long getStep() {
      return _step;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public double getTime() {
      return _time;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public boolean hasCollision() {
      return _hasCollision;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void shutdown() {
      try {
         _outfile.close();
      } catch (IOException exception) {
         System.err.println("CANNOT CLOSE LOG FILE: " + exception);
      }
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public void update() {
      try {
         if (_isFirstUpdate) {
            _isFirstUpdate = false;

            _outfile.write("step,time,id,x,y,vx,vy,fx,fy,mass,diameter");
            _outfile.newLine();
         }

         for (int iBody1 = 0; iBody1 < _numBodies; iBody1++) {
            Body body1 = _bodies[iBody1];

            body1.resetForce();

            for (int iBody2 = 0; iBody2 < _numBodies; iBody2++) {
               if (iBody1 != iBody2) {
                  body1.addForce(_bodies[iBody2]);
               }
            }
         }

         for (int iBody = 0; iBody < _numBodies; iBody++) {
            Body body = _bodies[iBody];

            body.update(_timeStep);

            if (ENABLE_LOGGING) {
               _outfile.write(_step + "," + _time + "," + body);
               _outfile.newLine();
            }

            Body.Pair position = body.getPosition();

            _analyzer.addEntry(_step, _time, body.getID(), position.getX(), position.getY());
         }
      } catch (IOException exception) {
         System.err.println("CANNOT WRITE TO LOG FILE: " + exception);
      }

      doCollisionDetection();

      _step++;

      _time += _timeStep;
   }
}
