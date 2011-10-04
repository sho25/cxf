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
name|saml
operator|.
name|authorization
package|;
end_package

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
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|RequestHandler
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
name|model
operator|.
name|ClassResourceInfo
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

begin_class
specifier|public
class|class
name|ClaimsAuthorizingFilter
implements|implements
name|RequestHandler
block|{
specifier|private
name|ClaimsAuthorizingInterceptor
name|interceptor
decl_stmt|;
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
try|try
block|{
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|FORBIDDEN
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setClaims
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ClaimBean
argument_list|>
argument_list|>
name|claimsMap
parameter_list|)
block|{
name|checkInterceptor
argument_list|()
expr_stmt|;
name|ClaimsAuthorizingInterceptor
name|simple
init|=
operator|new
name|ClaimsAuthorizingInterceptor
argument_list|()
decl_stmt|;
name|simple
operator|.
name|setClaims
argument_list|(
name|claimsMap
argument_list|)
expr_stmt|;
name|interceptor
operator|=
name|simple
expr_stmt|;
block|}
specifier|public
name|void
name|setSecuredObject
parameter_list|(
name|Object
name|securedObject
parameter_list|)
block|{
name|checkInterceptor
argument_list|()
expr_stmt|;
name|ClaimsAuthorizingInterceptor
name|simple
init|=
operator|new
name|ClaimsAuthorizingInterceptor
argument_list|()
decl_stmt|;
name|simple
operator|.
name|setSecuredObject
argument_list|(
name|securedObject
argument_list|)
expr_stmt|;
name|interceptor
operator|=
name|simple
expr_stmt|;
block|}
specifier|private
name|void
name|checkInterceptor
parameter_list|()
block|{
if|if
condition|(
name|interceptor
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Filter has already been initialized"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

