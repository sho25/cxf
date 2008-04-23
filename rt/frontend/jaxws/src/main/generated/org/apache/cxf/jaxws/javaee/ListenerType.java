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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  *   *   * 	The listenerType indicates the deployment properties for a web  * 	application listener bean.  *   *         *   *<p>Java class for listenerType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="listenerType">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;group ref="{http://java.sun.com/xml/ns/javaee}descriptionGroup"/>  *&lt;element name="listener-class" type="{http://java.sun.com/xml/ns/javaee}fully-qualified-classType"/>  *&lt;/sequence>  *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"listenerType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"description"
block|,
literal|"displayName"
block|,
literal|"icon"
block|,
literal|"listenerClass"
block|}
argument_list|)
specifier|public
class|class
name|ListenerType
block|{
specifier|protected
name|List
argument_list|<
name|DescriptionType
argument_list|>
name|description
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"display-name"
argument_list|)
specifier|protected
name|List
argument_list|<
name|DisplayNameType
argument_list|>
name|displayName
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|IconType
argument_list|>
name|icon
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"listener-class"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|protected
name|FullyQualifiedClassType
name|listenerClass
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
comment|/**      * Gets the value of the description property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the description property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getDescription().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link DescriptionType }      *       *       */
specifier|public
name|List
argument_list|<
name|DescriptionType
argument_list|>
name|getDescription
parameter_list|()
block|{
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
operator|new
name|ArrayList
argument_list|<
name|DescriptionType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|description
return|;
block|}
comment|/**      * Gets the value of the displayName property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the displayName property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getDisplayName().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link DisplayNameType }      *       *       */
specifier|public
name|List
argument_list|<
name|DisplayNameType
argument_list|>
name|getDisplayName
parameter_list|()
block|{
if|if
condition|(
name|displayName
operator|==
literal|null
condition|)
block|{
name|displayName
operator|=
operator|new
name|ArrayList
argument_list|<
name|DisplayNameType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|displayName
return|;
block|}
comment|/**      * Gets the value of the icon property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the icon property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getIcon().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link IconType }      *       *       */
specifier|public
name|List
argument_list|<
name|IconType
argument_list|>
name|getIcon
parameter_list|()
block|{
if|if
condition|(
name|icon
operator|==
literal|null
condition|)
block|{
name|icon
operator|=
operator|new
name|ArrayList
argument_list|<
name|IconType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|icon
return|;
block|}
comment|/**      * Gets the value of the listenerClass property.      *       * @return      *     possible object is      *     {@link FullyQualifiedClassType }      *           */
specifier|public
name|FullyQualifiedClassType
name|getListenerClass
parameter_list|()
block|{
return|return
name|listenerClass
return|;
block|}
comment|/**      * Sets the value of the listenerClass property.      *       * @param value      *     allowed object is      *     {@link FullyQualifiedClassType }      *           */
specifier|public
name|void
name|setListenerClass
parameter_list|(
name|FullyQualifiedClassType
name|value
parameter_list|)
block|{
name|this
operator|.
name|listenerClass
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

