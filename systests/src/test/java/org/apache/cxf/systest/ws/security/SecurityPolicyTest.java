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
name|ws
operator|.
name|security
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
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|callback
operator|.
name|Callback
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
name|callback
operator|.
name|CallbackHandler
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
name|callback
operator|.
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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
name|jaxws
operator|.
name|EndpointImpl
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
name|policytest
operator|.
name|doubleit
operator|.
name|DoubleItPortType
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
name|policytest
operator|.
name|doubleit
operator|.
name|DoubleItService
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
name|ws
operator|.
name|policy
operator|.
name|PolicyEngine
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSPasswordCallback
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

begin_class
specifier|public
class|class
name|SecurityPolicyTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_ADDRESS
init|=
literal|"http://localhost:9010/SecPolTest"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_HTTPS_ADDRESS
init|=
literal|"https://localhost:9009/SecPolTest"
decl_stmt|;
specifier|public
specifier|static
class|class
name|ServerPasswordCallback
implements|implements
name|CallbackHandler
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callbacks
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|pc
operator|.
name|getIdentifer
argument_list|()
operator|.
name|equals
argument_list|(
literal|"bob"
argument_list|)
condition|)
block|{
comment|// set the password on the callback. This will be compared to the
comment|// password which was sent from the client.
name|pc
operator|.
name|setPassword
argument_list|(
literal|"pwd"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|createStaticBus
argument_list|(
name|SecurityPolicyTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"https_config.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getStaticBus
argument_list|()
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
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
name|POLICY_HTTPS_ADDRESS
argument_list|,
operator|new
name|DoubleItImplHttps
argument_list|()
argument_list|)
decl_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
operator|new
name|ServerPasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|POLICY_ADDRESS
argument_list|,
operator|new
name|DoubleItImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|pt
decl_stmt|;
name|pt
operator|=
name|service
operator|.
name|getDoubleItPortHttps
argument_list|()
expr_stmt|;
try|try
block|{
name|pt
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|String
name|msg
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|msg
operator|.
name|contains
argument_list|(
literal|"UsernameToken: No user"
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|pt
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|pt
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|PASSWORD
argument_list|,
literal|"pwd"
argument_list|)
expr_stmt|;
name|pt
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|pt
operator|=
name|service
operator|.
name|getDoubleItPortHttp
argument_list|()
expr_stmt|;
name|pt
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"https policy should have triggered"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|String
name|msg
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|msg
operator|.
name|contains
argument_list|(
literal|"HttpsToken"
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/policytest/DoubleIt"
argument_list|,
name|portName
operator|=
literal|"DoubleItPortHttp"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.policytest.doubleit.DoubleItPortType"
argument_list|,
name|wsdlLocation
operator|=
literal|"classpath:/wsdl_systest/DoubleIt.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|DoubleItImpl
implements|implements
name|DoubleItPortType
block|{
comment|/** {@inheritDoc}*/
specifier|public
name|BigInteger
name|doubleIt
parameter_list|(
name|BigInteger
name|numberToDouble
parameter_list|)
block|{
return|return
name|numberToDouble
operator|.
name|multiply
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"2"
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/policytest/DoubleIt"
argument_list|,
name|portName
operator|=
literal|"DoubleItPortHttps"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.policytest.doubleit.DoubleItPortType"
argument_list|,
name|wsdlLocation
operator|=
literal|"classpath:/wsdl_systest/DoubleIt.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|DoubleItImplHttps
implements|implements
name|DoubleItPortType
block|{
comment|/** {@inheritDoc}*/
specifier|public
name|BigInteger
name|doubleIt
parameter_list|(
name|BigInteger
name|numberToDouble
parameter_list|)
block|{
return|return
name|numberToDouble
operator|.
name|multiply
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"2"
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

