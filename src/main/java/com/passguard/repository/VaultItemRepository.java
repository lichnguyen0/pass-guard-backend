package com.passguard.repository;

import com.passguard.entity.VaultItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaultItemRepository extends JpaRepository<VaultItem, Long> {
    List<VaultItem> findByUserId(Long userId);
    List<VaultItem> findByUserIdAndFavoriteTrue(Long userId);
}
