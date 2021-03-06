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
name|Collections
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|i18n
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|Client
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
name|endpoint
operator|.
name|Server
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
name|feature
operator|.
name|AbstractFeature
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
name|ExchangeImpl
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
name|MessageImpl
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
name|Service
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
name|DescriptionInfo
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
name|policy
operator|.
name|attachment
operator|.
name|external
operator|.
name|ExternalAttachmentProvider
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
name|policy
operator|.
name|attachment
operator|.
name|reference
operator|.
name|ReferenceResolver
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
name|policy
operator|.
name|attachment
operator|.
name|reference
operator|.
name|RemoteReferenceResolver
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
name|PolicyReference
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
name|PolicyRegistry
import|;
end_import

begin_comment
comment|/**  * Configures a Server, Client, Bus with the specified policies. If a series of  * Policy<code>Element</code>s are supplied, these will be loaded into a Policy  * class using the<code>PolicyBuilder</code> extension on the bus. If the  * PolicyEngine has not been started, this feature will start it.  *  * @see PolicyBuilder  * @see AbstractFeature  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|WSPolicyFeature
extends|extends
name|AbstractFeature
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
name|WSPolicyFeature
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Policy
argument_list|>
name|policies
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Element
argument_list|>
name|policyElements
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Element
argument_list|>
name|policyReferenceElements
decl_stmt|;
specifier|private
name|boolean
name|ignoreUnknownAssertions
decl_stmt|;
specifier|private
name|AlternativeSelector
name|alternativeSelector
decl_stmt|;
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
specifier|public
name|WSPolicyFeature
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|WSPolicyFeature
parameter_list|(
name|Policy
modifier|...
name|ps
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|policies
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|policies
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|initializePolicyEngine
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Policy
argument_list|>
name|loadedPolicies
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|policyElements
operator|!=
literal|null
operator|||
name|policyReferenceElements
operator|!=
literal|null
condition|)
block|{
name|loadedPolicies
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|PolicyBuilder
name|builder
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|policyElements
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|policyElements
control|)
block|{
name|loadedPolicies
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPolicy
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|policyReferenceElements
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|policyReferenceElements
control|)
block|{
name|PolicyReference
name|pr
init|=
name|builder
operator|.
name|getPolicyReference
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|Policy
name|resolved
init|=
name|resolveReference
argument_list|(
name|pr
argument_list|,
name|builder
argument_list|,
name|bus
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
name|loadedPolicies
operator|.
name|add
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Policy
name|thePolicy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
if|if
condition|(
name|policies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Policy
name|p
range|:
name|policies
control|)
block|{
name|thePolicy
operator|=
name|thePolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|loadedPolicies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Policy
name|p
range|:
name|loadedPolicies
control|)
block|{
name|thePolicy
operator|=
name|thePolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|thePolicy
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|pe
init|)
block|{
name|pe
operator|.
name|addPolicy
argument_list|(
name|thePolicy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|initializePolicyEngine
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
comment|// this should never be null as features are initialized only
comment|// after the bus and all its extensions have been created
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|pe
init|)
block|{
name|pe
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|pe
operator|.
name|setIgnoreUnknownAssertions
argument_list|(
name|ignoreUnknownAssertions
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|alternativeSelector
condition|)
block|{
name|pe
operator|.
name|setAlternativeSelector
argument_list|(
name|alternativeSelector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|MessageImpl
name|createMessage
parameter_list|(
name|Endpoint
name|e
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|e
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|initializeEndpointPolicy
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|EndpointPolicy
name|ep
init|=
name|pe
operator|.
name|getClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|pe
operator|.
name|setClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|ep
operator|.
name|updatePolicy
argument_list|(
name|p
argument_list|,
name|createMessage
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|initializeEndpointPolicy
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|EndpointPolicy
name|ep
init|=
name|pe
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|pe
operator|.
name|setServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|ep
operator|.
name|updatePolicy
argument_list|(
name|p
argument_list|,
name|createMessage
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add policy to the service model (and consequently to the WSDL)
comment|// FIXME - ideally this should probably be moved up to where the policies are applied to the
comment|// endpoint, rather than this late.  As a consequence of its location, you have to declare a
comment|// ws policy feature on every endpoint in order to get any policy attachments into the
comment|// wsdl.  Alternatively add to the WSDLServiceBuilder somehow.
name|ServiceModelPolicyUpdater
name|pu
init|=
operator|new
name|ServiceModelPolicyUpdater
argument_list|(
name|ei
argument_list|)
decl_stmt|;
for|for
control|(
name|PolicyProvider
name|pp
range|:
operator|(
operator|(
name|PolicyEngineImpl
operator|)
name|pe
operator|)
operator|.
name|getPolicyProviders
argument_list|()
control|)
block|{
if|if
condition|(
name|pp
operator|instanceof
name|ExternalAttachmentProvider
condition|)
block|{
name|pu
operator|.
name|addPolicyAttachments
argument_list|(
operator|(
operator|(
name|ExternalAttachmentProvider
operator|)
name|pp
operator|)
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Policy
name|initializeEndpointPolicy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|initializePolicyEngine
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|DescriptionInfo
name|i
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Policy
argument_list|>
name|loadedPolicies
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|policyElements
operator|!=
literal|null
operator|||
name|policyReferenceElements
operator|!=
literal|null
condition|)
block|{
name|loadedPolicies
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|PolicyBuilder
name|builder
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|policyElements
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|policyElements
control|)
block|{
name|loadedPolicies
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPolicy
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|policyReferenceElements
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|policyReferenceElements
control|)
block|{
name|PolicyReference
name|pr
init|=
name|builder
operator|.
name|getPolicyReference
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|Policy
name|resolved
init|=
name|resolveReference
argument_list|(
name|pr
argument_list|,
name|builder
argument_list|,
name|bus
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
name|loadedPolicies
operator|.
name|add
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Policy
name|thePolicy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
if|if
condition|(
name|policies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Policy
name|p
range|:
name|policies
control|)
block|{
name|thePolicy
operator|=
name|thePolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|loadedPolicies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Policy
name|p
range|:
name|loadedPolicies
control|)
block|{
name|thePolicy
operator|=
name|thePolicy
operator|.
name|merge
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|thePolicy
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Policy
argument_list|>
name|getPolicies
parameter_list|()
block|{
if|if
condition|(
name|policies
operator|==
literal|null
condition|)
block|{
name|policies
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|policies
return|;
block|}
specifier|public
name|void
name|setPolicies
parameter_list|(
name|Collection
argument_list|<
name|Policy
argument_list|>
name|policies
parameter_list|)
block|{
name|this
operator|.
name|policies
operator|=
name|policies
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|Element
argument_list|>
name|getPolicyElements
parameter_list|()
block|{
if|if
condition|(
name|policyElements
operator|==
literal|null
condition|)
block|{
name|policyElements
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|policyElements
return|;
block|}
specifier|public
name|void
name|setPolicyElements
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|elements
parameter_list|)
block|{
name|policyElements
operator|=
name|elements
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|Element
argument_list|>
name|getPolicyReferenceElements
parameter_list|()
block|{
if|if
condition|(
name|policyReferenceElements
operator|==
literal|null
condition|)
block|{
name|policyReferenceElements
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|policyReferenceElements
return|;
block|}
specifier|public
name|void
name|setPolicyReferenceElements
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|elements
parameter_list|)
block|{
name|policyReferenceElements
operator|=
name|elements
expr_stmt|;
block|}
specifier|public
name|void
name|setIgnoreUnknownAssertions
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|ignoreUnknownAssertions
operator|=
name|ignore
expr_stmt|;
block|}
specifier|public
name|void
name|setAlternativeSelector
parameter_list|(
name|AlternativeSelector
name|as
parameter_list|)
block|{
name|alternativeSelector
operator|=
name|as
expr_stmt|;
block|}
name|Policy
name|resolveReference
parameter_list|(
name|PolicyReference
name|ref
parameter_list|,
name|PolicyBuilder
name|builder
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|DescriptionInfo
name|i
parameter_list|)
block|{
name|Policy
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|ref
operator|.
name|getURI
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|String
name|base
init|=
name|i
operator|==
literal|null
condition|?
literal|null
else|:
name|i
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
name|p
operator|=
name|resolveExternal
argument_list|(
name|ref
argument_list|,
name|base
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
name|resolveLocal
argument_list|(
name|ref
argument_list|,
name|bus
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|p
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"UNRESOLVED_POLICY_REFERENCE_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|ref
operator|.
name|getURI
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|p
return|;
block|}
name|Policy
name|resolveLocal
parameter_list|(
name|PolicyReference
name|ref
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|,
name|DescriptionInfo
name|i
parameter_list|)
block|{
name|String
name|uri
init|=
name|ref
operator|.
name|getURI
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|absoluteURI
init|=
name|i
operator|==
literal|null
condition|?
name|uri
else|:
name|i
operator|.
name|getBaseURI
argument_list|()
operator|+
name|uri
decl_stmt|;
name|PolicyRegistry
name|registry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|Policy
name|resolved
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|absoluteURI
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
return|return
name|resolved
return|;
block|}
name|ReferenceResolver
name|resolver
init|=
operator|new
name|ReferenceResolver
argument_list|()
block|{
specifier|public
name|Policy
name|resolveReference
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|PolicyBean
name|pb
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
operator|.
name|getBeanOfType
argument_list|(
name|uri
argument_list|,
name|PolicyBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|pb
condition|)
block|{
name|PolicyBuilder
name|builder
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|builder
operator|.
name|getPolicy
argument_list|(
name|pb
operator|.
name|getElement
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|resolved
operator|=
name|resolver
operator|.
name|resolveReference
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
name|ref
operator|.
name|setURI
argument_list|(
name|absoluteURI
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|absoluteURI
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
block|}
return|return
name|resolved
return|;
block|}
specifier|protected
name|Policy
name|resolveExternal
parameter_list|(
name|PolicyReference
name|ref
parameter_list|,
name|String
name|baseURI
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|PolicyBuilder
name|builder
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|ReferenceResolver
name|resolver
init|=
operator|new
name|RemoteReferenceResolver
argument_list|(
name|baseURI
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|PolicyRegistry
name|registry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|Policy
name|resolved
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|ref
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|resolved
condition|)
block|{
return|return
name|resolved
return|;
block|}
return|return
name|resolver
operator|.
name|resolveReference
argument_list|(
name|ref
operator|.
name|getURI
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

