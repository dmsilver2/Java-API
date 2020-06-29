package com.dmsilver2.music.artist.album;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmsilver2.music.artist.ArtistController;
import com.dmsilver2.music.artist.album.song.Song;
import com.dmsilver2.music.artist.album.song.SongController;
import com.dmsilver2.music.artist.album.song.SongJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/albums")
public class AlbumController {

	@Autowired
	AlbumJpaRepository albumRepository;

	@Autowired
	SongJpaRepository songRepository;

	@GetMapping()
	public List<Album> findAllAlbums() {
		return albumRepository.findAll();
	}

	@GetMapping("/{id}")
	public EntityModel<Album> findAlbum(@PathVariable long id) throws NoSuchMethodException, SecurityException {
		log.info("id -> {}", id);

		Optional<Album> album = albumRepository.findById(id);

		if (!album.isPresent())
			throw new AlbumNotFoundException("Album was not found");

		EntityModel<Album> entity = new EntityModel<Album>(album.get());

		WebMvcLinkBuilder allAlbumsLink = linkTo(methodOn(ArtistController.class).findArtist(album.get().getArtist().getId()));
		entity.add(allAlbumsLink.withRel("artist"));

		return entity;
	}

	@PutMapping("/{id}")
	public EntityModel<Album> editAlbums(@PathVariable long id, @Valid @RequestBody Album album) {
		Optional<Album> optionalAlbum = albumRepository.findById(id);
		
		if(!optionalAlbum.isPresent())
			throw new AlbumNotFoundException("Album was not found");
		
		Album dbAlbum = optionalAlbum.get();
		
		dbAlbum.setName(album.getName());
		
		Album savedAlbum = albumRepository.save(dbAlbum);

		EntityModel<Album> entity = new EntityModel<Album>(savedAlbum);
		
		WebMvcLinkBuilder selfLink = linkTo(methodOn(this.getClass()).editAlbums(id, savedAlbum));
		entity.add(selfLink.withRel("self"));

		log.info("Saved Album Entity -> {}", entity);
		
		return entity;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteAlbum(@PathVariable long id) {
		Optional<Album> optionalAlbum = albumRepository.findById(id);
		
		if(!optionalAlbum.isPresent())
			throw new AlbumNotFoundException("Album was not found");
		
		albumRepository.delete(optionalAlbum.get());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/songs")
	public ResponseEntity<Song> createSong(@PathVariable long id, @Valid @RequestBody Song song) throws NoSuchMethodException, SecurityException {
		Optional<Album> optionalAlbum = albumRepository.findById(id);
		
		if(!optionalAlbum.isPresent())
			throw new AlbumNotFoundException("Album was not found");
		
		song.setAlbum(optionalAlbum.get());
		
		Song savedSong = songRepository.save(song);
		
		WebMvcLinkBuilder createdAlbumLink = linkTo(methodOn(SongController.class).findSong(savedSong.getId()));

		return ResponseEntity.created(createdAlbumLink.toUri()).build();
	}
}

