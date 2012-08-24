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
name|tokens
operator|.
name|mac
package|;
end_package

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
name|ServerAccessToken
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
name|utils
operator|.
name|OAuthConstants
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
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_class
specifier|public
class|class
name|MacAccessToken
extends|extends
name|ServerAccessToken
block|{
specifier|public
name|MacAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|long
name|lifetime
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|HmacAlgorithm
operator|.
name|HmacSHA256
argument_list|,
name|lifetime
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MacAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|macAuthAlgo
parameter_list|,
name|long
name|lifetime
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|HmacAlgorithm
operator|.
name|toHmacAlgorithm
argument_list|(
name|macAuthAlgo
argument_list|)
argument_list|,
name|lifetime
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MacAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|HmacAlgorithm
name|macAlgo
parameter_list|,
name|long
name|lifetime
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
name|macAlgo
argument_list|,
name|OAuthUtils
operator|.
name|generateRandomTokenKey
argument_list|()
argument_list|,
name|lifetime
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MacAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|HmacAlgorithm
name|algo
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|lifetime
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|MAC_TOKEN_TYPE
argument_list|,
name|tokenKey
argument_list|,
name|lifetime
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
name|this
operator|.
name|setExtraParameters
argument_list|(
name|algo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MacAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|HmacAlgorithm
name|algo
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|String
name|tokenSecret
parameter_list|,
name|long
name|lifetime
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|MAC_TOKEN_TYPE
argument_list|,
name|tokenKey
argument_list|,
name|lifetime
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
name|this
operator|.
name|setExtraParameters
argument_list|(
name|algo
argument_list|,
name|tokenSecret
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setExtraParameters
parameter_list|(
name|HmacAlgorithm
name|algo
parameter_list|,
name|String
name|secret
parameter_list|)
block|{
name|String
name|theSecret
init|=
name|secret
operator|==
literal|null
condition|?
name|HmacUtils
operator|.
name|generateSecret
argument_list|(
name|algo
argument_list|)
else|:
name|secret
decl_stmt|;
name|super
operator|.
name|getParameters
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_SECRET
argument_list|,
name|theSecret
argument_list|)
expr_stmt|;
name|super
operator|.
name|getParameters
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_ALGORITHM
argument_list|,
name|algo
operator|.
name|getOAuthName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getMacKey
parameter_list|()
block|{
return|return
name|super
operator|.
name|getTokenKey
argument_list|()
return|;
block|}
specifier|public
name|String
name|getMacSecret
parameter_list|()
block|{
return|return
name|super
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_SECRET
argument_list|)
return|;
block|}
specifier|public
name|String
name|getMacAlgorithm
parameter_list|()
block|{
return|return
name|super
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_ALGORITHM
argument_list|)
return|;
block|}
block|}
end_class

end_unit

