package ikube.listener;

/**
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public class Schedule {

	private String type;
	private long delay;
	private long period;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

}
