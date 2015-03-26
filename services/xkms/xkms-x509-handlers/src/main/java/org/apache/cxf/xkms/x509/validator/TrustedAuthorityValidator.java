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
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidAlgorithmParameterException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|CertPath
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
name|CertPathBuilder
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
name|CertPathBuilderException
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
name|CertPathValidator
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
name|CertPathValidatorException
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
name|CertStore
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
name|CertStoreParameters
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
name|CollectionCertStoreParameters
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
name|PKIXBuilderParameters
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
name|TrustAnchor
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
name|X509CRL
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
name|X509CertSelector
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
name|HashSet
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
name|Set
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
name|Level
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
name|xkms
operator|.
name|handlers
operator|.
name|Validator
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
name|x509
operator|.
name|repo
operator|.
name|CertificateRepo
import|;
end_import

begin_class
specifier|public
class|class
name|TrustedAuthorityValidator
implements|implements
name|Validator
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
name|TrustedAuthorityValidator
operator|.
name|class
argument_list|)
decl_stmt|;
name|CertificateRepo
name|certRepo
decl_stmt|;
name|boolean
name|enableRevocation
init|=
literal|true
decl_stmt|;
specifier|public
name|TrustedAuthorityValidator
parameter_list|(
name|CertificateRepo
name|certRepo
parameter_list|)
block|{
name|this
operator|.
name|certRepo
operator|=
name|certRepo
expr_stmt|;
block|}
comment|/**      * Checks if a certificate is signed by a trusted authority.      *       * @param x509Certificate to check      * @return the validity state of the certificate      */
name|boolean
name|isCertificateChainValid
parameter_list|(
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certificates
parameter_list|)
block|{
name|X509Certificate
name|targetCert
init|=
name|certificates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|X509CertSelector
name|selector
init|=
operator|new
name|X509CertSelector
argument_list|()
decl_stmt|;
name|selector
operator|.
name|setCertificate
argument_list|(
name|targetCert
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|intermediateCerts
init|=
name|certRepo
operator|.
name|getCaCerts
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|trustedAuthorityCerts
init|=
name|certRepo
operator|.
name|getTrustedCaCerts
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|TrustAnchor
argument_list|>
name|trustAnchors
init|=
name|asTrustAnchors
argument_list|(
name|trustedAuthorityCerts
argument_list|)
decl_stmt|;
name|CertStoreParameters
name|intermediateParams
init|=
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|intermediateCerts
argument_list|)
decl_stmt|;
name|CertStoreParameters
name|certificateParams
init|=
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|certificates
argument_list|)
decl_stmt|;
name|PKIXBuilderParameters
name|pkixParams
init|=
operator|new
name|PKIXBuilderParameters
argument_list|(
name|trustAnchors
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|pkixParams
operator|.
name|addCertStore
argument_list|(
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
name|intermediateParams
argument_list|)
argument_list|)
expr_stmt|;
name|pkixParams
operator|.
name|addCertStore
argument_list|(
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
name|certificateParams
argument_list|)
argument_list|)
expr_stmt|;
name|pkixParams
operator|.
name|setRevocationEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CertPathBuilder
name|builder
init|=
name|CertPathBuilder
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
decl_stmt|;
name|CertPath
name|certPath
init|=
name|builder
operator|.
name|build
argument_list|(
name|pkixParams
argument_list|)
operator|.
name|getCertPath
argument_list|()
decl_stmt|;
comment|// Now validate the CertPath (including CRL checking)
if|if
condition|(
name|enableRevocation
condition|)
block|{
name|List
argument_list|<
name|X509CRL
argument_list|>
name|crls
init|=
name|certRepo
operator|.
name|getCRLs
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|crls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pkixParams
operator|.
name|setRevocationEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CertStoreParameters
name|crlParams
init|=
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|crls
argument_list|)
decl_stmt|;
name|pkixParams
operator|.
name|addCertStore
argument_list|(
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
name|crlParams
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|CertPathValidator
name|validator
init|=
name|CertPathValidator
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
decl_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|certPath
argument_list|,
name|pkixParams
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidAlgorithmParameterException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Invalid algorithm parameter by certificate chain validation. "
operator|+
literal|"It is likely that issuer certificates are not found in XKMS trusted storage. "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Unknown algorithm by trust chain validation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|CertPathBuilderException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot build certification path: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|CertPathValidatorException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot vaidate certification path: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|Set
argument_list|<
name|TrustAnchor
argument_list|>
name|asTrustAnchors
parameter_list|(
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|trustedAuthorityCerts
parameter_list|)
block|{
name|Set
argument_list|<
name|TrustAnchor
argument_list|>
name|trustAnchors
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|X509Certificate
name|trustedAuthorityCert
range|:
name|trustedAuthorityCerts
control|)
block|{
name|trustAnchors
operator|.
name|add
argument_list|(
operator|new
name|TrustAnchor
argument_list|(
name|trustedAuthorityCert
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|trustAnchors
return|;
block|}
annotation|@
name|Override
specifier|public
name|StatusType
name|validate
parameter_list|(
name|ValidateRequestType
name|request
parameter_list|)
block|{
name|StatusType
name|status
init|=
operator|new
name|StatusType
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certificates
init|=
name|ValidateRequestParser
operator|.
name|parse
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|certificates
operator|==
literal|null
operator|||
name|certificates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_INDETERMINATE
argument_list|)
expr_stmt|;
name|status
operator|.
name|getIndeterminateReason
argument_list|()
operator|.
name|add
argument_list|(
literal|"http://www.cxf.apache.org/2002/03/xkms#RequestNotSupported"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCertificateChainValid
argument_list|(
name|certificates
argument_list|)
condition|)
block|{
name|status
operator|.
name|getValidReason
argument_list|()
operator|.
name|add
argument_list|(
name|ReasonEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_ISSUER_TRUST
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|status
operator|.
name|getInvalidReason
argument_list|()
operator|.
name|add
argument_list|(
name|ReasonEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_ISSUER_TRUST
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|status
operator|.
name|setStatusValue
argument_list|(
name|KeyBindingEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_INVALID
argument_list|)
expr_stmt|;
block|}
return|return
name|status
return|;
block|}
specifier|public
name|boolean
name|isEnableRevocation
parameter_list|()
block|{
return|return
name|enableRevocation
return|;
block|}
specifier|public
name|void
name|setEnableRevocation
parameter_list|(
name|boolean
name|enableRevocation
parameter_list|)
block|{
name|this
operator|.
name|enableRevocation
operator|=
name|enableRevocation
expr_stmt|;
block|}
block|}
end_class

end_unit

