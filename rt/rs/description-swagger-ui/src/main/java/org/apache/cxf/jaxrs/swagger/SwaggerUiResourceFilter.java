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
name|jaxrs
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
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
name|HttpMethod
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
name|Priorities
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|UriInfo
import|;
end_import

begin_class
annotation|@
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|USER
argument_list|)
class|class
name|SwaggerUiResourceFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|".*[.]js|.*[.]gz|.*[.]map|oauth2*[.]html|.*[.]png|.*[.]css|.*[.]ico|"
operator|+
literal|"/css/.*|/images/.*|/lib/.*|/fonts/.*"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|HttpMethod
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|rc
operator|.
name|getRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|UriInfo
name|ui
init|=
name|rc
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
name|String
name|path
init|=
literal|"/"
operator|+
name|ui
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|PATTERN
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|rc
operator|.
name|setRequestUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"api-docs"
operator|+
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

