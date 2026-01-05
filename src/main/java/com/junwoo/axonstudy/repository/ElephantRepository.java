package com.junwoo.axonstudy.repository;

import com.junwoo.axonstudy.entity.Elephant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
public interface ElephantRepository extends JpaRepository<Elephant, String> {
}
