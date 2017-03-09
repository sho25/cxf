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
name|interceptor
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
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
name|login
operator|.
name|AppConfigurationEntry
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
name|login
operator|.
name|AppConfigurationEntry
operator|.
name|LoginModuleControlFlag
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
name|login
operator|.
name|Configuration
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
name|security
operator|.
name|SecurityToken
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
name|security
operator|.
name|UsernameToken
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
name|security
operator|.
name|callback
operator|.
name|CallbackHandlerProvider
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
name|security
operator|.
name|callback
operator|.
name|CallbackHandlerTlsCert
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
name|security
operator|.
name|callback
operator|.
name|CertKeyToUserNameMapper
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|easymock
operator|.
name|IMocksControl
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
name|JAASLoginInterceptorTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SUBJECT_DN
init|=
literal|"CN="
operator|+
name|TestUserPasswordLoginModule
operator|.
name|TESTUSER
operator|+
literal|", o=Test Org"
decl_stmt|;
comment|/**      * Using default CallbackhandlerProviders and no authentication information      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AuthenticationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testLoginWithDefaultHandler
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Using default CallbackhandlerProviders and AuthPolicy      */
annotation|@
name|Test
specifier|public
name|void
name|testLoginWithDefaultHandlerAndAuthPol
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|addAuthPolicy
argument_list|(
name|message
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTUSER
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTPASS
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AuthenticationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testLoginWithDefaultHandlerAndAuthPolWrongPass
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|addAuthPolicy
argument_list|(
name|message
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTUSER
argument_list|,
literal|"wrong"
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Using default CallbackhandlerProviders and UserNameToken      */
annotation|@
name|Test
specifier|public
name|void
name|testLoginWithDefaultHandlerAndUsernameToken
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|addUsernameToken
argument_list|(
name|message
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTUSER
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTPASS
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AuthenticationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testLoginWithDefaultHandlerAndUsernameTokenWrongPass
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|addUsernameToken
argument_list|(
name|message
argument_list|,
name|TestUserPasswordLoginModule
operator|.
name|TESTUSER
argument_list|,
literal|"wrong"
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoginWithTlsHandler
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
name|createTestJaasLoginInterceptor
argument_list|()
decl_stmt|;
name|CallbackHandlerTlsCert
name|tlsHandler
init|=
operator|new
name|CallbackHandlerTlsCert
argument_list|()
decl_stmt|;
name|tlsHandler
operator|.
name|setFixedPassword
argument_list|(
name|TestUserPasswordLoginModule
operator|.
name|TESTPASS
argument_list|)
expr_stmt|;
name|CertKeyToUserNameMapper
name|certMapper
init|=
operator|new
name|CertKeyToUserNameMapper
argument_list|()
decl_stmt|;
name|certMapper
operator|.
name|setKey
argument_list|(
literal|"CN"
argument_list|)
expr_stmt|;
name|tlsHandler
operator|.
name|setCertMapper
argument_list|(
name|certMapper
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|setCallbackHandlerProviders
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|CallbackHandlerProvider
operator|)
name|tlsHandler
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|TLSSessionInfo
name|sessionInfo
init|=
operator|new
name|TLSSessionInfo
argument_list|(
literal|""
argument_list|,
literal|null
argument_list|,
operator|new
name|Certificate
index|[]
block|{
name|createTestCert
argument_list|(
name|TEST_SUBJECT_DN
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|,
name|sessionInfo
argument_list|)
expr_stmt|;
name|jaasInt
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|X509Certificate
name|createTestCert
parameter_list|(
name|String
name|subjectDn
parameter_list|)
block|{
name|IMocksControl
name|c
init|=
name|EasyMock
operator|.
name|createControl
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|c
operator|.
name|createMock
argument_list|(
name|X509Certificate
operator|.
name|class
argument_list|)
decl_stmt|;
name|Principal
name|principal
init|=
name|c
operator|.
name|createMock
argument_list|(
name|Principal
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|subjectDn
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|cert
operator|.
name|getSubjectDN
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|cert
return|;
block|}
specifier|private
name|void
name|addAuthPolicy
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|AuthorizationPolicy
name|authPol
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPol
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|authPol
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|,
name|authPol
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addUsernameToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|UsernameToken
name|token
init|=
operator|new
name|UsernameToken
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityToken
operator|.
name|class
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JAASLoginInterceptor
name|createTestJaasLoginInterceptor
parameter_list|()
block|{
name|JAASLoginInterceptor
name|jaasInt
init|=
operator|new
name|JAASLoginInterceptor
argument_list|()
decl_stmt|;
name|jaasInt
operator|.
name|setReportFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
operator|new
name|Configuration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AppConfigurationEntry
index|[]
name|getAppConfigurationEntry
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|AppConfigurationEntry
name|configEntry
init|=
operator|new
name|AppConfigurationEntry
argument_list|(
name|TestUserPasswordLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|LoginModuleControlFlag
operator|.
name|REQUIRED
argument_list|,
name|options
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|configEntry
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|AppConfigurationEntry
index|[]
block|{}
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|jaasInt
operator|.
name|setLoginConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|jaasInt
return|;
block|}
block|}
end_class

end_unit

