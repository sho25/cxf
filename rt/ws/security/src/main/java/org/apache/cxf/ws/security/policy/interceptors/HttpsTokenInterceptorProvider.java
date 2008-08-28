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
name|net
operator|.
name|HttpURLConnection
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
name|HashMap
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
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
name|model
operator|.
name|HttpsToken
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
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
name|PREPARE_SEND
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
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|HTTPS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
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
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"http.connection"
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
name|connection
operator|instanceof
name|HttpsURLConnection
condition|)
block|{
name|HttpsURLConnection
name|https
init|=
operator|(
name|HttpsURLConnection
operator|)
name|connection
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|isRequireClientCertificate
argument_list|()
operator|&&
name|https
operator|.
name|getLocalCertificates
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"RequireClientCertificate is set, but no local certificates"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isHttpBasicAuthentication
argument_list|()
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
block|}
if|if
condition|(
name|token
operator|.
name|isHttpDigestAuthentication
argument_list|()
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
block|}
block|}
else|else
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"HttpURLConnection is not a HttpsURLConnection"
argument_list|)
expr_stmt|;
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
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|HTTPS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|assertHttps
argument_list|(
name|ais
argument_list|,
name|message
argument_list|)
expr_stmt|;
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
block|}
block|}
block|}
specifier|private
name|void
name|assertHttps
parameter_list|(
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
name|isHttpBasicAuthentication
argument_list|()
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
block|}
if|if
condition|(
name|token
operator|.
name|isHttpDigestAuthentication
argument_list|()
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
name|isRequireClientCertificate
argument_list|()
operator|&&
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
block|}
block|}
block|}
block|}
end_class

end_unit

