package com.team11.store.controller;

import com.team11.store.model.Artwork;
import com.team11.store.service.ArtworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {
    private final ArtworkService svc;

    public ArtworkController(ArtworkService svc) { this.svc = svc; }

    @GetMapping
    public List<Artwork> list() { return svc.listAvailable(); }

    @PostMapping
    public ResponseEntity<Artwork> create(@RequestBody Artwork a) {
        Artwork saved = svc.create(a);
        return ResponseEntity.created(URI.create("/api/artworks/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artwork> get(@PathVariable Long id) {
        return svc.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Artwork> purchase(@PathVariable Long id) {
        return svc.purchase(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
