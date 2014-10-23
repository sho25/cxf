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
name|transport
operator|.
name|https
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Arrays
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|NoSuchElementException
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
name|TreeSet
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
name|SSLPeerUnverifiedException
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
name|net
operator|.
name|ssl
operator|.
name|SSLSocket
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

begin_comment
comment|/**  * ************************************************************************  * Copied from the not-yet-commons-ssl project at http://juliusdavies.ca/commons-ssl/  * As the above project is accepted into Apache and its JARs become available in  * the Maven 2 repos, we will have to switch to using the JARs instead  * ************************************************************************  *<p/>  * Interface for checking if a hostname matches the names stored inside the  * server's X.509 certificate.  Correctly implements  * javax.net.ssl.HostnameVerifier, but that interface is not recommended.  * Instead we added several check() methods that take SSLSocket,  * or X509Certificate, or ultimately (they all end up calling this one),  * String.  (It's easier to supply JUnit with Strings instead of mock  * SSLSession objects!)  *</p><p>Our check() methods throw exceptions if the name is  * invalid, whereas javax.net.ssl.HostnameVerifier just returns true/false.  *<p/>  * We provide the HostnameVerifier.DEFAULT, HostnameVerifier.STRICT, and  * HostnameVerifier.ALLOW_ALL implementations.  We also provide the more  * specialized HostnameVerifier.DEFAULT_AND_LOCALHOST, as well as  * HostnameVerifier.STRICT_IE6.  But feel free to define your own  * implementations!  *<p/>  * Inspired by Sebastian Hauer's original StrictSSLProtocolSocketFactory in the  * HttpClient "contrib" repository.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|CertificateHostnameVerifier
extends|extends
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
block|{
comment|/**      * The DEFAULT HostnameVerifier works the same way as Curl and Firefox.      *<p/>      * The hostname must match either the first CN, or any of the subject-alts.      * A wildcard can occur in the CN, and in any of the subject-alts.      *<p/>      * The only difference between DEFAULT and STRICT is that a wildcard (such      * as "*.foo.com") with DEFAULT matches all subdomains, including      * "a.b.foo.com".      */
name|CertificateHostnameVerifier
name|DEFAULT
init|=
operator|new
name|AbstractVerifier
argument_list|()
block|{
specifier|public
specifier|final
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|hosts
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
name|check
argument_list|(
name|hosts
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DEFAULT"
return|;
block|}
block|}
decl_stmt|;
comment|/**      * The DEFAULT_AND_LOCALHOST HostnameVerifier works like the DEFAULT      * one with one additional relaxation:  a host of "localhost",      * "localhost.localdomain", "127.0.0.1", "::1" will always pass, no matter      * what is in the server's certificate.      */
name|CertificateHostnameVerifier
name|DEFAULT_AND_LOCALHOST
init|=
operator|new
name|AbstractVerifier
argument_list|()
block|{
specifier|public
specifier|final
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|hosts
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
if|if
condition|(
name|isLocalhost
argument_list|(
name|hosts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return;
block|}
name|check
argument_list|(
name|hosts
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DEFAULT_AND_LOCALHOST"
return|;
block|}
block|}
decl_stmt|;
comment|/**      * The STRICT HostnameVerifier works the same way as java.net.URL in Sun      * Java 1.4, Sun Java 5, Sun Java 6.  It's also pretty close to IE6.      * This implementation appears to be compliant with RFC 2818 for dealing      * with wildcards.      *<p/>      * The hostname must match either the first CN, or any of the subject-alts.      * A wildcard can occur in the CN, and in any of the subject-alts.  The      * one divergence from IE6 is how we only check the first CN.  IE6 allows      * a match against any of the CNs present.  We decided to follow in      * Sun Java 1.4's footsteps and only check the first CN.      *<p/>      * A wildcard such as "*.foo.com" matches only subdomains in the same      * level, for example "a.foo.com".  It does not match deeper subdomains      * such as "a.b.foo.com".      */
name|CertificateHostnameVerifier
name|STRICT
init|=
operator|new
name|AbstractVerifier
argument_list|()
block|{
specifier|public
specifier|final
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|host
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
name|check
argument_list|(
name|host
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"STRICT"
return|;
block|}
block|}
decl_stmt|;
comment|/**      * The STRICT_IE6 HostnameVerifier works just like the STRICT one with one      * minor variation:  the hostname can match against any of the CN's in the      * server's certificate, not just the first one.  This behaviour is      * identical to IE6's behaviour.      */
name|CertificateHostnameVerifier
name|STRICT_IE6
init|=
operator|new
name|AbstractVerifier
argument_list|()
block|{
specifier|public
specifier|final
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|host
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
name|check
argument_list|(
name|host
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"STRICT_IE6"
return|;
block|}
block|}
decl_stmt|;
comment|/**      * The ALLOW_ALL HostnameVerifier essentially turns hostname verification      * off.  This implementation is a no-op, and never throws the SSLException.      */
name|CertificateHostnameVerifier
name|ALLOW_ALL
init|=
operator|new
name|AbstractVerifier
argument_list|()
block|{
specifier|public
specifier|final
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|host
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|)
block|{
comment|// Allow everything - so never blowup.
block|}
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ALLOW_ALL"
return|;
block|}
block|}
decl_stmt|;
name|boolean
name|verify
parameter_list|(
name|String
name|host
parameter_list|,
name|SSLSession
name|session
parameter_list|)
function_decl|;
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|SSLSocket
name|ssl
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|SSLException
function_decl|;
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|String
index|[]
name|cns
parameter_list|,
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
function_decl|;
name|void
name|check
parameter_list|(
name|String
index|[]
name|hosts
parameter_list|,
name|SSLSocket
name|ssl
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|void
name|check
parameter_list|(
name|String
index|[]
name|hosts
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|SSLException
function_decl|;
comment|/**      * Checks to see if the supplied hostname matches any of the supplied CNs      * or "DNS" Subject-Alts.  Most implementations only look at the first CN,      * and ignore any additional CNs.  Most implementations do look at all of      * the "DNS" Subject-Alts. The CNs or Subject-Alts may contain wildcards      * according to RFC 2818.      *      * @param cns         CN fields, in order, as extracted from the X.509      *                    certificate.      * @param subjectAlts Subject-Alt fields of type 2 ("DNS"), as extracted      *                    from the X.509 certificate.      * @param hosts       The array of hostnames to verify.      * @throws SSLException If verification failed.      */
name|void
name|check
parameter_list|(
name|String
index|[]
name|hosts
parameter_list|,
name|String
index|[]
name|cns
parameter_list|,
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
function_decl|;
specifier|abstract
class|class
name|AbstractVerifier
implements|implements
name|CertificateHostnameVerifier
block|{
comment|/**          * This contains a list of 2nd-level domains that aren't allowed to          * have wildcards when combined with country-codes.          * For example: [*.co.uk].          *<p/>          * The [*.co.uk] problem is an interesting one.  Should we just hope          * that CA's would never foolishly allow such a certificate to happen?          * Looks like we're the only implementation guarding against this.          * Firefox, Curl, Sun Java 1.4, 5, 6 don't bother with this check.          */
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|BAD_COUNTRY_2LDS
init|=
block|{
literal|"ac"
block|,
literal|"co"
block|,
literal|"com"
block|,
literal|"ed"
block|,
literal|"edu"
block|,
literal|"go"
block|,
literal|"gouv"
block|,
literal|"gov"
block|,
literal|"info"
block|,
literal|"lg"
block|,
literal|"ne"
block|,
literal|"net"
block|,
literal|"or"
block|,
literal|"org"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|LOCALHOSTS
init|=
block|{
literal|"::1"
block|,
literal|"127.0.0.1"
block|,
literal|"localhost"
block|,
literal|"localhost.localdomain"
block|}
decl_stmt|;
static|static
block|{
comment|// Just in case developer forgot to manually sort the array.  :-)
name|Arrays
operator|.
name|sort
argument_list|(
name|BAD_COUNTRY_2LDS
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|LOCALHOSTS
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractVerifier
parameter_list|()
block|{         }
comment|/**          * The javax.net.ssl.HostnameVerifier contract.          *          * @param host    'hostname' we used to create our socket          * @param session SSLSession with the remote server          * @return true if the host matched the one in the certificate.          */
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|host
parameter_list|,
name|SSLSession
name|session
parameter_list|)
block|{
try|try
block|{
name|Certificate
index|[]
name|certs
init|=
name|session
operator|.
name|getPeerCertificates
argument_list|()
decl_stmt|;
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
name|check
argument_list|(
operator|new
name|String
index|[]
block|{
name|host
block|}
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
name|SSLException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|SSLSocket
name|ssl
parameter_list|)
throws|throws
name|IOException
block|{
name|check
argument_list|(
operator|new
name|String
index|[]
block|{
name|host
block|}
argument_list|,
name|ssl
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|SSLException
block|{
name|check
argument_list|(
operator|new
name|String
index|[]
block|{
name|host
block|}
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|check
parameter_list|(
name|String
name|host
parameter_list|,
name|String
index|[]
name|cns
parameter_list|,
name|String
index|[]
name|subjectAlts
parameter_list|)
throws|throws
name|SSLException
block|{
name|check
argument_list|(
operator|new
name|String
index|[]
block|{
name|host
block|}
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|check
parameter_list|(
name|String
name|host
index|[]
parameter_list|,
name|SSLSocket
name|ssl
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"host to verify is null"
argument_list|)
throw|;
block|}
name|SSLSession
name|session
init|=
name|ssl
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
comment|// In our experience this only happens under IBM 1.4.x when
comment|// spurious (unrelated) certificates show up in the server'
comment|// chain.  Hopefully this will unearth the real problem:
name|InputStream
name|in
init|=
name|ssl
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|in
operator|.
name|available
argument_list|()
expr_stmt|;
comment|/*                   If you're looking at the 2 lines of code above because                   you're running into a problem, you probably have two                   options:                      #1.  Clean up the certificate chain that your server                          is presenting (e.g. edit "/etc/apache2/server.crt"                          or wherever it is your server's certificate chain                          is defined).                                                 OR                      #2.   Upgrade to an IBM 1.5.x or greater JVM, or switch                           to a non-IBM JVM.                 */
comment|// If ssl.getInputStream().available() didn't cause an
comment|// exception, maybe at least now the session is available?
name|session
operator|=
name|ssl
operator|.
name|getSession
argument_list|()
expr_stmt|;
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
comment|// If it's still null, probably a startHandshake() will
comment|// unearth the real problem.
name|ssl
operator|.
name|startHandshake
argument_list|()
expr_stmt|;
comment|// Okay, if we still haven't managed to cause an exception,
comment|// might as well go for the NPE.  Or maybe we're okay now?
name|session
operator|=
name|ssl
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
block|}
name|Certificate
index|[]
name|certs
decl_stmt|;
try|try
block|{
name|certs
operator|=
name|session
operator|.
name|getPeerCertificates
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SSLPeerUnverifiedException
name|spue
parameter_list|)
block|{
name|InputStream
name|in
init|=
name|ssl
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|in
operator|.
name|available
argument_list|()
expr_stmt|;
comment|// Didn't trigger anything interesting?  Okay, just throw
comment|// original.
throw|throw
name|spue
throw|;
block|}
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
name|check
argument_list|(
name|host
argument_list|,
name|x509
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|check
parameter_list|(
name|String
index|[]
name|host
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|SSLException
block|{
name|String
index|[]
name|cns
init|=
name|Certificates
operator|.
name|getCNs
argument_list|(
name|cert
argument_list|)
decl_stmt|;
name|String
index|[]
name|subjectAlts
init|=
name|Certificates
operator|.
name|getDNSSubjectAlts
argument_list|(
name|cert
argument_list|)
decl_stmt|;
name|check
argument_list|(
name|host
argument_list|,
name|cns
argument_list|,
name|subjectAlts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|check
parameter_list|(
specifier|final
name|String
index|[]
name|hosts
parameter_list|,
specifier|final
name|String
index|[]
name|cns
parameter_list|,
specifier|final
name|String
index|[]
name|subjectAlts
parameter_list|,
specifier|final
name|boolean
name|ie6
parameter_list|,
specifier|final
name|boolean
name|strictWithSubDomains
parameter_list|)
throws|throws
name|SSLException
block|{
comment|// Build up lists of allowed hosts For logging/debugging purposes.
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|32
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hosts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|h
init|=
name|hosts
index|[
name|i
index|]
decl_stmt|;
name|h
operator|=
name|h
operator|!=
literal|null
condition|?
name|h
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|""
expr_stmt|;
name|hosts
index|[
name|i
index|]
operator|=
name|h
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|String
name|hostnames
init|=
name|buf
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// Build the list of names we're going to check.  Our DEFAULT and
comment|// STRICT implementations of the HostnameVerifier only use the
comment|// first CN provided.  All other CNs are ignored.
comment|// (Firefox, wget, curl, Sun Java 1.4, 5, 6 all work this way).
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|cns
operator|!=
literal|null
operator|&&
name|cns
operator|.
name|length
operator|>
literal|0
operator|&&
name|cns
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|cns
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|ie6
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|cns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|cns
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|subjectAlts
operator|!=
literal|null
condition|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|subjectAlts
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|subjectAlts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|names
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"Certificate for "
operator|+
name|hosts
index|[
literal|0
index|]
operator|+
literal|" doesn't contain CN or DNS subjectAlt"
decl_stmt|;
throw|throw
operator|new
name|SSLException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
comment|// StringBuilder for building the error message.
name|buf
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|boolean
name|match
init|=
literal|false
decl_stmt|;
name|out
label|:
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|names
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
comment|// Don't trim the CN, though!
name|String
name|cn
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|cn
operator|=
name|cn
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
comment|// Store CN in StringBuilder in case we need to report an error.
name|buf
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|cn
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" OR"
argument_list|)
expr_stmt|;
block|}
comment|// The CN better have at least two dots if it wants wildcard
comment|// action.  It also can't be [*.co.uk] or [*.co.jp] or
comment|// [*.org.uk], etc...
name|boolean
name|doWildcard
init|=
name|cn
operator|.
name|startsWith
argument_list|(
literal|"*."
argument_list|)
operator|&&
name|cn
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
operator|&&
operator|!
name|isIP4Address
argument_list|(
name|cn
argument_list|)
operator|&&
name|acceptableCountryWildcard
argument_list|(
name|cn
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
name|hosts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|hostName
init|=
name|hosts
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|doWildcard
condition|)
block|{
name|match
operator|=
name|hostName
operator|.
name|endsWith
argument_list|(
name|cn
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|match
operator|&&
name|strictWithSubDomains
condition|)
block|{
comment|// If we're in strict mode, then [*.foo.com] is not
comment|// allowed to match [a.b.foo.com]
name|match
operator|=
name|countDots
argument_list|(
name|hostName
argument_list|)
operator|==
name|countDots
argument_list|(
name|cn
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|match
operator|=
name|hostName
operator|.
name|equals
argument_list|(
name|cn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|match
condition|)
block|{
break|break
name|out
break|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
throw|throw
operator|new
name|SSLException
argument_list|(
literal|"hostname in certificate didn't match: "
operator|+
name|hostnames
operator|+
literal|" !="
operator|+
name|buf
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isIP4Address
parameter_list|(
specifier|final
name|String
name|cn
parameter_list|)
block|{
name|boolean
name|isIP4
init|=
literal|true
decl_stmt|;
name|String
name|tld
init|=
name|cn
decl_stmt|;
name|int
name|x
init|=
name|cn
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
comment|// We only bother analyzing the characters after the final dot
comment|// in the name.
if|if
condition|(
name|x
operator|>=
literal|0
operator|&&
name|x
operator|+
literal|1
operator|<
name|cn
operator|.
name|length
argument_list|()
condition|)
block|{
name|tld
operator|=
name|cn
operator|.
name|substring
argument_list|(
name|x
operator|+
literal|1
argument_list|)
expr_stmt|;
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
name|tld
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|tld
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|isIP4
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
return|return
name|isIP4
return|;
block|}
specifier|public
specifier|static
name|boolean
name|acceptableCountryWildcard
parameter_list|(
specifier|final
name|String
name|cn
parameter_list|)
block|{
name|int
name|cnLen
init|=
name|cn
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|cnLen
operator|>=
literal|7
operator|&&
name|cnLen
operator|<=
literal|9
comment|// Look for the '.' in the 3rd-last position:
operator|&&
name|cn
operator|.
name|charAt
argument_list|(
name|cnLen
operator|-
literal|3
argument_list|)
operator|==
literal|'.'
condition|)
block|{
comment|// Trim off the [*.] and the [.XX].
name|String
name|s
init|=
name|cn
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
name|cnLen
operator|-
literal|3
argument_list|)
decl_stmt|;
comment|// And test against the sorted array of bad 2lds:
name|int
name|x
init|=
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|BAD_COUNTRY_2LDS
argument_list|,
name|s
argument_list|)
decl_stmt|;
return|return
name|x
operator|<
literal|0
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isLocalhost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|host
operator|=
name|host
operator|!=
literal|null
condition|?
name|host
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|""
expr_stmt|;
if|if
condition|(
name|host
operator|.
name|startsWith
argument_list|(
literal|"::1"
argument_list|)
condition|)
block|{
name|int
name|x
init|=
name|host
operator|.
name|lastIndexOf
argument_list|(
literal|'%'
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|>=
literal|0
condition|)
block|{
name|host
operator|=
name|host
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|x
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|x
init|=
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|LOCALHOSTS
argument_list|,
name|host
argument_list|)
decl_stmt|;
return|return
name|x
operator|>=
literal|0
return|;
block|}
comment|/**          * Counts the number of dots "." in a string.          *          * @param s string to count dots from          * @return number of dots          */
specifier|public
specifier|static
name|int
name|countDots
parameter_list|(
specifier|final
name|String
name|s
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
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
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'.'
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
block|}
specifier|final
class|class
name|Certificates
block|{
specifier|private
name|Certificates
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|String
index|[]
name|getCNs
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
block|{
try|try
block|{
specifier|final
name|String
name|subjectPrincipal
init|=
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|(
name|X500Principal
operator|.
name|RFC2253
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|cnList
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
for|for
control|(
specifier|final
name|Rdn
name|rds
range|:
name|subjectDN
operator|.
name|getRdns
argument_list|()
control|)
block|{
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
name|cnList
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|ignore
parameter_list|)
block|{
comment|//ignore
block|}
catch|catch
parameter_list|(
name|NamingException
name|ignore
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
if|if
condition|(
operator|!
name|cnList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|cnList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|cnList
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidNameException
name|ignore
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
comment|/**          * Extracts the array of SubjectAlt DNS names from an X509Certificate.          * Returns null if there aren't any.          *<p/>          * Note:  Java doesn't appear able to extract international characters          * from the SubjectAlts.  It can only extract international characters          * from the CN field.          *<p/>          * (Or maybe the version of OpenSSL I'm using to test isn't storing the          * international characters correctly in the SubjectAlts?).          *          * @param cert X509Certificate          * @return Array of SubjectALT DNS names stored in the certificate.          */
specifier|public
specifier|static
name|String
index|[]
name|getDNSSubjectAlts
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|subjectAltList
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|CertificateParsingException
name|cpe
parameter_list|)
block|{
comment|// Should probably log.debug() this?
name|cpe
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
comment|// If type is 2, then we've got a dNSName
if|if
condition|(
name|type
operator|==
literal|2
condition|)
block|{
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
if|if
condition|(
operator|!
name|subjectAltList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|subjectAlts
init|=
operator|new
name|String
index|[
name|subjectAltList
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|subjectAltList
operator|.
name|toArray
argument_list|(
name|subjectAlts
argument_list|)
expr_stmt|;
return|return
name|subjectAlts
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_interface

end_unit

