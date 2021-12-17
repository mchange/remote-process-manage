package com.rpm.repository;

import com.rpm.bean.Process;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * $
 *
 * @author zhang.zw
 * @date: 2021-12-01 11:20
 **/
@Repository
public interface ProcessRepository extends JpaRepository<Process, String> {

	@Query(value = "select DISTINCT host from process", nativeQuery = true)
	List<Map> findHosts();
}
