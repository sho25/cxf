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
name|security
operator|.
name|wss4j
package|;
end_package

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
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|Endpoint
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
name|ws
operator|.
name|security
operator|.
name|cache
operator|.
name|ReplayCacheFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|cache
operator|.
name|ReplayCache
import|;
end_import

begin_comment
comment|/**  * Some common functionality that can be shared between the WSS4JInInterceptor and the  * UsernameTokenInterceptor.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WSS4JUtils
block|{
specifier|private
name|WSS4JUtils
parameter_list|()
block|{
comment|// complete
block|}
comment|/**      * Get a ReplayCache instance. It first checks to see whether caching has been explicitly       * enabled or disabled via the booleanKey argument. If it has been set to false then no      * replay caching is done (for this booleanKey). If it has not been specified, then caching      * is enabled only if we are not the initiator of the exchange. If it has been specified, then      * caching is enabled.      *       * It tries to get an instance of ReplayCache via the instanceKey argument from a       * contextual property, and failing that the message exchange. If it can't find any, then it      * defaults to using an EH-Cache instance and stores that on the message exchange.      */
specifier|public
specifier|static
name|ReplayCache
name|getReplayCache
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|booleanKey
parameter_list|,
name|String
name|instanceKey
parameter_list|)
block|{
name|boolean
name|specified
init|=
literal|false
decl_stmt|;
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|booleanKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|specified
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|specified
operator|&&
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
operator|&&
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|EndpointInfo
name|info
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|ReplayCache
name|replayCache
init|=
operator|(
name|ReplayCache
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|instanceKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|replayCache
operator|==
literal|null
condition|)
block|{
name|replayCache
operator|=
operator|(
name|ReplayCache
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|instanceKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|replayCache
operator|==
literal|null
condition|)
block|{
name|ReplayCacheFactory
name|replayCacheFactory
init|=
name|ReplayCacheFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|String
name|cacheKey
init|=
name|instanceKey
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cacheKey
operator|+=
literal|"-"
operator|+
name|info
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|replayCache
operator|=
name|replayCacheFactory
operator|.
name|newReplayCache
argument_list|(
name|cacheKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|info
operator|.
name|setProperty
argument_list|(
name|instanceKey
argument_list|,
name|replayCache
argument_list|)
expr_stmt|;
block|}
return|return
name|replayCache
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Fetch the result of a given action from a given result list.      *       * @param resultList The result list to fetch an action from      * @param action The action to fetch      * @return The result fetched from the result list, null if the result      *         could not be found      */
specifier|public
specifier|static
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|fetchAllActionResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|resultList
parameter_list|,
name|int
name|action
parameter_list|)
block|{
return|return
name|fetchAllActionResults
argument_list|(
name|resultList
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|action
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Fetch the results of a given number of actions action from a given result list.      *       * @param resultList The result list to fetch an action from      * @param actions The list of actions to fetch      * @return The list of matching results fetched from the result list      */
specifier|public
specifier|static
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|fetchAllActionResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|resultList
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|actions
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|actionResultList
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|actions
operator|==
literal|null
operator|||
name|actions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|actionResultList
return|;
block|}
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|resultList
control|)
block|{
comment|//
comment|// Check the result of every action whether it matches the given action
comment|//
name|int
name|resultAction
init|=
operator|(
operator|(
name|java
operator|.
name|lang
operator|.
name|Integer
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|actions
operator|.
name|contains
argument_list|(
name|resultAction
argument_list|)
condition|)
block|{
if|if
condition|(
name|actionResultList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|actionResultList
operator|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|actionResultList
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|actionResultList
return|;
block|}
block|}
end_class

end_unit

