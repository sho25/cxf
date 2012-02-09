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
name|io
operator|.
name|Serializable
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * This bean represents a resource owner authorization challenge.  * Typically, an HTML view will be returned to a resource owner who  * will authorize or deny the third-party consumer  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"authorizationData"
argument_list|,
name|namespace
operator|=
literal|"http://org.apache.cxf.rs.security.oauth"
argument_list|)
specifier|public
class|class
name|OAuthAuthorizationData
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7755998413495017637L
decl_stmt|;
specifier|private
name|String
name|oauthToken
decl_stmt|;
specifier|private
name|String
name|authenticityToken
decl_stmt|;
specifier|private
name|String
name|applicationName
decl_stmt|;
specifier|private
name|String
name|applicationURI
decl_stmt|;
specifier|private
name|String
name|applicationDescription
decl_stmt|;
specifier|private
name|String
name|logoUri
decl_stmt|;
specifier|private
name|String
name|replyTo
decl_stmt|;
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|permissions
decl_stmt|;
specifier|public
name|OAuthAuthorizationData
parameter_list|()
block|{     }
specifier|public
name|OAuthAuthorizationData
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|getPermissions
parameter_list|()
block|{
return|return
name|permissions
return|;
block|}
specifier|public
name|void
name|setPermissions
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Permission
argument_list|>
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|permissions
operator|=
name|permissions
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthenticityToken
parameter_list|(
name|String
name|authenticityToken
parameter_list|)
block|{
name|this
operator|.
name|authenticityToken
operator|=
name|authenticityToken
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthenticityToken
parameter_list|()
block|{
return|return
name|authenticityToken
return|;
block|}
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyTo
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
block|}
specifier|public
name|String
name|getReplyTo
parameter_list|()
block|{
return|return
name|replyTo
return|;
block|}
specifier|public
name|void
name|setApplicationURI
parameter_list|(
name|String
name|applicationURI
parameter_list|)
block|{
name|this
operator|.
name|applicationURI
operator|=
name|applicationURI
expr_stmt|;
block|}
specifier|public
name|String
name|getApplicationURI
parameter_list|()
block|{
return|return
name|applicationURI
return|;
block|}
specifier|public
name|void
name|setApplicationDescription
parameter_list|(
name|String
name|applicationDescription
parameter_list|)
block|{
name|this
operator|.
name|applicationDescription
operator|=
name|applicationDescription
expr_stmt|;
block|}
specifier|public
name|String
name|getApplicationDescription
parameter_list|()
block|{
return|return
name|applicationDescription
return|;
block|}
specifier|public
name|void
name|setLogoUri
parameter_list|(
name|String
name|logoPath
parameter_list|)
block|{
name|this
operator|.
name|logoUri
operator|=
name|logoPath
expr_stmt|;
block|}
specifier|public
name|String
name|getLogoUri
parameter_list|()
block|{
return|return
name|logoUri
return|;
block|}
block|}
end_class

end_unit

