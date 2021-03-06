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
operator|.
name|services
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
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|BusFactory
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
name|bus
operator|.
name|blueprint
operator|.
name|BlueprintBus
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
name|CollectionUtils
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
name|wsn
operator|.
name|EndpointRegistrationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|osgi
operator|.
name|OSGIBusListener
operator|.
name|CONTEXT_NAME_PROPERTY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|osgi
operator|.
name|OSGIBusListener
operator|.
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|osgi
operator|.
name|OSGIBusListener
operator|.
name|CONTEXT_VERSION_PROPERTY
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|OSGiJaxwsEndpointManager
extends|extends
name|JaxwsEndpointManager
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|BlueprintContainer
name|container
decl_stmt|;
specifier|private
name|Object
name|cxfBus
decl_stmt|;
specifier|private
name|boolean
name|hasCXF
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|mbeanServer
operator|=
operator|(
name|MBeanServer
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|MBeanServer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|c
parameter_list|)
block|{
name|this
operator|.
name|container
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|register
parameter_list|(
name|String
name|address
parameter_list|,
name|Object
name|service
parameter_list|,
name|URL
name|wsdlLocation
parameter_list|)
throws|throws
name|EndpointRegistrationException
block|{
name|Object
name|o
init|=
name|setCXFBus
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|register
argument_list|(
name|address
argument_list|,
name|service
argument_list|,
name|wsdlLocation
argument_list|)
return|;
block|}
finally|finally
block|{
name|restoreCXFBus
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|restoreCXFBus
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|hasCXF
condition|)
block|{
name|restoreCXFBusInternal
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Object
name|setCXFBus
parameter_list|()
block|{
if|if
condition|(
name|cxfBus
operator|==
literal|null
operator|&&
name|hasCXF
condition|)
block|{
try|try
block|{
name|createCXFBus
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|hasCXF
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hasCXF
condition|)
block|{
return|return
name|setCXFBusInternal
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|cxfBus
operator|!=
literal|null
condition|)
block|{
name|destroyBus
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|destroyBus
parameter_list|()
block|{
operator|(
operator|(
name|Bus
operator|)
name|cxfBus
operator|)
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cxfBus
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|void
name|restoreCXFBusInternal
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
operator|(
name|Bus
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Object
name|setCXFBusInternal
parameter_list|()
block|{
return|return
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
operator|(
name|Bus
operator|)
name|cxfBus
argument_list|)
return|;
block|}
specifier|private
name|void
name|createCXFBus
parameter_list|()
block|{
name|BlueprintBus
name|bp
init|=
operator|new
name|BlueprintBus
argument_list|()
decl_stmt|;
name|bp
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|bp
operator|.
name|setBlueprintContainer
argument_list|(
name|container
argument_list|)
expr_stmt|;
name|bp
operator|.
name|setId
argument_list|(
literal|"WS-Notification"
argument_list|)
expr_stmt|;
name|bp
operator|.
name|initialize
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bundleContext
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_VERSION_PROPERTY
argument_list|,
name|getBundleVersion
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_NAME_PROPERTY
argument_list|,
name|bp
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|registerService
argument_list|(
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bp
argument_list|,
name|CollectionUtils
operator|.
name|toDictionary
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cxfBus
operator|=
name|bp
expr_stmt|;
block|}
specifier|private
name|Version
name|getBundleVersion
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|headers
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|version
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
return|return
operator|(
name|version
operator|!=
literal|null
operator|)
condition|?
name|Version
operator|.
name|parseVersion
argument_list|(
name|version
argument_list|)
else|:
name|Version
operator|.
name|emptyVersion
return|;
block|}
block|}
end_class

end_unit

