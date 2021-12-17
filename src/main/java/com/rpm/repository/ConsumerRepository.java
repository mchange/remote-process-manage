package com.rpm.repository;

import com.rpm.bean.Consumer;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * $
 *
 * @author zhang.zw
 * @date: 2021-12-01 12:01
 **/
@Repository
// 不加@Transactional注解不能执行update操作
@Transactional
public interface ConsumerRepository extends JpaRepository<Consumer, String> {

	/**
	 * consumer 关联查询 provider
	 * @param consumerHost
	 * @param consumerPid
	 * @param providerHost
	 * @param providerPort
	 * @return
	 */
	@Query(value = "SELECT cc.id AS c_id, "
		+ "       cc.host AS c_host, "
		+ "       cc.pid AS c_pid, "
		+ "       cc.program AS c_program, "
		+ "       cc.remark AS c_remark, "
		+ "       pro.user_name AS c_user_name, "
		+ "       pro.start_date AS c_start_date, "
		+ "       pro.command AS c_command, "
		+ "       cc.remote_host AS p_host, "
		+ "       cc.remote_port AS p_port,"
		+ "       pd.remark AS p_remark "
		+ "FROM consumer cc "
		+ "LEFT JOIN process pro ON (cc.host = pro.host AND cc.pid = pro.pid) "
		+ "left join provider pd on(cc.remote_host = pd.host and cc.remote_port = pd.port)"
		+ "where case when ?1 != '' then cc.host=?1 else 1=1 end and "
		+ "case when ?2 != '' then cc.pid=?2 else 1=1 end and "
		+ "case when ?3 != '' then cc.remote_host=?3 else 1=1 end and "
		+ "case when ?4 != '' then cc.remote_port=?4 else 1=1 end and 1=1 "
		+ "order by cc.host, cc.pid", nativeQuery = true)
	List<Map> findConsumerWithProvider(String consumerHost, String consumerPid, String providerHost, String providerPort);

	@Query(value = "select DISTINCT host from consumer", nativeQuery = true)
	List<Map> findHosts();


	@Modifying
	@Query(value = "update consumer set remark=?3 where host = ?1 and pid = ?2", nativeQuery = true)
	int updateRemark(String host, String pid, String remark);
}
