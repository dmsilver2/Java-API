package com.dmsilver2.music.artist;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ArtistJpaRepository extends JpaRepository<Artist, Long> {

}
