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
name|systest
operator|.
name|sts
operator|.
name|rest
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|interceptor
operator|.
name|WSS4JBasicAuthValidator
import|;
end_import

begin_comment
comment|/**  * Extends the WSS4J validator as a JAX-RS request filter  */
end_comment

begin_class
specifier|public
class|class
name|WSS4JBasicAuthFilter
extends|extends
name|WSS4JBasicAuthValidator
implements|implements
name|ContainerRequestFilter
block|{
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
name|Message
name|message
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|AuthorizationPolicy
name|policy
init|=
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|==
literal|null
operator|||
name|policy
operator|.
name|getUserName
argument_list|()
operator|==
literal|null
operator|||
name|policy
operator|.
name|getPassword
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
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|header
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"Basic realm=\"IdP\""
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|super
operator|.
name|validate
argument_list|(
name|message
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
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

