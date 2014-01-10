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
name|BlueprintNameSpaceHandlerFactory
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
name|NamespaceHandlerRegisterer
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
name|HTTPConduitConfigurer
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
name|blueprint
operator|.
name|HttpBPHandler
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
name|BundleActivator
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
name|ManagedServiceFactory
import|;
end_import

begin_class
specifier|public
class|class
name|HTTPTransportActivator
implements|implements
name|BundleActivator
block|{
name|ServiceRegistration
name|reg
decl_stmt|;
name|ServiceRegistration
name|reg2
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigAdminHttpConduitConfigurer
name|conduitConfigurer
init|=
operator|new
name|ConfigAdminHttpConduitConfigurer
argument_list|()
decl_stmt|;
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
name|ConfigAdminHttpConduitConfigurer
operator|.
name|FACTORY_PID
argument_list|)
expr_stmt|;
name|reg2
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|ManagedServiceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|conduitConfigurer
argument_list|,
name|servProps
argument_list|)
expr_stmt|;
name|servProps
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|servProps
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
literal|"org.apache.cxf.http.conduit-configurer"
argument_list|)
expr_stmt|;
name|reg
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|HTTPConduitConfigurer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|conduitConfigurer
argument_list|,
name|servProps
argument_list|)
expr_stmt|;
name|BlueprintNameSpaceHandlerFactory
name|factory
init|=
operator|new
name|BlueprintNameSpaceHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|createNamespaceHandler
parameter_list|()
block|{
return|return
operator|new
name|HttpBPHandler
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|NamespaceHandlerRegisterer
operator|.
name|register
argument_list|(
name|context
argument_list|,
name|factory
argument_list|,
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|reg2
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

