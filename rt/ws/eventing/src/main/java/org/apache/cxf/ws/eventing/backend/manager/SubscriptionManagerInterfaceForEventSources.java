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
name|eventing
operator|.
name|backend
operator|.
name|manager
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
name|eventing
operator|.
name|DeliveryType
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
name|eventing
operator|.
name|ExpirationType
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
name|eventing
operator|.
name|FilterType
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
name|eventing
operator|.
name|FormatType
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
name|eventing
operator|.
name|backend
operator|.
name|database
operator|.
name|SubscriptionTicket
import|;
end_import

begin_interface
specifier|public
interface|interface
name|SubscriptionManagerInterfaceForEventSources
block|{
name|SubscriptionTicketGrantingResponse
name|subscribe
parameter_list|(
name|DeliveryType
name|delivery
parameter_list|,
name|EndpointReferenceType
name|endTo
parameter_list|,
name|ExpirationType
name|expires
parameter_list|,
name|FilterType
name|filter
parameter_list|,
name|FormatType
name|format
parameter_list|)
function_decl|;
comment|/**      * READ ONLY. Returns an unmodifiable list of the subscriptions in database.      */
name|List
argument_list|<
name|SubscriptionTicket
argument_list|>
name|getTickets
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

