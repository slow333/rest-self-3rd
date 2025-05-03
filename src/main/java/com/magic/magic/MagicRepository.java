package com.magic.magic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagicRepository extends JpaRepository<Magic, String> {
}
