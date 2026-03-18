<#-- size in xl, lg, md, sm, xs -->
<#function class_width size>
  <#if size.widthUnit == '%'>
    <#local cols = size.width / 100 * 12>
    <#return "col-md-" + cols?string["0"] + " col-sm-12">
  </#if>
  <#return ''>
</#function>

<#function style_width size>
  <#if size.width lte 0>
    <#return ''>
  </#if>
  <#return size.width + size.widthUnit>
</#function>

<#function style_height size>
  <#if size.height lte 0>
    <#return ''>
  </#if>
  <#return size.height + size.heightUnit>
</#function>

<#function html widgets>
  <#list widgets as widget>
  </#list>
</#function>