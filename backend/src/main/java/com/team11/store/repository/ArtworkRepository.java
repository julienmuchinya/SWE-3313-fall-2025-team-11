package com.team11.store.repository;

import com.team11.store.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findBySoldFalse();
}
