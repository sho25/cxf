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
name|Closeable
import|;
end_import

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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|message
operator|.
name|MessageUtils
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
comment|/**  * Abstract base class holding logic common to any ConduitSelector  * that retrieves a Conduit from the ConduitInitiator.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractConduitSelector
implements|implements
name|ConduitSelector
implements|,
name|Closeable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONDUIT_COMPARE_FULL_URL
init|=
literal|"org.apache.cxf.ConduitSelector.compareFullUrl"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|KEEP_CONDUIT_ALIVE
init|=
literal|"KeepConduitAlive"
decl_stmt|;
comment|//collection of conduits that were created so we can close them all at the end
specifier|protected
name|List
argument_list|<
name|Conduit
argument_list|>
name|conduits
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|Conduit
argument_list|>
argument_list|()
decl_stmt|;
comment|//protected Conduit selectedConduit;
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
specifier|public
name|AbstractConduitSelector
parameter_list|()
block|{     }
comment|/**      * Constructor, allowing a specific conduit to override normal selection.      *      * @param c specific conduit      */
specifier|public
name|AbstractConduitSelector
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|conduits
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|Conduit
name|c
range|:
name|conduits
control|)
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|conduits
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|removeConduit
parameter_list|(
name|Conduit
name|conduit
parameter_list|)
block|{
if|if
condition|(
name|conduit
operator|!=
literal|null
condition|)
block|{
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|conduits
operator|.
name|remove
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Mechanics to actually get the Conduit from the ConduitInitiator      * if necessary.      *      * @param message the current Message      */
specifier|protected
name|Conduit
name|getSelectedConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Conduit
name|c
init|=
name|findCompatibleConduit
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
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
name|exchange
operator|.
name|getBus
argument_list|()
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
name|c
operator|=
name|createConduit
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|,
name|conduitInitiator
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
if|if
condition|(
name|c
operator|!=
literal|null
operator|&&
name|c
operator|.
name|getTarget
argument_list|()
operator|!=
literal|null
operator|&&
name|c
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|replaceEndpointAddressPropertyIfNeeded
argument_list|(
name|message
argument_list|,
name|c
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
comment|//the search for the conduit could cause extra properties to be reset/loaded.
name|message
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Conduit
operator|.
name|class
argument_list|,
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
specifier|protected
name|Conduit
name|createConduit
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ConduitInitiator
name|conduitInitiator
parameter_list|)
throws|throws
name|IOException
block|{
name|Conduit
name|c
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|endpoint
init|)
block|{
if|if
condition|(
operator|!
name|conduits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|c
operator|=
name|findCompatibleConduit
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
name|EndpointInfo
name|ei
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
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
name|String
name|basePath
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
name|BASE_PATH
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
name|c
operator|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|exchange
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|replaceEndpointAddressPropertyIfNeeded
argument_list|(
name|message
argument_list|,
name|add
argument_list|,
name|c
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
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|basePath
argument_list|)
condition|?
name|add
else|:
name|basePath
argument_list|)
expr_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|ad
argument_list|)
expr_stmt|;
name|c
operator|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|epr
argument_list|,
name|exchange
operator|.
name|getBus
argument_list|()
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
name|c
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
name|conduits
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
comment|// Some conduits may replace the endpoint address after it has already been prepared
comment|// but before the invocation has been done (ex, org.apache.cxf.clustering.LoadDistributorTargetSelector)
comment|// which may affect JAX-RS clients where actual endpoint address property may include additional path
comment|// segments.
specifier|protected
name|boolean
name|replaceEndpointAddressPropertyIfNeeded
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|endpointAddress
parameter_list|,
name|Conduit
name|cond
parameter_list|)
block|{
return|return
literal|false
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
comment|/**      * Called on completion of the MEP for which the Conduit was required.      *      * @param exchange represents the completed MEP      */
specifier|public
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Clients expecting explicit InputStream responses
comment|// will need to keep low level conduits operating on InputStreams open
comment|// and will be responsible for closing the streams
if|if
condition|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|KEEP_CONDUIT_ALIVE
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|Conduit
name|c
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Conduit
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
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
else|else
block|{
name|c
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
comment|/**      * If address protocol was changed, conduit should be re-initialised      *      * @param message the current Message      */
specifier|protected
name|Conduit
name|findCompatibleConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Conduit
name|c
init|=
name|message
operator|.
name|get
argument_list|(
name|Conduit
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|!=
name|message
condition|)
block|{
name|c
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Conduit
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
name|ContextualBooleanGetter
name|cbg
init|=
operator|new
name|ContextualBooleanGetter
argument_list|(
name|message
argument_list|)
decl_stmt|;
for|for
control|(
name|Conduit
name|c2
range|:
name|conduits
control|)
block|{
if|if
condition|(
name|c2
operator|.
name|getTarget
argument_list|()
operator|==
literal|null
operator|||
name|c2
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|==
literal|null
operator|||
name|c2
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|conduitAddress
init|=
name|c2
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
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
name|actualAddress
init|=
name|ei
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|String
name|messageAddress
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
name|messageAddress
operator|!=
literal|null
condition|)
block|{
name|actualAddress
operator|=
name|messageAddress
expr_stmt|;
block|}
if|if
condition|(
name|matchAddresses
argument_list|(
name|conduitAddress
argument_list|,
name|actualAddress
argument_list|,
name|cbg
argument_list|)
condition|)
block|{
return|return
name|c2
return|;
block|}
block|}
for|for
control|(
name|Conduit
name|c2
range|:
name|conduits
control|)
block|{
if|if
condition|(
name|c2
operator|.
name|getTarget
argument_list|()
operator|==
literal|null
operator|||
name|c2
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|==
literal|null
operator|||
name|c2
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|c2
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|matchAddresses
parameter_list|(
name|String
name|conduitAddress
parameter_list|,
name|String
name|actualAddress
parameter_list|,
name|ContextualBooleanGetter
name|cbg
parameter_list|)
block|{
if|if
condition|(
name|conduitAddress
operator|.
name|length
argument_list|()
operator|==
name|actualAddress
operator|.
name|length
argument_list|()
condition|)
block|{
comment|//let's be optimistic and try full comparison first, regardless of CONDUIT_COMPARE_FULL_URL value,
comment|//which can be expensive to fetch; as a matter of fact, anyway, if the addresses fully match,
comment|//their hosts also match
if|if
condition|(
name|conduitAddress
operator|.
name|equalsIgnoreCase
argument_list|(
name|actualAddress
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|cbg
operator|.
name|isFullComparison
argument_list|()
condition|?
literal|false
else|:
name|matchAddressSubstrings
argument_list|(
name|conduitAddress
argument_list|,
name|actualAddress
argument_list|)
return|;
block|}
return|return
name|cbg
operator|.
name|isFullComparison
argument_list|()
condition|?
literal|false
else|:
name|matchAddressSubstrings
argument_list|(
name|conduitAddress
argument_list|,
name|actualAddress
argument_list|)
return|;
block|}
comment|//smart address substring comparison that tries to avoid building and comparing substrings unless strictly required
specifier|private
name|boolean
name|matchAddressSubstrings
parameter_list|(
name|String
name|conduitAddress
parameter_list|,
name|String
name|actualAddress
parameter_list|)
block|{
name|int
name|idx
init|=
name|conduitAddress
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|==
name|actualAddress
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
condition|)
block|{
if|if
condition|(
name|idx
operator|<=
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|conduitAddress
operator|=
name|conduitAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|actualAddress
operator|=
name|actualAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
return|return
name|conduitAddress
operator|.
name|equalsIgnoreCase
argument_list|(
name|actualAddress
argument_list|)
return|;
block|}
comment|//no possible match as for sure the substrings before idx will be different
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ContextualBooleanGetter
block|{
specifier|private
name|Boolean
name|value
decl_stmt|;
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
name|ContextualBooleanGetter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFullComparison
parameter_list|()
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|CONDUIT_COMPARE_FULL_URL
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

