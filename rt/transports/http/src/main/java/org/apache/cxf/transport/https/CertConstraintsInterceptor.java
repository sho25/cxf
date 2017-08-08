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
name|transport
operator|.
name|https
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
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
name|UntrustedURLConnectionIOException
import|;
end_import

begin_comment
comment|/**  * An interceptor that enforces certificate constraints logic at the TLS layer.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CertConstraintsInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|CertConstraintsInterceptor
name|INSTANCE
init|=
operator|new
name|CertConstraintsInterceptor
argument_list|()
decl_stmt|;
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CertConstraintsInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CertConstraintsInterceptor
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
specifier|final
name|CertConstraints
name|certConstraints
init|=
operator|(
name|CertConstraints
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|CertConstraints
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|certConstraints
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
try|try
block|{
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
name|HttpsMessageTrustDecider
argument_list|(
name|certConstraints
argument_list|,
name|orig
argument_list|)
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
block|}
else|else
block|{
throw|throw
operator|new
name|UntrustedURLConnectionIOException
argument_list|(
literal|"TLS is not in use"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|UntrustedURLConnectionIOException
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
else|else
block|{
try|try
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
specifier|final
name|Certificate
index|[]
name|certs
init|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
decl_stmt|;
if|if
condition|(
name|certs
operator|==
literal|null
operator|||
name|certs
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
literal|"No client certificates were found"
argument_list|)
throw|;
block|}
name|X509Certificate
index|[]
name|x509Certs
init|=
operator|(
name|X509Certificate
index|[]
operator|)
name|certs
decl_stmt|;
if|if
condition|(
operator|!
name|certConstraints
operator|.
name|matches
argument_list|(
name|x509Certs
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UntrustedURLConnectionIOException
argument_list|(
literal|"The client certificate does not match the defined cert constraints"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|UntrustedURLConnectionIOException
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
block|}
block|}
end_class

end_unit

