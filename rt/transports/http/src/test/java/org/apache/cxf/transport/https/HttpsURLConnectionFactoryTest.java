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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|HttpsURLConnection
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
name|util
operator|.
name|ReflectionUtil
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
name|jsse
operator|.
name|SSLUtils
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
name|jsse
operator|.
name|TLSClientParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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

begin_class
specifier|public
class|class
name|HttpsURLConnectionFactoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|noExplicitKeystoreNoCertAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|clearDefaults
argument_list|()
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.net.ssl.keyStore"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.net.ssl.keyStorePassword"
argument_list|)
expr_stmt|;
name|HttpsURLConnectionFactory
name|factory
init|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|factory
operator|.
name|socketFactory
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsClientParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsClientParams
operator|.
name|setUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|HttpsURLConnection
name|conn
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpsURLConnection
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|decorateWithTLS
argument_list|(
name|tlsClientParams
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"should not fail with NullPointerException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|noExplicitKeystoreWithCertAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|clearDefaults
argument_list|()
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.net.ssl.keyStore"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"javax.net.ssl.keyStorePassword"
argument_list|)
expr_stmt|;
name|HttpsURLConnectionFactory
name|factory
init|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|factory
operator|.
name|socketFactory
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsClientParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsClientParams
operator|.
name|setUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tlsClientParams
operator|.
name|setCertAlias
argument_list|(
literal|"someAlias"
argument_list|)
expr_stmt|;
name|HttpsURLConnection
name|conn
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpsURLConnection
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|decorateWithTLS
argument_list|(
name|tlsClientParams
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"should not fail with NullPointerException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|defaultKeystoreNoCertAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|clearDefaults
argument_list|()
expr_stmt|;
name|String
name|keystorePath
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/defaultkeystore2"
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.net.ssl.keyStore"
argument_list|,
name|keystorePath
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.net.ssl.keyStorePassword"
argument_list|,
literal|"123456"
argument_list|)
expr_stmt|;
name|HttpsURLConnectionFactory
name|factory
init|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|factory
operator|.
name|socketFactory
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsClientParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsClientParams
operator|.
name|setUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|HttpsURLConnection
name|conn
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpsURLConnection
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|decorateWithTLS
argument_list|(
name|tlsClientParams
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"should not fail with NullPointerException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|defaultKeystoreWithCertAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|clearDefaults
argument_list|()
expr_stmt|;
name|String
name|keystorePath
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/defaultkeystore2"
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.net.ssl.keyStore"
argument_list|,
name|keystorePath
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.net.ssl.keyStorePassword"
argument_list|,
literal|"123456"
argument_list|)
expr_stmt|;
name|HttpsURLConnectionFactory
name|factory
init|=
operator|new
name|HttpsURLConnectionFactory
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|factory
operator|.
name|socketFactory
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsClientParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsClientParams
operator|.
name|setUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tlsClientParams
operator|.
name|setCertAlias
argument_list|(
literal|"someAlias"
argument_list|)
expr_stmt|;
name|HttpsURLConnection
name|conn
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpsURLConnection
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|decorateWithTLS
argument_list|(
name|tlsClientParams
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"should not fail with NullPointerException"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|clearDefaults
parameter_list|()
throws|throws
name|IllegalAccessException
block|{
name|Field
name|defaultManagers
init|=
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|SSLUtils
operator|.
name|class
argument_list|,
literal|"defaultManagers"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|defaultManagers
argument_list|)
expr_stmt|;
name|defaultManagers
operator|.
name|set
argument_list|(
name|SSLUtils
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

