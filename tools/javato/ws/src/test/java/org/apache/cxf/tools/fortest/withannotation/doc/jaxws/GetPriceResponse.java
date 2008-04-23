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
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|jaxws
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
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  *<p>Java class for getPriceResponse element declaration.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;element name="getPriceResponse">  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="responseType" type="{http://www.w3.org/2001/XMLSchema}float"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *&lt;/element>  *</pre>  *   *   */
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
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"getPriceResponse"
argument_list|)
specifier|public
class|class
name|GetPriceResponse
block|{
specifier|protected
name|float
name|responseType
decl_stmt|;
comment|/**      * Gets the value of the responseType property.      *       */
specifier|public
name|float
name|getResponseType
parameter_list|()
block|{
return|return
name|responseType
return|;
block|}
comment|/**      * Sets the value of the responseType property.      *       */
specifier|public
name|void
name|setResponseType
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|this
operator|.
name|responseType
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

