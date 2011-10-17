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

begin_comment
comment|/**  * Request Token representation  */
end_comment

begin_class
specifier|public
class|class
name|RequestToken
extends|extends
name|Token
block|{
specifier|private
name|String
name|oauthVerifier
decl_stmt|;
specifier|private
name|String
name|callback
decl_stmt|;
specifier|private
name|String
name|state
decl_stmt|;
specifier|public
name|RequestToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenString
parameter_list|,
name|String
name|tokenSecret
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|tokenString
argument_list|,
name|tokenSecret
argument_list|,
operator|-
literal|1L
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RequestToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenString
parameter_list|,
name|String
name|tokenSecret
parameter_list|,
name|Long
name|lifetime
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|tokenString
argument_list|,
name|tokenSecret
argument_list|,
name|lifetime
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the token verifier      * @param oauthVerifier      */
specifier|public
name|void
name|setVerifier
parameter_list|(
name|String
name|verifier
parameter_list|)
block|{
name|this
operator|.
name|oauthVerifier
operator|=
name|verifier
expr_stmt|;
block|}
comment|/**      * Gets the token verifier      * @return the verifier      */
specifier|public
name|String
name|getVerifier
parameter_list|()
block|{
return|return
name|oauthVerifier
return|;
block|}
comment|/**      * Sets the callback URI       * @param callback the callback      */
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
comment|/**      * Gets the callback URI      * @return the callback      */
specifier|public
name|String
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
comment|/**      * Sets the state - it will be reported back to the consumer      * after the authorization decision on this token has been made.       * @param state      */
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
comment|/**      * Gets the state      * @return the state      */
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
block|}
end_class

end_unit

