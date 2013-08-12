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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * Implementations of this interface are responsible for mapping  * between abstract and concrete endpoint references, and/or  * renewing stale references.  *<p>  * An underlying mechanism in the style of the OGSA WS-Naming  * specification is assumed, where an EPR maybe be fully abstract,  * or concrete but with sufficient information embedded to enable  * its renewal if necessary.  */
end_comment

begin_interface
specifier|public
interface|interface
name|EndpointResolver
block|{
comment|/**      * Retrieve a concrete EPR corresponding to the given abstract EPR,      * returning a cached reference if already resolved.      *      * @param logical the abstract EPR to resolve      * @return the resolved concrete EPR if appropriate, null otherwise      */
name|EndpointReferenceType
name|resolve
parameter_list|(
name|EndpointReferenceType
name|logical
parameter_list|)
function_decl|;
comment|/**      * Force a fresh resolution of the given abstract EPR, discarding any      * previously cached reference.      *      * @param logical the previously resolved abstract EPR      * @param physical the concrete EPR to refresh      * @return the renewed concrete EPR if appropriate, null otherwise      */
name|EndpointReferenceType
name|renew
parameter_list|(
name|EndpointReferenceType
name|logical
parameter_list|,
name|EndpointReferenceType
name|physical
parameter_list|)
function_decl|;
comment|/**      * Mint a new abstract EPR for a given service name.      *       * @param serviceName      * @return the newly minted EPR if appropriate, null otherwise      */
name|EndpointReferenceType
name|mint
parameter_list|(
name|QName
name|serviceName
parameter_list|)
function_decl|;
comment|/**      * Mint a new abstract EPR for a given concrete EPR      *       * @param physical      * @return the newly minted EPR if appropriate, null otherwise      */
name|EndpointReferenceType
name|mint
parameter_list|(
name|EndpointReferenceType
name|physical
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

