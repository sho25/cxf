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
name|ws
operator|.
name|security
operator|.
name|trust
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
import|;
end_import

begin_comment
comment|/**  * This interface allows you to plug in some custom logic when storing/retrieving STS tokens in/from the cache  */
end_comment

begin_interface
specifier|public
interface|interface
name|STSTokenCacher
block|{
comment|/**      * Retrieve a cached STS token      */
name|SecurityToken
name|retrieveToken
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Retrieve a cached STS token for a given delegation token Element      */
name|SecurityToken
name|retrieveToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|delegationToken
parameter_list|,
name|String
name|cacheKey
parameter_list|)
function_decl|;
comment|/**      * Store a token in the cache      */
name|void
name|storeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|SecurityToken
name|securityToken
parameter_list|)
function_decl|;
comment|/**      * Store a given delegation token in the cache (or update it if it's already there), with a reference to the      * security token obtained from the STS.      */
name|void
name|storeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|delegationToken
parameter_list|,
name|String
name|secTokenId
parameter_list|,
name|String
name|cacheKey
parameter_list|)
function_decl|;
comment|/**      * Remove a cached STS token      */
name|void
name|removeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|SecurityToken
name|securityToken
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

