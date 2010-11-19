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
name|X509Certificate
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

begin_class
specifier|final
class|class
name|HttpsMessageTrustDecider
extends|extends
name|MessageTrustDecider
block|{
specifier|private
specifier|final
name|CertConstraints
name|certConstraints
decl_stmt|;
specifier|private
specifier|final
name|MessageTrustDecider
name|orig
decl_stmt|;
name|HttpsMessageTrustDecider
parameter_list|(
name|CertConstraints
name|certConstraints
parameter_list|,
name|MessageTrustDecider
name|orig
parameter_list|)
block|{
name|this
operator|.
name|certConstraints
operator|=
name|certConstraints
expr_stmt|;
name|this
operator|.
name|orig
operator|=
name|orig
expr_stmt|;
block|}
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
name|getServerCertificates
argument_list|()
operator|==
literal|null
operator|||
name|info
operator|.
name|getServerCertificates
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
literal|"No server certificates were found"
argument_list|)
throw|;
block|}
else|else
block|{
name|X509Certificate
index|[]
name|certs
init|=
operator|(
name|X509Certificate
index|[]
operator|)
name|info
operator|.
name|getServerCertificates
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|certConstraints
operator|.
name|matches
argument_list|(
name|certs
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
literal|"The server certificate(s) do not match the defined cert constraints"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

