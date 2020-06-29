package com.dmsilver2.music.artist.album.song;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmsilver2.music.artist.album.AlbumController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/songs")
public class SongController {

	@Autowired
	SongJpaRepository songRepository;

	@GetMapping("/{id}")
	public EntityModel<Song> findSong(@PathVariable long id) throws NoSuchMethodException, SecurityException {
		log.info("id -> {}", id);

		Optional<Song> song = songRepository.findById(id);

		if (!song.isPresent())
			throw new SongNotFoundException("Song was not found");

		EntityModel<Song> entity = new EntityModel<Song>(song.get());

		WebMvcLinkBuilder allSongsLink = linkTo(methodOn(AlbumController.class).findAlbum(song.get().getAlbum().getId()));
		entity.add(allSongsLink.withRel("album"));

		return entity;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteSong(@PathVariable long id) {
		Optional<Song> optionalSong = songRepository.findById(id);
		
		if(!optionalSong.isPresent())
			throw new SongNotFoundException("Song was not found");
		
		songRepository.delete(optionalSong.get());

		return ResponseEntity.ok().build();
	}

}

