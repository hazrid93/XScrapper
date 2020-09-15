package com.jrp.pma.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Star {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="star_seq")
	private long id;
	
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "star")
	private List<Video> videos;
	
	public Star() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	
}
