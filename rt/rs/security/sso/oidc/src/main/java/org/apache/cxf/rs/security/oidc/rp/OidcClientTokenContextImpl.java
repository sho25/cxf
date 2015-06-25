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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|client
operator|.
name|ClientTokenContextImpl
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
name|OidcClientTokenContextImpl
extends|extends
name|ClientTokenContextImpl
implements|implements
name|OidcClientTokenContext
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|117239739331303618L
decl_stmt|;
specifier|private
name|IdToken
name|idToken
decl_stmt|;
specifier|private
name|UserInfo
name|userInfo
decl_stmt|;
specifier|public
name|OidcClientTokenContextImpl
parameter_list|()
block|{              }
specifier|public
name|OidcClientTokenContextImpl
parameter_list|(
name|IdToken
name|idToken
parameter_list|)
block|{
name|this
operator|.
name|idToken
operator|=
name|idToken
expr_stmt|;
block|}
specifier|public
name|IdToken
name|getIdToken
parameter_list|()
block|{
return|return
name|idToken
return|;
block|}
specifier|public
name|void
name|setIdToken
parameter_list|(
name|IdToken
name|idToken
parameter_list|)
block|{
name|this
operator|.
name|idToken
operator|=
name|idToken
expr_stmt|;
block|}
specifier|public
name|UserInfo
name|getUserInfo
parameter_list|()
block|{
return|return
name|userInfo
return|;
block|}
specifier|public
name|void
name|setUserInfo
parameter_list|(
name|UserInfo
name|userInfo
parameter_list|)
block|{
name|this
operator|.
name|userInfo
operator|=
name|userInfo
expr_stmt|;
block|}
block|}
end_class

end_unit

