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
name|Set
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_comment
comment|/**  * Factory for Destinations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DestinationFactory
block|{
comment|/**      * Create a destination.      *      * @param ei the endpoint info of the destination.      * @return the created Destination.      */
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getTransportIds
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

