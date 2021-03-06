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
name|jaxws
operator|.
name|handler
operator|.
name|types
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  * The display-name type contains a short name that is intended to be displayed by tools. It is used by  * display-name elements. The display name need not be unique. Example: ...<display-name xml:lang="en">  * Employee Self Service</display-name> The value of the xml:lang attribute is "en" (English) by default.  *<p>  * Java class for display-nameType complex type.  *<p>  * The following schema fragment specifies the expected content contained within this class.  *  *<pre>  *&lt;complexType name="display-nameType">  *&lt;simpleContent>  *&lt;extension base="&lt;http://java.sun.com/xml/ns/javaee>string">  *&lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>  *&lt;/extension>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  */
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
literal|"display-nameType"
argument_list|)
specifier|public
class|class
name|DisplayNameType
extends|extends
name|CString
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|namespace
operator|=
literal|"http://www.w3.org/XML/1998/namespace"
argument_list|)
specifier|protected
name|java
operator|.
name|lang
operator|.
name|String
name|lang
decl_stmt|;
comment|/**      * Gets the value of the lang property.      *      * @return possible object is {@link java.lang.String }      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getLang
parameter_list|()
block|{
return|return
name|lang
return|;
block|}
comment|/**      * Sets the value of the lang property.      *      * @param value allowed object is {@link java.lang.String }      */
specifier|public
name|void
name|setLang
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
name|lang
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

