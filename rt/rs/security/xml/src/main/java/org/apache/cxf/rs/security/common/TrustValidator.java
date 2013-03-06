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
name|rs
operator|.
name|security
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|Crypto
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
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
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
name|dom
operator|.
name|handler
operator|.
name|RequestData
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
name|dom
operator|.
name|validate
operator|.
name|Credential
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
name|dom
operator|.
name|validate
operator|.
name|SignatureTrustValidator
import|;
end_import

begin_class
specifier|public
class|class
name|TrustValidator
block|{
specifier|public
name|void
name|validateTrust
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|X509Certificate
name|cert
parameter_list|,
name|PublicKey
name|publicKey
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SignatureTrustValidator
name|validator
init|=
operator|new
name|SignatureTrustValidator
argument_list|()
decl_stmt|;
name|RequestData
name|data
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|data
operator|.
name|setSigVerCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|Credential
name|trustCredential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|trustCredential
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
name|trustCredential
operator|.
name|setCertificates
argument_list|(
operator|new
name|X509Certificate
index|[]
block|{
name|cert
block|}
argument_list|)
expr_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|trustCredential
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

