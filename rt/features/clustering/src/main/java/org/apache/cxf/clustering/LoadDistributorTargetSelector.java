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
name|clustering
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  *  * @author jtalbut  *   * The LoadDistributorTargetSelector attempts to do the same job as the   * FailoverTargetSelector, but to choose an alternate target on every request  * rather than just when a fault occurs.  * The LoadDistributorTargetSelector uses the same FailoverStrategy interface as   * the FailoverTargetSelector, but has a few significant limitations:  * 1. Because the LoadDistributorTargetSelector needs to maintain a list of targets  *    between calls it has to obtain that list without reference to a Message.  *    Most FailoverStrategy classes can support this for addresses, but it cannot  *    be supported for endpoints.  *    If the list of targets cannot be obtained without reference to a Message then  *    the list will still be obtained but it will be specific to the Message and thus  *    discarded after this message has been processed.  As a consequence, if the  *    strategy chosen is a simple sequential one the first item in the list will  *    be chosen every time.  *    Conclusion: Be aware that if you are working with targets that are   *    dependent on the Message the process will be less efficient and that the  *    SequentialStrategy will not distribute the load at all.  * 2. The AbstractStaticFailoverStrategy base class excludes the 'default' endpoint  *    from the list of alternate endpoints.  *    If alternate endpoints (as opposed to alternate addresses) are to be used  *    you should probably ensure that your FailoverStrategy overrides getAlternateEndpoints  *    and calls getEndpoints with acceptCandidatesWithSameAddress = true.  */
end_comment

begin_class
specifier|public
class|class
name|LoadDistributorTargetSelector
extends|extends
name|FailoverTargetSelector
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LoadDistributorTargetSelector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IS_DISTRIBUTED
init|=
literal|"org.apache.cxf.clustering.LoadDistributorTargetSelector.IS_DISTRIBUTED"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|addressList
decl_stmt|;
specifier|private
name|boolean
name|failover
init|=
literal|true
decl_stmt|;
comment|/**      * Normal constructor.      */
specifier|public
name|LoadDistributorTargetSelector
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor, allowing a specific conduit to override normal selection.      *      * @param c specific conduit      */
specifier|public
name|LoadDistributorTargetSelector
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFailover
parameter_list|()
block|{
return|return
name|failover
return|;
block|}
specifier|public
name|void
name|setFailover
parameter_list|(
name|boolean
name|failover
parameter_list|)
block|{
name|this
operator|.
name|failover
operator|=
name|failover
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
comment|/**      * Called when a Conduit is actually required.      *      * @param message      * @return the Conduit to use for mediation of the message      */
specifier|public
specifier|synchronized
name|Conduit
name|selectConduit
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
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|InvocationKey
name|key
init|=
operator|new
name|InvocationKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|InvocationContext
name|invocation
init|=
name|inProgress
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|invocation
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|invocation
operator|.
name|getContext
argument_list|()
operator|.
name|containsKey
argument_list|(
name|IS_DISTRIBUTED
argument_list|)
condition|)
block|{
name|Endpoint
name|target
init|=
name|getDistributionTarget
argument_list|(
name|exchange
argument_list|,
name|invocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|setEndpoint
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|target
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|CONDUIT_COMPARE_FULL_URL
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|overrideAddressProperty
argument_list|(
name|invocation
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|invocation
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|IS_DISTRIBUTED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|getSelectedConduit
argument_list|(
name|message
argument_list|)
return|;
block|}
comment|/**      * Get the failover target endpoint, if a suitable one is available.      *      * @param exchange the current Exchange      * @param invocation the current InvocationContext      * @return a failover endpoint if one is available      *       * Note: The only difference between this and the super implementation is      * that the current (failed) address is removed from the list set of alternates,      * it could be argued that that change should be in the super implementation      * but I'm not sure of the impact.      */
specifier|protected
name|Endpoint
name|getFailoverTarget
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InvocationContext
name|invocation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|invocation
operator|.
name|hasAlternates
argument_list|()
condition|)
block|{
comment|// no previous failover attempt on this invocation
comment|//
name|alternateAddresses
operator|=
name|getStrategy
argument_list|()
operator|.
name|getAlternateAddresses
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
condition|)
block|{
name|alternateAddresses
operator|.
name|remove
argument_list|(
name|exchange
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|invocation
operator|.
name|setAlternateAddresses
argument_list|(
name|alternateAddresses
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invocation
operator|.
name|setAlternateEndpoints
argument_list|(
name|getStrategy
argument_list|()
operator|.
name|getAlternateEndpoints
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|alternateAddresses
operator|=
name|invocation
operator|.
name|getAlternateAddresses
argument_list|()
expr_stmt|;
block|}
name|Endpoint
name|failoverTarget
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
condition|)
block|{
name|String
name|alternateAddress
init|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateAddress
argument_list|(
name|alternateAddresses
argument_list|)
decl_stmt|;
if|if
condition|(
name|alternateAddress
operator|!=
literal|null
condition|)
block|{
comment|// re-use current endpoint
comment|//
name|failoverTarget
operator|=
name|getEndpoint
argument_list|()
expr_stmt|;
name|failoverTarget
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|alternateAddress
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|failoverTarget
operator|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateEndpoint
argument_list|(
name|invocation
operator|.
name|getAlternateEndpoints
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|failoverTarget
return|;
block|}
comment|/**      * Get the distribution target endpoint, if a suitable one is available.      *      * @param exchange the current Exchange      * @param invocation the current InvocationContext      * @return a distribution endpoint if one is available      */
specifier|private
name|Endpoint
name|getDistributionTarget
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InvocationContext
name|invocation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|addressList
operator|==
literal|null
operator|)
operator|||
operator|(
name|addressList
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
try|try
block|{
name|addressList
operator|=
name|getStrategy
argument_list|()
operator|.
name|getAlternateAddresses
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|fine
argument_list|(
literal|"Strategy "
operator|+
name|getStrategy
argument_list|()
operator|.
name|getClass
argument_list|()
operator|+
literal|" cannot handle a null argument to getAlternateAddresses: "
operator|+
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|alternateAddresses
operator|=
name|addressList
expr_stmt|;
if|if
condition|(
operator|(
name|alternateAddresses
operator|==
literal|null
operator|)
operator|||
operator|(
name|alternateAddresses
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|alternateAddresses
operator|=
name|getStrategy
argument_list|()
operator|.
name|getAlternateAddresses
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
condition|)
block|{
name|invocation
operator|.
name|setAlternateAddresses
argument_list|(
name|alternateAddresses
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invocation
operator|.
name|setAlternateEndpoints
argument_list|(
name|getStrategy
argument_list|()
operator|.
name|getAlternateEndpoints
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Endpoint
name|distributionTarget
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|alternateAddresses
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|alternateAddresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|alternateAddress
init|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateAddress
argument_list|(
name|alternateAddresses
argument_list|)
decl_stmt|;
if|if
condition|(
name|alternateAddress
operator|!=
literal|null
condition|)
block|{
comment|// re-use current endpoint
name|distributionTarget
operator|=
name|getEndpoint
argument_list|()
expr_stmt|;
name|distributionTarget
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|alternateAddress
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|distributionTarget
operator|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateEndpoint
argument_list|(
name|invocation
operator|.
name|getAlternateEndpoints
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|distributionTarget
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|requiresFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|failover
operator|&&
name|super
operator|.
name|requiresFailover
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

