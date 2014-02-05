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
name|jms
package|;
end_package

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
name|ArrayList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|hello_world_jms
operator|.
name|HelloWorldPortType
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
name|hello_world_jms
operator|.
name|HelloWorldServiceAppCorrelationID
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
name|hello_world_jms
operator|.
name|HelloWorldServiceAppCorrelationIDNoPrefix
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
name|hello_world_jms
operator|.
name|HelloWorldServiceAppCorrelationIDStaticPrefix
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
name|hello_world_jms
operator|.
name|HelloWorldServiceRuntimeCorrelationIDDynamicPrefix
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
name|hello_world_jms
operator|.
name|HelloWorldServiceRuntimeCorrelationIDStaticPrefix
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
name|testutil
operator|.
name|common
operator|.
name|EmbeddedJMSBrokerLauncher
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
name|jms
operator|.
name|JMSConstants
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
name|jms
operator|.
name|JMSMessageHeadersType
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
name|JMSSharedQueueTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BROKER_URI
init|=
literal|"vm://SharedQueueTest"
operator|+
literal|"?jms.watchTopicAdvisories=false&broker.persistent=false"
decl_stmt|;
specifier|private
specifier|static
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|private
name|String
name|wsdlString
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
name|broker
operator|=
operator|new
name|EmbeddedJMSBrokerLauncher
argument_list|(
name|BROKER_URI
argument_list|)
expr_stmt|;
name|launchServer
argument_list|(
name|broker
argument_list|)
expr_stmt|;
name|launchServer
argument_list|(
operator|new
name|Server
argument_list|(
name|broker
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
specifier|public
name|URL
name|getWSDLURL
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|u
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"WSDL classpath resource not found "
operator|+
name|s
argument_list|)
throw|;
block|}
name|wsdlString
operator|=
name|u
operator|.
name|toString
argument_list|()
operator|.
name|intern
argument_list|()
expr_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdlString
argument_list|)
expr_stmt|;
return|return
name|u
return|;
block|}
specifier|private
interface|interface
name|CorrelationIDFactory
block|{
name|String
name|createCorrealtionID
parameter_list|()
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|ClientRunnable
implements|implements
name|Runnable
block|{
specifier|private
name|HelloWorldPortType
name|port
decl_stmt|;
specifier|private
name|CorrelationIDFactory
name|corrFactory
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|private
name|Throwable
name|ex
decl_stmt|;
specifier|public
name|ClientRunnable
parameter_list|(
name|HelloWorldPortType
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
specifier|public
name|ClientRunnable
parameter_list|(
name|HelloWorldPortType
name|port
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|ClientRunnable
parameter_list|(
name|HelloWorldPortType
name|port
parameter_list|,
name|CorrelationIDFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|corrFactory
operator|=
name|factory
expr_stmt|;
block|}
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|ex
return|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|5
condition|;
name|idx
operator|++
control|)
block|{
name|callGreetMe
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|callGreetMe
parameter_list|()
block|{
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|port
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|bp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
name|String
name|request
init|=
literal|"World"
operator|+
operator|(
operator|(
name|prefix
operator|!=
literal|null
operator|)
condition|?
literal|":"
operator|+
name|prefix
else|:
literal|""
operator|)
decl_stmt|;
name|String
name|correlationID
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|corrFactory
operator|!=
literal|null
condition|)
block|{
name|correlationID
operator|=
name|corrFactory
operator|.
name|createCorrealtionID
argument_list|()
expr_stmt|;
name|requestHeader
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
name|request
operator|+=
literal|":"
operator|+
name|correlationID
expr_stmt|;
block|}
name|String
name|expected
init|=
literal|"Hello "
operator|+
name|request
decl_stmt|;
name|String
name|response
init|=
name|port
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Response didn't match expected request"
argument_list|,
name|expected
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|corrFactory
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|bp
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|responseHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|responseContext
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Request and Response CorrelationID didn't match"
argument_list|,
name|correlationID
argument_list|,
name|responseHeader
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|executeAsync
parameter_list|(
name|ClientRunnable
modifier|...
name|clients
parameter_list|)
throws|throws
name|Throwable
block|{
name|executeAsync
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|clients
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|executeAsync
parameter_list|(
name|Collection
argument_list|<
name|ClientRunnable
argument_list|>
name|clients
parameter_list|)
throws|throws
name|Throwable
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
for|for
control|(
name|ClientRunnable
name|client
range|:
name|clients
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
for|for
control|(
name|ClientRunnable
name|client
range|:
name|clients
control|)
block|{
if|if
condition|(
name|client
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|client
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayQueueAppCorrelationID
parameter_list|()
throws|throws
name|Throwable
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldServiceAppCorrelationID"
argument_list|)
decl_stmt|;
name|QName
name|portNameEng
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortAppCorrelationIDEng"
argument_list|)
decl_stmt|;
name|QName
name|portNameSales
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortAppCorrelationIDSales"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldServiceAppCorrelationID
name|service
init|=
operator|new
name|HelloWorldServiceAppCorrelationID
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|ClientRunnable
name|engClient
init|=
operator|new
name|ClientRunnable
argument_list|(
name|service
operator|.
name|getPort
argument_list|(
name|portNameEng
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|CorrelationIDFactory
argument_list|()
block|{
specifier|private
name|int
name|counter
decl_stmt|;
specifier|public
name|String
name|createCorrealtionID
parameter_list|()
block|{
return|return
literal|"com.mycompany.eng:"
operator|+
name|counter
operator|++
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|ClientRunnable
name|salesClient
init|=
operator|new
name|ClientRunnable
argument_list|(
name|service
operator|.
name|getPort
argument_list|(
name|portNameSales
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|CorrelationIDFactory
argument_list|()
block|{
specifier|private
name|int
name|counter
decl_stmt|;
specifier|public
name|String
name|createCorrealtionID
parameter_list|()
block|{
return|return
literal|"com.mycompany.sales:"
operator|+
name|counter
operator|++
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|executeAsync
argument_list|(
name|engClient
argument_list|,
name|salesClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayQueueAppCorrelationIDStaticPrefix
parameter_list|()
throws|throws
name|Throwable
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldServiceAppCorrelationIDStaticPrefix"
argument_list|)
decl_stmt|;
name|QName
name|portNameEng
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortAppCorrelationIDStaticPrefixEng"
argument_list|)
decl_stmt|;
name|QName
name|portNameSales
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortAppCorrelationIDStaticPrefixSales"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldServiceAppCorrelationIDStaticPrefix
name|service
init|=
operator|new
name|HelloWorldServiceAppCorrelationIDStaticPrefix
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|ClientRunnable
name|engClient
init|=
operator|new
name|ClientRunnable
argument_list|(
name|service
operator|.
name|getPort
argument_list|(
name|portNameEng
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|ClientRunnable
name|salesClient
init|=
operator|new
name|ClientRunnable
argument_list|(
name|service
operator|.
name|getPort
argument_list|(
name|portNameSales
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|executeAsync
argument_list|(
name|engClient
argument_list|,
name|salesClient
argument_list|)
expr_stmt|;
block|}
comment|/* TO DO:      * This tests shows a missing QoS. When CXF clients share a named (persistent) reply queue      *  with an application provided correlationID there will be a guaranteed response      * message loss.       *       * A large number of threads is used to ensure message loss and avoid a false       * positive assertion      */
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayQueueAppCorrelationIDNoPrefix
parameter_list|()
throws|throws
name|Throwable
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldServiceAppCorrelationIDNoPrefix"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortAppCorrelationIDNoPrefix"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldServiceAppCorrelationIDNoPrefix
name|service
init|=
operator|new
name|HelloWorldServiceAppCorrelationIDNoPrefix
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ClientRunnable
argument_list|>
name|clients
init|=
operator|new
name|ArrayList
argument_list|<
name|ClientRunnable
argument_list|>
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
literal|1
condition|;
operator|++
name|i
control|)
block|{
name|clients
operator|.
name|add
argument_list|(
operator|new
name|ClientRunnable
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|executeAsync
argument_list|(
name|clients
argument_list|)
expr_stmt|;
block|}
comment|/*      * This tests a use case where there is a shared request and reply queues between      * two servers (Eng and Sales). However each server has a design time provided selector      * which allows them to share the same queue and do not consume the other's      * messages.       *       * The clients to these two servers use the same request and reply queues.      * An Eng client uses a design time selector prefix to form request message       * correlationID and to form a reply consumer that filters only reply      * messages originated from the Eng server. To differentiate between      * one Eng client instance from another this suffix is supplemented by      * a runtime value of ConduitId which has 1-1 relation to a client instance      * This guarantees that an Eng client instance will only consume its own reply       * messages.       *       * In case of a single client instance being shared among multiple threads      * the third portion of the request message correlationID,       * an atomic rolling message counter, ensures that each message gets a unique ID      *        * So the model is:      *       * Many concurrent Sales clients to a single request and reply queues (Q1, Q2)       * to a single Sales server      * Many concurrent Eng clients to a single request and reply queues (Q1, Q2)       * to a single Eng server      */
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayQueueRuntimeCorrelationIDStaticPrefix
parameter_list|()
throws|throws
name|Throwable
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldServiceRuntimeCorrelationIDStaticPrefix"
argument_list|)
decl_stmt|;
name|QName
name|portNameEng
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortRuntimeCorrelationIDStaticPrefixEng"
argument_list|)
decl_stmt|;
name|QName
name|portNameSales
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortRuntimeCorrelationIDStaticPrefixSales"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldServiceRuntimeCorrelationIDStaticPrefix
name|service
init|=
operator|new
name|HelloWorldServiceRuntimeCorrelationIDStaticPrefix
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|portEng
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portNameEng
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|portSales
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portNameSales
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ClientRunnable
argument_list|>
name|clients
init|=
operator|new
name|ArrayList
argument_list|<
name|ClientRunnable
argument_list|>
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
operator|++
name|i
control|)
block|{
name|clients
operator|.
name|add
argument_list|(
operator|new
name|ClientRunnable
argument_list|(
name|portEng
argument_list|,
literal|"com.mycompany.eng:"
argument_list|)
argument_list|)
expr_stmt|;
name|clients
operator|.
name|add
argument_list|(
operator|new
name|ClientRunnable
argument_list|(
name|portSales
argument_list|,
literal|"com.mycompany.sales:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|executeAsync
argument_list|(
name|clients
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayQueueRuntimeCorrelationDynamicPrefix
parameter_list|()
throws|throws
name|Throwable
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldServiceRuntimeCorrelationIDDynamicPrefix"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPortRuntimeCorrelationIDDynamicPrefix"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldServiceRuntimeCorrelationIDDynamicPrefix
name|service
init|=
operator|new
name|HelloWorldServiceRuntimeCorrelationIDDynamicPrefix
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ClientRunnable
argument_list|>
name|clients
init|=
operator|new
name|ArrayList
argument_list|<
name|ClientRunnable
argument_list|>
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
operator|++
name|i
control|)
block|{
name|clients
operator|.
name|add
argument_list|(
operator|new
name|ClientRunnable
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|executeAsync
argument_list|(
name|clients
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

