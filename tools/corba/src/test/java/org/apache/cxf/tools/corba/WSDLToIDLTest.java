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
name|corba
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
name|ByteArrayInputStream
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
name|File
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

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
name|helpers
operator|.
name|FileUtils
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
name|corba
operator|.
name|common
operator|.
name|ToolTestBase
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
name|corba
operator|.
name|processors
operator|.
name|wsdl
operator|.
name|WSDLToProcessor
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
name|corba
operator|.
name|utils
operator|.
name|TestUtils
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToIDLTest
extends|extends
name|ToolTestBase
block|{
specifier|private
specifier|static
name|String
name|usage
decl_stmt|;
specifier|private
specifier|static
name|int
name|noError
decl_stmt|;
specifier|private
specifier|static
name|int
name|error
init|=
operator|-
literal|1
decl_stmt|;
name|ByteArrayOutputStream
name|bout
decl_stmt|;
name|PrintStream
name|newOut
decl_stmt|;
specifier|private
name|File
name|output
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
try|try
block|{
name|TestUtils
name|utils
init|=
operator|new
name|TestUtils
argument_list|(
name|WSDLToIDL
operator|.
name|TOOL_NAME
argument_list|,
name|WSDLToIDL
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/toolspecs/wsdl2idl.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|usage
operator|=
name|utils
operator|.
name|getUsage
argument_list|()
expr_stmt|;
name|bout
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|newOut
operator|=
operator|new
name|PrintStream
argument_list|(
name|bout
argument_list|)
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|newOut
argument_list|)
expr_stmt|;
name|System
operator|.
name|setErr
argument_list|(
name|newOut
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// complete
block|}
try|try
block|{
name|output
operator|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"."
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"generated-idl"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|mkDir
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// complete
block|}
block|}
specifier|private
name|void
name|deleteDir
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|removeDir
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
try|try
block|{
name|deleteDir
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|output
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|int
name|execute
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|WSDLToIDL
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|private
name|void
name|checkStrings
parameter_list|(
name|byte
name|orig
index|[]
parameter_list|,
name|byte
name|generated
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|BufferedReader
name|origReader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|orig
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|BufferedReader
name|genReader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|generated
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|sorig
init|=
name|origReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|sgen
init|=
name|genReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|sorig
operator|!=
literal|null
operator|&&
name|sgen
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|sorig
operator|.
name|equals
argument_list|(
name|sgen
argument_list|)
condition|)
block|{
comment|//assertEquals(sorig, sgen);
comment|//sorig = origReader.readLine();
name|sgen
operator|=
name|genReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|sorig
argument_list|,
name|sgen
argument_list|)
expr_stmt|;
name|sorig
operator|=
literal|null
expr_stmt|;
name|sgen
operator|=
literal|null
expr_stmt|;
break|break;
block|}
block|}
name|origReader
operator|.
name|close
argument_list|()
expr_stmt|;
name|genReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBindingGenDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-corba"
block|,
literal|"-i"
block|,
literal|"BasePortType"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|noError
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"simpleList-corba.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simpleList-corba.wsdl should be generated"
argument_list|,
name|f
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToProcessor
name|proc
init|=
operator|new
name|WSDLToProcessor
argument_list|()
decl_stmt|;
try|try
block|{
name|proc
operator|.
name|parseWSDL
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Definition
name|model
init|=
name|proc
operator|.
name|getWSDLDefinition
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL Definition Should not be Null"
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|QName
name|bindingName
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.apache.org/tests"
argument_list|,
literal|"BaseCORBABinding"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Binding Node not found in WSDL"
argument_list|,
name|model
operator|.
name|getBinding
argument_list|(
name|bindingName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"WSDLToCORBA generated an invalid simpleList-corba.wsdl"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testBindingGenSpecifiedFile
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-corba"
block|,
literal|"-i"
block|,
literal|"BasePortType"
block|,
literal|"-w"
block|,
literal|"simpleList-corba_gen.wsdl"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|noError
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"simpleList-corba_gen.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simpleList-corba_gen.wsdl should be generated"
argument_list|,
name|f
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToProcessor
name|proc
init|=
operator|new
name|WSDLToProcessor
argument_list|()
decl_stmt|;
try|try
block|{
name|proc
operator|.
name|parseWSDL
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Definition
name|model
init|=
name|proc
operator|.
name|getWSDLDefinition
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL Definition Should not be Null"
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|QName
name|bindingName
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.apache.org/tests"
argument_list|,
literal|"BaseCORBABinding"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Binding Node not found in WSDL"
argument_list|,
name|model
operator|.
name|getBinding
argument_list|(
name|bindingName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"WSDLToIDL generated an invalid simpleList-corba.wsdl"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testIDLGenDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-idl"
block|,
literal|"-b"
block|,
literal|"BaseCORBABinding"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simple-binding.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|noError
argument_list|,
name|exc
argument_list|)
expr_stmt|;
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
literal|"simple-binding.idl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simple-binding.idl should be generated"
argument_list|,
name|Files
operator|.
name|isReadable
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|line
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
literal|"Invalid Idl File Generated"
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIDLGenSpecifiedFile
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-idl"
block|,
literal|"-b"
block|,
literal|"BaseCORBABinding"
block|,
literal|"-o"
block|,
literal|"simple-binding_gen.idl"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simple-binding.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed in Idl Generation"
argument_list|,
name|noError
argument_list|,
name|exc
argument_list|)
expr_stmt|;
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
literal|"simple-binding_gen.idl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simple-binding_gen.idl should be generated"
argument_list|,
name|Files
operator|.
name|isReadable
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|line
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
literal|"Invalid Idl File Generated"
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// tests generating corba and idl in default wsdl and idl files
comment|// pass the temp directory to create the wsdl files.
specifier|public
name|void
name|testBindAndIDLGen
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-i"
block|,
literal|"BasePortType"
block|,
literal|"-b"
block|,
literal|"BaseOneCORBABinding"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simple-binding.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|noError
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|Path
name|path1
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
literal|"simple-binding-corba.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simple-binding-corba.wsdl should be generated"
argument_list|,
name|Files
operator|.
name|isReadable
argument_list|(
name|path1
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|path2
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
literal|"simple-binding.idl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"simple-binding.idl should be generated"
argument_list|,
name|Files
operator|.
name|isReadable
argument_list|(
name|path2
argument_list|)
argument_list|)
expr_stmt|;
name|WSDLToProcessor
name|proc
init|=
operator|new
name|WSDLToProcessor
argument_list|()
decl_stmt|;
try|try
block|{
name|proc
operator|.
name|parseWSDL
argument_list|(
name|path1
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Definition
name|model
init|=
name|proc
operator|.
name|getWSDLDefinition
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL Definition Should not be Null"
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|QName
name|bindingName
init|=
operator|new
name|QName
argument_list|(
literal|"http://schemas.apache.org/tests"
argument_list|,
literal|"BaseOneCORBABinding"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Binding Node not found in WSDL"
argument_list|,
name|model
operator|.
name|getBinding
argument_list|(
name|bindingName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"WSDLToIDL generated an invalid simple-binding-corba.wsdl"
argument_list|)
expr_stmt|;
block|}
name|String
name|line
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|path2
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Invalid Idl File Generated"
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoArgs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|StringBuilder
name|strBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|strBuf
operator|.
name|append
argument_list|(
literal|"Missing argument: wsdlurl\n\n"
argument_list|)
expr_stmt|;
name|strBuf
operator|.
name|append
argument_list|(
name|usage
argument_list|)
expr_stmt|;
name|checkStrings
argument_list|(
name|strBuf
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMissingRequiredFlags
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-i"
block|,
literal|" interfaceName"
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|StringBuilder
name|expected
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|expected
operator|.
name|append
argument_list|(
literal|"Missing argument: wsdlurl\n\n"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|append
argument_list|(
name|usage
argument_list|)
expr_stmt|;
name|checkStrings
argument_list|(
name|expected
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindingGenInvalidInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-corba"
block|,
literal|"-i"
block|,
literal|"TestInterface"
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Error : PortType TestInterface doesn't exist in WSDL."
decl_stmt|;
name|checkStrings
argument_list|(
name|expected
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBindingGenDuplicate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-i"
block|,
literal|"BasePortType"
block|,
literal|"-b"
block|,
literal|"BaseCORBABinding"
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simple-binding.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Error : Binding BaseCORBABinding already exists in WSDL."
decl_stmt|;
name|checkStrings
argument_list|(
name|expected
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIdlGenMissingBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-d"
block|,
name|output
operator|.
name|getAbsolutePath
argument_list|()
block|,
literal|"-idl"
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Error : Binding Name required for generating IDL"
decl_stmt|;
name|checkStrings
argument_list|(
name|expected
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIdlGenInvalidBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-d"
block|,
name|output
operator|.
name|getAbsolutePath
argument_list|()
block|,
literal|"-idl"
block|,
literal|"-b"
block|,
literal|"TestBinding"
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|int
name|exc
init|=
name|execute
argument_list|(
name|cmdArgs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToCORBA Failed"
argument_list|,
name|error
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Error : Binding TestBinding doesn't exist in WSDL."
decl_stmt|;
name|checkStrings
argument_list|(
name|expected
operator|.
name|getBytes
argument_list|()
argument_list|,
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMissingBindingName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|cmdArgs
init|=
block|{
literal|"-d"
block|,
name|output
operator|.
name|getAbsolutePath
argument_list|()
block|,
literal|"-i"
block|,
literal|"BasePortType"
block|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/simpleList.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDLToIDL should succeed even without Binding name. "
operator|+
literal|"Name used from creation of CORBA binding to generate IDL."
argument_list|,
name|noError
argument_list|,
name|execute
argument_list|(
name|cmdArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDetailOutput
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
literal|"-?"
block|}
decl_stmt|;
name|WSDLToIDL
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getStdOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testVersionOutput
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
literal|"-v"
block|}
decl_stmt|;
name|WSDLToIDL
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getStdOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testHelpOutput
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
literal|"-help"
block|}
decl_stmt|;
name|WSDLToIDL
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getStdOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

