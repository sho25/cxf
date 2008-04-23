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
name|ws
operator|.
name|addressing
package|;
end_package

begin_comment
comment|/**  * Abstraction of Message Addressing Properties.   */
end_comment

begin_interface
specifier|public
interface|interface
name|AddressingProperties
extends|extends
name|AddressingType
block|{
comment|/**      * Accessor for the<b>To</b> property.      * @return current value of To property      */
name|EndpointReferenceType
name|getToEndpointReference
parameter_list|()
function_decl|;
comment|/**      * Accessor for the<b>To</b> property.      * @return current value of To property      */
name|AttributedURIType
name|getTo
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>To</b> property.      * @param epr new value for To property      */
name|void
name|setTo
parameter_list|(
name|EndpointReferenceType
name|epr
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>From</b> property.      * @return current value of From property      */
name|EndpointReferenceType
name|getFrom
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>From</b> property.      * @param epr new value for From property      */
name|void
name|setFrom
parameter_list|(
name|EndpointReferenceType
name|epr
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>MessageID</b> property.      * @return current value of MessageID property      */
name|AttributedURIType
name|getMessageID
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>MessageID</b> property.      * @param iri new value for MessageID property      */
name|void
name|setMessageID
parameter_list|(
name|AttributedURIType
name|iri
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>ReplyTo</b> property.      * @return current value of ReplyTo property      */
name|EndpointReferenceType
name|getReplyTo
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>ReplyTo</b> property.      * @param ref new value for ReplyTo property      */
name|void
name|setReplyTo
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>FaultTo</b> property.      * @return current value of FaultTo property      */
name|EndpointReferenceType
name|getFaultTo
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>FaultTo</b> property.      * @param ref new value for FaultTo property      */
name|void
name|setFaultTo
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>RelatesTo</b> property.      * @return current value of RelatesTo property      */
name|RelatesToType
name|getRelatesTo
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>RelatesTo</b> property.      * @param relatesTo new value for RelatesTo property      */
name|void
name|setRelatesTo
parameter_list|(
name|RelatesToType
name|relatesTo
parameter_list|)
function_decl|;
comment|/**      * Accessor for the<b>Action</b> property.      * @return current value of Action property      */
name|AttributedURIType
name|getAction
parameter_list|()
function_decl|;
comment|/**      * Mutator for the<b>Action</b> property.      * @param iri new value for Action property      */
name|void
name|setAction
parameter_list|(
name|AttributedURIType
name|iri
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

