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
name|grants
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
name|MultivaluedMap
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
name|impl
operator|.
name|MetadataMap
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
name|AccessTokenGrant
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

begin_comment
comment|/**  * Abstract access token grant  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractGrant
implements|implements
name|AccessTokenGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3586571576928674560L
decl_stmt|;
specifier|private
name|String
name|grantType
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|String
name|audience
decl_stmt|;
specifier|protected
name|AbstractGrant
parameter_list|(
name|String
name|grantType
parameter_list|)
block|{
name|this
argument_list|(
name|grantType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractGrant
parameter_list|(
name|String
name|grantType
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
name|this
argument_list|(
name|grantType
argument_list|,
name|scope
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractGrant
parameter_list|(
name|String
name|grantType
parameter_list|,
name|String
name|scope
parameter_list|,
name|String
name|audience
parameter_list|)
block|{
name|this
operator|.
name|grantType
operator|=
name|grantType
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|audience
operator|=
name|audience
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|grantType
return|;
block|}
specifier|public
name|void
name|setAudience
parameter_list|(
name|String
name|audience
parameter_list|)
block|{
name|this
operator|.
name|audience
operator|=
name|audience
expr_stmt|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|,
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|audience
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|,
name|audience
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

