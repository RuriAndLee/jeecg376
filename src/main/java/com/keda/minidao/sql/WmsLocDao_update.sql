UPDATE wms_loc
SET 
	   <#if loc.layer ?exists>
		   layer = :loc.layer,
		</#if>
		<#if loc.topflag ?exists>
		   topflag = :loc.topflag,
		</#if>
WHERE id = :loc.id