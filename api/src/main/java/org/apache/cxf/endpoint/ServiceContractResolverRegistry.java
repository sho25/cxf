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
name|endpoint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * A registry for maintaining a collection of contract resolvers.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ServiceContractResolverRegistry
block|{
comment|/**      * Resolves a QName to a URI respresenting the location of a WSDL contract.      * The registry method is called by the bus and should use the       *<code>getContractLocation</code> methods of the registered contract       * resolvers to do the actual resolution.      *      * @param qname the qname to resolve into a URI      * @return URI representing the WSDL contract's location      */
name|URI
name|getContractLocation
parameter_list|(
name|QName
name|qname
parameter_list|)
function_decl|;
comment|/**      * Registers a contract resolver.      *      * @param resolver the contract resolver being registered      */
name|void
name|register
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Removes a contract resolver from the registry.      *      * @param resolver the contract resolver being removed      */
name|void
name|unregister
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Determines if a contract resolver is already registered with a      * registry.      *      * @param resolver the contract resolver for which to search      * @return<code>true</code> if the contract resolver is already registered      */
name|boolean
name|isRegistered
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

