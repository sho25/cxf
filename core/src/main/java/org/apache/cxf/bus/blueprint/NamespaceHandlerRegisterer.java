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
name|aries
operator|.
name|blueprint
operator|.
name|NamespaceHandler
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|NamespaceHandlerRegisterer
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
name|NamespaceHandlerRegisterer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|NamespaceHandlerRegisterer
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|BundleContext
name|bc
parameter_list|,
name|BlueprintNameSpaceHandlerFactory
name|factory
parameter_list|,
name|String
modifier|...
name|namespaces
parameter_list|)
block|{
try|try
block|{
name|Object
name|handler
init|=
name|factory
operator|.
name|createNamespaceHandler
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|namespaces
control|)
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
literal|"osgi.service.blueprint.namespace"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|bc
operator|.
name|registerService
argument_list|(
name|NamespaceHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|handler
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Registered blueprint namespace handler for "
operator|+
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
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
literal|"Aries Blueprint packages not available. So namespaces will not be registered"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Unexpected exception when trying to install Aries Blueprint namespaces"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

