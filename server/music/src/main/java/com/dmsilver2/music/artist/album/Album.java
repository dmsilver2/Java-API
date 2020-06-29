package com.dmsilver2.music.artist.album;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.dmsilver2.music.artist.Artist;
import com.dmsilver2.music.artist.album.song.Song;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Artist Albums")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Album {

	@Id
	@GeneratedValue
	@ApiModelProperty(required = true)
	private Long id;

	@Size(min = 3, message = "Name should have at least 3 charaters")
	@ApiModelProperty(required = true, notes = "Should be at least 3 characters long")
	private String name;
	
	@OneToMany(mappedBy = "album", cascade=CascadeType.ALL, orphanRemoval=true)
	List<Song> songs;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	Artist artist;
}
