import java.util.*;
class Plane {
    private int id;
    private int fuelAmount;
    private int queueTime;
    /**
     * @param id id of plane
     * @param fuelAmount fuel of plane
     * @param queueTime  the queue time of the plane.
     */

    public Plane(int id, int fuelAmount) {
        this.id = id;
        this.fuelAmount = fuelAmount;
        this.queueTime = 0;
    }

    /**
     * @return The ID of the plane.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The fuel amount of the plane.
     */
    public int getFuelAmount() {
        return fuelAmount;
    }

    /**
     * @return The queue time of the plane.
     */
    public int getQueueTime() {
        return queueTime;
    }

    /**
     * Decrements the fuel amount of plane.
     */
    public void decrementFuel() {
        if (fuelAmount > 0) {
            fuelAmount--;
        }
    }

    /**
     * Increments the queue time of plane.
     */
    public void incrementQueueTime() {
        queueTime++;
    }
}
public class Airport {


    Queue<Plane> departing = new LinkedList<>();
    Queue<Plane> departed = new LinkedList<>();
    Queue<Plane> notEnoughFuel = new LinkedList<>();
    Plane plane;

    int departingQueueTime;
    int fuel;

    int totalPlanes;
    final int fuelNeededForFlight = 100;
    final int simulationTime = 50;

    public Airport() {
        departingQueueTime = 3;
        fuel = 150;
    }
    /**
            * creates new plane with the given ID, adds new plane to the departing queue.
           * Increments totalPlanes
            * @param id The ID of the new plane.
     */
    void startNewPlane(int id) {
        Plane newPlane = new Plane(id, fuel);
        departing.offer(newPlane);

        totalPlanes++;
    }
    /**
     * Checks if the runway is empty.
     */
    boolean isRunwayEmpty() {

        return plane == null;
    }
    /**
     * Removes plane from runway
     */
    void removePlaneFromRunway() {

        plane = null;
    }
    /**
     * Goes through every plane in the departing queue and decrements the fuel for all of them
     */
    void decrementFuelOfAllPlanes() {

        for (Plane p : departing) {
            p.decrementFuel();
        }
    }

    /**
     * Processes the departiing queue, it places planes on the runway if it conditions are met
     * If the runway is empty, it selects the next plane from the departing queue and checks its fuel level.
     * If the selected plane has enough fuel, it is moved to the runway; otherwise, it is placed in a separate queue.
     */
    void processDepartingQueue() {
        if (isRunwayEmpty()) {
            Plane nextPlane = departing.poll();
            if (nextPlane != null) {
                if (nextPlane.getFuelAmount() >= fuelNeededForFlight) {
                    plane = nextPlane;
                } else {
                    notEnoughFuel.offer(nextPlane);
                    processDepartingQueue();


                }
            }
        }
    }


    /**
     * Processes the plane currently on the runway, incrementing its queue time and handling takeoff.
     * If there is a plane on the runway:
     *   - Increments the queue time of the plane waiting.
     *   - Checks if the plane's queue time has reached the threshold for takeoff.
     *   - If the queue time is sufficient, the plane is marked as departed, and the runway is cleared.
     * This method ensures that planes on the runway are processed based on their queue times.
     */
    void processDepartingPlane() {

        if (plane != null) {
            plane.incrementQueueTime();
            if (plane.getQueueTime() >= departingQueueTime) {
                // Mark the plane as departed
                departed.offer(plane);
                //Removes the plane from the runway
                removePlaneFromRunway();

            }
        }
    }

    /** Manages the runway by calling 3 methods that handle fuel, departing planes, and planes currently on the runway*/
    void processRunway() {
        decrementFuelOfAllPlanes();
        processDepartingQueue();
        processDepartingPlane();
    }

    /** Displays/prints information in an organized format*/
    void printSimulationInfo() {
        System.out.printf("\nSimulation time: %d\n", simulationTime);

        System.out.printf("Total Planes: %d\n", totalPlanes);
        System.out.printf("Planes that departed: %d\n", departed.size());
        System.out.printf("Planes that don't have enough fuel: %d\n", notEnoughFuel.size());
        System.out.printf("Planes still on the runway: %d\n", departing.size());
    }

    /**
     * Displays information about planes that have successfully taken off.
     * Prints details such as the ID and fuel amount of each departed plane.
     * This method helps users see the planes that have taken off and no longer on the runway.
     */
    void printDepartedQueue() {
        Iterator<Plane> it = departed.iterator();
        System.out.println("Planes departed queue:");
        // Iterates through the departed planes and print their ID and fuel amount
        while (it.hasNext()) {
            Plane plane = it.next();
            System.out.printf("[%d, %d]->", plane.getId(), plane.getFuelAmount());
        }
        System.out.println();
    }

    /**
     * Displays information about planes that couldn't take off due to insufficient fuel.
     * Prints details such as the ID and remaining fuel of each affected plane.
     * This method helps users identify planes that have fuel-related issues.
     */
    void printNotEnoughFuelQueue() {
        Iterator<Plane> it = notEnoughFuel.iterator();
        System.out.println("Planes not enough fuel queue:");
        // Iterate through the planes with insufficient fuel and print their ID and remaining fuel
        while (it.hasNext()) {
            Plane plane = it.next();
            System.out.printf("[%d, %d]->", plane.getId(), plane.getFuelAmount());
        }
        System.out.println();
    }

    /**
     * The main function that runs the airport simulation.
     * Creates an instance of the Airport.
     * Starts a loop to simulate planes arriving and taking off.
     * Prints key information about the simulation at the end.
     */
    public static void main(String[] args) {
        Airport runway = new Airport();
        int i = 0;
        do {
            // If the simulation time hasn't reached its limit, start a new plane
            if (i < runway.simulationTime)
                runway.startNewPlane(i);
            runway.processRunway();
            // Increment the simulation time counter
            i++;
        } while (i < runway.simulationTime || runway.departing.size() > 0);

        runway.printSimulationInfo();
        runway.printDepartedQueue();
        runway.printNotEnoughFuelQueue();
    }
}
