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
name|kerberos
operator|.
name|jaxrs
operator|.
name|kerberos
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|jaxrs
operator|.
name|security
operator|.
name|KerberosAuthOutInterceptor
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
name|systest
operator|.
name|kerberos
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|AbstractBusClientServerTestBase
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
name|transport
operator|.
name|http
operator|.
name|auth
operator|.
name|HttpAuthHeader
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
name|transport
operator|.
name|http
operator|.
name|auth
operator|.
name|SpnegoAuthSupplier
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
name|CreateKdcServer
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
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|kerberos
operator|.
name|KeyDerivationInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|GSSName
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_comment
comment|/**  * A set of tests for Kerberos Tokens that use an Apache DS instance as the KDC.  */
end_comment

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
literal|"AbstractKerberosTest-class"
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
argument_list|,
name|additionalInterceptors
operator|=
block|{
name|KeyDerivationInterceptor
operator|.
name|class
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
argument_list|)
block|}
argument_list|)
annotation|@
name|CreateKdcServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"KRB"
argument_list|,
name|address
operator|=
literal|"127.0.0.1"
argument_list|)
block|}
argument_list|,
name|primaryRealm
operator|=
literal|"service.ws.apache.org"
argument_list|,
name|kdcPrincipal
operator|=
literal|"krbtgt/service.ws.apache.org@service.ws.apache.org"
argument_list|)
comment|//Inject an file containing entries
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"kerberos.ldif"
argument_list|)
specifier|public
class|class
name|JAXRSKerberosBookTest
extends|extends
name|AbstractLdapTestUnit
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookKerberosServer
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KERBEROS_CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/kerberos/jaxrs/kerberos/kerberosClient.xml"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|runTests
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|portUpdated
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|updatePort
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|portUpdated
condition|)
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
comment|// Read in krb5.conf and substitute in the correct port
name|Path
name|path
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/src/test/resources/krb5.conf"
argument_list|)
decl_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|path
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"port"
argument_list|,
literal|""
operator|+
name|super
operator|.
name|getKdcServer
argument_list|()
operator|.
name|getTransports
argument_list|()
index|[
literal|0
index|]
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|path2
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/target/test-classes/jaxrs.krb5.conf"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|path2
argument_list|,
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.krb5.conf"
argument_list|,
name|path2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|portUpdated
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
comment|//
comment|// This test fails with the IBM JDK
comment|//
if|if
condition|(
operator|!
literal|"IBM Corporation"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
argument_list|)
condition|)
block|{
name|runTests
operator|=
literal|true
expr_stmt|;
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
comment|// System.setProperty("sun.security.krb5.debug", "true");
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.auth.login.config"
argument_list|,
name|basedir
operator|+
literal|"/src/test/resources/kerberos.jaas"
argument_list|)
expr_stmt|;
block|}
comment|// Launch servers
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|AbstractBusClientServerTestBase
operator|.
name|launchServer
argument_list|(
name|BookKerberosServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|AbstractBusClientServerTestBase
operator|.
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWithConfigInHttpConduit
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
condition|)
block|{
return|return;
block|}
name|doTestGetBook123Proxy
argument_list|(
name|KERBEROS_CONFIG_FILE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGetBook123Proxy
parameter_list|(
name|String
name|configFile
parameter_list|)
throws|throws
name|Exception
block|{
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|configFile
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|bs
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SpnegoAuthSupplier
name|authSupplier
init|=
operator|new
name|SpnegoAuthSupplier
argument_list|()
decl_stmt|;
name|authSupplier
operator|.
name|setServicePrincipalName
argument_list|(
literal|"bob@service.ws.apache.org"
argument_list|)
expr_stmt|;
name|authSupplier
operator|.
name|setServiceNameType
argument_list|(
name|GSSName
operator|.
name|NT_HOSTBASED_SERVICE
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|bs
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|setAuthSupplier
argument_list|(
name|authSupplier
argument_list|)
expr_stmt|;
comment|// just to verify the interface call goes through CGLIB proxy too
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|bs
operator|.
name|getBook
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWithInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
condition|)
block|{
return|return;
block|}
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/books/123"
argument_list|)
decl_stmt|;
name|KerberosAuthOutInterceptor
name|kbInterceptor
init|=
operator|new
name|KerberosAuthOutInterceptor
argument_list|()
decl_stmt|;
name|AuthorizationPolicy
name|policy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setAuthorizationType
argument_list|(
name|HttpAuthHeader
operator|.
name|AUTH_TYPE_NEGOTIATE
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setAuthorization
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setUserName
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setPassword
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|kbInterceptor
operator|.
name|setPolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|kbInterceptor
operator|.
name|setCredDelegation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|kbInterceptor
argument_list|)
expr_stmt|;
comment|// Required so as to get it working with our KDC
name|kbInterceptor
operator|.
name|setServicePrincipalName
argument_list|(
literal|"bob@service.ws.apache.org"
argument_list|)
expr_stmt|;
name|kbInterceptor
operator|.
name|setServiceNameType
argument_list|(
name|GSSName
operator|.
name|NT_HOSTBASED_SERVICE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

