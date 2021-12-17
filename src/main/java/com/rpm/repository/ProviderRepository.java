package com.rpm.repository;

import com.rpm.bean.Provider;
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
 * @date: 2021-12-01 12:02
 **/
@Repository
@Transactional
public interface ProviderRepository extends JpaRepository<Provider, String> {

	/**
	 * provider 关联查询 consumer
	 * @param providerHost
	 * @param providerPid
	 * @param providerPort
	 * @return
	 */
	@Query(value = "SELECT cc.host as c_host, "
		+ "   cc.pid as c_pid, "
		+ "   cc.program as c_program, "
		+ "   cc.user_name as c_user_name, "
		+ "   cc.start_date as c_start_date, "
		+ "   cc.command as c_command, "
		+ "   cc.remark as c_remark, "
		+ "   p.host as p_host, "
		+ "   p.pid as p_pid, "
		+ "   p.port as p_port, "
		+ "   p.program as p_program, "
		+ "   ps.user_name as p_user_name, "
		+ "   ps.start_date as p_start_date, "
		+ "   ps.command as p_command, "
		+ "   p.remark as p_remark, "
		+ "   p.id as p_id "
		+ "FROM provider p "
		+ "LEFT JOIN "
		+ "  (SELECT c.host, "
		+ "          c.pid, "
		+ "          c.program, "
		+ "          c.remote_host, "
		+ "          c.remote_port, "
		+ "          c.remark, "
		+ "          ps.user_name, "
		+ "          ps.start_date, "
		+ "          ps.command "
		+ "   FROM consumer c "
		+ "   LEFT JOIN process ps ON (c.host = ps.host AND c.pid = ps.pid) "
		+ "   ) cc ON (p.host = cc.remote_host AND p.port = cc.remote_port) "
		+ "left join process ps on (p.host = ps.host and p.pid = ps.pid)"
		+ "where case when ?1 != '' then p.host=?1 else 1=1 end and "
		+ "case when ?2 != '' then p.pid=?2 else 1=1 end and "
		+ "case when ?3 != '' then p.port=?3 else 1=1 end and 1=1 "
		+ "order by p.host, p.pid"
		, nativeQuery = true)
	List<Map> findProviderWithConsumer( String providerHost, String providerPid,  String providerPort);

	/**
	 * 查询主机列表
	 * @return
	 */
	@Query(value = "select DISTINCT host from provider", nativeQuery = true)
	List<Map> findHosts();

	/**
	 * 查询provider的进程信息
	 * @param providerHost
	 * @param providerPort
	 * @return
	 */
	@Query(value = "select p.host, p.port, p.pid, p.program, ps.start_date, ps.user_name, ps.command "
		+ "from provider p "
		+ "left join process ps on (p.host = ps.host and p.pid = ps.pid) "
		+ "where case when ?1 != '' then p.host=?1 else 1=1 end and "
		+ "case when ?2 != '' then p.port=?2 else 1=1 end and 1=1", nativeQuery = true)
	List<Map> findProcessDetail(String providerHost, String providerPort);

	@Modifying
	@Query(value = "update provider set remark=?3 where host = ?1 and pid = ?2", nativeQuery = true)
	int updateRemark(String host, String pid, String remark);
}
