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
name|itests
operator|.
name|handlers
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|itests
operator|.
name|BasicIntegrationTest
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
name|ReasonEnum
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
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
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
specifier|public
class|class
name|ValidatorCRLTest
extends|extends
name|BasicIntegrationTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_TO_RESOURCES
init|=
literal|"/data/xkms/certificates/"
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ValidatorCRLTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|getConfig
parameter_list|()
block|{
return|return
operator|new
name|Option
index|[]
block|{
name|CoreOptions
operator|.
name|composite
argument_list|(
name|super
operator|.
name|getConfig
argument_list|()
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/crls/wss40CACRL.cer"
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.cxf.xkms.cfg"
argument_list|,
literal|"xkms.enableRevocation"
argument_list|,
literal|"true"
argument_list|)
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidCertWithCRL
parameter_list|()
throws|throws
name|CertificateException
block|{
name|X509Certificate
name|wss40Certificate
init|=
name|readCertificate
argument_list|(
literal|"wss40.cer"
argument_list|)
decl_stmt|;
name|ValidateRequestType
name|request
init|=
name|prepareValidateXKMSRequest
argument_list|(
name|wss40Certificate
argument_list|)
decl_stmt|;
name|StatusType
name|result
init|=
name|doValidate
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALID
argument_list|,
name|result
operator|.
name|getStatusValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result
operator|.
name|getValidReason
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ReasonEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALIDITY_INTERVAL
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getValidReason
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ReasonEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_ISSUER_TRUST
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getValidReason
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRevokedCertificate
parameter_list|()
throws|throws
name|CertificateException
block|{
name|X509Certificate
name|wss40Certificate
init|=
name|readCertificate
argument_list|(
literal|"wss40rev.cer"
argument_list|)
decl_stmt|;
name|ValidateRequestType
name|request
init|=
name|prepareValidateXKMSRequest
argument_list|(
name|wss40Certificate
argument_list|)
decl_stmt|;
name|StatusType
name|result
init|=
name|doValidate
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_INVALID
argument_list|,
name|result
operator|.
name|getStatusValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result
operator|.
name|getInvalidReason
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ReasonEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_ISSUER_TRUST
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getInvalidReason
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Method is taken from {@link org.apache.cxf.xkms.client.XKMSInvoker}.      */
specifier|private
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
specifier|private
name|X509Certificate
name|readCertificate
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|CertificateException
block|{
name|InputStream
name|inputStream
init|=
name|ValidatorCRLTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|PATH_TO_RESOURCES
operator|+
name|path
argument_list|)
decl_stmt|;
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
return|return
operator|(
name|X509Certificate
operator|)
name|cf
operator|.
name|generateCertificate
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
specifier|private
name|StatusType
name|doValidate
parameter_list|(
name|ValidateRequestType
name|request
parameter_list|)
block|{
try|try
block|{
return|return
name|xkmsService
operator|.
name|validate
argument_list|(
name|request
argument_list|)
operator|.
name|getKeyBinding
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatus
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Avoid serialization problems for some exceptions when transported by pax exam
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

