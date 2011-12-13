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
name|javascript
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaType
import|;
end_import

begin_comment
comment|/**  * Elements and attributes both provide these.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ItemInfo
block|{
name|String
name|getJavascriptName
parameter_list|()
function_decl|;
name|boolean
name|isAny
parameter_list|()
function_decl|;
name|XmlSchemaType
name|getType
parameter_list|()
function_decl|;
name|boolean
name|isOptional
parameter_list|()
function_decl|;
name|boolean
name|isArray
parameter_list|()
function_decl|;
name|boolean
name|isNillable
parameter_list|()
function_decl|;
name|String
name|getDefaultValue
parameter_list|()
function_decl|;
name|void
name|setDefaultValue
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

