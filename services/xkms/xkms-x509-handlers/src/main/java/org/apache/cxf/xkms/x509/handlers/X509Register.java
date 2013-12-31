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
name|xkms
operator|.
name|x509
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|CertificateException
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
name|CertificateFactory
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|xkms
operator|.
name|handlers
operator|.
name|Register
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyBindingEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|KeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|PrototypeKeyBindingType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RecoverRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RecoverResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RegisterResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ReissueRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ReissueResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RequestAbstractType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RespondWithEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RevokeRequestType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|RevokeResultType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|StatusType
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|UseKeyWithType
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
name|xkms
operator|.
name|model
operator|.
name|xmldsig
operator|.
name|KeyInfoType
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
name|xkms
operator|.
name|model
operator|.
name|xmldsig
operator|.
name|X509DataType
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
name|xkms
operator|.
name|x509
operator|.
name|repo
operator|.
name|CertificateRepo
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
name|xkms
operator|.
name|x509
operator|.
name|utils
operator|.
name|X509Utils
import|;
end_import

begin_class
specifier|public
class|class
name|X509Register
implements|implements
name|Register
block|{
specifier|protected
specifier|final
name|CertificateFactory
name|certFactory
decl_stmt|;
specifier|private
name|CertificateRepo
name|certRepo
decl_stmt|;
specifier|public
name|X509Register
parameter_list|(
name|CertificateRepo
name|certRepo
parameter_list|)
throws|throws
name|CertificateException
block|{
name|this
operator|.
name|certRepo
operator|=
name|certRepo
expr_stmt|;
name|certFactory
operator|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canProcess
parameter_list|(
name|RequestAbstractType
name|request
parameter_list|)
block|{
if|if
condition|(
name|request
operator|instanceof
name|RecoverRequestType
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|respondWithList
init|=
name|request
operator|.
name|getRespondWith
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|respondWithList
operator|!=
literal|null
operator|)
operator|&&
operator|!
operator|(
name|respondWithList
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
name|respondWithList
operator|.
name|contains
argument_list|(
name|RespondWithEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_X_509_CERT
argument_list|)
return|;
block|}
else|else
block|{
comment|// Default handler
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RegisterResultType
name|register
parameter_list|(
name|RegisterRequestType
name|request
parameter_list|,
name|RegisterResultType
name|response
parameter_list|)
block|{
try|try
block|{
name|PrototypeKeyBindingType
name|binding
init|=
name|request
operator|.
name|getPrototypeKeyBinding
argument_list|()
decl_stmt|;
name|X509Utils
operator|.
name|assertElementNotNull
argument_list|(
name|binding
argument_list|,
name|PrototypeKeyBindingType
operator|.
name|class
argument_list|)
expr_stmt|;
name|KeyInfoType
name|keyInfo
init|=
name|binding
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
name|X509Utils
operator|.
name|assertElementNotNull
argument_list|(
name|binding
argument_list|,
name|KeyInfoType
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|useKeyWithList
init|=
name|binding
operator|.
name|getUseKeyWith
argument_list|()
decl_stmt|;
if|if
condition|(
name|useKeyWithList
operator|==
literal|null
operator|||
name|useKeyWithList
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Exactly one useKeyWith element is supported"
argument_list|)
throw|;
comment|//TODO standard requires support for multiple useKeyWith attributes
block|}
name|UseKeyWithType
name|useKeyWith
init|=
name|useKeyWithList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certList
init|=
name|getCertsFromKeyInfo
argument_list|(
name|keyInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|certList
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must provide one X509Certificate"
argument_list|)
throw|;
block|}
name|X509Certificate
name|cert
init|=
name|certList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|certRepo
operator|.
name|saveCertificate
argument_list|(
name|cert
argument_list|,
name|useKeyWith
argument_list|)
expr_stmt|;
name|KeyBindingType
name|responseBinding
init|=
name|prepareResponseBinding
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|response
operator|.
name|getKeyBinding
argument_list|()
operator|.
name|add
argument_list|(
name|responseBinding
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|KeyBindingType
name|prepareResponseBinding
parameter_list|(
name|PrototypeKeyBindingType
name|binding
parameter_list|)
block|{
name|KeyBindingType
name|responseBinding
init|=
operator|new
name|KeyBindingType
argument_list|()
decl_stmt|;
name|responseBinding
operator|.
name|setKeyInfo
argument_list|(
name|binding
operator|.
name|getKeyInfo
argument_list|()
argument_list|)
expr_stmt|;
name|StatusType
name|status
init|=
operator|new
name|StatusType
argument_list|()
decl_stmt|;
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALID
argument_list|)
expr_stmt|;
name|responseBinding
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
return|return
name|responseBinding
return|;
block|}
annotation|@
name|Override
specifier|public
name|ReissueResultType
name|reissue
parameter_list|(
name|ReissueRequestType
name|request
parameter_list|,
name|ReissueResultType
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This service does not support reissue"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|RevokeResultType
name|revoke
parameter_list|(
name|RevokeRequestType
name|request
parameter_list|,
name|RevokeResultType
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This service does not support revoke"
argument_list|)
throw|;
block|}
specifier|private
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getCertsFromKeyInfo
parameter_list|(
name|KeyInfoType
name|keyInfo
parameter_list|)
throws|throws
name|CertificateException
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certList
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|keyInfo
operator|.
name|getContent
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|instanceof
name|JAXBElement
condition|)
block|{
name|Object
name|value
init|=
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|key
operator|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|X509DataType
condition|)
block|{
name|X509DataType
name|x509Data
init|=
operator|(
name|X509DataType
operator|)
name|value
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|data
init|=
name|x509Data
operator|.
name|getX509IssuerSerialOrX509SKIOrX509SubjectName
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|certO
range|:
name|data
control|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|certO2
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|certO
decl_stmt|;
if|if
condition|(
name|certO2
operator|.
name|getDeclaredType
argument_list|()
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
name|byte
index|[]
name|certContent
init|=
operator|(
name|byte
index|[]
operator|)
name|certO2
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|certFactory
operator|.
name|generateCertificate
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|certContent
argument_list|)
argument_list|)
decl_stmt|;
name|certList
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|certList
return|;
block|}
annotation|@
name|Override
specifier|public
name|RecoverResultType
name|recover
parameter_list|(
name|RecoverRequestType
name|request
parameter_list|,
name|RecoverResultType
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Recover is currently not supported"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

