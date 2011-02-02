package ikube.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

/**
 * This object is passed around in the cluster. It contains transient data that must only be modified by the server from which it
 * originates.
 * 
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
@Entity()
public class Server extends Persistable implements Comparable<Server> {

	public class Action implements Serializable {

		/** The row id of the next row. */
		private long idNumber;
		/** The currently executing indexable. */
		private String indexableName;
		/** The actionName of the currently executing index. */
		private String indexName;
		/** The time the action was started. */
		private long startTime;

		public Action() {
		}

		public Action(long idNumber, String indexableName, String indexName, long startTime) {
			this.idNumber = idNumber;
			this.indexableName = indexableName;
			this.indexName = indexName;
			this.startTime = startTime;
		}

		public long getIdNumber() {
			return idNumber;
		}

		public void setIdNumber(long idNumber) {
			this.idNumber = idNumber;
		}

		public String getIndexableName() {
			return indexableName;
		}

		public void setIndexableName(String indexableName) {
			this.indexableName = indexableName;
		}

		public String getIndexName() {
			return indexName;
		}

		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public String toString() {
			final StringBuilder builder = new StringBuilder("[");
			builder.append(getIndexableName());
			builder.append(", ");
			builder.append(getIndexName());
			builder.append(", ");
			builder.append(getStartTime());
			builder.append("]");
			return builder.toString();
		}

	}

	/** The ip of the server. */
	private String ip;
	/** The address of this machine. */
	private String address;
	/** Whether this server is working. */
	private boolean working;
	/** The details about the action that this server is executing. */
	private List<Action> actions;
	/** The list of web service urls. */
	private List<String> webServiceUrls;

	public Server() {
		this.actions = new ArrayList<Server.Action>();
		this.webServiceUrls = new ArrayList<String>();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public List<Action> getActions() {
		return actions;
	}
	
	public List<String> getWebServiceUrls() {
		return webServiceUrls;
	}

	@Override
	public int compareTo(Server o) {
		return this.getAddress().compareTo(o.getAddress());
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder("[");
		builder.append(getId()).append(", ");
		builder.append(getIp()).append(", ");
		builder.append(getAddress()).append(", ");
		builder.append(isWorking()).append(", ");
		builder.append(getActions());
		builder.append("]");
		return builder.toString();
	}

}