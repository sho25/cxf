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
name|databinding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Provides for alternative ways of initializing DataBindings  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|PropertiesAwareDataBinding
extends|extends
name|DataBinding
block|{
comment|/**      * Identifies org.apache.cxf.service.Service model object      */
name|String
name|SERVICE_MODEL_PROPERTY
init|=
literal|"org.apache.cxf.databinding.service"
decl_stmt|;
comment|/**      * Indentifies a set of all classes which can be used to initialize a DataBinding      */
name|String
name|TYPES_PROPERTY
init|=
literal|"org.apache.cxf.databinding.types"
decl_stmt|;
comment|/**      * Initialize a databinding with provided properties       * @param properties properties which contain information       *        like classes, Service models, etc      */
name|void
name|initialize
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

