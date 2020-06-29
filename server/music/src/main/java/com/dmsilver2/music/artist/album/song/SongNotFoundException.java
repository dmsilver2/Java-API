package com.dmsilver2.music.artist.album.song;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SongNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5898088635213158162L;

	public SongNotFoundException(String message) {
		super(message);
	}
}

