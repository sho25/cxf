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
name|managers
package|;
end_package

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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|endpoint
operator|.
name|EndpointResolver
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
name|endpoint
operator|.
name|EndpointResolverRegistry
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * This implementation class is responsible for mediating  * access to registered EndpointResolvers, which themselves map  * between abstract and concrete endpoint references, and/or  * facilitate renewal of stale references.  *<p>  * An underlying mechanism in the style of the OGSA WS-Naming  * specification is assumed, where an EPR maybe be fully abstract,  * or concrete but with sufficient information embedded to enable  * its renewal if necessary.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|EndpointResolverRegistryImpl
implements|implements
name|EndpointResolverRegistry
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|List
argument_list|<
name|EndpointResolver
argument_list|>
name|resolvers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|EndpointResolver
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EndpointResolverRegistryImpl
parameter_list|()
block|{              }
specifier|public
name|EndpointResolverRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param b Bus to encapsulate      */
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
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
name|EndpointResolverRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Register an endpoint resolver.      *      * @param resolver the EndpointResolver to add to the chain.      */
specifier|public
specifier|synchronized
name|void
name|register
parameter_list|(
name|EndpointResolver
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
comment|/**      * Unregister an endpoint resolver.      *      * @param resolver the EndpointResolver to remove from the chain.      */
specifier|public
specifier|synchronized
name|void
name|unregister
parameter_list|(
name|EndpointResolver
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
comment|/**      * Walk the list of registered EndpointResolvers, so as to      * retrieve a concrete EPR corresponding to the given abstract EPR,      * returning a cached reference if already resolved.      *<p>      * This API is used by any actor that requires a concrete EPR (e.g.      * a transport-level Conduit), and must be called each and every      * time the EPR content is to be accessed (e.g. before each connection      * establishment attempt).       *      * @param logical the abstract EPR to resolve      * @return the resolved concrete EPR if appropriate, null otherwise      */
specifier|public
specifier|synchronized
name|EndpointReferenceType
name|resolve
parameter_list|(
name|EndpointReferenceType
name|logical
parameter_list|)
block|{
name|EndpointReferenceType
name|physical
init|=
literal|null
decl_stmt|;
for|for
control|(
name|EndpointResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|physical
operator|=
name|resolver
operator|.
name|resolve
argument_list|(
name|logical
argument_list|)
expr_stmt|;
if|if
condition|(
name|physical
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|physical
return|;
block|}
comment|/**      * Walk the list of registered EndpointResolvers, so as to force a fresh       * resolution of the given abstract EPR, discarding any previously cached       * reference.      *<p>      * This API may be used by say the transport-level Conduit when it      * detects a non-transient error on the outgoing connection, or      * by any other actor in the dispatch with the ability to infer      * server-side unavailability.      *       * @param logical the previously resolved abstract EPR      * @param physical the concrete EPR to refresh      * @return the renewed concrete EPR if appropriate, null otherwise      */
specifier|public
name|EndpointReferenceType
name|renew
parameter_list|(
name|EndpointReferenceType
name|logical
parameter_list|,
name|EndpointReferenceType
name|physical
parameter_list|)
block|{
name|EndpointReferenceType
name|fresh
init|=
literal|null
decl_stmt|;
for|for
control|(
name|EndpointResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|fresh
operator|=
name|resolver
operator|.
name|renew
argument_list|(
name|logical
argument_list|,
name|physical
argument_list|)
expr_stmt|;
if|if
condition|(
name|fresh
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|fresh
return|;
block|}
comment|/**      * Walk the list of registered EndpointResolvers, so as to mint a new       * abstract EPR for a given service name.      *       * @param serviceName      * @return the newly minted EPR if appropriate, null otherwise      */
specifier|public
name|EndpointReferenceType
name|mint
parameter_list|(
name|QName
name|serviceName
parameter_list|)
block|{
name|EndpointReferenceType
name|logical
init|=
literal|null
decl_stmt|;
for|for
control|(
name|EndpointResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|logical
operator|=
name|resolver
operator|.
name|mint
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
if|if
condition|(
name|logical
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|logical
return|;
block|}
comment|/**      * Walk the list of registered EndpointResolvers, so as to mint a new       * abstract EPR for a given physical EPR.      *       * @param physical      * @return the newly minted EPR if appropriate, null otherwise      */
specifier|public
name|EndpointReferenceType
name|mint
parameter_list|(
name|EndpointReferenceType
name|physical
parameter_list|)
block|{
name|EndpointReferenceType
name|logical
init|=
literal|null
decl_stmt|;
for|for
control|(
name|EndpointResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|logical
operator|=
name|resolver
operator|.
name|mint
argument_list|(
name|physical
argument_list|)
expr_stmt|;
if|if
condition|(
name|logical
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|logical
return|;
block|}
comment|/**      * @return the encapsulated list of registered resolvers      */
specifier|protected
name|List
argument_list|<
name|EndpointResolver
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

