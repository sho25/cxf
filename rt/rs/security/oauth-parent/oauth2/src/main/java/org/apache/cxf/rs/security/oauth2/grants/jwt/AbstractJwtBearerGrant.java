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
operator|.
name|jwt
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
name|common
operator|.
name|util
operator|.
name|Base64UrlUtility
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
name|provider
operator|.
name|OAuthServiceException
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJwtBearerGrant
implements|implements
name|AccessTokenGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5754722119855372511L
decl_stmt|;
specifier|private
name|String
name|assertion
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|boolean
name|encoded
decl_stmt|;
specifier|private
name|String
name|grantType
decl_stmt|;
specifier|protected
name|AbstractJwtBearerGrant
parameter_list|(
name|String
name|grantType
parameter_list|,
name|String
name|assertion
parameter_list|,
name|boolean
name|encoded
parameter_list|,
name|String
name|scope
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
name|assertion
operator|=
name|assertion
expr_stmt|;
name|this
operator|.
name|encoded
operator|=
name|encoded
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|scope
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
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|initMap
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
name|grantType
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|protected
name|void
name|addScope
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
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
block|}
specifier|protected
name|String
name|encodeAssertion
parameter_list|()
block|{
if|if
condition|(
name|encoded
condition|)
block|{
return|return
name|assertion
return|;
block|}
try|try
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|assertion
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

