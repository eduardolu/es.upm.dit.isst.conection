package es.upm.dit.isst.conection.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo implements Serializable {


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String author;
	private String summary;
	private String description;
	boolean finished;

	public Todo(String author, String summary, String description) {
		this.author = author;
		this.summary = summary;
		this.description = description;
		finished = false;
	}

	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getShortDescription() {
		return summary;
	}

	public void setShortDescription(String shortDescription) {
		this.summary = shortDescription;
	}

	public String getLongDescription() {
		return description;
	}

	public void setLongDescription(String longDescription) {
		this.description = longDescription;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

} 

