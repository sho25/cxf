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
name|cert
operator|.
name|CertificateExpiredException
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
name|CertificateNotYetValidException
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
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_class
specifier|public
class|class
name|DateValidator
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
name|DateValidator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Checks if a certificate is within its validity period.      *      * @param certificate to check      * @return the validity state of the certificate      */
specifier|public
name|boolean
name|isCertificateValid
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|)
block|{
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
try|try
block|{
name|certificate
operator|.
name|checkValidity
argument_list|(
name|Date
operator|.
name|from
argument_list|(
name|dateTime
operator|.
name|toInstant
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateNotYetValidException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|CertificateExpiredException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
comment|/*          * TODO: clarify use of KeyUsage with customer if (null == certificate.getKeyUsage()) { return false; }          * boolean[] keyUsage = certificate.getKeyUsage(); if (!keyUsage[KeyUsage.digitalSignature.ordinal()] ||          * !keyUsage[KeyUsage.dataEncipherment.ordinal()] || keyUsage[KeyUsage.encipherOnly.ordinal()] ||          * keyUsage[KeyUsage.decipherOnly.ordinal()]) { return false; }          */
return|return
literal|true
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
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALIDITY_INTERVAL
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
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_VALIDITY_INTERVAL
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
name|isCertificateChainValid
parameter_list|(
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certificates
parameter_list|)
block|{
if|if
condition|(
name|certificates
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|X509Certificate
name|x509Certificate
range|:
name|certificates
control|)
block|{
if|if
condition|(
operator|!
name|isCertificateValid
argument_list|(
name|x509Certificate
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Certificate is expired: "
operator|+
name|x509Certificate
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

