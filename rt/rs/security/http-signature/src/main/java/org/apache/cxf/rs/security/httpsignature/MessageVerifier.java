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
name|httpsignature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|Objects
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|exception
operator|.
name|MissingSignatureHeaderException
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|exception
operator|.
name|MultipleSignatureHeaderException
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|provider
operator|.
name|AlgorithmProvider
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|provider
operator|.
name|PublicKeyProvider
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|provider
operator|.
name|SecurityProvider
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|utils
operator|.
name|DefaultSignatureConstants
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|utils
operator|.
name|SignatureHeaderUtils
import|;
end_import

begin_class
specifier|public
class|class
name|MessageVerifier
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
name|MessageVerifier
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AlgorithmProvider
name|algorithmProvider
decl_stmt|;
specifier|private
name|PublicKeyProvider
name|publicKeyProvider
decl_stmt|;
specifier|private
name|SecurityProvider
name|securityProvider
decl_stmt|;
specifier|private
specifier|final
name|SignatureValidator
name|signatureValidator
decl_stmt|;
specifier|public
name|MessageVerifier
parameter_list|(
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|)
block|{
name|this
argument_list|(
name|publicKeyProvider
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageVerifier
parameter_list|(
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requiredHeaders
parameter_list|)
block|{
name|this
argument_list|(
name|publicKeyProvider
argument_list|,
name|keyId
lambda|->
name|Security
operator|.
name|getProvider
argument_list|(
name|DefaultSignatureConstants
operator|.
name|SECURITY_PROVIDER
argument_list|)
argument_list|,
name|keyId
lambda|->
name|DefaultSignatureConstants
operator|.
name|SIGNING_ALGORITHM
argument_list|,
name|requiredHeaders
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageVerifier
parameter_list|(
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|,
name|SecurityProvider
name|securityProvider
parameter_list|,
name|AlgorithmProvider
name|algorithmProvider
parameter_list|)
block|{
name|this
argument_list|(
name|publicKeyProvider
argument_list|,
name|securityProvider
argument_list|,
name|algorithmProvider
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageVerifier
parameter_list|(
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|,
name|SecurityProvider
name|securityProvider
parameter_list|,
name|AlgorithmProvider
name|algorithmProvider
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requiredHeaders
parameter_list|)
block|{
name|setPublicKeyProvider
argument_list|(
name|publicKeyProvider
argument_list|)
expr_stmt|;
name|setSecurityProvider
argument_list|(
name|securityProvider
argument_list|)
expr_stmt|;
name|setAlgorithmProvider
argument_list|(
name|algorithmProvider
argument_list|)
expr_stmt|;
name|this
operator|.
name|signatureValidator
operator|=
operator|new
name|TomitribeSignatureValidator
argument_list|(
name|requiredHeaders
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setPublicKeyProvider
parameter_list|(
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|)
block|{
name|this
operator|.
name|publicKeyProvider
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|publicKeyProvider
argument_list|,
literal|"public key provider cannot be null"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setSecurityProvider
parameter_list|(
name|SecurityProvider
name|securityProvider
parameter_list|)
block|{
name|this
operator|.
name|securityProvider
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|securityProvider
argument_list|,
literal|"security provider cannot be null"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setAlgorithmProvider
parameter_list|(
name|AlgorithmProvider
name|algorithmProvider
parameter_list|)
block|{
name|this
operator|.
name|algorithmProvider
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|algorithmProvider
argument_list|,
literal|"algorithm provider cannot be null"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|verifyMessage
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|messageHeaders
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|SignatureHeaderUtils
operator|.
name|inspectMessageHeaders
argument_list|(
name|messageHeaders
argument_list|)
expr_stmt|;
name|inspectMissingSignatureHeader
argument_list|(
name|messageHeaders
argument_list|)
expr_stmt|;
name|inspectMultipleSignatureHeaders
argument_list|(
name|messageHeaders
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Starting signature verification"
argument_list|)
expr_stmt|;
name|signatureValidator
operator|.
name|validate
argument_list|(
name|messageHeaders
argument_list|,
name|algorithmProvider
argument_list|,
name|publicKeyProvider
argument_list|,
name|securityProvider
argument_list|,
name|method
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished signature verification"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|inspectMultipleSignatureHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|responseHeaders
parameter_list|)
block|{
if|if
condition|(
name|responseHeaders
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|MultipleSignatureHeaderException
argument_list|(
literal|"there are multiple signature headers in request"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|inspectMissingSignatureHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|responseHeaders
parameter_list|)
block|{
if|if
condition|(
operator|!
name|responseHeaders
operator|.
name|containsKey
argument_list|(
literal|"Signature"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MissingSignatureHeaderException
argument_list|(
literal|"there is no signature header in request"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

