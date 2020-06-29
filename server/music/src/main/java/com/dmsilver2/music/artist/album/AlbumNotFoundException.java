package com.dmsilver2.music.artist.album;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlbumNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2405318758273445127L;

	public AlbumNotFoundException(String message) {
		super(message);
	}
}