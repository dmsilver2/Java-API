package com.dmsilver2.music.artist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.dmsilver2.music.artist.album.Album;
import com.fasterxml.jackson.annotation.JsonFilter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description="Description of the artist")
@Getter @Setter
@NoArgsConstructor
@Entity
@JsonFilter("ArtistFilter")
public class Artist {
	
	@Id
	@GeneratedValue
	@ApiModelProperty(required = true)
	private Long id;
	
	@Size(min = 3, message = "Name should have at least 3 charaters")
	@ApiModelProperty(required = true, notes = "Should be at least 3 characters long")
	private String name;
	
	@OneToMany(mappedBy = "artist", cascade=CascadeType.ALL, orphanRemoval=true)
	List<Album> albums;
}
