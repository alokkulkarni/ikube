package ikube.model;

import javax.persistence.Entity;

/**
 * @author Michael Couck
 * @since 23.11.10
 * @version 01.00
 */
@Entity()
public class Batch extends Persistable {

	private Long idNumber;
	private String indexName;

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(getId());
		builder.append(",");
		builder.append(getIndexName());
		builder.append(", ");
		builder.append(getIdNumber());
		builder.append("]");
		return builder.toString();
	}

}