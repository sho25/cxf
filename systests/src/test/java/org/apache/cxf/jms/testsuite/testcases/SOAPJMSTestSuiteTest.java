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
name|jms
operator|.
name|testsuite
operator|.
name|testcases
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
name|jms
operator|.
name|DeliveryMode
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
name|jms
operator|.
name|testsuite
operator|.
name|services
operator|.
name|Server
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
name|jms
operator|.
name|testsuite
operator|.
name|util
operator|.
name|JMSTestUtil
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
name|jms_simple
operator|.
name|JMSSimplePortType
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
name|jms_simple
operator|.
name|JMSSimpleService0001
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
name|jms_simple
operator|.
name|JMSSimpleService0003
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
name|jms_simple
operator|.
name|JMSSimpleService0005
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
name|jms_simple
operator|.
name|JMSSimpleService0006
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
name|jms_simple
operator|.
name|JMSSimpleService0008
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
name|jms_simple
operator|.
name|JMSSimpleService0009
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
name|jms_simple
operator|.
name|JMSSimpleService0010
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
name|jms_simple
operator|.
name|JMSSimpleService0011
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
name|jms_simple
operator|.
name|JMSSimpleService0012
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
name|jms_simple
operator|.
name|JMSSimpleService1001
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
name|jms
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
name|testsuite
operator|.
name|testcase
operator|.
name|TestCaseType
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SOAPJMSTestSuiteTest
extends|extends
name|AbstractSOAPJMSTestSuite
block|{
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"activemq.store.dir"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"activemq.store.dir"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"activemq.store.dir"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
literal|"java.util.logging.config.file"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.util.logging.config.file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|EmbeddedJMSBrokerLauncher
operator|.
name|class
argument_list|,
name|props
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|oneWayTest
parameter_list|(
name|TestCaseType
name|testcase
parameter_list|,
name|JMSSimplePortType
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|handler
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
name|Exception
name|e
init|=
literal|null
decl_stmt|;
try|try
block|{
name|port
operator|.
name|ping
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|e
operator|=
name|e1
expr_stmt|;
block|}
name|checkJMSProperties
argument_list|(
name|testcase
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
specifier|private
name|void
name|twoWayTest
parameter_list|(
name|TestCaseType
name|testcase
parameter_list|,
specifier|final
name|JMSSimplePortType
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|twoWayTestWithRequestHeader
argument_list|(
name|testcase
argument_list|,
name|port
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|twoWayTestWithRequestHeader
parameter_list|(
name|TestCaseType
name|testcase
parameter_list|,
specifier|final
name|JMSSimplePortType
name|port
parameter_list|,
name|JMSMessageHeadersType
name|requestHeader
parameter_list|)
throws|throws
name|Exception
block|{
name|InvocationHandler
name|handler
init|=
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|handler
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
if|if
condition|(
name|requestHeader
operator|==
literal|null
condition|)
block|{
name|requestHeader
operator|=
operator|new
name|JMSMessageHeadersType
argument_list|()
expr_stmt|;
block|}
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
name|Exception
name|e
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|response
init|=
name|port
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|e
operator|=
name|e1
expr_stmt|;
block|}
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
name|checkJMSProperties
argument_list|(
name|testcase
argument_list|,
name|requestHeader
argument_list|,
name|responseHeader
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0001
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0001"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0001"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0001
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|oneWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0002
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0002"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0001"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0001
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0003
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0003"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0003"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0003
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|oneWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0004
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0004"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0003"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0003
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0005
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0005"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0005"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0005
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0006
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0006"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0006"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0006
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0008
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0008"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0008"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0008
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestHeader
operator|.
name|setJMSDeliveryMode
argument_list|(
name|DeliveryMode
operator|.
name|NON_PERSISTENT
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setTimeToLive
argument_list|(
literal|14400000
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setJMSPriority
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setJMSReplyTo
argument_list|(
literal|"dynamicQueues/replyqueue0008"
argument_list|)
expr_stmt|;
name|twoWayTestWithRequestHeader
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0009
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0009"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0009"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0009
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestHeader
operator|.
name|setJMSDeliveryMode
argument_list|(
name|DeliveryMode
operator|.
name|NON_PERSISTENT
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setTimeToLive
argument_list|(
literal|10800000
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setJMSPriority
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|requestHeader
operator|.
name|setJMSReplyTo
argument_list|(
literal|"dynamicQueues/replyqueue00093"
argument_list|)
expr_stmt|;
name|twoWayTestWithRequestHeader
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0010
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0010"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0010"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0010
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0011
parameter_list|()
throws|throws
name|Exception
block|{
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0011"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0011"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0011
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test0012
parameter_list|()
throws|throws
name|Exception
block|{
comment|// same to test0002
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0012"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService0012"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService0012
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|twoWayTest
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test1001
parameter_list|()
throws|throws
name|Exception
block|{
comment|// same to test0002
name|TestCaseType
name|testcase
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1001"
argument_list|)
decl_stmt|;
specifier|final
name|JMSSimplePortType
name|simplePort
init|=
name|getPort
argument_list|(
literal|"JMSSimpleService1001"
argument_list|,
literal|"SimplePort"
argument_list|,
name|JMSSimpleService1001
operator|.
name|class
argument_list|,
name|JMSSimplePortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestHeader
operator|.
name|setSOAPJMSBindingVersion
argument_list|(
literal|"0.3"
argument_list|)
expr_stmt|;
try|try
block|{
name|twoWayTestWithRequestHeader
argument_list|(
name|testcase
argument_list|,
name|simplePort
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unrecognized BindingVersion"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

