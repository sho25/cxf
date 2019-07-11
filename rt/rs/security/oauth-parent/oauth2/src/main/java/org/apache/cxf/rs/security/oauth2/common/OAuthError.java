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
name|common
package|;
end_package

begin_comment
comment|/**  * Captures OAuth2 error properties  */
end_comment

begin_class
specifier|public
class|class
name|OAuthError
block|{
specifier|private
name|String
name|error
decl_stmt|;
specifier|private
name|String
name|errorDescription
decl_stmt|;
specifier|private
name|String
name|errorUri
decl_stmt|;
specifier|private
name|String
name|state
decl_stmt|;
specifier|public
name|OAuthError
parameter_list|()
block|{      }
specifier|public
name|OAuthError
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
specifier|public
name|OAuthError
parameter_list|(
name|String
name|error
parameter_list|,
name|String
name|descr
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
name|this
operator|.
name|errorDescription
operator|=
name|descr
expr_stmt|;
block|}
comment|/**      * Sets the error such as "invalid_grant", etc      * @param error the error      */
specifier|public
name|void
name|setError
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
comment|/**      * Gets the error      * @return error      */
specifier|public
name|String
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
comment|/**      * Sets the error description      * @param errorDescription error description      */
specifier|public
name|void
name|setErrorDescription
parameter_list|(
name|String
name|errorDescription
parameter_list|)
block|{
name|this
operator|.
name|errorDescription
operator|=
name|errorDescription
expr_stmt|;
block|}
comment|/**      * Gets the error description      * @return error description      */
specifier|public
name|String
name|getErrorDescription
parameter_list|()
block|{
return|return
name|errorDescription
return|;
block|}
comment|/**      * Sets the optional link to the page      * describing the error in detail      * @param errorUri error page URI      */
specifier|public
name|void
name|setErrorUri
parameter_list|(
name|String
name|errorUri
parameter_list|)
block|{
name|this
operator|.
name|errorUri
operator|=
name|errorUri
expr_stmt|;
block|}
comment|/**      * Gets the optional link to the page      * describing the error in detail      * @param errorUri error page URI      */
specifier|public
name|String
name|getErrorUri
parameter_list|()
block|{
return|return
name|errorUri
return|;
block|}
comment|/**      * Sets the client state token which needs to be returned      * to the client alongside the error information      * if it was provided during the client request      * @param state the client state token      */
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
comment|/**      * Gets the client state token      * @return the state      */
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OAuthError[error='"
operator|+
name|error
operator|+
literal|'\''
operator|+
literal|", errorDescription='"
operator|+
name|errorDescription
operator|+
literal|'\''
operator|+
literal|", errorUri='"
operator|+
name|errorUri
operator|+
literal|'\''
operator|+
literal|", state='"
operator|+
name|state
operator|+
literal|'\''
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

