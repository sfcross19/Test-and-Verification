package laneChanger;

public class Car {
	
	private boolean manualSensorInput = false;
	
	private char symbol;
	private static final int MOVEMENT_INCREMENT_SIZE = 5;
	private static final int END_OF_THE_ROAD = 99;
	private static final int LANE_WIDTH = 5;
	private static final int SENSOR_UPPER_BOUNDS = 50;
	private Actuators actuators = new Actuators();
	private Sensor[] sensors = {new Sensor(0), new Sensor(0), new Sensor(0), new Sensor(0)};
	
	public Car (int position, int lane, Street street) {
		this(position, lane, street, '^');
				
	}
	public Car (int position, int lane, Street street, char symbol) {
		this.symbol = symbol;
		if(position >= 0 && position <= END_OF_THE_ROAD && lane < 3 && lane >= 0) {				// This statement makes certain that the car can not start in an invalid location
			actuators = new Actuators(position, lane);
			street.placeCar(this, position, lane);
		}
		else {
			
			actuators = new Actuators(); 			// This statement assigns position a default value if the given position was not valid
			street.placeCar(this, 0, 0);
		}
				
	}
	
	/** This method moves the car forwards if it has not reached the end of the road */
	public void moveForward(Street street) {
		if(actuators.getPosition() <= (END_OF_THE_ROAD - actuators.getSpeed()) ) {		// This statement was added to make sure the car can not leave the road
			if(actuators.moveForward(street) && !manualSensorInput) {
				updateSensors(street);
				
			}
		}
		return;
	}
	/** @return true if there is no obstacle in the lane to the left of the car */
	public int leftLaneDetect() {
		boolean wasClear = false;
		for(int j = 0; j < 2; j++) {		// Loop will always run twice
			if(!checkSensors(sensors[0].getSensor(), sensors[1].getSensor(), sensors[2].getSensor(), sensors[3].getSensor())) {
				return -1;
			}
			if(sensors[0].getSensor() >= 0 && sensors[0].getSensor() < LANE_WIDTH ) {		// The sensors are checked and if the values are good it increments i by 1
				return 0;
			}
			if(sensors[1].getSensor() >= 0 && sensors[1].getSensor() < LANE_WIDTH) {
				return 0;
			}
			if(sensors[2].getSensor() >= 0 && sensors[2].getSensor() < LANE_WIDTH) {
				return 0;
			}
			if(sensors[3].getSensor() >= 0 && sensors[3].getSensor() < LANE_WIDTH) {
				return 0;
			}
			if(wasClear) {
				return 1;
			}
			else {
				wasClear = true;
			}
		}
		return -1;
	}
	/** @return false if more than two sensors have out of bounds values
	 *  @param sensor1 The first radar value
	 *  @param sensor2 The second radar value
	 *  @param sensor3 The third radar value
	 *	@param sensor4 The lidar value
	 */
	private boolean checkSensors(int sensor1, int sensor2, int sensor3, int sensor4) {
		int i = 0;
		if(sensor1 < 0 || sensor1 > SENSOR_UPPER_BOUNDS) {
			i++; 
		}
		if(sensor2 < 0 || sensor2 > SENSOR_UPPER_BOUNDS) {
			i++; 
		}
		if(sensor3 < 0 || sensor3 > SENSOR_UPPER_BOUNDS) {
			i++; 
		}
		if(sensor4 < 0 || sensor4 > SENSOR_UPPER_BOUNDS) {
			i++; 
		}
		if(i <= 2) {
			return true;
		}
		return false;
	}
	/** @return true if the car changes lane */
	public boolean changeLane(Street street) {
		if(!manualSensorInput) {
			updateSensors(street);
		}
		int leftLane = leftLaneDetect();
		if(leftLane == 1 && actuators.getPosition() < END_OF_THE_ROAD && actuators.getLane() <= 1 && actuators.getLane() >= 0) {	// Checks that the lane is clear and that the car is on the road
			moveForward(street);		// Moves the car one increment forward
			actuators.changeLane(street);				// Changes one lane to the left
			return true;		// The car was successful in changing lanes
		}
		else if(leftLane == 0) {
			try {
				throw new CustomException("Could not change lanes as there was no space");
			}catch(CustomException e) {
				System.out.println(e);
			}
			moveForward(street);
			return false;
		}
		else if(actuators.getLane() > 1 || actuators.getLane() < 0) {
			try {
				throw new CustomException("Could not change lanes as the car is either already in the leftmost lane or a lane that does not exist");
			}catch(CustomException e) {
				System.out.println(e);
			}
			moveForward(street);
			return false;
		}
		else if(actuators.getPosition() >= END_OF_THE_ROAD) {
			try {
				throw new CustomException("Could not change lanes as the end of the road was already reached");
			}catch(CustomException e) {
				System.out.println(e);
			}
			moveForward(street);
			return false;
		}
		try {
			throw new CustomException("Could not change lanes as two or more sensors returned out of bounds values");
		}catch(CustomException e) {
			System.out.println(e);
		}
		moveForward(street);
		return false;			// The car was not successful in changing lanes
	}
	/** @return position the current position of the car */
	public int whereIs() {
		return actuators.getPosition();
	}
	/**
	 * This method provides sensor values to the car
	 * 
	 * @param radar1 sensor value for radar1 
	 * @param radar2 sensor value for radar2 
	 * @param radar3 sensor value for radar3 
	 * @param lidar sensor value for lidar 
	 */
	public void setSensors(int radar1, int radar2, int radar3, int lidar) {
		
		manualSensorInput = true;
		sensors[0].setSensor(radar1);
		sensors[1].setSensor(radar2);
		sensors[2].setSensor(radar3);
		sensors[3].setSensor(lidar);
	}
	public Actuators getActuators() {
		return actuators;
	}
	public Sensor[] getSensors() {
		return sensors;
	}
	
	public char getSymbol() {
		return symbol;
	}
	public void updateSensors(Street street) {
		for (int i = 0; i<sensors.length;i++) {
			sensors[i].detectLane(street, actuators);
		}
	}

}
