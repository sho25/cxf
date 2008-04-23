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
name|binding
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusException
import|;
end_import

begin_comment
comment|/**  * The manager interface represents a repository for accessing   *<code>BindingFactory</code>s.  *  * Provides methods necessary for registering, deregistering or retrieving of  * BindingFactorys.  */
end_comment

begin_interface
specifier|public
interface|interface
name|BindingFactoryManager
block|{
comment|/**      * Registers a BindingFactory using the provided name.      *      * @param name The name of the BindingFactory.      * @param binding The instance of the class that implements the      * BindingFactory interface.      */
name|void
name|registerBindingFactory
parameter_list|(
name|String
name|name
parameter_list|,
name|BindingFactory
name|binding
parameter_list|)
function_decl|;
comment|/**      * Deregisters the BindingFactory with the provided name.      *      * @param name The name of the BindingFactory.      */
name|void
name|unregisterBindingFactory
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Retrieves the BindingFactory registered with the given name.      *      * @param name The name of the BindingFactory.      * @return BindingFactory The registered BindingFactory.      * @throws BusException If there is an error retrieving the BindingFactory.      */
name|BindingFactory
name|getBindingFactory
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|BusException
function_decl|;
block|}
end_interface

end_unit

