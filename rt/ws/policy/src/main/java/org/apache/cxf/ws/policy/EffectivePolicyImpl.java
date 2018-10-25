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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|BindingMessageInfo
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
name|Destination
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|EffectivePolicyImpl
implements|implements
name|EffectivePolicy
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
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Policy
name|policy
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|chosenAlternative
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|interceptors
decl_stmt|;
specifier|public
name|EffectivePolicyImpl
parameter_list|()
block|{     }
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
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
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
name|void
name|initialise
parameter_list|(
name|EndpointPolicy
name|epi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|inbound
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialise
argument_list|(
name|epi
argument_list|,
name|engine
argument_list|,
name|inbound
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialise
parameter_list|(
name|EndpointPolicy
name|epi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|inbound
parameter_list|,
name|boolean
name|fault
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|policy
operator|=
name|epi
operator|.
name|getPolicy
argument_list|()
expr_stmt|;
name|chosenAlternative
operator|=
name|epi
operator|.
name|getChosenAlternative
argument_list|()
expr_stmt|;
if|if
condition|(
name|chosenAlternative
operator|==
literal|null
condition|)
block|{
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
name|inbound
argument_list|,
name|fault
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialise
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|boolean
name|request
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|engine
argument_list|,
name|requestor
argument_list|,
name|request
argument_list|,
name|assertor
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialise
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|incoming
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|engine
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|assertor
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|,
name|incoming
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialise
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|boolean
name|request
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Assertor
name|assertor
init|=
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|engine
argument_list|,
name|requestor
argument_list|,
name|request
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestor
operator|||
operator|!
name|request
condition|)
block|{
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
name|requestor
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//incoming server should not choose an alternative, need to include all the policies
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getAssertions
argument_list|(
name|this
operator|.
name|policy
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|this
operator|.
name|setChosenAlternative
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|initialise
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|BindingFaultInfo
name|bfi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|bfi
argument_list|,
name|engine
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getAssertorAs
parameter_list|(
name|Assertor
name|as
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|.
name|isInstance
argument_list|(
name|as
argument_list|)
condition|)
block|{
return|return
name|t
operator|.
name|cast
argument_list|(
name|as
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|as
operator|instanceof
name|PolicyUtils
operator|.
name|WrappedAssertor
condition|)
block|{
name|Object
name|o
init|=
operator|(
operator|(
name|PolicyUtils
operator|.
name|WrappedAssertor
operator|)
name|as
operator|)
operator|.
name|getWrappedAssertor
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|isInstance
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
name|t
operator|.
name|cast
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
name|Assertor
name|initialisePolicy
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|boolean
name|request
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
block|}
name|BindingMessageInfo
name|bmi
init|=
name|request
condition|?
name|boi
operator|.
name|getInput
argument_list|()
else|:
name|boi
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|EndpointPolicy
name|ep
decl_stmt|;
if|if
condition|(
name|requestor
condition|)
block|{
name|ep
operator|=
name|engine
operator|.
name|getClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|getAssertorAs
argument_list|(
name|assertor
argument_list|,
name|Conduit
operator|.
name|class
argument_list|)
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ep
operator|=
name|engine
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|getAssertorAs
argument_list|(
name|assertor
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|policy
operator|=
name|ep
operator|.
name|getPolicy
argument_list|()
expr_stmt|;
if|if
condition|(
name|ep
operator|instanceof
name|EndpointPolicyImpl
condition|)
block|{
name|assertor
operator|=
operator|(
operator|(
name|EndpointPolicyImpl
operator|)
name|ep
operator|)
operator|.
name|getAssertor
argument_list|()
expr_stmt|;
block|}
name|policy
operator|=
name|policy
operator|.
name|merge
argument_list|(
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getAggregatedOperationPolicy
argument_list|(
name|boi
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bmi
condition|)
block|{
name|policy
operator|=
name|policy
operator|.
name|merge
argument_list|(
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getAggregatedMessagePolicy
argument_list|(
name|bmi
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|policy
operator|=
name|policy
operator|.
name|normalize
argument_list|(
name|engine
operator|.
name|getRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|assertor
return|;
block|}
name|void
name|initialisePolicy
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|BindingFaultInfo
name|bfi
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|policy
operator|=
name|engine
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
operator|(
name|Destination
operator|)
literal|null
argument_list|,
name|m
argument_list|)
operator|.
name|getPolicy
argument_list|()
expr_stmt|;
name|policy
operator|=
name|policy
operator|.
name|merge
argument_list|(
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getAggregatedOperationPolicy
argument_list|(
name|boi
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|bfi
operator|!=
literal|null
condition|)
block|{
name|policy
operator|=
name|policy
operator|.
name|merge
argument_list|(
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getAggregatedFaultPolicy
argument_list|(
name|bfi
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|policy
operator|=
name|policy
operator|.
name|normalize
argument_list|(
name|engine
operator|.
name|getRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|chooseAlternative
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|void
name|chooseAlternative
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|incoming
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
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
name|incoming
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|alternative
condition|)
block|{
name|PolicyUtils
operator|.
name|logPolicy
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|FINE
argument_list|,
literal|"No alternative supported."
argument_list|,
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
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
name|setChosenAlternative
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
block|}
name|void
name|initialiseInterceptors
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|void
name|initialiseInterceptors
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|useIn
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|,
name|useIn
argument_list|,
literal|false
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|void
name|initialiseInterceptors
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|boolean
name|useIn
parameter_list|,
name|boolean
name|fault
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
operator|.
name|getBus
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|PolicyInterceptorProviderRegistry
name|reg
init|=
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|engine
operator|)
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|out
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Assertion
name|a
range|:
name|getChosenAlternative
argument_list|()
control|)
block|{
name|initialiseInterceptors
argument_list|(
name|reg
argument_list|,
name|engine
argument_list|,
name|out
argument_list|,
name|a
argument_list|,
name|useIn
argument_list|,
name|fault
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|setInterceptors
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
argument_list|(
name|out
argument_list|)
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
parameter_list|(
name|PolicyEngine
name|engine
parameter_list|,
name|Policy
name|p
parameter_list|,
name|Message
name|m
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
argument_list|<>
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
argument_list|,
name|m
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
name|initialiseInterceptors
parameter_list|(
name|PolicyInterceptorProviderRegistry
name|reg
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Set
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|out
parameter_list|,
name|Assertion
name|a
parameter_list|,
name|boolean
name|useIn
parameter_list|,
name|boolean
name|fault
parameter_list|,
name|Message
name|m
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
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|i
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|useIn
operator|&&
operator|!
name|fault
condition|)
block|{
name|i
operator|=
name|reg
operator|.
name|getInInterceptorsForAssertion
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|useIn
operator|&&
operator|!
name|fault
condition|)
block|{
name|i
operator|=
name|reg
operator|.
name|getOutInterceptorsForAssertion
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|useIn
operator|&&
name|fault
condition|)
block|{
name|i
operator|=
name|reg
operator|.
name|getInFaultInterceptorsForAssertion
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|useIn
operator|&&
name|fault
condition|)
block|{
name|i
operator|=
name|reg
operator|.
name|getOutFaultInterceptorsForAssertion
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|addAll
argument_list|(
name|i
argument_list|)
expr_stmt|;
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
name|engine
argument_list|,
name|p
argument_list|,
name|m
argument_list|)
control|)
block|{
name|initialiseInterceptors
argument_list|(
name|reg
argument_list|,
name|engine
argument_list|,
name|out
argument_list|,
name|a2
argument_list|,
name|useIn
argument_list|,
name|fault
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// for tests
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
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
argument_list|>
argument_list|>
name|out
parameter_list|)
block|{
name|interceptors
operator|=
name|out
expr_stmt|;
block|}
block|}
end_class

end_unit

