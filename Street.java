package laneChanger;

public class Street {
	private Car[][] street = new Car[100][3];
	private static final int MOVEMENT_INCREMENT_SIZE = 5;
	private static final int END_OF_THE_ROAD = 99;
	public Street() {
		
	}
	public Street(Car[][] street) {
		this.street = street;
	}
	public void placeCar(Car car, int position, int lane) {
		if(street[position][lane] == null) {
			street[position][lane] = car;
			car.getActuators().setPosition(position);
			car.getActuators().setLane(lane);
		}
		else {
			System.out.println(street[position][lane]);
			//System.out.println(car.getActuators().getLane());
			try {
				throw new CustomException(" Street.placeCar(), There was already a car at that location");
			}
			catch(CustomException e) {
				System.out.println(e);				
			}
		}
	}
	public void MoveCar(Actuators actuator) throws CustomException {
		if (actuator.getPosition() == END_OF_THE_ROAD) {
			throw new CustomException("Street.moveCar(), The car has already reached the end of the street");
		}
		if (street[actuator.getPosition()][actuator.getLane()] != null && street[actuator.getPosition() + actuator.getSpeed()][actuator.getLane()] == null) {
			Car car = street[actuator.getPosition()][actuator.getLane()];
			street[actuator.getPosition() + actuator.getSpeed()][actuator.getLane()] = car;
			street[actuator.getPosition()][actuator.getLane()] = null;
			
		}else {
			throw new CustomException("Street.moveCar(), There is a car in the way or the location indicated does not contain a car");
		}
	}
	public void SwitchLane(Actuators actuator) {
		if( street[actuator.getPosition()][actuator.getLane()+1] == null) {

			street[actuator.getPosition()][actuator.getLane()+1] =  street[actuator.getPosition()][actuator.getLane()];
			street[actuator.getPosition()][actuator.getLane()] = null;
			actuator.setLane(actuator.getLane()+1);						// Fixed all!
		}
			
	}
	public Car getCar(int position, int lane) {
		return street[position][lane];
	}
}
