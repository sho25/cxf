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
name|oauth
operator|.
name|data
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

begin_comment
comment|/**  * Captures the information associated with the request token registration request.  * @see RequestToken    */
end_comment

begin_class
specifier|public
class|class
name|RequestTokenRegistration
block|{
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|String
name|state
decl_stmt|;
specifier|private
name|String
name|callback
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|scopes
decl_stmt|;
specifier|private
name|long
name|lifetime
decl_stmt|;
specifier|private
name|long
name|issuedAt
decl_stmt|;
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
specifier|public
name|void
name|setCallback
parameter_list|(
name|String
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
specifier|public
name|String
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|void
name|setScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
specifier|public
name|void
name|setLifetime
parameter_list|(
name|long
name|lifetime
parameter_list|)
block|{
name|this
operator|.
name|lifetime
operator|=
name|lifetime
expr_stmt|;
block|}
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
specifier|public
name|void
name|setIssuedAt
parameter_list|(
name|long
name|issuedAt
parameter_list|)
block|{
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
specifier|public
name|long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|issuedAt
return|;
block|}
block|}
end_class

end_unit

