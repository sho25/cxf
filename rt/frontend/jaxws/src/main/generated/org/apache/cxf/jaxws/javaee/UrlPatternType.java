begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//
end_comment

begin_comment
comment|// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs
end_comment

begin_comment
comment|// See<a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
end_comment

begin_comment
comment|// Any modifications to this file will be lost upon recompilation of the source schema.
end_comment

begin_comment
comment|// Generated on: 2006.10.31 at 10:25:50 AM GMT+08:00
end_comment

begin_comment
comment|//
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlValue
import|;
end_import

begin_comment
comment|/**  *   *   * 	The url-patternType contains the url pattern of the mapping.  * 	It must follow the rules specified in Section 11.2 of the  * 	Servlet API Specification. This pattern is assumed to be in  * 	URL-decoded form and must not contain CR(#xD) or LF(#xA).  * 	If it contains those characters, the container must inform  * 	the developer with a descriptive error message.  * 	The container must preserve all characters including whitespaces.  *   *         *   *<p>Java class for url-patternType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="url-patternType">  *&lt;simpleContent>  *&lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">  *&lt;/extension>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  *   *   */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"url-patternType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"value"
block|}
argument_list|)
specifier|public
class|class
name|UrlPatternType
block|{
annotation|@
name|XmlValue
specifier|protected
name|java
operator|.
name|lang
operator|.
name|String
name|value
decl_stmt|;
comment|/**      * Gets the value of the value property.      *       * @return      *     possible object is      *     {@link java.lang.String }      *           */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Sets the value of the value property.      *       * @param value      *     allowed object is      *     {@link java.lang.String }      *           */
specifier|public
name|void
name|setValue
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

