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

begin_comment
comment|/**  *   *   * 	The message-destination-usageType specifies the use of the  * 	message destination indicated by the reference.  The value  * 	indicates whether messages are consumed from the message  * 	destination, produced for the destination, or both.  The  * 	Assembler makes use of this information in linking producers  * 	of a destination with its consumers.  *   * 	The value of the message-destination-usage element must be  * 	one of the following:  * 	    Consumes  * 	    Produces  * 	    ConsumesProduces  *   *         *   *<p>Java class for message-destination-usageType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="message-destination-usageType">  *&lt;simpleContent>  *&lt;restriction base="&lt;http://java.sun.com/xml/ns/javaee>string">  *&lt;/restriction>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"message-destination-usageType"
argument_list|)
specifier|public
class|class
name|MessageDestinationUsageType
extends|extends
name|CString
block|{   }
end_class

end_unit

