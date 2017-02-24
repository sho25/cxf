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
name|ext
operator|.
name|logging
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
name|Dictionary
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ext
operator|.
name|logging
operator|.
name|LoggingFeature
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
name|feature
operator|.
name|AbstractFeature
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
name|feature
operator|.
name|Feature
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Activator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG_PID
init|=
literal|"org.apache.cxf.features.logging"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
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
name|CONFIG_PID
argument_list|)
expr_stmt|;
name|bundleContext
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
operator|new
name|ConfigUpdater
argument_list|(
name|bundleContext
argument_list|)
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
block|{      }
specifier|private
specifier|static
specifier|final
class|class
name|ConfigUpdater
implements|implements
name|ManagedService
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ServiceRegistration
name|serviceReg
decl_stmt|;
specifier|private
name|ServiceRegistration
name|intentReg
decl_stmt|;
specifier|private
name|LoggingFeature
name|logging
decl_stmt|;
name|ConfigUpdater
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|logging
operator|=
operator|new
name|LoggingFeature
argument_list|()
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|updated
parameter_list|(
name|Dictionary
name|config
parameter_list|)
throws|throws
name|ConfigurationException
block|{
name|boolean
name|enabled
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"enabled"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"CXF message logging feature "
operator|+
operator|(
name|enabled
condition|?
literal|"enabled"
else|:
literal|"disabled"
operator|)
argument_list|)
expr_stmt|;
name|Integer
name|limit
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"limit"
argument_list|,
literal|"65536"
argument_list|)
argument_list|)
decl_stmt|;
name|Boolean
name|pretty
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"pretty"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
name|Boolean
name|verbose
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"verbose"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
name|Long
name|inMemThreshold
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"inMemThresHold"
argument_list|,
literal|"-1"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|!=
literal|null
condition|)
block|{
name|logging
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|inMemThreshold
operator|!=
literal|null
condition|)
block|{
name|logging
operator|.
name|setInMemThreshold
argument_list|(
name|inMemThreshold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pretty
operator|!=
literal|null
condition|)
block|{
name|logging
operator|.
name|setPrettyLogging
argument_list|(
name|pretty
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verbose
operator|!=
literal|null
condition|)
block|{
name|logging
operator|.
name|setVerbose
argument_list|(
name|verbose
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|intentReg
operator|==
literal|null
condition|)
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.dosgi.IntentName"
argument_list|,
literal|"logging"
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|registerService
argument_list|(
name|AbstractFeature
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|logging
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enabled
condition|)
block|{
if|if
condition|(
name|serviceReg
operator|==
literal|null
condition|)
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"logging"
argument_list|)
expr_stmt|;
name|serviceReg
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|Feature
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|logging
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|serviceReg
operator|!=
literal|null
condition|)
block|{
name|serviceReg
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|serviceReg
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
name|String
name|getValue
parameter_list|(
name|Dictionary
name|config
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
name|String
name|value
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|defaultValue
return|;
block|}
block|}
block|}
end_class

end_unit

