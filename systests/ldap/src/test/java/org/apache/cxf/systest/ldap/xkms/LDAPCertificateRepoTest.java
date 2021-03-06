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
name|systest
operator|.
name|ldap
operator|.
name|xkms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|testutil
operator|.
name|common
operator|.
name|AbstractClientServerTestBase
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
name|ldap
operator|.
name|LdapCertificateRepo
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
name|ldap
operator|.
name|LdapSchemaConfig
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
name|ldap
operator|.
name|LdapSearch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateLdapServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|ApplyLdifFiles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateDS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateIndex
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreatePartition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|AbstractLdapTestUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|FrameworkRunner
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|FrameworkRunner
operator|.
name|class
argument_list|)
comment|//Define the DirectoryService
annotation|@
name|CreateDS
argument_list|(
name|name
operator|=
literal|"LDAPCertificateRepoTest-class"
argument_list|,
name|enableAccessControl
operator|=
literal|false
argument_list|,
name|allowAnonAccess
operator|=
literal|false
argument_list|,
name|enableChangeLog
operator|=
literal|true
argument_list|,
name|partitions
operator|=
block|{
annotation|@
name|CreatePartition
argument_list|(
name|name
operator|=
literal|"example"
argument_list|,
name|suffix
operator|=
literal|"dc=example,dc=com"
argument_list|,
name|indexes
operator|=
block|{
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"objectClass"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"dc"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"ou"
argument_list|)
block|}
argument_list|)
block|}
argument_list|)
annotation|@
name|CreateLdapServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"LDAP"
argument_list|,
name|address
operator|=
literal|"localhost"
argument_list|)
block|}
argument_list|)
comment|//Inject an file containing entries
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"ldap.ldif"
argument_list|)
comment|/**  * Add a test for the XKMS LDAP CertificateRepo  */
specifier|public
class|class
name|LDAPCertificateRepoTest
extends|extends
name|AbstractLdapTestUnit
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_SUBJECT_DN
init|=
literal|"cn=dave,ou=users"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROOT_DN
init|=
literal|"dc=example,dc=com"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_SUBJECT_DN2
init|=
literal|"cn=newuser,ou=users"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_SERVICE_URI
init|=
literal|"http://myservice.apache.org/MyServiceName"
decl_stmt|;
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractClientServerTestBase
operator|.
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindUserCert
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|NamingException
throws|,
name|CertificateException
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
name|EXPECTED_SUBJECT_DN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindUserCertForNonExistentDn
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|NamingException
throws|,
name|CertificateException
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
literal|"CN=wrong"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Certificate should be null"
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindUserCertViaUID
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|NamingException
throws|,
name|CertificateException
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
literal|"dave"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindUserCertViaWrongUID
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|NamingException
throws|,
name|CertificateException
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
literal|"wrong"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Certificate should be null"
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSave
parameter_list|()
throws|throws
name|Exception
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cert1.cer"
argument_list|)
decl_stmt|;
name|CertificateFactory
name|factory
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
name|factory
operator|.
name|generateCertificate
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|UseKeyWithType
name|key
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|key
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
name|key
operator|.
name|setIdentifier
argument_list|(
name|EXPECTED_SUBJECT_DN2
argument_list|)
expr_stmt|;
name|persistenceManager
operator|.
name|saveCertificate
argument_list|(
name|cert
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|X509Certificate
name|foundCert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
name|EXPECTED_SUBJECT_DN2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|foundCert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveServiceCert
parameter_list|()
throws|throws
name|Exception
block|{
name|CertificateRepo
name|persistenceManager
init|=
name|createLdapCertificateRepo
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cert1.cer"
argument_list|)
decl_stmt|;
name|CertificateFactory
name|factory
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
name|factory
operator|.
name|generateCertificate
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|UseKeyWithType
name|key
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|key
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|SERVICE_NAME
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setIdentifier
argument_list|(
name|EXPECTED_SERVICE_URI
argument_list|)
expr_stmt|;
name|persistenceManager
operator|.
name|saveCertificate
argument_list|(
name|cert
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|// Search by DN
name|X509Certificate
name|foundCert
init|=
name|persistenceManager
operator|.
name|findByServiceName
argument_list|(
name|EXPECTED_SERVICE_URI
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|foundCert
argument_list|)
expr_stmt|;
comment|// Search by UID
name|foundCert
operator|=
name|persistenceManager
operator|.
name|findByServiceName
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|foundCert
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CertificateRepo
name|createLdapCertificateRepo
parameter_list|()
throws|throws
name|CertificateException
block|{
name|LdapSearch
name|ldapSearch
init|=
operator|new
name|LdapSearch
argument_list|(
literal|"ldap://localhost:"
operator|+
name|super
operator|.
name|getLdapServer
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|,
literal|"UID=admin,DC=example,DC=com"
argument_list|,
literal|"ldap_su"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|LdapSchemaConfig
name|ldapSchemaConfig
init|=
operator|new
name|LdapSchemaConfig
argument_list|()
decl_stmt|;
name|ldapSchemaConfig
operator|.
name|setAttrCrtBinary
argument_list|(
literal|"userCertificate"
argument_list|)
expr_stmt|;
return|return
operator|new
name|LdapCertificateRepo
argument_list|(
name|ldapSearch
argument_list|,
name|ldapSchemaConfig
argument_list|,
name|ROOT_DN
argument_list|)
return|;
block|}
block|}
end_class

end_unit

