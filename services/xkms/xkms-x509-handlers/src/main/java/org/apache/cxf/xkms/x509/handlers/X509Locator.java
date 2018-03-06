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
name|exception
operator|.
name|XKMSCertificateException
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
name|Locator
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
name|ResultMajorEnum
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
name|ResultMinorEnum
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
name|UnverifiedKeyBindingType
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
name|model
operator|.
name|xmldsig
operator|.
name|X509IssuerSerialType
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
name|X509Locator
implements|implements
name|Locator
block|{
specifier|private
name|CertificateRepo
name|certRepo
decl_stmt|;
specifier|public
name|X509Locator
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
block|}
annotation|@
name|Override
specifier|public
name|UnverifiedKeyBindingType
name|locate
parameter_list|(
name|LocateRequestType
name|request
parameter_list|)
block|{
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|keyIDs
init|=
name|parse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|X509Certificate
name|cert
decl_stmt|;
try|try
block|{
name|cert
operator|=
name|findCertificate
argument_list|(
name|keyIDs
argument_list|)
expr_stmt|;
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|UnverifiedKeyBindingType
name|result
init|=
operator|new
name|UnverifiedKeyBindingType
argument_list|()
decl_stmt|;
name|result
operator|.
name|setKeyInfo
argument_list|(
name|X509Utils
operator|.
name|getKeyInfo
argument_list|(
name|cert
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XKMSCertificateException
argument_list|(
literal|"Cannot encode certificate: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|XKMSCertificateException
argument_list|(
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e1
argument_list|)
throw|;
block|}
block|}
specifier|public
name|X509Certificate
name|findCertificate
parameter_list|(
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|ids
parameter_list|)
throws|throws
name|CertificateException
block|{
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No UseKeyWithType elements found"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ids
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Applications
name|application
init|=
name|Applications
operator|.
name|fromUri
argument_list|(
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|ids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|application
operator|==
name|Applications
operator|.
name|PKIX
condition|)
block|{
name|cert
operator|=
name|certRepo
operator|.
name|findBySubjectDn
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|application
operator|==
name|Applications
operator|.
name|SERVICE_NAME
condition|)
block|{
name|cert
operator|=
name|certRepo
operator|.
name|findByServiceName
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|application
operator|==
name|Applications
operator|.
name|SERVICE_ENDPOINT
condition|)
block|{
name|cert
operator|=
name|certRepo
operator|.
name|findByEndpoint
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|issuer
init|=
name|getIdForApplication
argument_list|(
name|Applications
operator|.
name|ISSUER
argument_list|,
name|ids
argument_list|)
decl_stmt|;
name|String
name|serial
init|=
name|getIdForApplication
argument_list|(
name|Applications
operator|.
name|SERIAL
argument_list|,
name|ids
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|issuer
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|serial
operator|!=
literal|null
operator|)
condition|)
block|{
name|cert
operator|=
name|certRepo
operator|.
name|findByIssuerSerial
argument_list|(
name|issuer
argument_list|,
name|serial
argument_list|)
expr_stmt|;
block|}
return|return
name|cert
return|;
block|}
specifier|private
name|String
name|getIdForApplication
parameter_list|(
name|Applications
name|application
parameter_list|,
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|ids
parameter_list|)
block|{
for|for
control|(
name|UseKeyWithType
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|application
operator|.
name|getUri
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|id
operator|.
name|getApplication
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|id
operator|.
name|getIdentifier
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|parse
parameter_list|(
name|LocateRequestType
name|request
parameter_list|)
block|{
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|keyIDs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
return|return
name|keyIDs
return|;
block|}
name|QueryKeyBindingType
name|query
init|=
name|request
operator|.
name|getQueryKeyBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
return|return
name|keyIDs
return|;
block|}
comment|// http://www.w3.org/TR/xkms2/ [213]
if|if
condition|(
name|query
operator|.
name|getTimeInstant
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|XKMSException
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_RECEIVER
argument_list|,
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_TIME_INSTANT_NOT_SUPPORTED
argument_list|)
throw|;
block|}
name|keyIDs
operator|.
name|addAll
argument_list|(
name|parse
argument_list|(
name|query
operator|.
name|getKeyInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|useKeyList
init|=
name|query
operator|.
name|getUseKeyWith
argument_list|()
decl_stmt|;
name|keyIDs
operator|.
name|addAll
argument_list|(
name|useKeyList
argument_list|)
expr_stmt|;
return|return
name|keyIDs
return|;
block|}
specifier|private
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|parse
parameter_list|(
name|KeyInfoType
name|keyInfo
parameter_list|)
block|{
name|List
argument_list|<
name|UseKeyWithType
argument_list|>
name|keyIDs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyInfo
operator|==
literal|null
condition|)
block|{
return|return
name|keyIDs
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|content
init|=
name|keyInfo
operator|.
name|getContent
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj1
range|:
name|content
control|)
block|{
if|if
condition|(
name|obj1
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|keyInfoChild
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj1
decl_stmt|;
if|if
condition|(
name|X509Utils
operator|.
name|X509_KEY_NAME
operator|.
name|equals
argument_list|(
name|keyInfoChild
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|UseKeyWithType
name|keyDN
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|keyDN
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|PKIX
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|keyDN
operator|.
name|setIdentifier
argument_list|(
operator|(
name|String
operator|)
name|keyInfoChild
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|keyIDs
operator|.
name|add
argument_list|(
name|keyDN
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|X509Utils
operator|.
name|X509_DATA
operator|.
name|equals
argument_list|(
name|keyInfoChild
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|X509DataType
name|x509Data
init|=
operator|(
name|X509DataType
operator|)
name|keyInfoChild
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|x509DataContent
init|=
name|x509Data
operator|.
name|getX509IssuerSerialOrX509SKIOrX509SubjectName
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj2
range|:
name|x509DataContent
control|)
block|{
if|if
condition|(
name|obj2
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|x509DataChild
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj2
decl_stmt|;
if|if
condition|(
name|X509Utils
operator|.
name|X509_ISSUER_SERIAL
operator|.
name|equals
argument_list|(
name|x509DataChild
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|X509IssuerSerialType
name|x509IssuerSerial
init|=
operator|(
name|X509IssuerSerialType
operator|)
name|x509DataChild
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|UseKeyWithType
name|issuer
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|issuer
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|ISSUER
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|issuer
operator|.
name|setIdentifier
argument_list|(
name|x509IssuerSerial
operator|.
name|getX509IssuerName
argument_list|()
argument_list|)
expr_stmt|;
name|keyIDs
operator|.
name|add
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
name|UseKeyWithType
name|serial
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|serial
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|SERIAL
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|serial
operator|.
name|setIdentifier
argument_list|(
name|x509IssuerSerial
operator|.
name|getX509SerialNumber
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|keyIDs
operator|.
name|add
argument_list|(
name|serial
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|X509Utils
operator|.
name|X509_SUBJECT_NAME
operator|.
name|equals
argument_list|(
name|x509DataChild
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|UseKeyWithType
name|keyDN
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|keyDN
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|PKIX
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|keyDN
operator|.
name|setIdentifier
argument_list|(
operator|(
name|String
operator|)
name|x509DataChild
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|keyIDs
operator|.
name|add
argument_list|(
name|keyDN
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|keyIDs
return|;
block|}
block|}
end_class

end_unit

