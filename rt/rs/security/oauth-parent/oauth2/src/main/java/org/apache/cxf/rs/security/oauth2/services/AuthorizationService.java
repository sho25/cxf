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
name|services
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Consumes
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
name|GET
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
name|POST
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
name|Path
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
name|Produces
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
name|QueryParam
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
name|MultivaluedMap
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
annotation|@
name|Path
argument_list|(
literal|"authorize"
argument_list|)
specifier|public
class|class
name|AuthorizationService
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RedirectionBasedGrantService
argument_list|>
name|servicesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|RedirectionBasedGrantService
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
for|for
control|(
name|RedirectionBasedGrantService
name|service
range|:
name|servicesMap
operator|.
name|values
argument_list|()
control|)
block|{
name|service
operator|.
name|setMessageContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xhtml+xml"
block|,
literal|"text/html"
block|,
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
specifier|public
name|Response
name|authorize
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
name|String
name|responseType
parameter_list|)
block|{
return|return
name|getService
argument_list|(
name|responseType
argument_list|)
operator|.
name|authorize
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/decision"
argument_list|)
specifier|public
name|Response
name|authorizeDecision
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
name|String
name|responseType
parameter_list|)
block|{
return|return
name|getService
argument_list|(
name|responseType
argument_list|)
operator|.
name|authorizeDecision
argument_list|()
return|;
block|}
comment|/**      * Processes the end user decision      * @return The grant value, authorization code or the token      */
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/decision"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|authorizeDecisionForm
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|String
name|responseType
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
decl_stmt|;
return|return
name|getService
argument_list|(
name|responseType
argument_list|)
operator|.
name|authorizeDecisionForm
argument_list|(
name|params
argument_list|)
return|;
block|}
specifier|private
name|RedirectionBasedGrantService
name|getService
parameter_list|(
name|String
name|responseType
parameter_list|)
block|{
if|if
condition|(
name|responseType
operator|==
literal|null
operator|||
operator|!
name|servicesMap
operator|.
name|containsKey
argument_list|(
name|responseType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
return|return
name|servicesMap
operator|.
name|get
argument_list|(
name|responseType
argument_list|)
return|;
block|}
specifier|public
name|void
name|setServices
parameter_list|(
name|List
argument_list|<
name|RedirectionBasedGrantService
argument_list|>
name|services
parameter_list|)
block|{
for|for
control|(
name|RedirectionBasedGrantService
name|service
range|:
name|services
control|)
block|{
for|for
control|(
name|String
name|responseType
range|:
name|service
operator|.
name|getSupportedResponseTypes
argument_list|()
control|)
block|{
name|servicesMap
operator|.
name|put
argument_list|(
name|responseType
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

