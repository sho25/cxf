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
name|javascript
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
name|io
operator|.
name|Reader
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|Bus
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
name|IOUtils
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
name|javascript
operator|.
name|JavascriptTestUtilities
operator|.
name|Notifier
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
name|test
operator|.
name|AbstractCXFSpringTest
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
name|XPathAssert
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
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|PropertyPlaceholderConfigurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
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

begin_comment
comment|/**  * This test is ignored by default as it is continually failing on Jenkins.  */
end_comment

begin_class
specifier|public
class|class
name|JsHttpRequestTest
extends|extends
name|AbstractCXFSpringTest
block|{
comment|// shadow declaration from base class.
specifier|private
name|JavascriptTestUtilities
name|testUtilities
decl_stmt|;
specifier|public
name|JsHttpRequestTest
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|=
operator|new
name|JavascriptTestUtilities
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// bring in some property values from a Properties file
name|PropertyPlaceholderConfigurer
name|cfg
init|=
operator|new
name|PropertyPlaceholderConfigurer
argument_list|()
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"staticResourceURL"
argument_list|,
name|getStaticResourceURL
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// now actually do the replacement
name|cfg
operator|.
name|postProcessBeanFactory
argument_list|(
name|applicationContext
operator|.
name|getBeanFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
name|JsHttpRequestTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:XMLHttpRequestTestBeans.xml"
block|}
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setupRhino
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|setBus
argument_list|(
name|getBean
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|initializeRhino
argument_list|()
expr_stmt|;
name|testUtilities
operator|.
name|readResourceIntoRhino
argument_list|(
literal|"/org/apache/cxf/javascript/XMLHttpRequestTests.js"
argument_list|)
expr_stmt|;
block|}
comment|// just one test function to avoid muddles with engine startup/shutdown
annotation|@
name|Test
specifier|public
name|void
name|runTests
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|rhinoCallExpectingExceptionInContext
argument_list|(
literal|"SYNTAX_ERR"
argument_list|,
literal|"testOpaqueURI"
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallExpectingExceptionInContext
argument_list|(
literal|"SYNTAX_ERR"
argument_list|,
literal|"testNonAbsolute"
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallExpectingExceptionInContext
argument_list|(
literal|"SYNTAX_ERR"
argument_list|,
literal|"testNonHttp"
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallExpectingExceptionInContext
argument_list|(
literal|"INVALID_STATE_ERR"
argument_list|,
literal|"testSendNotOpenError"
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallInContext
argument_list|(
literal|"testStateNotificationSync"
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"testAsyncHttpFetch1"
argument_list|,
name|Notifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|testUtilities
operator|.
name|rhinoCallInContext
argument_list|(
literal|"testAsyncHttpFetch2"
argument_list|)
expr_stmt|;
name|boolean
name|notified
init|=
name|notifier
operator|.
name|waitForJavascript
argument_list|(
literal|2
operator|*
literal|10000
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|notified
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"HEADERS_RECEIVED"
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncGotHeadersReceived"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"LOADING"
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncGotLoading"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"DONE"
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncGotDone"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|outOfOrder
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"outOfOrderError"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OutOfOrder"
argument_list|,
literal|null
argument_list|,
name|outOfOrder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"status 200"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|200
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncStatus"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"status text"
argument_list|,
literal|"OK"
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncStatusText"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"headers"
argument_list|,
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"asyncResponseHeaders"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Content-Type: text/html"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|httpObj
init|=
name|testUtilities
operator|.
name|rhinoCallInContext
argument_list|(
literal|"testSyncHttpFetch"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|httpObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|httpObj
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|String
name|httpResponse
init|=
operator|(
name|String
operator|)
name|httpObj
decl_stmt|;
name|assertTrue
argument_list|(
name|httpResponse
operator|.
name|contains
argument_list|(
literal|"Test"
argument_list|)
argument_list|)
expr_stmt|;
name|Reader
name|r
init|=
name|getResourceAsReader
argument_list|(
literal|"/org/apache/cxf/javascript/XML_GreetMeDocLiteralReq.xml"
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|EndpointImpl
name|endpoint
init|=
name|this
operator|.
name|getBean
argument_list|(
name|EndpointImpl
operator|.
name|class
argument_list|,
literal|"greeter-service-endpoint"
argument_list|)
decl_stmt|;
name|JsSimpleDomNode
name|xmlResponse
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"testSyncXml"
argument_list|,
name|JsSimpleDomNode
operator|.
name|class
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|xml
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xmlResponse
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
operator|(
name|Document
operator|)
name|xmlResponse
operator|.
name|getWrappedNode
argument_list|()
decl_stmt|;
name|testUtilities
operator|.
name|addNamespace
argument_list|(
literal|"t"
argument_list|,
literal|"http://apache.org/hello_world_xml_http/wrapped/types"
argument_list|)
expr_stmt|;
name|XPath
name|textPath
init|=
name|XPathAssert
operator|.
name|createXPath
argument_list|(
name|testUtilities
operator|.
name|getNamespaces
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|nodeText
init|=
operator|(
name|String
operator|)
name|textPath
operator|.
name|evaluate
argument_list|(
literal|"//t:responseType/text()"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello \u05e9\u05dc\u05d5\u05dd"
argument_list|,
name|nodeText
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getStaticResourceURL
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|staticFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test.html"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|staticFile
operator|=
name|staticFile
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|staticFile
operator|=
name|staticFile
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|URL
name|furl
init|=
name|staticFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
return|return
name|furl
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

