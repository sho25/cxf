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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ClientRegistrationProvider
block|{
comment|/**      * Get a Client with the given id      * @param clientId the client id      * @return Client      */
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
function_decl|;
comment|/**      * Set a Client      * @param client the client      */
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
function_decl|;
comment|/**      * Remove a Client with the given id      * @param clientId the client id      * @return Client      */
name|Client
name|removeClient
parameter_list|(
name|String
name|clientId
parameter_list|)
function_decl|;
comment|/**      * Get a list of clients registered by a resource owner.      *      * @param resourceOwner the resource owner, can be null      * @return the list of clients      */
name|List
argument_list|<
name|Client
argument_list|>
name|getClients
parameter_list|(
name|UserSubject
name|resourceOwner
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

