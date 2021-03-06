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
name|schemaimport
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
literal|"ResponseType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"text"
block|}
argument_list|)
specifier|public
class|class
name|ResponseType
block|{
specifier|protected
name|String
name|text
decl_stmt|;
comment|/**      * Gets the value of the text property.      *      * @return possible object is {@link String }      */
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
comment|/**      * Sets the value of the text property.      *      * @param value allowed object is {@link String }      */
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

