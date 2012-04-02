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
name|tokenstore
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * This interface defines a caching mechanism for security tokens. It is up to the underlying implementation  * to handle token expiration (e.g. by querying the SecurityToken's expires date).  */
end_comment

begin_interface
specifier|public
interface|interface
name|TokenStore
block|{
comment|/**      * Add the given token to the cache.      * @param token The token to be added      */
name|void
name|add
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
function_decl|;
comment|/**      * Remove an existing token.      */
name|void
name|remove
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
function_decl|;
comment|/**      * Return the list of all valid token identifiers.      * @return As array of (valid) token identifiers      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getTokenIdentifiers
parameter_list|()
function_decl|;
comment|/**      * Return the list of expired tokens.      * @return An array of expired<code>Tokens</code>      */
name|Collection
argument_list|<
name|SecurityToken
argument_list|>
name|getExpiredTokens
parameter_list|()
function_decl|;
comment|/**      * Returns the<code>Token</code> of the given id      * @param id      * @return The requested<code>Token</code> identified by the given id      */
name|SecurityToken
name|getToken
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the<code>Token</code> by the associated hash.       * @param hashCode      * @return the<code>Token</code> by the associated hash.       */
name|SecurityToken
name|getTokenByAssociatedHash
parameter_list|(
name|int
name|hashCode
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

