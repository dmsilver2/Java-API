package com.dmsilver2.music.artist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dmsilver2.music.artist.album.Album;
import com.dmsilver2.music.artist.album.AlbumController;
import com.dmsilver2.music.artist.album.AlbumJpaRepository;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/artists")
public class ArtistController {

	@Autowired
	ArtistJpaRepository artistRepository;
	
	@Autowired
	AlbumJpaRepository albumRepository;

	@GetMapping()
	public MappingJacksonValue findAllArtists() {
		List<Artist> artists = artistRepository.findAll();
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

		FilterProvider filters = new SimpleFilterProvider().addFilter("ArtistFilter", filter);

		MappingJacksonValue mapping = new MappingJacksonValue(artists);

		mapping.setFilters(filters);

		return mapping;
	}

	@GetMapping("/{id}")
	public EntityModel<Artist> findArtist(@PathVariable long id) throws NoSuchMethodException, SecurityException {
		log.info("id -> {}", id);

		Optional<Artist> artist = artistRepository.findById(id);

		if (!artist.isPresent())
			throw new ArtistNotFoundException("Artist was not found");

		EntityModel<Artist> entity = new EntityModel<Artist>(artist.get());
		
		WebMvcLinkBuilder allArtistsLink = linkTo(methodOn(this.getClass()).findAllArtists());
		entity.add(allArtistsLink.withRel("all-artists"));

		WebMvcLinkBuilder editArtistLink = linkTo(methodOn(this.getClass()).saveArtist(id, artist.get()));
		entity.add(editArtistLink.withRel("edit-artist"));

		WebMvcLinkBuilder deleteArtistLink = linkTo(methodOn(this.getClass()).deleteArtist(id));
		entity.add(deleteArtistLink.withRel("delete-artist"));

		return entity;
	}

	@PostMapping()
	public ResponseEntity<Object> createArtist(@Valid @RequestBody Artist artist) {
		log.info("Request Body's artist -> {}", artist);
		Artist savedArtist = artistRepository.save(artist);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedArtist.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public EntityModel<Artist> saveArtist(@PathVariable long id, @Valid @RequestBody Artist artist) {
		Optional<Artist> optionalArtist = artistRepository.findById(id);

		if (!optionalArtist.isPresent())
			throw new ArtistNotFoundException("Artist was not found");

		Artist dbArtist = optionalArtist.get();
		dbArtist.setName(artist.getName());

		Artist savedArtist = artistRepository.save(dbArtist);

		EntityModel<Artist> entity = new EntityModel<Artist>(savedArtist);
		
		WebMvcLinkBuilder allArtistsLink = linkTo(methodOn(this.getClass()).findAllArtists());
		entity.add(allArtistsLink.withRel("all-artists"));

		WebMvcLinkBuilder selfLink = linkTo(methodOn(this.getClass()).saveArtist(id, savedArtist));
		entity.add(selfLink.withRel("self"));

		log.info("Saved Artist Entity -> {}", entity);
		
		return entity;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteArtist(@PathVariable long id) {
		artistRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/{id}/albums")
	public ResponseEntity<Album> createAlbum(@PathVariable long id, @Valid @RequestBody Album album) throws NoSuchMethodException, SecurityException {
		Optional<Artist> optionalArtist = artistRepository.findById(id);
		
		if(!optionalArtist.isPresent())
			throw new ArtistNotFoundException("Artist was not found");
		
		album.setArtist(optionalArtist.get());
		
		Album savedAlbum = albumRepository.save(album);
		
		WebMvcLinkBuilder createdAlbumLink = linkTo(methodOn(AlbumController.class).findAlbum(savedAlbum.getId()));

		return ResponseEntity.created(createdAlbumLink.toUri()).build();
	}

}
