package com.passguard.service;
import com.passguard.entity.User;
import com.passguard.entity.VaultItem;
import com.passguard.repository.UserRepository;
import com.passguard.repository.VaultItemRepository;
import com.passguard.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaultItemService {

    private final VaultItemRepository vaultItemRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        return userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new RuntimeException("Default user not found"));
    }

    public List<VaultItem> getAllVaultItems() {
        User currentUser = getCurrentUser();
        List<VaultItem> items = vaultItemRepository.findByUserId(currentUser.getId());

        // Decrypt passwords before sending to frontend
        items.forEach(item -> {
            item.setEncryptedPassword(EncryptionUtil.decrypt(item.getEncryptedPassword()));
        });

        return items;
    }

    public VaultItem createVaultItem(VaultItem vaultItem) {
        User currentUser = getCurrentUser();
        vaultItem.setUser(currentUser);

        // Encrypt password before saving
        vaultItem.setEncryptedPassword(EncryptionUtil.encrypt(vaultItem.getEncryptedPassword()));

        VaultItem savedItem = vaultItemRepository.save(vaultItem);

        // Decrypt back for the response
        savedItem.setEncryptedPassword(EncryptionUtil.decrypt(savedItem.getEncryptedPassword()));
        return savedItem;
    }

    public Optional<VaultItem> updateVaultItem(Long id, VaultItem vaultItemRequest) {
        User currentUser = getCurrentUser();
        Optional<VaultItem> optionalItem = vaultItemRepository.findById(id);

        if (optionalItem.isEmpty() || !optionalItem.get().getUser().getId().equals(currentUser.getId())) {
            return Optional.empty();
        }

        VaultItem existingItem = optionalItem.get();
        existingItem.setWebsiteName(vaultItemRequest.getWebsiteName());
        existingItem.setUsername(vaultItemRequest.getUsername());
        existingItem.setWebsiteUrl(vaultItemRequest.getWebsiteUrl());
        existingItem.setCategory(vaultItemRequest.getCategory());
        existingItem.setNotes(vaultItemRequest.getNotes());
        existingItem.setFavorite(vaultItemRequest.getFavorite() != null ? vaultItemRequest.getFavorite() : false);

        // Re-encrypt the incoming password if provided
        if (vaultItemRequest.getEncryptedPassword() != null && !vaultItemRequest.getEncryptedPassword().isEmpty()) {
            existingItem.setEncryptedPassword(EncryptionUtil.encrypt(vaultItemRequest.getEncryptedPassword()));
        }

        VaultItem updatedItem = vaultItemRepository.save(existingItem);

        // Decrypt back for response
        updatedItem.setEncryptedPassword(EncryptionUtil.decrypt(updatedItem.getEncryptedPassword()));
        return Optional.of(updatedItem);
    }

    public boolean deleteVaultItem(Long id) {
        User currentUser = getCurrentUser();
        Optional<VaultItem> optionalItem = vaultItemRepository.findById(id);

        if (optionalItem.isEmpty() || !optionalItem.get().getUser().getId().equals(currentUser.getId())) {
            return false;
        }

        vaultItemRepository.delete(optionalItem.get());
        return true;
    }
}
