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
name|XmlAttribute
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
name|XmlElement
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
name|XmlID
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
name|adapters
operator|.
name|CollapsedStringAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_comment
comment|/**  *   *   * 	Specifies a name/value pair.  *   *         *   *<p>Java class for propertyType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="propertyType">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="name" type="{http://java.sun.com/xml/ns/javaee}xsdStringType"/>  *&lt;element name="value" type="{http://java.sun.com/xml/ns/javaee}xsdStringType"/>  *&lt;/sequence>  *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"propertyType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|,
literal|"value"
block|}
argument_list|)
specifier|public
class|class
name|PropertyType
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|protected
name|XsdStringType
name|name
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|protected
name|XsdStringType
name|value
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|CollapsedStringAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlID
specifier|protected
name|java
operator|.
name|lang
operator|.
name|String
name|id
decl_stmt|;
comment|/**      * Gets the value of the name property.      *       * @return      *     possible object is      *     {@link XsdStringType }      *           */
specifier|public
name|XsdStringType
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets the value of the name property.      *       * @param value      *     allowed object is      *     {@link XsdStringType }      *           */
specifier|public
name|void
name|setName
parameter_list|(
name|XsdStringType
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the value property.      *       * @return      *     possible object is      *     {@link XsdStringType }      *           */
specifier|public
name|XsdStringType
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Sets the value of the value property.      *       * @param value      *     allowed object is      *     {@link XsdStringType }      *           */
specifier|public
name|void
name|setValue
parameter_list|(
name|XsdStringType
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
comment|/**      * Gets the value of the id property.      *       * @return      *     possible object is      *     {@link java.lang.String }      *           */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the value of the id property.      *       * @param value      *     allowed object is      *     {@link java.lang.String }      *           */
specifier|public
name|void
name|setId
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
name|id
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

