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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|HttpHeaders
import|;
end_import

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
name|Response
import|;
end_import

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
name|Response
operator|.
name|ResponseBuilder
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
name|Base64Utility
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
name|ext
operator|.
name|MessageContext
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
name|ExceptionUtils
import|;
end_import

begin_comment
comment|/**  * Authorization helpers  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AuthorizationUtils
block|{
specifier|private
name|AuthorizationUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
index|[]
name|getBasicAuthParts
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|String
name|authDecoded
init|=
literal|null
decl_stmt|;
try|try
block|{
name|authDecoded
operator|=
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|authInfo
index|[]
init|=
name|authDecoded
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|authInfo
operator|.
name|length
operator|==
literal|2
condition|)
block|{
return|return
name|authInfo
return|;
block|}
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|getAuthorizationParts
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|getAuthorizationParts
argument_list|(
name|mc
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
literal|"Basic"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|getAuthorizationParts
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|challenges
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|headers
init|=
name|mc
operator|.
name|getHttpHeaders
argument_list|()
operator|.
name|getRequestHeader
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|headers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
return|return
name|parts
return|;
block|}
block|}
name|throwAuthorizationFailure
argument_list|(
name|challenges
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|void
name|throwAuthorizationFailure
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|challenges
parameter_list|)
block|{
name|throwAuthorizationFailure
argument_list|(
name|challenges
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|throwAuthorizationFailure
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|challenges
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
name|ResponseBuilder
name|rb
init|=
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|challenge
range|:
name|challenges
control|)
block|{
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|challenge
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|challenge
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" realm=\""
operator|+
name|realm
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|rb
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Response
name|r
init|=
name|rb
operator|.
name|build
argument_list|()
decl_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|r
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

