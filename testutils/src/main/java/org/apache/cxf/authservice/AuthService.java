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
name|authservice
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/AuthService"
argument_list|,
name|name
operator|=
literal|"AuthService"
argument_list|)
specifier|public
interface|interface
name|AuthService
block|{
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|java
operator|.
name|lang
operator|.
name|String
argument_list|>
name|getRoles
parameter_list|(
name|String
name|sid
parameter_list|)
function_decl|;
name|java
operator|.
name|lang
operator|.
name|String
index|[]
name|getRolesAsArray
parameter_list|(
name|String
name|sid
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"authenticate1"
argument_list|)
name|boolean
name|authenticate
parameter_list|(
name|String
name|sid
parameter_list|,
name|String
name|uid
parameter_list|,
name|String
name|pwd
parameter_list|)
function_decl|;
comment|//test bean mode
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"authenticate2"
argument_list|)
name|boolean
name|authenticate
parameter_list|(
name|Authenticate
name|auth
parameter_list|)
function_decl|;
name|String
name|getAuthentication
parameter_list|(
name|String
name|sid
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

