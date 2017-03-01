begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|mtom_schema_validation
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for helloResponse complex type.  *  *<p>The following schema fragment specifies the expected content contained within this class.  *  *<pre>  *&lt;complexType name="helloResponse">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *  *  */
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
literal|"helloResponse"
argument_list|)
specifier|public
class|class
name|HelloResponse
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"return"
argument_list|)
specifier|protected
name|String
name|res
decl_stmt|;
comment|/**      * Gets the value of the return property.      *      * @return      *     possible object is      *     {@link String }      *      */
specifier|public
name|String
name|getReturn
parameter_list|()
block|{
return|return
name|res
return|;
block|}
comment|/**      * Sets the value of the return property.      *      * @param value      *     allowed object is      *     {@link String }      *      */
specifier|public
name|void
name|setReturn
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|res
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

