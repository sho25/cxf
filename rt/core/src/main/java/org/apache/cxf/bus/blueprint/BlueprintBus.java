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
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|utils
operator|.
name|BundleDelegatingClassLoader
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
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
specifier|public
name|BlueprintBus
parameter_list|()
block|{     }
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|c
parameter_list|)
block|{
name|context
operator|=
name|c
expr_stmt|;
name|super
operator|.
name|setExtension
argument_list|(
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
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//setExtension(new ConfigurerImpl(c), Configurer.class);
block|}
block|}
end_class

end_unit

