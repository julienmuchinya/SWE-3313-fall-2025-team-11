package com.team11.store.service;

import com.team11.store.model.Artwork;
import com.team11.store.repository.ArtworkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtworkService {
    private final ArtworkRepository repo;

    public ArtworkService(ArtworkRepository repo) { this.repo = repo; }

    public List<Artwork> listAvailable() { return repo.findBySoldFalse(); }

    public Artwork create(Artwork a) { a.setSold(false); return repo.save(a); }

    public Optional<Artwork> get(Long id) { return repo.findById(id); }

    public Optional<Artwork> purchase(Long id) {
        return repo.findById(id).map(a -> {
            if (a.isSold()) return a;
            a.setSold(true);
            return repo.save(a);
        });
    }
}
