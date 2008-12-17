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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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

begin_comment
comment|/**  * A simple contract resolver registry. It maintains a list of contract resolvers in an  *<code>ArrayList</code>.  */
end_comment

begin_class
specifier|public
class|class
name|ServiceContractResolverRegistryImpl
implements|implements
name|ServiceContractResolverRegistry
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ServiceContractResolver
argument_list|>
name|resolvers
decl_stmt|;
comment|/**      * Initialize registry, and register itself on Bus as an extension.      */
annotation|@
name|PostConstruct
specifier|public
name|void
name|init
parameter_list|()
block|{
name|resolvers
operator|=
operator|new
name|ArrayList
argument_list|<
name|ServiceContractResolver
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|ServiceContractResolverRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Calls each of the registered<code>ServiceContractResolver</code> instances      * to resolve the location of the service's contract. It returns the location       * from the first resolver that matches the QName to a location.      *      * @param qname QName to be resolved into a contract location      * @return URI representing the location of the contract     */
specifier|public
name|URI
name|getContractLocation
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
for|for
control|(
name|ServiceContractResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|URI
name|contractLocation
init|=
name|resolver
operator|.
name|getContractLocation
argument_list|(
name|qname
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|contractLocation
condition|)
block|{
return|return
name|contractLocation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Tests if a resolver is alreadey registered with this registry.      *      * @param resolver the contract resolver for which to searche      * @return<code>true</code> if the resolver is registered      */
specifier|public
name|boolean
name|isRegistered
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
block|{
return|return
name|resolvers
operator|.
name|contains
argument_list|(
name|resolver
argument_list|)
return|;
block|}
comment|/**      * Registers a contract resolver with this registry.      *      * @param resolver the contract resolver to register      */
specifier|public
specifier|synchronized
name|void
name|register
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
block|{
name|resolvers
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a contract resolver from this registry.      *      * @param resolver the contract resolver to remove      */
specifier|public
specifier|synchronized
name|void
name|unregister
parameter_list|(
name|ServiceContractResolver
name|resolver
parameter_list|)
block|{
name|resolvers
operator|.
name|remove
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the bus with which the registry is associated.      *      * @param bus      */
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|ServiceContractResolver
argument_list|>
name|getResolvers
parameter_list|()
block|{
return|return
name|resolvers
return|;
block|}
block|}
end_class

end_unit

