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
name|Collections
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
name|builder
operator|.
name|xml
operator|.
name|XmlPrimitiveAssertion
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
name|model
operator|.
name|HttpsToken
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
name|HttpsToken
name|httpsToken
init|=
operator|new
name|HttpsToken
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|Policy
name|policy
init|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
argument_list|)
decl_stmt|;
name|policy
operator|=
operator|(
name|Policy
operator|)
name|policy
operator|.
name|normalize
argument_list|(
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|policy
operator|.
name|getAlternatives
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|processAlternative
argument_list|(
operator|(
name|List
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|httpsToken
argument_list|)
expr_stmt|;
break|break;
comment|// since there should be only one alternative
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|SP12Constants
operator|.
name|HTTPS_TOKEN
argument_list|)
return|;
block|}
comment|/**      * Process policy alternatives inside the HttpsToken element.      * Essentially this method will search for<br>      *<ul>      *<li><code>HttpBasicAuthentication</code></li>      *<li><code>HttpDigestAuthentication</code></li>      *<li><code>RequireClientCertificate</code></li>      *</ul>      * elements.      * @param assertions the list of assertions to be searched through.      * @param parent the https token, that is to be populated with retrieved data.      */
specifier|private
name|void
name|processAlternative
parameter_list|(
name|List
name|assertions
parameter_list|,
name|HttpsToken
name|parent
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iterator
init|=
name|assertions
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|XmlPrimitiveAssertion
name|primtive
init|=
operator|(
name|XmlPrimitiveAssertion
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|qname
init|=
name|primtive
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qname
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
name|qname
argument_list|)
condition|)
block|{
name|parent
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
name|qname
argument_list|)
condition|)
block|{
name|parent
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
name|qname
argument_list|)
condition|)
block|{
name|parent
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

