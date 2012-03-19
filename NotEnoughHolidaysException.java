
public class NotEnoughHolidaysException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4361034055550205074L;

	int holidaysLeft;
	
	@SuppressWarnings("serial")
	
	public NotEnoughHolidaysException(String arg)
	{
		super(arg);
	}
	
	public NotEnoughHolidaysException(int reqHolidaysLeft){
		holidaysLeft = reqHolidaysLeft;
	}
	
	public int getHolidaysLeft(){
		return holidaysLeft;
	}
}
