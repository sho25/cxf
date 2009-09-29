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
name|servicemix
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http_osgi
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|DestinationFactory
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
name|transport
operator|.
name|DestinationFactoryManager
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPTransportFactory
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiTransportFactory
extends|extends
name|AbstractHTTPTransportFactory
implements|implements
name|DestinationFactory
block|{
specifier|private
name|OsgiDestinationRegistryIntf
name|registry
decl_stmt|;
specifier|public
name|void
name|setRegistry
parameter_list|(
name|OsgiDestinationRegistryIntf
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"bus should not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"registry should not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|activationNamespaces
operator|==
literal|null
condition|)
block|{
name|activationNamespaces
operator|=
name|getTransportIds
argument_list|()
expr_stmt|;
block|}
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dfm
operator|!=
literal|null
operator|&&
name|activationNamespaces
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|activationNamespaces
control|)
block|{
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|URI
operator|.
name|create
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Endpoint address should be a relative URI "
operator|+
literal|"wrt to the servlet address (use '/xxx' for example)"
argument_list|)
throw|;
block|}
name|OsgiDestination
name|d
init|=
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|String
name|path
init|=
name|OsgiDestinationRegistry
operator|.
name|getTrimmedPath
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
name|d
operator|=
operator|new
name|OsgiDestination
argument_list|(
name|getBus
argument_list|()
argument_list|,
literal|null
argument_list|,
name|endpointInfo
argument_list|,
name|registry
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addDestination
argument_list|(
name|path
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
block|}
end_class

end_unit

