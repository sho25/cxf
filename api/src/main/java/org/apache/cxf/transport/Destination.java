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
name|io
operator|.
name|IOException
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
name|message
operator|.
name|Message
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
comment|/**  * A Destination is a transport-level endpoint capable of receiving  * unsolicited incoming messages from different peers.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Destination
extends|extends
name|Observable
block|{
comment|/**      * @return the reference associated with this Destination      */
name|EndpointReferenceType
name|getAddress
parameter_list|()
function_decl|;
comment|/**      * Retreive a back-channel Conduit, which must be policy-compatible      * with the current Message and associated Destination. For example      * compatible Quality of Protection must be asserted on the back-channel.      *       * @param inMessage the current message      * @param unused1 - will likely always be null        * @param unused2 - will likely always be null      * @return a suitable Conduit      */
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|unused1
parameter_list|,
name|EndpointReferenceType
name|unused2
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Shutdown the Destination, i.e. stop accepting incoming messages.      */
name|void
name|shutdown
parameter_list|()
function_decl|;
name|MessageObserver
name|getMessageObserver
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

