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
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

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
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|CastUtils
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|security
operator|.
name|SecurityContext
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|http
operator|.
name|MessageTrustDecider
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
name|http
operator|.
name|URLConnectionInfo
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
name|http
operator|.
name|UntrustedURLConnectionIOException
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
name|https
operator|.
name|HttpsURLConnectionInfo
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
name|AbstractPolicyInterceptorProvider
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
name|AssertionInfo
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
name|AssertionInfoMap
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
name|PolicyException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
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
name|wss4j
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
name|wss4j
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
name|wss4j
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
name|wss4j
operator|.
name|stax
operator|.
name|impl
operator|.
name|securityToken
operator|.
name|HttpsSecurityTokenImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|HttpsTokenSecurityEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|securityToken
operator|.
name|WSSecurityTokenConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|exceptions
operator|.
name|XMLSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEvent
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|HttpsTokenInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
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
name|HttpsTokenInterceptorProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|13951002554477036L
decl_stmt|;
specifier|public
name|HttpsTokenInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|TRANSPORT_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|TRANSPORT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|ISSUED_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|ISSUED_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|HTTPS_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|HTTPS_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HttpsTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HttpsTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HttpsTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HttpsTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getSetProtocolHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|headers
condition|)
block|{
name|headers
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
specifier|static
class|class
name|HttpsTokenOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|HttpsTokenOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|NegotiationUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTPS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|assertHttps
argument_list|(
name|aim
argument_list|,
name|ais
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//server side should be checked on the way in
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|assertHttps
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|HttpsToken
name|token
init|=
operator|(
name|HttpsToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|String
name|scheme
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"http.scheme"
argument_list|)
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|getSetProtocolHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|RequireClientCertificate
condition|)
block|{
specifier|final
name|MessageTrustDecider
name|orig
init|=
name|message
operator|.
name|get
argument_list|(
name|MessageTrustDecider
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessageTrustDecider
name|trust
init|=
operator|new
name|MessageTrustDecider
argument_list|()
block|{
specifier|public
name|void
name|establishTrust
parameter_list|(
name|String
name|conduitName
parameter_list|,
name|URLConnectionInfo
name|connectionInfo
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|UntrustedURLConnectionIOException
block|{
if|if
condition|(
name|orig
operator|!=
literal|null
condition|)
block|{
name|orig
operator|.
name|establishTrust
argument_list|(
name|conduitName
argument_list|,
name|connectionInfo
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|HttpsURLConnectionInfo
name|info
init|=
operator|(
name|HttpsURLConnectionInfo
operator|)
name|connectionInfo
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getLocalCertificates
argument_list|()
operator|==
literal|null
operator|||
name|info
operator|.
name|getLocalCertificates
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|UntrustedURLConnectionIOException
argument_list|(
literal|"RequireClientCertificate is set, "
operator|+
literal|"but no local certificates were negotiated.  Is"
operator|+
literal|" the server set to ask for client authorization?"
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MessageTrustDecider
operator|.
name|class
argument_list|,
name|trust
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|HttpBasicAuthentication
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|auth
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
operator|||
name|auth
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|auth
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Basic"
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"HttpBasicAuthentication is set, but not being used"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_BASIC_AUTHENTICATION
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|HttpDigestAuthentication
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|auth
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
operator|||
name|auth
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|auth
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Digest"
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"HttpDigestAuthentication is set, but not being used"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_DIGEST_AUTHENTICATION
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Not an HTTPs connection"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ai
operator|.
name|isAsserted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
name|ai
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|static
class|class
name|HttpsTokenInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|HttpsTokenInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|NegotiationUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTPS_TOKEN
argument_list|)
decl_stmt|;
name|boolean
name|requestor
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|requestor
condition|)
block|{
try|try
block|{
name|assertNonHttpsTransportToken
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return;
block|}
if|if
condition|(
operator|!
name|requestor
condition|)
block|{
try|try
block|{
name|assertHttps
argument_list|(
name|aim
argument_list|,
name|ais
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Store the TLS principal on the message context
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|==
literal|null
operator|||
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
operator|&&
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|!=
literal|null
operator|&&
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|&&
operator|(
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|X509Certificate
operator|)
condition|)
block|{
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|//client side should be checked on the way out
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_DIGEST_AUTHENTICATION
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_BASIC_AUTHENTICATION
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|assertHttps
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|XMLSecurityException
block|{
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|securityEvents
init|=
name|getSecurityEventList
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|AuthorizationPolicy
name|policy
init|=
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|boolean
name|asserted
init|=
literal|true
decl_stmt|;
name|HttpsToken
name|token
init|=
operator|(
name|HttpsToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|HttpsTokenSecurityEvent
name|httpsTokenSecurityEvent
init|=
operator|new
name|HttpsTokenSecurityEvent
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|getSetProtocolHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|HttpBasicAuthentication
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|auth
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
operator|||
name|auth
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|auth
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Basic"
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpBasicAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|(
literal|true
argument_list|,
name|policy
operator|.
name|getUserName
argument_list|()
argument_list|)
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_BASIC_AUTHENTICATION
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|HttpDigestAuthentication
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|auth
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
operator|||
name|auth
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|auth
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Digest"
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpDigestAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|(
literal|false
argument_list|,
name|policy
operator|.
name|getUserName
argument_list|()
argument_list|)
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HTTP_DIGEST_AUTHENTICATION
argument_list|)
expr_stmt|;
block|}
block|}
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|token
operator|.
name|getAuthenticationType
argument_list|()
operator|==
name|HttpsToken
operator|.
name|AuthenticationType
operator|.
name|RequireClientCertificate
condition|)
block|{
if|if
condition|(
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|==
literal|null
operator|||
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|!=
literal|null
operator|&&
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpsClientCertificateAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|(
operator|(
name|X509Certificate
operator|)
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|httpsTokenSecurityEvent
operator|.
name|getAuthenticationType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpsNoAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|()
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
name|asserted
argument_list|)
expr_stmt|;
if|if
condition|(
name|asserted
condition|)
block|{
name|securityEvents
operator|.
name|add
argument_list|(
name|httpsTokenSecurityEvent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// We might have an IssuedToken TransportToken
specifier|private
name|void
name|assertNonHttpsTransportToken
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|XMLSecurityException
block|{
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
name|HttpsTokenSecurityEvent
name|httpsTokenSecurityEvent
init|=
operator|new
name|HttpsTokenSecurityEvent
argument_list|()
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|!=
literal|null
operator|&&
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpsClientCertificateAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|(
operator|(
name|X509Certificate
operator|)
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|httpsTokenSecurityEvent
operator|.
name|getAuthenticationType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpsNoAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|()
decl_stmt|;
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|securityEvents
init|=
name|getSecurityEventList
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|securityEvents
operator|.
name|add
argument_list|(
name|httpsTokenSecurityEvent
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|getSecurityEventList
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|securityEvents
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".out"
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityEvents
operator|==
literal|null
condition|)
block|{
name|securityEvents
operator|=
operator|new
name|ArrayList
argument_list|<
name|SecurityEvent
argument_list|>
argument_list|()
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".out"
argument_list|,
name|securityEvents
argument_list|)
expr_stmt|;
block|}
return|return
name|securityEvents
return|;
block|}
specifier|private
name|SecurityContext
name|createSecurityContext
parameter_list|(
specifier|final
name|Principal
name|p
parameter_list|)
block|{
return|return
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|p
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

