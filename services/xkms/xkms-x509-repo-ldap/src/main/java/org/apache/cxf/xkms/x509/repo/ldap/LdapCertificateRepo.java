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
name|repo
operator|.
name|ldap
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
name|CRLException
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|BasicAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|BasicAttributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchResult
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
name|LdapCertificateRepo
implements|implements
name|CertificateRepo
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
name|LdapCertificateRepo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ATTR_OBJECT_CLASS
init|=
literal|"objectClass"
decl_stmt|;
specifier|private
name|LdapSearch
name|ldapSearch
decl_stmt|;
specifier|private
name|String
name|rootDN
decl_stmt|;
specifier|private
name|CertificateFactory
name|certificateFactory
decl_stmt|;
specifier|private
specifier|final
name|LdapSchemaConfig
name|ldapConfig
decl_stmt|;
specifier|private
specifier|final
name|String
name|filterUIDTemplate
decl_stmt|;
specifier|private
specifier|final
name|String
name|filterIssuerSerialTemplate
decl_stmt|;
comment|/**      *      * @param ldapSearch      * @param rootDN rootDN of the LDAP tree      * @param trustedAuthorityFilter      * @param intermediateFilter      * @param attrName      */
specifier|public
name|LdapCertificateRepo
parameter_list|(
name|LdapSearch
name|ldapSearch
parameter_list|,
name|LdapSchemaConfig
name|ldapConfig
parameter_list|,
name|String
name|rootDN
parameter_list|)
block|{
name|this
operator|.
name|ldapSearch
operator|=
name|ldapSearch
expr_stmt|;
name|this
operator|.
name|ldapSearch
operator|=
name|ldapSearch
expr_stmt|;
name|this
operator|.
name|ldapConfig
operator|=
name|ldapConfig
expr_stmt|;
name|this
operator|.
name|rootDN
operator|=
name|rootDN
expr_stmt|;
try|try
block|{
name|this
operator|.
name|certificateFactory
operator|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|filterUIDTemplate
operator|=
literal|"("
operator|+
name|ldapConfig
operator|.
name|getAttrUID
argument_list|()
operator|+
literal|"=%s)"
expr_stmt|;
name|filterIssuerSerialTemplate
operator|=
literal|"(&("
operator|+
name|ldapConfig
operator|.
name|getAttrIssuerID
argument_list|()
operator|+
literal|"=%s)("
operator|+
name|ldapConfig
operator|.
name|getAttrSerialNumber
argument_list|()
operator|+
literal|"=%s))"
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getTrustedCaCerts
parameter_list|()
block|{
return|return
name|getCertificatesFromLdap
argument_list|(
name|rootDN
argument_list|,
name|ldapConfig
operator|.
name|getTrustedAuthorityFilter
argument_list|()
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getCaCerts
parameter_list|()
block|{
return|return
name|getCertificatesFromLdap
argument_list|(
name|rootDN
argument_list|,
name|ldapConfig
operator|.
name|getIntermediateFilter
argument_list|()
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|X509CRL
argument_list|>
name|getCRLs
parameter_list|()
block|{
return|return
name|getCRLsFromLdap
argument_list|(
name|rootDN
argument_list|,
name|ldapConfig
operator|.
name|getCrlFilter
argument_list|()
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrlBinary
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getCertificatesFromLdap
parameter_list|(
name|String
name|tmpRootDN
parameter_list|,
name|String
name|tmpFilter
parameter_list|,
name|String
name|tmpAttrName
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certificates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|answer
init|=
name|ldapSearch
operator|.
name|searchSubTree
argument_list|(
name|tmpRootDN
argument_list|,
name|tmpFilter
argument_list|)
decl_stmt|;
while|while
condition|(
name|answer
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|SearchResult
name|sr
init|=
name|answer
operator|.
name|next
argument_list|()
decl_stmt|;
name|Attributes
name|attrs
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Attribute
name|attribute
init|=
name|attrs
operator|.
name|get
argument_list|(
name|tmpAttrName
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
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
name|certificate
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
operator|(
name|byte
index|[]
operator|)
name|attribute
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|certificates
operator|.
name|add
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|certificates
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
catch|catch
parameter_list|(
name|NamingException
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
name|List
argument_list|<
name|X509CRL
argument_list|>
name|getCRLsFromLdap
parameter_list|(
name|String
name|tmpRootDN
parameter_list|,
name|String
name|tmpFilter
parameter_list|,
name|String
name|tmpAttrName
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|X509CRL
argument_list|>
name|crls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|answer
init|=
name|ldapSearch
operator|.
name|searchSubTree
argument_list|(
name|tmpRootDN
argument_list|,
name|tmpFilter
argument_list|)
decl_stmt|;
while|while
condition|(
name|answer
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|SearchResult
name|sr
init|=
name|answer
operator|.
name|next
argument_list|()
decl_stmt|;
name|Attributes
name|attrs
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Attribute
name|attribute
init|=
name|attrs
operator|.
name|get
argument_list|(
name|tmpAttrName
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
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
name|X509CRL
name|crl
init|=
operator|(
name|X509CRL
operator|)
name|cf
operator|.
name|generateCRL
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|attribute
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|crls
operator|.
name|add
argument_list|(
name|crl
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|crls
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
catch|catch
parameter_list|(
name|NamingException
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
catch|catch
parameter_list|(
name|CRLException
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
name|void
name|saveCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|String
name|dn
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appAttrs
parameter_list|)
block|{
name|Attributes
name|attribs
init|=
operator|new
name|BasicAttributes
argument_list|()
decl_stmt|;
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|ATTR_OBJECT_CLASS
argument_list|,
name|ldapConfig
operator|.
name|getCertObjectClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|ldapConfig
operator|.
name|getAttrUID
argument_list|()
argument_list|,
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|ldapConfig
operator|.
name|getAttrIssuerID
argument_list|()
argument_list|,
name|cert
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|ldapConfig
operator|.
name|getAttrSerialNumber
argument_list|()
argument_list|,
name|cert
operator|.
name|getSerialNumber
argument_list|()
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|addConstantAttributes
argument_list|(
name|ldapConfig
operator|.
name|getConstAttrNamesCSV
argument_list|()
argument_list|,
name|ldapConfig
operator|.
name|getConstAttrValuesCSV
argument_list|()
argument_list|,
name|attribs
argument_list|)
expr_stmt|;
if|if
condition|(
name|appAttrs
operator|!=
literal|null
operator|&&
operator|!
name|appAttrs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|appAttrs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|,
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ldapSearch
operator|.
name|bind
argument_list|(
name|dn
argument_list|,
name|attribs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|void
name|addConstantAttributes
parameter_list|(
name|String
name|names
parameter_list|,
name|String
name|values
parameter_list|,
name|Attributes
name|attribs
parameter_list|)
block|{
name|String
index|[]
name|arrNames
init|=
name|names
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
index|[]
name|arrValues
init|=
name|values
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|arrNames
operator|.
name|length
operator|!=
name|arrValues
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Inconsintent constant attributes: %s; %s"
argument_list|,
name|names
argument_list|,
name|values
argument_list|)
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|attribs
operator|.
name|put
argument_list|(
operator|new
name|BasicAttribute
argument_list|(
name|arrNames
index|[
name|i
index|]
argument_list|,
name|arrValues
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findBySubjectDn
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|dn
init|=
name|id
decl_stmt|;
if|if
condition|(
name|rootDN
operator|!=
literal|null
operator|&&
operator|!
name|rootDN
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|dn
operator|=
name|dn
operator|+
literal|","
operator|+
name|rootDN
expr_stmt|;
block|}
name|cert
operator|=
name|getCertificateForDn
argument_list|(
name|dn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Not found
block|}
comment|// Try to find certificate by search for uid attribute
try|try
block|{
name|cert
operator|=
name|getCertificateForUIDAttr
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Not found
block|}
return|return
name|cert
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findByServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|dn
init|=
name|getDnForIdentifier
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|cert
operator|=
name|getCertificateForDn
argument_list|(
name|dn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Not found
block|}
comment|// Try to find certificate by search for uid attribute
try|try
block|{
name|String
name|uidAttr
init|=
name|String
operator|.
name|format
argument_list|(
name|ldapConfig
operator|.
name|getServiceCertUIDTemplate
argument_list|()
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|cert
operator|=
name|getCertificateForUIDAttr
argument_list|(
name|uidAttr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Not found
block|}
return|return
name|cert
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findByEndpoint
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
name|String
name|filter
init|=
name|String
operator|.
name|format
argument_list|(
literal|"(%s=%s)"
argument_list|,
name|ldapConfig
operator|.
name|getAttrEndpoint
argument_list|()
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
try|try
block|{
name|Attribute
name|attr
init|=
name|ldapSearch
operator|.
name|findAttribute
argument_list|(
name|rootDN
argument_list|,
name|filter
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
decl_stmt|;
name|cert
operator|=
name|getCert
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Not found
block|}
return|return
name|cert
return|;
block|}
specifier|private
name|String
name|getDnForIdentifier
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|String
name|escapedIdentifier
init|=
name|id
operator|.
name|replaceAll
argument_list|(
literal|"\\/"
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
literal|"\\/"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
name|ldapConfig
operator|.
name|getServiceCertRDNTemplate
argument_list|()
argument_list|,
name|escapedIdentifier
argument_list|)
operator|+
literal|","
operator|+
name|rootDN
return|;
block|}
specifier|private
name|X509Certificate
name|getCertificateForDn
parameter_list|(
name|String
name|dn
parameter_list|)
throws|throws
name|NamingException
block|{
name|Attribute
name|attr
init|=
name|ldapSearch
operator|.
name|getAttribute
argument_list|(
name|dn
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getCert
argument_list|(
name|attr
argument_list|)
return|;
block|}
specifier|private
name|X509Certificate
name|getCertificateForUIDAttr
parameter_list|(
name|String
name|uid
parameter_list|)
throws|throws
name|NamingException
block|{
name|String
name|filter
init|=
name|String
operator|.
name|format
argument_list|(
name|filterUIDTemplate
argument_list|,
name|uid
argument_list|)
decl_stmt|;
name|Attribute
name|attr
init|=
name|ldapSearch
operator|.
name|findAttribute
argument_list|(
name|rootDN
argument_list|,
name|filter
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getCert
argument_list|(
name|attr
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findByIssuerSerial
parameter_list|(
name|String
name|issuer
parameter_list|,
name|String
name|serial
parameter_list|)
block|{
if|if
condition|(
name|issuer
operator|==
literal|null
operator|||
name|serial
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Issuer and serial applications are expected in request"
argument_list|)
throw|;
block|}
name|String
name|filter
init|=
name|String
operator|.
name|format
argument_list|(
name|filterIssuerSerialTemplate
argument_list|,
name|issuer
argument_list|,
name|serial
argument_list|)
decl_stmt|;
try|try
block|{
name|Attribute
name|attr
init|=
name|ldapSearch
operator|.
name|findAttribute
argument_list|(
name|rootDN
argument_list|,
name|filter
argument_list|,
name|ldapConfig
operator|.
name|getAttrCrtBinary
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getCert
argument_list|(
name|attr
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
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
name|X509Certificate
name|getCert
parameter_list|(
name|Attribute
name|attr
parameter_list|)
block|{
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|byte
index|[]
name|data
decl_stmt|;
try|try
block|{
name|data
operator|=
operator|(
name|byte
index|[]
operator|)
name|attr
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
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
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
operator|(
name|X509Certificate
operator|)
name|certificateFactory
operator|.
name|generateCertificate
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
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
literal|"Error deserializing certificate: "
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|UseKeyWithType
name|key
parameter_list|)
block|{
name|Applications
name|application
init|=
name|Applications
operator|.
name|fromUri
argument_list|(
name|key
operator|.
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|dn
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attrs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|dn
operator|=
name|key
operator|.
name|getIdentifier
argument_list|()
operator|+
literal|","
operator|+
name|rootDN
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
name|dn
operator|=
name|getDnForIdentifier
argument_list|(
name|key
operator|.
name|getIdentifier
argument_list|()
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
name|attrs
operator|.
name|put
argument_list|(
name|ldapConfig
operator|.
name|getAttrEndpoint
argument_list|()
argument_list|,
name|key
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|dn
operator|=
name|getDnForIdentifier
argument_list|(
name|key
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported Application "
operator|+
name|application
argument_list|)
throw|;
block|}
name|saveCertificate
argument_list|(
name|cert
argument_list|,
name|dn
argument_list|,
name|attrs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

