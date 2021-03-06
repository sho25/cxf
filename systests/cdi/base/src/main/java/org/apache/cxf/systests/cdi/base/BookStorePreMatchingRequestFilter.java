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
name|systests
operator|.
name|cdi
operator|.
name|base
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
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
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
name|Context
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
name|UriInfo
import|;
end_import

begin_class
annotation|@
name|ApplicationScoped
annotation|@
name|PreMatching
specifier|public
class|class
name|BookStorePreMatchingRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setUriInfo
parameter_list|(
name|UriInfo
name|uriInfo
parameter_list|)
block|{
name|this
operator|.
name|uriInfo
operator|=
name|uriInfo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Contextual instances should be injected independently
if|if
condition|(
name|uriInfo
operator|==
literal|null
operator|||
name|uriInfo
operator|.
name|getBaseUri
argument_list|()
operator|==
literal|null
condition|)
block|{
name|requestContext
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|entity
argument_list|(
literal|"uriInfo is not set"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

