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
name|jaxws
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
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Dispatch
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
name|Service
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
name|endpoint
operator|.
name|ClientImpl
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
name|JaxWsClientProxy
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
name|context
operator|.
name|WrappedMessageContext
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
name|test
operator|.
name|AbstractCXFTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
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
name|JaxWsClientThreadTest
extends|extends
name|AbstractCXFTest
block|{
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testRequestContextThreadSafety
parameter_list|()
throws|throws
name|Throwable
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|s
init|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|create
argument_list|(
name|url
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|s
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|handler
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|JaxWsClientProxy
operator|.
name|THREAD_LOCAL_REQUEST_CONTEXT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|handler
operator|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|String
name|address
init|=
operator|(
name|String
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
decl_stmt|;
specifier|final
name|Throwable
name|errorHolder
index|[]
init|=
operator|new
name|Throwable
index|[
literal|1
index|]
decl_stmt|;
name|Runnable
name|r
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
specifier|final
name|String
name|protocol
init|=
literal|"http-"
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|String
name|threadSpecificaddress
init|=
name|protocol
operator|+
literal|"://localhost:80/"
operator|+
name|i
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|handler
operator|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|threadSpecificaddress
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we get what we set"
argument_list|,
name|threadSpecificaddress
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Hi"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|expected
parameter_list|)
block|{
comment|//expected.getCause().printStackTrace();
name|MalformedURLException
name|mue
init|=
operator|(
name|MalformedURLException
operator|)
name|expected
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|mue
operator|==
literal|null
operator|||
name|mue
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
name|expected
throw|;
block|}
name|assertTrue
argument_list|(
literal|"protocol contains thread id from context"
argument_list|,
name|mue
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|protocol
argument_list|)
operator|!=
literal|0
argument_list|)
expr_stmt|;
block|}
name|requestContext
operator|.
name|remove
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"property is null"
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// capture assert failures
name|errorHolder
index|[
literal|0
index|]
operator|=
name|t
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
specifier|final
name|int
name|numThreads
init|=
literal|5
decl_stmt|;
name|Thread
index|[]
name|threads
init|=
operator|new
name|Thread
index|[
name|numThreads
index|]
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|(
name|r
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|.
name|start
argument_list|()
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|errorHolder
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
throw|throw
name|errorHolder
index|[
literal|0
index|]
throw|;
block|}
comment|// main thread contextValues are un changed
name|assertTrue
argument_list|(
literal|"address from existing context has not changed"
argument_list|,
name|address
operator|.
name|equals
argument_list|(
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// get the latest values
operator|(
call|(
name|ClientImpl
operator|.
name|EchoContext
call|)
argument_list|(
operator|(
name|WrappedMessageContext
operator|)
name|requestContext
argument_list|)
operator|.
name|getWrappedMap
argument_list|()
operator|)
operator|.
name|reload
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"address is different"
argument_list|,
operator|!
name|address
operator|.
name|equals
argument_list|(
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// verify value reflects what other threads were doing
name|assertTrue
argument_list|(
literal|"property is null from last thread execution"
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestContextThreadSafetyDispatch
parameter_list|()
throws|throws
name|Throwable
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|s
init|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|create
argument_list|(
name|url
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|JAXBContext
name|c
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|ObjectFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Dispatch
argument_list|<
name|Object
argument_list|>
name|disp
init|=
name|s
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|c
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|disp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|JaxWsClientProxy
operator|.
name|THREAD_LOCAL_REQUEST_CONTEXT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|disp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|String
name|address
init|=
operator|(
name|String
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
decl_stmt|;
specifier|final
name|Throwable
name|errorHolder
index|[]
init|=
operator|new
name|Throwable
index|[
literal|1
index|]
decl_stmt|;
name|Runnable
name|r
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
specifier|final
name|String
name|protocol
init|=
literal|"http-"
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|String
name|threadSpecificaddress
init|=
name|protocol
operator|+
literal|"://localhost:80/"
operator|+
name|i
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|disp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|threadSpecificaddress
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we get what we set"
argument_list|,
name|threadSpecificaddress
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMe
name|gm
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMe
argument_list|()
decl_stmt|;
name|gm
operator|.
name|setRequestType
argument_list|(
literal|"Hi"
argument_list|)
expr_stmt|;
name|disp
operator|.
name|invoke
argument_list|(
name|gm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|expected
parameter_list|)
block|{
comment|//expected.getCause().printStackTrace();
name|MalformedURLException
name|mue
init|=
operator|(
name|MalformedURLException
operator|)
name|expected
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|mue
operator|==
literal|null
operator|||
name|mue
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
name|expected
throw|;
block|}
name|assertTrue
argument_list|(
literal|"protocol contains thread id from context"
argument_list|,
name|mue
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|protocol
argument_list|)
operator|!=
literal|0
argument_list|)
expr_stmt|;
block|}
name|requestContext
operator|.
name|remove
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"property is null"
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// capture assert failures
name|errorHolder
index|[
literal|0
index|]
operator|=
name|t
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
specifier|final
name|int
name|numThreads
init|=
literal|5
decl_stmt|;
name|Thread
index|[]
name|threads
init|=
operator|new
name|Thread
index|[
name|numThreads
index|]
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|(
name|r
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|.
name|start
argument_list|()
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|errorHolder
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
throw|throw
name|errorHolder
index|[
literal|0
index|]
throw|;
block|}
comment|// main thread contextValues are un changed
name|assertTrue
argument_list|(
literal|"address from existing context has not changed"
argument_list|,
name|address
operator|.
name|equals
argument_list|(
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// get the latest values
operator|(
call|(
name|ClientImpl
operator|.
name|EchoContext
call|)
argument_list|(
operator|(
name|WrappedMessageContext
operator|)
name|requestContext
argument_list|)
operator|.
name|getWrappedMap
argument_list|()
operator|)
operator|.
name|reload
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"address is different"
argument_list|,
operator|!
name|address
operator|.
name|equals
argument_list|(
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// verify value reflects what other threads were doing
name|assertTrue
argument_list|(
literal|"property is null from last thread execution"
argument_list|,
name|requestContext
operator|.
name|get
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

