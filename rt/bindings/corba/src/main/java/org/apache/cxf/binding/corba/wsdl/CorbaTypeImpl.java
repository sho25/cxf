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
name|binding
operator|.
name|corba
operator|.
name|wsdl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaTypeImpl
block|{
specifier|protected
name|QName
name|qname
decl_stmt|;
comment|/**      * Gets the value of the qname property.      *      * @return      *     possible object is      *     {@link QName }      *      */
specifier|public
name|QName
name|getQName
parameter_list|()
block|{
return|return
name|qname
return|;
block|}
comment|/**      * Sets the value of the name property.      *      * @param value      *     allowed object is      *     {@link QName }      *      */
specifier|public
name|void
name|setQName
parameter_list|(
name|QName
name|value
parameter_list|)
block|{
name|this
operator|.
name|qname
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetQName
parameter_list|()
block|{
return|return
name|this
operator|.
name|qname
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

