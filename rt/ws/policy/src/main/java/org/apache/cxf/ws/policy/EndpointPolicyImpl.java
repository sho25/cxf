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
name|policy
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|ResourceBundle
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
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|Interceptor
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
name|BindingFaultInfo
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
name|BindingOperationInfo
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
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyContainingAssertion
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|EndpointPolicyImpl
implements|implements
name|EndpointPolicy
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|EndpointPolicyImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Policy
name|policy
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|chosenAlternative
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|vocabulary
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|faultVocabulary
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|faultInterceptors
decl_stmt|;
specifier|private
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
name|PolicyEngineImpl
name|engine
decl_stmt|;
specifier|private
name|boolean
name|requestor
decl_stmt|;
specifier|private
name|Assertor
name|assertor
decl_stmt|;
specifier|public
name|EndpointPolicyImpl
parameter_list|()
block|{              }
specifier|public
name|EndpointPolicyImpl
parameter_list|(
name|Policy
name|p
parameter_list|)
block|{
name|policy
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|EndpointPolicyImpl
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|PolicyEngineImpl
name|engine
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|Assertor
name|assertor
parameter_list|)
block|{
name|this
operator|.
name|ei
operator|=
name|ei
expr_stmt|;
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
name|this
operator|.
name|requestor
operator|=
name|requestor
expr_stmt|;
name|this
operator|.
name|assertor
operator|=
name|assertor
expr_stmt|;
block|}
specifier|public
name|Policy
name|getPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
specifier|public
name|Assertor
name|getAssertor
parameter_list|()
block|{
return|return
name|assertor
return|;
block|}
specifier|public
name|EndpointPolicy
name|updatePolicy
parameter_list|(
name|Policy
name|p
parameter_list|)
block|{
name|EndpointPolicyImpl
name|epi
init|=
name|createEndpointPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|PolicyUtils
operator|.
name|isEmptyPolicy
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|Policy
name|normalizedPolicy
init|=
operator|(
name|Policy
operator|)
name|p
operator|.
name|normalize
argument_list|(
name|engine
operator|==
literal|null
condition|?
literal|null
else|:
name|engine
operator|.
name|getRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|epi
operator|.
name|setPolicy
argument_list|(
name|getPolicy
argument_list|()
operator|.
name|merge
argument_list|(
name|normalizedPolicy
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Policy
name|clonedPolicy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|clonedPolicy
operator|.
name|addPolicyComponents
argument_list|(
name|getPolicy
argument_list|()
operator|.
name|getPolicyComponents
argument_list|()
argument_list|)
expr_stmt|;
name|epi
operator|.
name|setPolicy
argument_list|(
name|clonedPolicy
argument_list|)
expr_stmt|;
block|}
name|epi
operator|.
name|checkExactlyOnes
argument_list|()
expr_stmt|;
name|epi
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
return|return
name|epi
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|getChosenAlternative
parameter_list|()
block|{
return|return
name|chosenAlternative
return|;
block|}
specifier|public
specifier|synchronized
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|getVocabulary
parameter_list|()
block|{
if|if
condition|(
name|vocabulary
operator|==
literal|null
condition|)
block|{
name|initializeVocabulary
argument_list|()
expr_stmt|;
block|}
return|return
name|vocabulary
return|;
block|}
specifier|public
specifier|synchronized
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|getFaultVocabulary
parameter_list|()
block|{
if|if
condition|(
name|vocabulary
operator|==
literal|null
condition|)
block|{
name|initializeVocabulary
argument_list|()
expr_stmt|;
block|}
return|return
name|faultVocabulary
return|;
block|}
specifier|public
specifier|synchronized
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInterceptors
parameter_list|()
block|{
if|if
condition|(
name|interceptors
operator|==
literal|null
condition|)
block|{
name|initializeInterceptors
argument_list|()
expr_stmt|;
block|}
return|return
name|interceptors
return|;
block|}
specifier|public
specifier|synchronized
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getFaultInterceptors
parameter_list|()
block|{
if|if
condition|(
name|interceptors
operator|==
literal|null
condition|)
block|{
name|initializeInterceptors
argument_list|()
expr_stmt|;
block|}
return|return
name|faultInterceptors
return|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|initializePolicy
argument_list|()
expr_stmt|;
name|checkExactlyOnes
argument_list|()
expr_stmt|;
name|finalizeConfig
argument_list|()
expr_stmt|;
block|}
name|void
name|finalizeConfig
parameter_list|()
block|{
name|chooseAlternative
argument_list|()
expr_stmt|;
block|}
name|void
name|initializePolicy
parameter_list|()
block|{
name|policy
operator|=
name|engine
operator|.
name|getAggregatedServicePolicy
argument_list|(
name|ei
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|policy
operator|=
name|policy
operator|.
name|merge
argument_list|(
name|engine
operator|.
name|getAggregatedEndpointPolicy
argument_list|(
name|ei
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|policy
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|policy
operator|=
operator|(
name|Policy
operator|)
name|policy
operator|.
name|normalize
argument_list|(
name|engine
operator|==
literal|null
condition|?
literal|null
else|:
name|engine
operator|.
name|getRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|chooseAlternative
parameter_list|()
block|{
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|requestor
condition|)
block|{
name|alternative
operator|=
name|engine
operator|.
name|getAlternativeSelector
argument_list|()
operator|.
name|selectAlternative
argument_list|(
name|policy
argument_list|,
name|engine
argument_list|,
name|assertor
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|alternative
operator|=
name|getSupportedAlternatives
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|alternative
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_ALTERNATIVE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
name|setChosenAlternative
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|getSupportedAlternatives
parameter_list|()
block|{
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternatives
init|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|it
init|=
name|policy
operator|.
name|getAlternatives
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|alternative
argument_list|,
name|assertor
argument_list|)
condition|)
block|{
name|alternatives
operator|.
name|addAll
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|alternatives
return|;
block|}
specifier|private
name|void
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|target
parameter_list|,
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|l1
parameter_list|)
block|{
for|for
control|(
name|Assertion
name|l
range|:
name|l1
control|)
block|{
if|if
condition|(
operator|!
name|target
operator|.
name|contains
argument_list|(
name|l
argument_list|)
condition|)
block|{
name|target
operator|.
name|add
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|initializeVocabulary
parameter_list|()
block|{
name|vocabulary
operator|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|requestor
condition|)
block|{
name|faultVocabulary
operator|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|// vocabulary of alternative chosen for endpoint
if|if
condition|(
name|getChosenAlternative
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Assertion
name|a
range|:
name|getChosenAlternative
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|.
name|isOptional
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|vocabulary
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|faultVocabulary
condition|)
block|{
name|faultVocabulary
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// add assertions for specific inbound (in case of a server endpoint) or outbound
comment|// (in case of a client endpoint) messages
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|EffectivePolicy
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|requestor
condition|)
block|{
name|p
operator|=
name|engine
operator|.
name|getEffectiveServerRequestPolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|c
init|=
name|engine
operator|.
name|getAssertions
argument_list|(
name|p
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|addAll
argument_list|(
name|vocabulary
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|p
operator|=
name|engine
operator|.
name|getEffectiveClientResponsePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|c
init|=
name|engine
operator|.
name|getAssertions
argument_list|(
name|p
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|addAll
argument_list|(
name|vocabulary
argument_list|,
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|faultVocabulary
condition|)
block|{
name|addAll
argument_list|(
name|faultVocabulary
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|boi
operator|.
name|getFaults
argument_list|()
operator|!=
literal|null
operator|&&
literal|null
operator|!=
name|faultVocabulary
condition|)
block|{
for|for
control|(
name|BindingFaultInfo
name|bfi
range|:
name|boi
operator|.
name|getFaults
argument_list|()
control|)
block|{
name|p
operator|=
name|engine
operator|.
name|getEffectiveClientFaultPolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|bfi
argument_list|)
expr_stmt|;
name|c
operator|=
name|engine
operator|.
name|getAssertions
argument_list|(
name|p
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|addAll
argument_list|(
name|faultVocabulary
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|getSupportedAlternatives
parameter_list|(
name|Policy
name|p
parameter_list|)
block|{
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternatives
init|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|it
init|=
name|p
operator|.
name|getAlternatives
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|alternative
argument_list|,
literal|null
argument_list|)
condition|)
block|{
name|alternatives
operator|.
name|addAll
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|alternatives
return|;
block|}
name|void
name|initializeInterceptors
parameter_list|(
name|PolicyInterceptorProviderRegistry
name|reg
parameter_list|,
name|Set
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|out
parameter_list|,
name|Assertion
name|a
parameter_list|,
name|boolean
name|fault
parameter_list|)
block|{
name|QName
name|qn
init|=
name|a
operator|.
name|getName
argument_list|()
decl_stmt|;
name|PolicyInterceptorProvider
name|pp
init|=
name|reg
operator|.
name|get
argument_list|(
name|qn
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|pp
condition|)
block|{
name|out
operator|.
name|addAll
argument_list|(
name|fault
condition|?
name|pp
operator|.
name|getInFaultInterceptors
argument_list|()
else|:
name|pp
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|a
operator|instanceof
name|PolicyContainingAssertion
condition|)
block|{
name|Policy
name|p
init|=
operator|(
operator|(
name|PolicyContainingAssertion
operator|)
name|a
operator|)
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Assertion
name|a2
range|:
name|getSupportedAlternatives
argument_list|(
name|p
argument_list|)
control|)
block|{
name|initializeInterceptors
argument_list|(
name|reg
argument_list|,
name|out
argument_list|,
name|a2
argument_list|,
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|void
name|initializeInterceptors
parameter_list|()
block|{
if|if
condition|(
name|engine
operator|==
literal|null
operator|||
name|engine
operator|.
name|getBus
argument_list|()
operator|==
literal|null
operator|||
name|engine
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|PolicyInterceptorProviderRegistry
name|reg
init|=
name|engine
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|out
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|getChosenAlternative
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Assertion
name|a
range|:
name|getChosenAlternative
argument_list|()
control|)
block|{
name|initializeInterceptors
argument_list|(
name|reg
argument_list|,
name|out
argument_list|,
name|a
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|requestor
condition|)
block|{
name|interceptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Assertion
name|a
range|:
name|getChosenAlternative
argument_list|()
control|)
block|{
name|initializeInterceptors
argument_list|(
name|reg
argument_list|,
name|out
argument_list|,
name|a
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|faultInterceptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ei
operator|!=
literal|null
operator|&&
name|ei
operator|.
name|getBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|EffectivePolicy
name|p
init|=
name|engine
operator|.
name|getEffectiveServerRequestPolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|getPolicy
argument_list|()
operator|==
literal|null
operator|||
name|p
operator|.
name|getPolicy
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|c
init|=
name|engine
operator|.
name|getAssertions
argument_list|(
name|p
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Assertion
name|a
range|:
name|c
control|)
block|{
name|initializeInterceptors
argument_list|(
name|reg
argument_list|,
name|out
argument_list|,
name|a
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|initializeInterceptors
argument_list|(
name|reg
argument_list|,
name|out
argument_list|,
name|a
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|interceptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|interceptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
comment|// for test
name|void
name|setPolicy
parameter_list|(
name|Policy
name|ep
parameter_list|)
block|{
name|policy
operator|=
name|ep
expr_stmt|;
block|}
name|void
name|setChosenAlternative
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|c
parameter_list|)
block|{
name|chosenAlternative
operator|=
name|c
expr_stmt|;
block|}
name|void
name|setVocabulary
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|v
parameter_list|)
block|{
name|vocabulary
operator|=
name|v
expr_stmt|;
block|}
name|void
name|setFaultVocabulary
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|v
parameter_list|)
block|{
name|faultVocabulary
operator|=
name|v
expr_stmt|;
block|}
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|in
parameter_list|)
block|{
name|interceptors
operator|=
name|in
expr_stmt|;
block|}
name|void
name|setFaultInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inFault
parameter_list|)
block|{
name|faultInterceptors
operator|=
name|inFault
expr_stmt|;
block|}
specifier|protected
name|EndpointPolicyImpl
name|createEndpointPolicy
parameter_list|()
block|{
return|return
operator|new
name|EndpointPolicyImpl
argument_list|(
name|this
operator|.
name|ei
argument_list|,
name|this
operator|.
name|engine
argument_list|,
name|this
operator|.
name|requestor
argument_list|,
name|this
operator|.
name|assertor
argument_list|)
return|;
block|}
name|void
name|checkExactlyOnes
parameter_list|()
block|{
comment|// Policy has been normalized and merged by now but unfortunately
comment|// ExactlyOnce have not been normalized properly by Neethi, for ex
comment|//<Policy>
comment|//<ExactlyOne><All><A></All></ExactlyOne>
comment|//<ExactlyOne><All><B></All></ExactlyOne>
comment|//</Policy>
comment|// this is what we can see after the normalization happens but in fact this
comment|// is still unnormalized expression, should be
comment|//<Policy>
comment|//<ExactlyOne><All><A></All><All><B></All></ExactlyOne>
comment|//</Policy>
name|List
argument_list|<
name|?
argument_list|>
name|assertions
init|=
name|policy
operator|.
name|getPolicyComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|assertions
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
return|return;
block|}
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|alternatives
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|alternatives
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|a
range|:
name|assertions
control|)
block|{
name|alternatives
operator|.
name|addPolicyComponents
argument_list|(
operator|(
operator|(
name|ExactlyOne
operator|)
name|a
operator|)
operator|.
name|getPolicyComponents
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setPolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

