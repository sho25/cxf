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
name|bus
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|extension
operator|.
name|ExtensionManagerBus
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
name|ConfiguredBeanLocator
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
name|Configurer
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
name|resource
operator|.
name|ClassLoaderResolver
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
name|resource
operator|.
name|ResourceManager
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
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|BlueprintBus
extends|extends
name|ExtensionManagerBus
block|{
name|BundleContext
name|context
decl_stmt|;
name|BlueprintContainer
name|container
decl_stmt|;
specifier|public
name|BlueprintBus
parameter_list|()
block|{
comment|// Using the BlueprintBus Classloader to load the extensions
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|BlueprintBus
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|loadAdditionalFeatures
parameter_list|()
block|{
name|super
operator|.
name|loadAdditionalFeatures
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
specifier|final
name|BundleContext
name|c
parameter_list|)
block|{
name|context
operator|=
name|c
expr_stmt|;
name|ClassLoader
name|bundleClassLoader
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
operator|new
name|BundleDelegatingClassLoader
argument_list|(
name|c
operator|.
name|getBundle
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|super
operator|.
name|setExtension
argument_list|(
name|bundleClassLoader
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Setup the resource resolver with the bundle classloader
name|ResourceManager
name|rm
init|=
name|super
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|rm
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ClassLoaderResolver
argument_list|(
name|bundleClassLoader
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|setExtension
argument_list|(
name|c
argument_list|,
name|BundleContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|con
parameter_list|)
block|{
name|container
operator|=
name|con
expr_stmt|;
name|setExtension
argument_list|(
operator|new
name|ConfigurerImpl
argument_list|(
name|con
argument_list|)
argument_list|,
name|Configurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|setExtension
argument_list|(
operator|new
name|BlueprintBeanLocator
argument_list|(
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
argument_list|,
name|container
argument_list|,
name|context
argument_list|)
argument_list|,
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|super
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|id
operator|=
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"-"
operator|+
name|DEFAULT_BUS_ID
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|this
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

