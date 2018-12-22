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
name|http_undertow
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
name|UndeclaredThrowableException
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
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
name|greeter_control
operator|.
name|Greeter
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
name|greeter_control
operator|.
name|GreeterService
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
name|helpers
operator|.
name|CastUtils
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
name|Ignore
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertTrue
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|ClientServerSessionTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|SessionServer
operator|.
name|PORT
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|SessionServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithSession
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bp
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.http.request.headers"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"javax.xml.ws.http.request.headers"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|cookies
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a=a"
block|,
literal|"b=b"
block|}
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Cookie"
argument_list|,
name|cookies
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|String
name|cookie
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|cookie
operator|=
name|greeting
operator|.
name|substring
argument_list|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeting
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bonjour"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"a=a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"b=b"
argument_list|)
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|cookie
operator|=
literal|""
expr_stmt|;
if|if
condition|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|cookie
operator|=
name|greeting
operator|.
name|substring
argument_list|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeting
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bonjour"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"a=a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"b=b"
argument_list|)
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"NiHao"
argument_list|)
expr_stmt|;
name|cookie
operator|=
literal|""
expr_stmt|;
if|if
condition|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|cookie
operator|=
name|greeting
operator|.
name|substring
argument_list|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeting
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Hello"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"a=a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cookie
operator|.
name|contains
argument_list|(
literal|"b=b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithoutSession
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bonjour"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Hello"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"NiHao"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello NiHao"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"seem to get random failures on everything except Linux with this."
operator|+
literal|" Maybe a undertow issue."
argument_list|)
specifier|public
name|void
name|testPublishOnBusyPort
parameter_list|()
block|{
name|boolean
name|isWindows
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
decl_stmt|;
name|GreeterSessionImpl
name|implementor
init|=
operator|new
name|GreeterSessionImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
try|try
block|{
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isWindows
condition|)
block|{
name|fail
argument_list|(
literal|"Should have failed to publish as the port is busy"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Should have failed to publish as the port is busy, but certains "
operator|+
literal|"of Windows allow this."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
comment|//CXF-1589
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isWindows
condition|)
block|{
name|fail
argument_list|(
literal|"Should have failed to publish as the port is busy"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Should have failed to publish as the port is busy, but certains "
operator|+
literal|"of Windows allow this."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithSessionFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|doSessionsTest
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/Stateful1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithSessionAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|doSessionsTest
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/Stateful2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithPerRequestAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/PerRequest"
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour default"
argument_list|,
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithSpringBeanAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SpringBean"
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour World"
argument_list|,
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnewayInvocationWithSession
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bp
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"Bonjour"
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
if|if
condition|(
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|greeting
operator|=
name|greeting
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|greeting
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bonjour"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
specifier|private
name|void
name|doSessionsTest
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|Greeter
name|greeter2
init|=
name|service
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|bp
operator|=
operator|(
name|BindingProvider
operator|)
name|greeter2
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour World"
argument_list|,
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|greeter2
operator|.
name|greetMe
argument_list|(
literal|"Universe"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Universe"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour Universe"
argument_list|,
name|greeter2
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
comment|//make sure session 1 was maintained
name|assertEquals
argument_list|(
literal|"Bonjour World"
argument_list|,
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

