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
name|jaxrs
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Configurable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|FeatureContext
import|;
end_import

begin_comment
comment|/**  * Manages the creation of server-side {@code Configurable<FeatureContext>} depending on   * the presence of managed runtime (like CDI f.e.).   *   *<b>Note:</b> this interface may change in the future without a prior   * notice, please be aware of that.   */
end_comment

begin_interface
specifier|public
interface|interface
name|ServerConfigurableFactory
block|{
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|create
parameter_list|(
name|FeatureContext
name|context
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

