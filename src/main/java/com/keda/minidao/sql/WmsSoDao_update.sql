UPDATE wms_so
SET 
	   <#if so.create_name ?exists>
		   create_name = :so.create_name,
		</#if>
	   <#if so.create_by ?exists>
		   create_by = :so.create_by,
		</#if>
	   <#if so.create_date ?exists>
		   create_date = :so.create_date,
		</#if>
	    <#if so.update_name ?exists>
		   update_name = :so.update_name,
		</#if>
	   <#if so.update_by ?exists>
		   update_by = :so.update_by,
		</#if>
		<#if so.update_date ?exists>
		   update_date = :so.update_date,
		</#if>
	   <#if so.sys_org_code ?exists>
		   sys_org_code = :so.sys_org_code,
		</#if>
	    <#if so.sys_company_code ?exists>
		   sys_company_code = :so.sys_company_code,
		</#if>
	   <#if so.bpm_status ?exists>
		   bpm_status = :so.bpm_status,
		</#if>
	    <#if so.sono ?exists>
		   sono = :so.sono,
		</#if>
		<#if so.status ?exists>
		   status = :so.status,
		</#if>
		<#if so.error_msg ?exists>
		   error_msg = :so.error_msg,
		</#if>
WHERE id = :so.id