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
name|wsdlto
operator|.
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|ToolContext
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WSDLToJavaScriptTest
extends|extends
name|ProcessorTestBase
block|{
specifier|public
name|int
name|countChar
parameter_list|(
name|String
name|text
parameter_list|,
name|char
name|symbol
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
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
name|text
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|text
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
name|symbol
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
comment|// just run with a minimum of fuss.
annotation|@
name|Test
specifier|public
name|void
name|testGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaScriptContainer
name|container
init|=
operator|new
name|JavaScriptContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ToolContext
name|context
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|prefixes
init|=
operator|new
name|String
index|[
literal|1
index|]
decl_stmt|;
name|prefixes
index|[
literal|0
index|]
operator|=
literal|"http://apache.org/hello_world_soap_http=murble"
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|,
name|prefixes
argument_list|)
expr_stmt|;
name|container
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|container
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// now we really want to check some results.
name|Path
name|path
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"SOAPService_Test1.js"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|isReadable
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|javascript
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|path
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|javascript
operator|.
name|contains
argument_list|(
literal|"xmlns:murble='http://apache.org/hello_world_soap_http'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of '{' does not match number of '}' in generated JavaScript."
argument_list|,
name|countChar
argument_list|(
name|javascript
argument_list|,
literal|'{'
argument_list|)
argument_list|,
name|countChar
argument_list|(
name|javascript
argument_list|,
literal|'}'
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF3891
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaScriptContainer
name|container
init|=
operator|new
name|JavaScriptContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ToolContext
name|context
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"hello_world_ref.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|prefixes
init|=
operator|new
name|String
index|[
literal|1
index|]
decl_stmt|;
name|prefixes
index|[
literal|0
index|]
operator|=
literal|"http://apache.org/hello_world_soap_http=murble"
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_JSPACKAGEPREFIX
argument_list|,
name|prefixes
argument_list|)
expr_stmt|;
name|container
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|container
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// now we really want to check some results.
name|Path
name|path
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|output
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"SOAPService.js"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|isReadable
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|javascript
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|path
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|javascript
operator|.
name|contains
argument_list|(
literal|"xmlns:murble='http://apache.org/hello_world_soap_http'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of '{' does not match number of '}' in generated JavaScript."
argument_list|,
name|countChar
argument_list|(
name|javascript
argument_list|,
literal|'{'
argument_list|)
argument_list|,
name|countChar
argument_list|(
name|javascript
argument_list|,
literal|'}'
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

