package com.dmsilver2.music.artist.album.song;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.dmsilver2.music.artist.album.Album;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Songs that are part of an Album")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Song {

	@Id
	@GeneratedValue
	@ApiModelProperty(required = true)
	private Long id;

	@Size(min = 3, message = "Name should have at least 3 charaters")
	@ApiModelProperty(required = true, notes = "Should be at least 3 characters long")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Album album;
}

