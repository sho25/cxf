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
name|wsdl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Port
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|abc
operator|.
name|test
operator|.
name|AnotherPolicyType
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
name|abc
operator|.
name|test
operator|.
name|NewServiceType
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
name|abc
operator|.
name|test
operator|.
name|TestPolicyType
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXBExtensionHelperTest
extends|extends
name|Assert
block|{
specifier|private
name|WSDLFactory
name|wsdlFactory
decl_stmt|;
specifier|private
name|WSDLReader
name|wsdlReader
decl_stmt|;
specifier|private
name|Definition
name|wsdlDefinition
decl_stmt|;
specifier|private
name|ExtensionRegistry
name|registry
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|wsdlFactory
operator|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|wsdlReader
operator|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
expr_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|registry
operator|=
name|wsdlReader
operator|.
name|getExtensionRegistry
argument_list|()
expr_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTestExtension
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Port"
argument_list|,
literal|"org.apache.cxf.abc.test.TestPolicyType"
argument_list|)
expr_stmt|;
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Port"
argument_list|,
literal|"org.apache.cxf.abc.test.AnotherPolicyType"
argument_list|)
expr_stmt|;
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Definition"
argument_list|,
literal|"org.apache.cxf.abc.test.NewServiceType"
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/test_ext.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|checkTestExt
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrettyPrintXMLStreamWriter
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Definition"
argument_list|,
literal|"org.apache.cxf.abc.test.NewServiceType"
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/test_ext.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|extList
init|=
name|wsdlDefinition
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|NewServiceType
name|newService
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|ext
range|:
name|extList
control|)
block|{
if|if
condition|(
name|ext
operator|instanceof
name|NewServiceType
condition|)
block|{
name|newService
operator|=
operator|(
name|NewServiceType
operator|)
name|ext
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Could not find extension element NewServiceType"
argument_list|,
name|newService
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|stream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|JAXBExtensionHelper
name|helper
init|=
operator|new
name|JAXBExtensionHelper
argument_list|(
name|NewServiceType
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|helper
operator|.
name|marshall
argument_list|(
name|javax
operator|.
name|wsdl
operator|.
name|Definition
operator|.
name|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/hello_world"
argument_list|,
literal|"newService"
argument_list|)
argument_list|,
name|newService
argument_list|,
operator|new
name|PrintWriter
argument_list|(
name|stream
argument_list|)
argument_list|,
name|wsdlDefinition
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
operator|new
name|String
argument_list|(
name|stream
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|actual
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|int
name|spaces
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|actual
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|actual
operator|.
name|endsWith
argument_list|(
literal|"/>"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|actual
operator|.
name|contains
argument_list|(
literal|"</"
argument_list|)
condition|)
block|{
name|spaces
operator|+=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|spaces
operator|-=
literal|2
expr_stmt|;
block|}
block|}
name|checkSpaces
argument_list|(
name|actual
argument_list|,
name|spaces
argument_list|)
expr_stmt|;
name|actual
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMappedNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Port"
argument_list|,
literal|"org.apache.cxf.abc.test.TestPolicyType"
argument_list|,
literal|"http://cxf.apache.org/abc/test/remapped"
argument_list|)
expr_stmt|;
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Port"
argument_list|,
literal|"org.apache.cxf.abc.test.AnotherPolicyType"
argument_list|,
literal|"http://cxf.apache.org/abc/test/remapped"
argument_list|)
expr_stmt|;
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
literal|"javax.wsdl.Definition"
argument_list|,
literal|"org.apache.cxf.abc.test.NewServiceType"
argument_list|,
literal|"http://cxf.apache.org/abc/test/remapped"
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/test_ext_remapped.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|checkTestExt
argument_list|()
expr_stmt|;
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|wsdlFactory
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|wsdlDefinition
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
literal|null
argument_list|,
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|out
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkTestExt
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|checkTestExt
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
name|wsdlDefinition
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/hello_world"
argument_list|,
literal|"HelloWorldService"
argument_list|)
argument_list|)
decl_stmt|;
name|Port
name|p
init|=
name|s
operator|.
name|getPort
argument_list|(
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|extPortList
init|=
name|p
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|TestPolicyType
name|tp
init|=
literal|null
decl_stmt|;
name|AnotherPolicyType
name|ap
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|ext
range|:
name|extPortList
control|)
block|{
if|if
condition|(
name|ext
operator|instanceof
name|TestPolicyType
condition|)
block|{
name|tp
operator|=
operator|(
name|TestPolicyType
operator|)
name|ext
expr_stmt|;
block|}
if|if
condition|(
name|ext
operator|instanceof
name|AnotherPolicyType
condition|)
block|{
name|ap
operator|=
operator|(
name|AnotherPolicyType
operator|)
name|ext
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Could not find extension element TestPolicyType"
argument_list|,
name|tp
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find extension element AnotherPolicyType"
argument_list|,
name|ap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for TestPolicyType intAttr"
argument_list|,
literal|30
argument_list|,
name|tp
operator|.
name|getIntAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for TestPolicyType stringAttr"
argument_list|,
literal|"hello"
argument_list|,
name|tp
operator|.
name|getStringAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for AnotherPolicyType floatAttr"
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
literal|0.1F
operator|-
name|ap
operator|.
name|getFloatAttr
argument_list|()
argument_list|)
operator|<
literal|0.5E
operator|-
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkSpaces
parameter_list|(
name|String
name|actual
parameter_list|,
name|int
name|spaces
parameter_list|)
block|{
name|String
name|space
init|=
literal|""
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
name|spaces
condition|;
name|i
operator|++
control|)
block|{
name|space
operator|+=
literal|" "
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Indentation level not proper when marshalling a extension element;"
operator|+
name|actual
argument_list|,
name|actual
operator|.
name|startsWith
argument_list|(
name|space
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

