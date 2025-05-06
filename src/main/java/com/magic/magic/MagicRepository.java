package com.magic.magic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagicRepository extends JpaRepository<Magic, String>,
        PagingAndSortingRepository<Magic, String>, JpaSpecificationExecutor<Magic> {

}
