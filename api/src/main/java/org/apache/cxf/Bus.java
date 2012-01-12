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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|interceptor
operator|.
name|InterceptorProvider
import|;
end_import

begin_comment
comment|/**  * The Bus is the central place in CXF. Its primary responsibility is  * providing access to the different extensions (such as the DestinationFactoryManager,  * ConduitFactoryManager, BindingFactoryManager, etc). Depending on the implementation  * of the Bus it may also be responsible for wiring up the CXF internals.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Bus
extends|extends
name|InterceptorProvider
block|{
name|String
name|DEFAULT_BUS_ID
init|=
literal|"cxf"
decl_stmt|;
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
function_decl|;
parameter_list|<
name|T
parameter_list|>
name|void
name|setExtension
parameter_list|(
name|T
name|extension
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
function_decl|;
name|boolean
name|hasExtensionByName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|String
name|getId
parameter_list|()
function_decl|;
name|void
name|shutdown
parameter_list|(
name|boolean
name|wait
parameter_list|)
function_decl|;
name|void
name|run
parameter_list|()
function_decl|;
name|void
name|setProperty
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
function_decl|;
name|Object
name|getProperty
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

