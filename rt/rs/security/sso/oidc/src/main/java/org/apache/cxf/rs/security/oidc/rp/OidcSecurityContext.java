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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|SecurityContext
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
name|common
operator|.
name|security
operator|.
name|SimpleSecurityContext
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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

begin_class
specifier|public
class|class
name|OidcSecurityContext
extends|extends
name|SimpleSecurityContext
implements|implements
name|SecurityContext
block|{
specifier|private
name|OidcClientTokenContext
name|oidcContext
decl_stmt|;
specifier|public
name|OidcSecurityContext
parameter_list|(
name|IdToken
name|token
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|OidcClientTokenContextImpl
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OidcSecurityContext
parameter_list|(
name|OidcClientTokenContext
name|oidcContext
parameter_list|)
block|{
name|super
argument_list|(
name|getUserName
argument_list|(
name|oidcContext
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|oidcContext
operator|=
name|oidcContext
expr_stmt|;
block|}
specifier|public
name|OidcClientTokenContext
name|getOidcContext
parameter_list|()
block|{
return|return
name|oidcContext
return|;
block|}
specifier|private
specifier|static
name|String
name|getUserName
parameter_list|(
name|OidcClientTokenContext
name|oidcContext
parameter_list|)
block|{
if|if
condition|(
name|oidcContext
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|oidcContext
operator|.
name|getUserInfo
argument_list|()
operator|.
name|getEmail
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|oidcContext
operator|.
name|getIdToken
argument_list|()
operator|.
name|getEmail
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
name|String
name|value
init|=
name|HttpUtils
operator|.
name|getEndpointAddress
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|value
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthenticationScheme
parameter_list|()
block|{
return|return
literal|"OIDC"
return|;
block|}
block|}
end_class

end_unit

