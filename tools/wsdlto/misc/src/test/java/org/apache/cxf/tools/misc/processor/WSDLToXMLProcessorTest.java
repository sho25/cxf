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
name|tools
operator|.
name|misc
operator|.
name|processor
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
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingOperation
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
name|http
operator|.
name|HTTPAddress
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
name|apache
operator|.
name|cxf
operator|.
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLFormatBinding
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
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|misc
operator|.
name|WSDLToXML
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
name|WSDLToXMLProcessorTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-i"
block|,
literal|"Greeter"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLToXML
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"hello_world-xmlbinding.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New wsdl file is not generated"
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToXMLProcessor
name|processor
init|=
operator|new
name|WSDLToXMLProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|parseWSDL
argument_list|(
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Binding
name|binding
init|=
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getBinding
argument_list|(
operator|new
name|QName
argument_list|(
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter_XMLBinding"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:binding Greeter_XMLBinding Missed!"
argument_list|)
expr_stmt|;
block|}
name|Iterator
name|it
init|=
name|binding
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XMLFormatBinding
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Element<xformat:binding/> Missed!"
argument_list|)
expr_stmt|;
block|}
name|BindingOperation
name|bo
init|=
name|binding
operator|.
name|getBindingOperation
argument_list|(
literal|"sayHi"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|bo
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element<wsdl:operation name=\"sayHi\"> Missed!"
argument_list|)
expr_stmt|;
block|}
name|it
operator|=
name|bo
operator|.
name|getBindingInput
argument_list|()
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|found
operator|=
literal|false
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XMLBindingMessageFormat
operator|&&
operator|(
operator|(
name|XMLBindingMessageFormat
operator|)
name|obj
operator|)
operator|.
name|getRootNode
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
literal|"sayHi"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Element<xformat:body rootNode=\"tns:sayHi\" /> Missed!"
argument_list|)
expr_stmt|;
block|}
name|Service
name|service
init|=
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter_XMLService"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:service Greeter_XMLService Missed!"
argument_list|)
expr_stmt|;
block|}
name|it
operator|=
name|service
operator|.
name|getPort
argument_list|(
literal|"Greeter_XMLPort"
argument_list|)
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|found
operator|=
literal|false
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|HTTPAddress
condition|)
block|{
name|HTTPAddress
name|xmlHttpAddress
init|=
operator|(
name|HTTPAddress
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|xmlHttpAddress
operator|.
name|getLocationURI
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Element http:address of service port Missed!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception Encountered when parsing wsdl, error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

