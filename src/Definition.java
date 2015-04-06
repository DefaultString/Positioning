
public class Definition 
{
	public static final int NUM_OF_BEACONS = 8;
	
	public static final int BEACONS_PER_EDGE = (Definition.NUM_OF_BEACONS + 4)/4;
	
	public static final int TRIAGLES_PER_EDGE = (Definition.BEACONS_PER_EDGE * ( Definition.BEACONS_PER_EDGE - 1)) / 2;	
}
