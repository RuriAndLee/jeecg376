UPDATE wms_fetch
SET 
	   <#if fetch.create_name ?exists>
		   create_name = :fetch.create_name,
		</#if>
	   <#if fetch.create_by ?exists>
		   create_by = :fetch.create_by,
		</#if>
	   <#if fetch.create_date ?exists>
		   create_date = :fetch.create_date,
		</#if>
	    <#if fetch.update_name ?exists>
		   update_name = :fetch.update_name,
		</#if>
	   <#if fetch.update_by ?exists>
		   update_by = :fetch.update_by,
		</#if>
		<#if fetch.update_date ?exists>
		   update_date = :fetch.update_date,
		</#if>
	   <#if fetch.sys_org_code ?exists>
		   sys_org_code = :fetch.sys_org_code,
		</#if>
	    <#if fetch.sys_company_code ?exists>
		   sys_company_code = :fetch.sys_company_code,
		</#if>
	   <#if fetch.bpm_status ?exists>
		   bpm_status = :fetch.bpm_status,
		</#if>
	    <#if fetch.fetchno ?exists>
		   fetchno = :fetch.fetchno,
		</#if>
		<#if fetch.status ?exists>
		   status = :fetch.status,
		</#if>
		<#if fetch.error_msg ?exists>
		   error_msg = :fetch.error_msg,
		</#if>
WHERE id = :fetch.id