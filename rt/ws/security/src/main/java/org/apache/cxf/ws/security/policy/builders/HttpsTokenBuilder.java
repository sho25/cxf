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
name|policy
operator|.
name|builders
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|helpers
operator|.
name|DOMUtils
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
name|AssertionBuilder
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
name|PolicyAssertion
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
name|PolicyBuilder
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
name|PolicyConstants
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
name|policy
operator|.
name|SP11Constants
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
name|policy
operator|.
name|SP12Constants
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
name|policy
operator|.
name|SPConstants
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
name|policy
operator|.
name|model
operator|.
name|HttpsToken
import|;
end_import

begin_comment
comment|/**  * This is a standard assertion builder implementation for the https token   * as specified by the ws security policy 1.2 specification. In order for this builder to be used  * it is required that the security policy namespace uri is {@link SP12Constants#SP_NS}   * The builder will handle  *<ul>  *<li><code>HttpBasicAuthentication</code></li>  *<li><code>HttpDigestAuthentication</code></li>  *<li><code>RequireClientCertificate</code></li>  *</ul>   * alternatives in the HttpsToken considering both cases whether the policy is normalized or not.  *   */
end_comment

begin_class
specifier|public
class|class
name|HttpsTokenBuilder
implements|implements
name|AssertionBuilder
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|QName
argument_list|>
name|KNOWN_ELEMENTS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|HTTPS_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|HTTPS_TOKEN
argument_list|)
decl_stmt|;
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|HttpsTokenBuilder
parameter_list|(
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|builder
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|SPConstants
name|consts
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|?
name|SP11Constants
operator|.
name|INSTANCE
else|:
name|SP12Constants
operator|.
name|INSTANCE
decl_stmt|;
name|HttpsToken
name|httpsToken
init|=
operator|new
name|HttpsToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|httpsToken
operator|.
name|setOptional
argument_list|(
name|PolicyConstants
operator|.
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|consts
operator|.
name|getVersion
argument_list|()
operator|==
name|SPConstants
operator|.
name|Version
operator|.
name|SP_V11
condition|)
block|{
name|String
name|attr
init|=
name|DOMUtils
operator|.
name|getAttribute
argument_list|(
name|element
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
name|httpsToken
operator|.
name|setRequireClientCertificate
argument_list|(
literal|"true"
operator|.
name|equals
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Element
name|polEl
init|=
name|PolicyConstants
operator|.
name|findPolicyElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|polEl
operator|!=
literal|null
condition|)
block|{
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|polEl
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|SP12Constants
operator|.
name|HTTP_BASIC_AUTHENTICATION
operator|.
name|equals
argument_list|(
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|child
argument_list|)
argument_list|)
condition|)
block|{
name|httpsToken
operator|.
name|setHttpBasicAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|HTTP_DIGEST_AUTHENTICATION
operator|.
name|equals
argument_list|(
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|child
argument_list|)
argument_list|)
condition|)
block|{
name|httpsToken
operator|.
name|setHttpDigestAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
operator|.
name|equals
argument_list|(
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|child
argument_list|)
argument_list|)
condition|)
block|{
name|httpsToken
operator|.
name|setRequireClientCertificate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|httpsToken
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|List
argument_list|<
name|QName
argument_list|>
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
specifier|public
name|PolicyAssertion
name|buildCompatible
parameter_list|(
name|PolicyAssertion
name|a
parameter_list|,
name|PolicyAssertion
name|b
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

