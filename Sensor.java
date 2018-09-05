package laneChanger;

public class Sensor {
	private int value;
	private static final int LANE_WIDTH = 5;
	public Sensor(int value) {
		this.value = value;
	}
	public void setSensor(int value) {
		this.value = value;
	}
	public int getSensor() {
		return value;
	}
	public void detectLane(Street street, Actuators actuator) {
		try {
			if(street.getCar(actuator.getPosition(), actuator.getLane() + 1) != null) {
				value = 0;	
			}
			else if(street.getCar(actuator.getPosition(), actuator.getLane() + 2) != null) {
				value = LANE_WIDTH;
			}
			else {
				value = LANE_WIDTH * 2;
			}
		}catch(IndexOutOfBoundsException e) {
			if(actuator.getLane() == 1) {
				value = LANE_WIDTH;
			}
			else {
				value = 0;
			}
			
		}
	}
}
