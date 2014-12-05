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
name|transport
operator|.
name|http
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Servlet
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
name|DestinationRegistry
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
name|servlet
operator|.
name|CXFNonSpringServlet
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
name|ServiceReference
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
name|ServiceRegistration
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
name|cm
operator|.
name|ConfigurationException
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
name|cm
operator|.
name|ManagedService
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
name|http
operator|.
name|HttpService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
import|;
end_import

begin_class
specifier|final
class|class
name|HttpServiceTrackerCust
implements|implements
name|ServiceTrackerCustomizer
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CXF_CONFIG_PID
init|=
literal|"org.apache.cxf.osgi"
decl_stmt|;
specifier|private
specifier|final
name|DestinationRegistry
name|destinationRegistry
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|context
decl_stmt|;
specifier|private
name|ServiceRegistration
name|servletPublisherReg
decl_stmt|;
specifier|private
name|ServletExporter
name|servletExporter
decl_stmt|;
name|HttpServiceTrackerCust
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|destinationRegistry
operator|=
name|destinationRegistry
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
name|reference
parameter_list|,
name|Object
name|service
parameter_list|)
block|{
name|servletPublisherReg
operator|.
name|unregister
argument_list|()
expr_stmt|;
try|try
block|{
name|servletExporter
operator|.
name|updated
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|modifiedService
parameter_list|(
name|ServiceReference
name|reference
parameter_list|,
name|Object
name|service
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|Object
name|addingService
parameter_list|(
name|ServiceReference
name|reference
parameter_list|)
block|{
name|HttpService
name|httpService
init|=
operator|(
name|HttpService
operator|)
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|Servlet
name|servlet
init|=
operator|new
name|CXFNonSpringServlet
argument_list|(
name|destinationRegistry
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|servletExporter
operator|=
operator|new
name|ServletExporter
argument_list|(
name|servlet
argument_list|,
name|httpService
argument_list|)
expr_stmt|;
name|Properties
name|servProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|servProps
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
name|CXF_CONFIG_PID
argument_list|)
expr_stmt|;
name|servletPublisherReg
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|ManagedService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|servletExporter
argument_list|,
name|servProps
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

