package com.passguard.controller;

import com.passguard.entity.VaultItem;
import com.passguard.service.VaultItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class VaultItemController {

    private final VaultItemService vaultItemService;

    @GetMapping
    public ResponseEntity<List<VaultItem>> getAllVaultItems() {
        return ResponseEntity.ok(vaultItemService.getAllVaultItems());
    }

    @PostMapping
    public ResponseEntity<VaultItem> createVaultItem(@RequestBody VaultItem vaultItem) {
        return ResponseEntity.ok(vaultItemService.createVaultItem(vaultItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaultItem> updateVaultItem(@PathVariable Long id, @RequestBody VaultItem vaultItemRequest) {
        Optional<VaultItem> updatedItem = vaultItemService.updateVaultItem(id, vaultItemRequest);
        return updatedItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaultItem(@PathVariable Long id) {
        boolean deleted = vaultItemService.deleteVaultItem(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
