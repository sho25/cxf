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
name|asyncclient
package|;
end_package

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
name|Enumeration
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
name|Hashtable
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
name|ServiceReference
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
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|private
name|ConduitConfigurer
name|conduitConfigurer
decl_stmt|;
annotation|@
name|Override
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
name|conduitConfigurer
operator|=
operator|new
name|ConduitConfigurer
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|conduitConfigurer
operator|.
name|open
argument_list|()
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
literal|"org.apache.cxf.transport.http.async"
argument_list|)
expr_stmt|;
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
name|conduitConfigurer
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|conduitConfigurer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
class|class
name|ConduitConfigurer
extends|extends
name|ServiceTracker
implements|implements
name|ManagedService
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|currentConfig
decl_stmt|;
specifier|public
name|ConduitConfigurer
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|updated
parameter_list|(
name|Dictionary
name|properties
parameter_list|)
throws|throws
name|ConfigurationException
block|{
name|this
operator|.
name|currentConfig
operator|=
name|toMap
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Bus
index|[]
name|buses
init|=
operator|(
name|Bus
index|[]
operator|)
name|getServices
argument_list|()
decl_stmt|;
if|if
condition|(
name|buses
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Bus
name|bus
range|:
name|buses
control|)
block|{
name|configureConduitFactory
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|super
operator|.
name|addingService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|configureConduitFactory
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|bus
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|toMap
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
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
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
return|return
name|props
return|;
block|}
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|properties
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
specifier|private
name|void
name|configureConduitFactory
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|AsyncHTTPConduitFactory
name|conduitFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AsyncHTTPConduitFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|conduitFactory
operator|.
name|update
argument_list|(
name|this
operator|.
name|currentConfig
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

