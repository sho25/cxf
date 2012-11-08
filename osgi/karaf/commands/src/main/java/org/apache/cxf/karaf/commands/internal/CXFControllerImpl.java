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
name|karaf
operator|.
name|commands
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|karaf
operator|.
name|commands
operator|.
name|CXFController
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
name|ConfigurationAdmin
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CXFControllerImpl
implements|implements
name|CXFController
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CXFControllerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|configAdmin
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
block|}
specifier|public
name|List
argument_list|<
name|Bus
argument_list|>
name|getBusses
parameter_list|()
block|{
name|List
argument_list|<
name|Bus
argument_list|>
name|busses
init|=
operator|new
name|ArrayList
argument_list|<
name|Bus
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|busses
operator|.
name|add
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Cannot retrieve the list of CXF Busses."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|busses
return|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|!=
literal|null
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"cxf.bus.id"
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|(
name|Bus
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Cannot retrieve the CXF Bus."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Cannot retrieve the CXF Bus "
operator|+
name|name
operator|+
literal|"."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
return|return
name|configAdmin
return|;
block|}
specifier|public
name|void
name|setConfigAdmin
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

