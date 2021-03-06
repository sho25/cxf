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
name|wsn
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|CreatePullPoint
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractEndpoint
implements|implements
name|EndpointMBean
block|{
specifier|protected
specifier|final
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|address
decl_stmt|;
specifier|protected
name|EndpointManager
name|manager
decl_stmt|;
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
specifier|protected
name|W3CEndpointReference
name|endpointEpr
decl_stmt|;
specifier|public
name|AbstractEndpoint
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|ObjectName
name|getMBeanName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
specifier|public
specifier|final
name|URL
name|getWSDLLocation
parameter_list|()
block|{
return|return
name|CreatePullPoint
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/cxf/wsn/wsdl/wsn.wsdl"
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|register
parameter_list|()
throws|throws
name|EndpointRegistrationException
block|{
name|endpoint
operator|=
name|manager
operator|.
name|register
argument_list|(
name|getAddress
argument_list|()
argument_list|,
name|this
argument_list|,
name|getWSDLLocation
argument_list|()
argument_list|)
expr_stmt|;
name|endpointEpr
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|unregister
parameter_list|()
throws|throws
name|EndpointRegistrationException
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|unregister
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|endpointEpr
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|W3CEndpointReference
name|getEpr
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|endpointEpr
operator|==
literal|null
condition|)
block|{
name|endpointEpr
operator|=
name|manager
operator|.
name|getEpr
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|endpointEpr
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|EndpointManager
name|getManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
specifier|public
name|void
name|setManager
parameter_list|(
name|EndpointManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
block|}
end_class

end_unit

