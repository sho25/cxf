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
name|transport
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
comment|/**  * A MultiplexDestination is a transport-level endpoint capable of receiving  * unsolicited incoming messages from different peers for multiple targets   * identified by a unique id.  * The disambiguation of targets is handled by higher layers as the target  * address is made available as a context property or as a WS-A-To header  */
end_comment

begin_interface
specifier|public
interface|interface
name|MultiplexDestination
extends|extends
name|Destination
block|{
comment|/**      * @return the a reference containing the id that is       * associated with this Destination      */
name|EndpointReferenceType
name|getAddressWithId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * @param contextMap for this invocation. Obtained for example from       * JAX-WS WebServiceContext.getMessageContext(). The context will      * either contain the WS-A To content and/or some property that       * identifies the target address, eg MessageContext.PATH_INFO for      * the current invocation      * @return the id associated with the current invocation      */
name|String
name|getId
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextMap
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

