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
name|oidc
operator|.
name|rp
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
name|jaxrs
operator|.
name|ext
operator|.
name|ContextProvider
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|client
operator|.
name|ClientTokenContext
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
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
name|oidc
operator|.
name|common
operator|.
name|UserInfo
import|;
end_import

begin_class
specifier|public
class|class
name|OidcUserInfoProvider
implements|implements
name|ContextProvider
argument_list|<
name|UserInfoContext
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|UserInfoContext
name|createContext
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
specifier|final
name|OidcClientTokenContext
name|ctx
init|=
operator|(
name|OidcClientTokenContext
operator|)
name|m
operator|.
name|getContent
argument_list|(
name|ClientTokenContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|UserInfo
name|userInfo
init|=
name|ctx
operator|!=
literal|null
condition|?
name|ctx
operator|.
name|getUserInfo
argument_list|()
else|:
name|m
operator|.
name|getContent
argument_list|(
name|UserInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
condition|)
block|{
specifier|final
name|IdToken
name|idToken
init|=
name|ctx
operator|!=
literal|null
condition|?
name|ctx
operator|.
name|getIdToken
argument_list|()
else|:
name|m
operator|.
name|getContent
argument_list|(
name|IdToken
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|UserInfoContext
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|UserInfo
name|getUserInfo
parameter_list|()
block|{
return|return
name|userInfo
return|;
block|}
annotation|@
name|Override
specifier|public
name|IdToken
name|getIdToken
parameter_list|()
block|{
return|return
name|idToken
return|;
block|}
block|}
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

