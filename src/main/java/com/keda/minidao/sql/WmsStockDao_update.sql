UPDATE wms_stock
SET 
	   <#if stock.locno ?exists>
		   locno = :stock.locno,
		</#if>
		<#if stock.zoneno ?exists>
		   zoneno = :stock.zoneno,
		</#if>
		<#if stock.layer ?exists>
		   layer = :stock.layer,
		</#if>
WHERE id = :stock.id