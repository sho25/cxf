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
name|logging
operator|.
name|Logger
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
name|BusException
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
name|util
operator|.
name|StringUtils
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
name|interceptor
operator|.
name|Fault
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
name|Exchange
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|ConduitInitiator
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
name|transport
operator|.
name|ConduitInitiatorManager
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
name|transport
operator|.
name|MessageObserver
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
name|AttributedURIType
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
comment|/**  * Abstract base class holding logic common to any ConduitSelector  * that retreives a Conduit from the ConduitInitiator.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractConduitSelector
implements|implements
name|ConduitSelector
block|{
specifier|protected
name|Conduit
name|selectedConduit
decl_stmt|;
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
comment|/**      * Constructor, allowing a specific conduit to override normal selection.      *       * @param c specific conduit      */
specifier|public
name|AbstractConduitSelector
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
name|selectedConduit
operator|=
name|c
expr_stmt|;
block|}
comment|/**      * Mechanics to actually get the Conduit from the ConduitInitiator      * if necessary.      *       * @param message the current Message      */
specifier|protected
name|Conduit
name|getSelectedConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|selectedConduit
operator|==
literal|null
condition|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|String
name|transportID
init|=
name|ei
operator|.
name|getTransportId
argument_list|()
decl_stmt|;
try|try
block|{
name|ConduitInitiatorManager
name|conduitInitiatorMgr
init|=
name|getBus
argument_list|(
name|exchange
argument_list|)
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduitInitiatorMgr
operator|!=
literal|null
condition|)
block|{
name|ConduitInitiator
name|conduitInitiator
init|=
name|conduitInitiatorMgr
operator|.
name|getConduitInitiator
argument_list|(
name|transportID
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduitInitiator
operator|!=
literal|null
condition|)
block|{
name|String
name|add
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|add
argument_list|)
operator|||
name|add
operator|.
name|equals
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
condition|)
block|{
name|selectedConduit
operator|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EndpointReferenceType
name|epr
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|AttributedURIType
name|ad
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|ad
operator|.
name|setValue
argument_list|(
name|add
argument_list|)
expr_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|ad
argument_list|)
expr_stmt|;
name|selectedConduit
operator|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|epr
argument_list|)
expr_stmt|;
block|}
name|MessageObserver
name|observer
init|=
name|exchange
operator|.
name|get
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|observer
operator|!=
literal|null
condition|)
block|{
name|selectedConduit
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"MessageObserver not found"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"ConduitInitiator not found: "
operator|+
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"ConduitInitiatorManager not found"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|selectedConduit
return|;
block|}
comment|/**      * @return the encapsulated Endpoint      */
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
comment|/**      * @param ep the endpoint to encapsulate      */
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|endpoint
operator|=
name|ep
expr_stmt|;
block|}
comment|/**      * Get the associated Bus instance from the exchange.      *       * @param exchange the current exchange      * @return the Bus instance      */
specifier|private
name|Bus
name|getBus
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Called on completion of the MEP for which the Conduit was required.      *       * @param exchange represents the completed MEP      */
specifier|public
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getSelectedConduit
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
operator|.
name|close
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//IGNORE
block|}
block|}
comment|/**      * @return the logger to use      */
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
block|}
end_class

end_unit

