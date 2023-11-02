import javax.swing.SwingUtilities;

//=============================================================================================================================================================
//
// DO NOT CHANGE EXISTING CODE IN THIS CLASS EXCEPT FOR main() AND Task3(). YOU MAY ADD YOUR OWN CODE.
//
public class Task3 {
   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   // SELECT YOUR TEST AND ADJUST THE VIEWER HERE
   public static void main(final String[] arguments) {
      String testDesignator = "7"; // get these from the task description

      new Task3(testDesignator);
   }

   private Viewer _viewer;

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   // CONFIGURE YOUR TESTS HERE
   public Task3(final String testDesignator) {
      System.out.println("STARTING TEST " + testDesignator + "\n");

      double zoomFactor = 100; // higher values are closer to the bodies; do not change

      double timeStep = 0.1; // time step in seconds; do not change

      double runToTime = 50000; // how long to run in simulation seconds before terminating automatically;
      // collisions terminate at any time; set as needed below
      // Double.MAX_VALUE: <-- if you forget

      int frameDelay = 50; // milliseconds between viewer updates; you may change this depending on how
                           // well your machine handles the graphics

      Analyzer analyzer = new Analyzer();

      switch (testDesignator) {
         case "1.A": // this one is already done
         {
            Body bodyA = new Body("A", -150000, 0, 0, 0, 30000, true);
            Body bodyB = new Body("B", +150000, 0, 0, 0, 30000, true);

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyA, bodyB);

            break;
         }

         case "1.B": {

            Body bodyA = new Body("A", -150000, 0, 5, 0, 30000, true);
            Body bodyB = new Body("B", +150000, 0, -5, 0, 30000, true);

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyA, bodyB);

            break;
         }

         case "1.C": {

            Body bodyA = new Body("A", -150000, 0, -5, 0, 30000, true);
            Body bodyB = new Body("B", +150000, 0, 5, 0, 30000, true);

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyA, bodyB);

            break;
         }

         case "2.A": {

            Body bodyA = new Body("A", -150000, 39000, 5, 0, 30000, true);
            Body bodyB = new Body("B", +150000, 0, -5, 0, 30000, true);

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyA, bodyB);

            break;
         }

         case "2.B": {
            // add whatever you need
            Body bodyA = new Body("A", -150000, 33000, 10, 0, 30000, true);
            Body bodyB = new Body("B", +150000, 0, -10, 0, 30000, true);

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyA, bodyB);

            break;
         }

         case "3.A": {
            Body bodyEarth = new Body("EARTH", 0, 0, 0, 0, 100000, false);
            Body bodyRocket = new Body("ROCKET", 0, 55000, 19, 0, 1000, false);

            // add whatever you need

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyEarth, bodyRocket);

            break;
         }

         case "3.B": {
            // add whatever you need

            break;
         }

         case "3.C": {
            // add whatever you need

            break;
         }

         case "4": {
            // add whatever you need

            break;
         }

         case "5.A": {
            Body bodyEarth = new Body("EARTH", 0, 0, 0, 0, 100000, true);
            Body bodyRocket = new Body("ROCKET", 400000, 100000, 0, 0, 1000, true);

            // add whatever you need

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyEarth, bodyRocket);

            break;
         }

         case "5.B": {
            // add whatever you need

            break;
         }

         case "6.A": {
            // add whatever you need

            break;
         }

         case "6.B": {
            // add whatever you need

            break;
         }

         case "7": // grad students only
         {
            Body bodyEarth = new Body("EARTH", -50000, 0, 0, 0, 100000, true);
            Body bodyMoon = new Body("MOON", -50000, 400000, 7, 0, 40000, true);
            Body bodyRocket = new Body("ROCKET", -50000, 50500, 0, 23, 1000, true);

            // add whatever you need

            launch(zoomFactor, frameDelay, timeStep, runToTime, analyzer, bodyEarth, bodyMoon, bodyRocket);

            break;
         }

         default:

            throw new IllegalArgumentException("invalid test [" + testDesignator + "]");
      }

      analyzer.printResults();
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   // DO NOT TOUCH
   private void launch(final double zoomFactor,
         final int frameDelay,
         final double timeStep,
         final double runToTime,
         final Analyzer analyzer,
         final Body... bodies) {
      System.out.println("STARTING SIMULATOR\n");

      Simulator simulator = new Simulator(analyzer, timeStep, bodies);

      System.out.println("\nSTARTING VIEWER\n");

      _viewer = new Viewer(simulator, zoomFactor, frameDelay, bodies);

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            _viewer.launch();
         }
      });

      _viewer.refresh();

      double time = 0;

      boolean hasCollision = false;

      while ((time <= runToTime) && !hasCollision) {
         simulator.update();

         hasCollision = simulator.hasCollision();

         time = simulator.getTime();
      }

      simulator.shutdown();

      System.out.println("\nSHUTTING DOWN\n");
   }
}
