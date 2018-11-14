begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * This software consists of voluntary contributions made by many  * individuals on behalf of the Apache Software Foundation.  For more  * information on the Apache Software Foundation, please see  *<http://www.apache.org/>.  *  */
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
operator|.
name|httpclient
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|Certificate
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
name|CertificateParsingException
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
name|Collection
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
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
name|javax
operator|.
name|naming
operator|.
name|InvalidNameException
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
name|ldap
operator|.
name|LdapName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|Rdn
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|x500
operator|.
name|X500Principal
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

begin_comment
comment|/**  * Default {@link javax.net.ssl.HostnameVerifier} implementation.  * Copied from httpclient.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DefaultHostnameVerifier
implements|implements
name|HostnameVerifier
block|{
enum|enum
name|TYPE
block|{
name|IPv4
block|,
name|IPv6
block|,
name|DNS
block|}
empty_stmt|;
specifier|static
specifier|final
name|int
name|DNS_NAME_TYPE
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|IP_ADDRESS_TYPE
init|=
literal|7
decl_stmt|;
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
name|DefaultHostnameVerifier
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
decl_stmt|;
specifier|public
name|DefaultHostnameVerifier
parameter_list|(
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|)
block|{
name|this
operator|.
name|publicSuffixMatcher
operator|=
name|publicSuffixMatcher
expr_stmt|;
block|}
specifier|public
name|DefaultHostnameVerifier
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|SSLSession
name|session
parameter_list|)
block|{
try|try
block|{
specifier|final
name|Certificate
index|[]
name|certs
init|=
name|session
operator|.
name|getPeerCertificates
argument_list|()
decl_stmt|;
specifier|final
name|X509Certificate
name|x509
init|=
operator|(
name|X509Certificate
operator|)
name|certs
index|[
literal|0
index|]
decl_stmt|;
name|verify
argument_list|(
name|host
argument_list|,
name|x509
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|SSLException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"HostnameVerifier, socket reset for TTL"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|verify
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|SSLException
block|{
name|TYPE
name|hostFormat
init|=
name|TYPE
operator|.
name|DNS
decl_stmt|;
if|if
condition|(
name|InetAddressUtils
operator|.
name|isIPv4Address
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|hostFormat
operator|=
name|TYPE
operator|.
name|IPv4
expr_stmt|;
block|}
else|else
block|{
name|String
name|s
init|=
name|host
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|s
operator|=
name|host
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|host
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|InetAddressUtils
operator|.
name|isIPv6Address
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|hostFormat
operator|=
name|TYPE
operator|.
name|IPv6
expr_stmt|;
block|}
block|}
specifier|final
name|int
name|subjectType
init|=
name|hostFormat
operator|==
name|TYPE
operator|.
name|IPv4
operator|||
name|hostFormat
operator|==
name|TYPE
operator|.
name|IPv6
condition|?
name|IP_ADDRESS_TYPE
else|:
name|DNS_NAME_TYPE
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|subjectAlts
init|=
name|extractSubjectAlts
argument_list|(
name|cert
argument_list|,
name|subjectType
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectAlts
operator|!=
literal|null
operator|&&
operator|!
name|subjectAlts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
switch|switch
condition|(
name|hostFormat
condition|)
block|{
case|case
name|IPv4
case|:
name|matchIPAddress
argument_list|(
name|host
argument_list|,
name|subjectAlts
argument_list|)
expr_stmt|;
break|break;
case|case
name|IPv6
case|:
name|matchIPv6Address
argument_list|(
name|host
argument_list|,
name|subjectAlts
argument_list|)
expr_stmt|;
break|break;
default|default:
name|matchDNSName
argument_list|(
name|host
argument_list|,
name|subjectAlts
argument_list|,
name|this
operator|.
name|publicSuffixMatcher
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// CN matching has been deprecated by rfc2818 and can be used
comment|// as fallback only when no subjectAlts are available
specifier|final
name|X500Principal
name|subjectPrincipal
init|=
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
decl_stmt|;
specifier|final
name|String
name|cn
init|=
name|extractCN
argument_list|(
name|subjectPrincipal
operator|.
name|getName
argument_list|(
name|X500Principal
operator|.
name|RFC2253
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"Certificate subject for<"
operator|+
name|host
operator|+
literal|"> doesn't contain "
operator|+
literal|"a common name and does not have alternative names"
argument_list|)
throw|;
block|}
name|matchCN
argument_list|(
name|host
argument_list|,
name|cn
argument_list|,
name|this
operator|.
name|publicSuffixMatcher
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|verify
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|certHostname
parameter_list|)
block|{
try|try
block|{
name|matchCN
argument_list|(
name|host
argument_list|,
name|certHostname
argument_list|,
name|this
operator|.
name|publicSuffixMatcher
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SSLException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|static
name|void
name|matchIPAddress
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subjectAlts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|subjectAlt
init|=
name|subjectAlts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|equals
argument_list|(
name|subjectAlt
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"Certificate for<"
operator|+
name|host
operator|+
literal|"> doesn't match any "
operator|+
literal|"of the subject alternative names: "
operator|+
name|subjectAlts
argument_list|)
throw|;
block|}
specifier|static
name|void
name|matchIPv6Address
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
specifier|final
name|String
name|normalisedHost
init|=
name|normaliseAddress
argument_list|(
name|host
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subjectAlts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|subjectAlt
init|=
name|subjectAlts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|String
name|normalizedSubjectAlt
init|=
name|normaliseAddress
argument_list|(
name|subjectAlt
argument_list|)
decl_stmt|;
if|if
condition|(
name|normalisedHost
operator|.
name|equals
argument_list|(
name|normalizedSubjectAlt
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"Certificate for<"
operator|+
name|host
operator|+
literal|"> doesn't match any "
operator|+
literal|"of the subject alternative names: "
operator|+
name|subjectAlts
argument_list|)
throw|;
block|}
specifier|static
name|void
name|matchDNSName
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|subjectAlts
parameter_list|,
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|)
throws|throws
name|SSLException
block|{
specifier|final
name|String
name|normalizedHost
init|=
name|host
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subjectAlts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|subjectAlt
init|=
name|subjectAlts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|String
name|normalizedSubjectAlt
init|=
name|subjectAlt
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchIdentityStrict
argument_list|(
name|normalizedHost
argument_list|,
name|normalizedSubjectAlt
argument_list|,
name|publicSuffixMatcher
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"Certificate for<"
operator|+
name|host
operator|+
literal|"> doesn't match any "
operator|+
literal|"of the subject alternative names: "
operator|+
name|subjectAlts
argument_list|)
throw|;
block|}
specifier|static
name|void
name|matchCN
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|cn
parameter_list|,
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|)
throws|throws
name|SSLException
block|{
if|if
condition|(
operator|!
name|matchIdentityStrict
argument_list|(
name|host
argument_list|,
name|cn
argument_list|,
name|publicSuffixMatcher
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"Certificate for<"
operator|+
name|host
operator|+
literal|"> doesn't match "
operator|+
literal|"common name of the certificate subject: "
operator|+
name|cn
argument_list|)
throw|;
block|}
block|}
specifier|static
name|boolean
name|matchDomainRoot
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|domainRoot
parameter_list|)
block|{
if|if
condition|(
name|domainRoot
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|host
operator|.
name|endsWith
argument_list|(
name|domainRoot
argument_list|)
operator|&&
operator|(
name|host
operator|.
name|length
argument_list|()
operator|==
name|domainRoot
operator|.
name|length
argument_list|()
operator|||
name|host
operator|.
name|charAt
argument_list|(
name|host
operator|.
name|length
argument_list|()
operator|-
name|domainRoot
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'.'
operator|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|matchIdentity
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|,
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|,
specifier|final
name|boolean
name|strict
parameter_list|)
block|{
if|if
condition|(
name|publicSuffixMatcher
operator|!=
literal|null
operator|&&
name|host
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
operator|&&
operator|!
name|matchDomainRoot
argument_list|(
name|host
argument_list|,
name|publicSuffixMatcher
operator|.
name|getDomainRoot
argument_list|(
name|identity
argument_list|,
name|DomainType
operator|.
name|ICANN
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// RFC 2818, 3.1. Server Identity
comment|// "...Names may contain the wildcard
comment|// character * which is considered to match any single domain name
comment|// component or component fragment..."
comment|// Based on this statement presuming only singular wildcard is legal
specifier|final
name|int
name|asteriskIdx
init|=
name|identity
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
decl_stmt|;
if|if
condition|(
name|asteriskIdx
operator|!=
operator|-
literal|1
condition|)
block|{
specifier|final
name|String
name|prefix
init|=
name|identity
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|asteriskIdx
argument_list|)
decl_stmt|;
specifier|final
name|String
name|suffix
init|=
name|identity
operator|.
name|substring
argument_list|(
name|asteriskIdx
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|host
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|suffix
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|host
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Additional sanity checks on content selected by wildcard can be done here
if|if
condition|(
name|strict
condition|)
block|{
specifier|final
name|String
name|remainder
init|=
name|host
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|,
name|host
operator|.
name|length
argument_list|()
operator|-
name|suffix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
name|host
operator|.
name|equalsIgnoreCase
argument_list|(
name|identity
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|matchIdentity
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|,
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|)
block|{
return|return
name|matchIdentity
argument_list|(
name|host
argument_list|,
name|identity
argument_list|,
name|publicSuffixMatcher
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|matchIdentity
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|)
block|{
return|return
name|matchIdentity
argument_list|(
name|host
argument_list|,
name|identity
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|matchIdentityStrict
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|,
specifier|final
name|PublicSuffixMatcher
name|publicSuffixMatcher
parameter_list|)
block|{
return|return
name|matchIdentity
argument_list|(
name|host
argument_list|,
name|identity
argument_list|,
name|publicSuffixMatcher
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|matchIdentityStrict
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|)
block|{
return|return
name|matchIdentity
argument_list|(
name|host
argument_list|,
name|identity
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|static
name|String
name|extractCN
parameter_list|(
specifier|final
name|String
name|subjectPrincipal
parameter_list|)
throws|throws
name|SSLException
block|{
if|if
condition|(
name|subjectPrincipal
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
specifier|final
name|LdapName
name|subjectDN
init|=
operator|new
name|LdapName
argument_list|(
name|subjectPrincipal
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Rdn
argument_list|>
name|rdns
init|=
name|subjectDN
operator|.
name|getRdns
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|rdns
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
specifier|final
name|Rdn
name|rds
init|=
name|rdns
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|Attributes
name|attributes
init|=
name|rds
operator|.
name|toAttributes
argument_list|()
decl_stmt|;
specifier|final
name|Attribute
name|cn
init|=
name|attributes
operator|.
name|get
argument_list|(
literal|"cn"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cn
operator|!=
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|Object
name|value
init|=
name|cn
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
decl||
name|NamingException
name|ignore
parameter_list|)
block|{
comment|//
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InvalidNameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SSLException
argument_list|(
name|subjectPrincipal
operator|+
literal|" is not a valid X500 distinguished name"
argument_list|)
throw|;
block|}
block|}
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|extractSubjectAlts
parameter_list|(
specifier|final
name|X509Certificate
name|cert
parameter_list|,
specifier|final
name|int
name|subjectType
parameter_list|)
block|{
name|Collection
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|c
init|=
literal|null
decl_stmt|;
try|try
block|{
name|c
operator|=
name|cert
operator|.
name|getSubjectAlternativeNames
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|CertificateParsingException
name|ignore
parameter_list|)
block|{
comment|//
block|}
name|List
argument_list|<
name|String
argument_list|>
name|subjectAltList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|aC
range|:
name|c
control|)
block|{
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|aC
decl_stmt|;
specifier|final
name|int
name|type
init|=
operator|(
operator|(
name|Integer
operator|)
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|subjectType
condition|)
block|{
specifier|final
name|String
name|s
init|=
operator|(
name|String
operator|)
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectAltList
operator|==
literal|null
condition|)
block|{
name|subjectAltList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|subjectAltList
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subjectAltList
return|;
block|}
comment|/*      * Normalize IPv6 or DNS name.      */
specifier|static
name|String
name|normaliseAddress
parameter_list|(
specifier|final
name|String
name|hostname
parameter_list|)
block|{
if|if
condition|(
name|hostname
operator|==
literal|null
condition|)
block|{
return|return
name|hostname
return|;
block|}
try|try
block|{
specifier|final
name|InetAddress
name|inetAddress
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|hostname
argument_list|)
decl_stmt|;
return|return
name|inetAddress
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|UnknownHostException
name|unexpected
parameter_list|)
block|{
comment|// Should not happen, because we check for IPv6 address above
return|return
name|hostname
return|;
block|}
block|}
block|}
end_class

end_unit

