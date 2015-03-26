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
name|client
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
name|math
operator|.
name|BigInteger
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
name|CertificateEncodingException
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
name|UUID
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
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|exception
operator|.
name|ExceptionMapper
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
name|exception
operator|.
name|XKMSException
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
name|exception
operator|.
name|XKMSLocateException
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
name|exception
operator|.
name|XKMSValidateException
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
name|Applications
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
name|XKMSConstants
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
name|KeyUsageEnum
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
name|LocateRequestType
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
name|LocateResultType
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
name|MessageAbstractType
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
name|QueryKeyBindingType
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
name|xkms
operator|.
name|ValidateRequestType
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
name|ValidateResultType
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3
operator|.
name|_2002
operator|.
name|_03
operator|.
name|xkms_wsdl
operator|.
name|XKMSPortType
import|;
end_import

begin_class
specifier|public
class|class
name|XKMSInvoker
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XKMSInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
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
name|ObjectFactory
name|DSIG_OF
init|=
operator|new
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
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
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
name|ObjectFactory
name|XKMS_OF
init|=
operator|new
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
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XKMS_LOCATE_INVALID_CERTIFICATE
init|=
literal|"Cannot instantiate X509 certificate from XKMS response"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XKMS_VALIDATE_ERROR
init|=
literal|"Certificate [%s] is not valid"
decl_stmt|;
specifier|private
specifier|final
name|XKMSPortType
name|xkmsConsumer
decl_stmt|;
specifier|public
name|XKMSInvoker
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|)
block|{
name|this
operator|.
name|xkmsConsumer
operator|=
name|xkmsConsumer
expr_stmt|;
block|}
specifier|public
name|X509Certificate
name|getServiceCertificate
parameter_list|(
name|QName
name|serviceName
parameter_list|)
block|{
return|return
name|getCertificateForId
argument_list|(
name|Applications
operator|.
name|SERVICE_NAME
argument_list|,
name|serviceName
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|X509Certificate
name|getCertificateForId
parameter_list|(
name|Applications
name|application
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|X509AppId
argument_list|(
name|application
argument_list|,
name|id
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|getCertificate
argument_list|(
name|ids
argument_list|)
return|;
block|}
specifier|public
name|X509Certificate
name|getCertificateForIssuerSerial
parameter_list|(
name|String
name|issuerDN
parameter_list|,
name|BigInteger
name|serial
parameter_list|)
block|{
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
operator|new
name|X509AppId
argument_list|(
name|Applications
operator|.
name|ISSUER
argument_list|,
name|issuerDN
argument_list|)
argument_list|)
expr_stmt|;
name|ids
operator|.
name|add
argument_list|(
operator|new
name|X509AppId
argument_list|(
name|Applications
operator|.
name|SERIAL
argument_list|,
name|serial
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|getCertificate
argument_list|(
name|ids
argument_list|)
return|;
block|}
specifier|public
name|X509Certificate
name|getCertificateForEndpoint
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
operator|new
name|X509AppId
argument_list|(
name|Applications
operator|.
name|SERVICE_ENDPOINT
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|getCertificate
argument_list|(
name|ids
argument_list|)
return|;
block|}
specifier|public
name|X509Certificate
name|getCertificate
parameter_list|(
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
parameter_list|)
block|{
try|try
block|{
name|LocateRequestType
name|locateRequestType
init|=
name|prepareLocateXKMSRequest
argument_list|(
name|ids
argument_list|)
decl_stmt|;
name|LocateResultType
name|locateResultType
init|=
name|xkmsConsumer
operator|.
name|locate
argument_list|(
name|locateRequestType
argument_list|)
decl_stmt|;
return|return
name|parseLocateXKMSResponse
argument_list|(
name|locateResultType
argument_list|,
name|ids
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"XKMS locate call fails for certificate: %s. Error: %s"
argument_list|,
name|ids
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|XKMSLocateException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|validateCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
block|{
return|return
name|checkCertificateValidity
argument_list|(
name|cert
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|validateDirectTrustCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
block|{
return|return
name|checkCertificateValidity
argument_list|(
name|cert
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|checkCertificateValidity
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|boolean
name|directTrust
parameter_list|)
block|{
try|try
block|{
name|ValidateRequestType
name|validateRequestType
init|=
name|prepareValidateXKMSRequest
argument_list|(
name|cert
argument_list|)
decl_stmt|;
if|if
condition|(
name|directTrust
condition|)
block|{
name|validateRequestType
operator|.
name|getQueryKeyBinding
argument_list|()
operator|.
name|getKeyUsage
argument_list|()
operator|.
name|add
argument_list|(
name|KeyUsageEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SIGNATURE
argument_list|)
expr_stmt|;
block|}
name|ValidateResultType
name|validateResultType
init|=
name|xkmsConsumer
operator|.
name|validate
argument_list|(
name|validateRequestType
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|cert
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|CertificateValidationResult
name|result
init|=
name|parseValidateXKMSResponse
argument_list|(
name|validateResultType
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Certificate %s is not valid: %s"
argument_list|,
name|cert
operator|.
name|getSubjectDN
argument_list|()
argument_list|,
name|result
operator|.
name|getDescription
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|isValid
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"XKMS validate call fails for certificate: %s. Error: %s"
argument_list|,
name|cert
operator|.
name|getSubjectDN
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|XKMSValidateException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|LocateRequestType
name|prepareLocateXKMSRequest
parameter_list|(
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
parameter_list|)
block|{
name|QueryKeyBindingType
name|queryKeyBindingType
init|=
name|XKMS_OF
operator|.
name|createQueryKeyBindingType
argument_list|()
decl_stmt|;
for|for
control|(
name|X509AppId
name|id
range|:
name|ids
control|)
block|{
name|UseKeyWithType
name|useKeyWithType
init|=
name|XKMS_OF
operator|.
name|createUseKeyWithType
argument_list|()
decl_stmt|;
name|useKeyWithType
operator|.
name|setIdentifier
argument_list|(
name|id
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|useKeyWithType
operator|.
name|setApplication
argument_list|(
name|id
operator|.
name|getApplication
argument_list|()
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|queryKeyBindingType
operator|.
name|getUseKeyWith
argument_list|()
operator|.
name|add
argument_list|(
name|useKeyWithType
argument_list|)
expr_stmt|;
block|}
name|LocateRequestType
name|locateRequestType
init|=
name|XKMS_OF
operator|.
name|createLocateRequestType
argument_list|()
decl_stmt|;
name|locateRequestType
operator|.
name|setQueryKeyBinding
argument_list|(
name|queryKeyBindingType
argument_list|)
expr_stmt|;
name|setGenericRequestParams
argument_list|(
name|locateRequestType
argument_list|)
expr_stmt|;
return|return
name|locateRequestType
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|X509Certificate
name|parseLocateXKMSResponse
parameter_list|(
name|LocateResultType
name|locateResultType
parameter_list|,
name|List
argument_list|<
name|X509AppId
argument_list|>
name|ids
parameter_list|)
block|{
name|XKMSException
name|exception
init|=
name|ExceptionMapper
operator|.
name|fromResponse
argument_list|(
name|locateResultType
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
if|if
condition|(
operator|!
name|locateResultType
operator|.
name|getUnverifiedKeyBinding
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"X509Certificate is not found in XKMS for id: "
operator|+
name|ids
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|KeyInfoType
name|keyInfo
init|=
name|locateResultType
operator|.
name|getUnverifiedKeyBinding
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|keyInfo
operator|.
name|getContent
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"X509Certificate is not found in XKMS for id: "
operator|+
name|ids
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|JAXBElement
argument_list|<
name|X509DataType
argument_list|>
name|x509Data
init|=
operator|(
name|JAXBElement
argument_list|<
name|X509DataType
argument_list|>
operator|)
name|keyInfo
operator|.
name|getContent
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|byte
index|[]
argument_list|>
name|certificate
init|=
operator|(
name|JAXBElement
argument_list|<
name|byte
index|[]
argument_list|>
operator|)
name|x509Data
operator|.
name|getValue
argument_list|()
operator|.
name|getX509IssuerSerialOrX509SKIOrX509SubjectName
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|CertificateFactory
name|cf
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|cf
operator|.
name|generateCertificate
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|certificate
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|cert
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
name|XKMSLocateException
argument_list|(
name|XKMS_LOCATE_INVALID_CERTIFICATE
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|ValidateRequestType
name|prepareValidateXKMSRequest
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
block|{
name|JAXBElement
argument_list|<
name|byte
index|[]
argument_list|>
name|x509Cert
decl_stmt|;
try|try
block|{
name|x509Cert
operator|=
name|DSIG_OF
operator|.
name|createX509DataTypeX509Certificate
argument_list|(
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|X509DataType
name|x509DataType
init|=
name|DSIG_OF
operator|.
name|createX509DataType
argument_list|()
decl_stmt|;
name|x509DataType
operator|.
name|getX509IssuerSerialOrX509SKIOrX509SubjectName
argument_list|()
operator|.
name|add
argument_list|(
name|x509Cert
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|X509DataType
argument_list|>
name|x509Data
init|=
name|DSIG_OF
operator|.
name|createX509Data
argument_list|(
name|x509DataType
argument_list|)
decl_stmt|;
name|KeyInfoType
name|keyInfoType
init|=
name|DSIG_OF
operator|.
name|createKeyInfoType
argument_list|()
decl_stmt|;
name|keyInfoType
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|x509Data
argument_list|)
expr_stmt|;
name|QueryKeyBindingType
name|queryKeyBindingType
init|=
name|XKMS_OF
operator|.
name|createQueryKeyBindingType
argument_list|()
decl_stmt|;
name|queryKeyBindingType
operator|.
name|setKeyInfo
argument_list|(
name|keyInfoType
argument_list|)
expr_stmt|;
name|ValidateRequestType
name|validateRequestType
init|=
name|XKMS_OF
operator|.
name|createValidateRequestType
argument_list|()
decl_stmt|;
name|setGenericRequestParams
argument_list|(
name|validateRequestType
argument_list|)
expr_stmt|;
name|validateRequestType
operator|.
name|setQueryKeyBinding
argument_list|(
name|queryKeyBindingType
argument_list|)
expr_stmt|;
comment|// temporary
name|validateRequestType
operator|.
name|setId
argument_list|(
name|cert
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|validateRequestType
return|;
block|}
specifier|protected
name|CertificateValidationResult
name|parseValidateXKMSResponse
parameter_list|(
name|ValidateResultType
name|validateResultType
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|XKMSException
name|exception
init|=
name|ExceptionMapper
operator|.
name|fromResponse
argument_list|(
name|validateResultType
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
name|StatusType
name|status
init|=
name|validateResultType
operator|.
name|getKeyBinding
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALID
operator|!=
name|status
operator|.
name|getStatusValue
argument_list|()
condition|)
block|{
return|return
operator|new
name|CertificateValidationResult
argument_list|(
literal|false
argument_list|,
name|XKMS_VALIDATE_ERROR
argument_list|)
return|;
block|}
return|return
operator|new
name|CertificateValidationResult
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|CertificateValidationResult
block|{
specifier|private
specifier|final
name|boolean
name|valid
decl_stmt|;
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
specifier|public
name|CertificateValidationResult
parameter_list|(
name|boolean
name|valid
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|valid
operator|=
name|valid
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|valid
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
specifier|private
name|void
name|setGenericRequestParams
parameter_list|(
name|MessageAbstractType
name|request
parameter_list|)
block|{
name|request
operator|.
name|setService
argument_list|(
name|XKMSConstants
operator|.
name|XKMS_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|request
operator|.
name|setId
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

