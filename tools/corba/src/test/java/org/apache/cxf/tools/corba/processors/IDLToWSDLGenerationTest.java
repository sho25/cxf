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
operator|.
name|processors
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
name|BufferedWriter
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
name|InputStream
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|corba
operator|.
name|common
operator|.
name|ProcessorEnvironment
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
name|ToolCorbaConstants
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
name|idl
operator|.
name|IDLToWSDLProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|IDLToWSDLGenerationTest
extends|extends
name|ProcessorTestBase
block|{
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SCHEMA_IGNORE_ATTR
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"attributeFormDefault"
block|,
literal|"elementFormDefault"
block|,
literal|"form"
block|,
literal|"schemaLocation"
block|}
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{     }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{     }
specifier|public
name|void
name|testWSDLGeneration
parameter_list|(
name|String
name|sourceIdlFilename
parameter_list|,
name|String
name|expectedWsdlFilename
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|idl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|sourceIdlFilename
argument_list|)
decl_stmt|;
name|ProcessorEnvironment
name|env
init|=
operator|new
name|ProcessorEnvironment
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfg
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLFILE
argument_list|,
name|idl
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|IDLToWSDLProcessor
name|processor
init|=
operator|new
name|IDLToWSDLProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|out
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setOutputWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|InputStream
name|origStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|expectedWsdlFilename
argument_list|)
decl_stmt|;
name|InputStream
name|actualStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertWsdlEquals
argument_list|(
name|origStream
argument_list|,
name|actualStream
argument_list|,
name|DEFAULT_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelloWorldWSDLGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/HelloWorld.idl"
argument_list|,
literal|"/idl/expected_HelloWorld.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrimitivesGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/primitives.idl"
argument_list|,
literal|"/idl/expected_Primitives.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExceptionGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Exception.idl"
argument_list|,
literal|"/idl/expected_Exception.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStructGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Struct.idl"
argument_list|,
literal|"/idl/expected_Struct.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopedStructGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/scopedStruct.idl"
argument_list|,
literal|"/idl/expected_scopedStruct.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnewayGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Oneway.idl"
argument_list|,
literal|"/idl/expected_Oneway.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Const.idl"
argument_list|,
literal|"/idl/expected_Const.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnumGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Enum.idl"
argument_list|,
literal|"/idl/expected_Enum.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnionGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Union.idl"
argument_list|,
literal|"/idl/expected_Union.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixedGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Fixed.idl"
argument_list|,
literal|"/idl/expected_Fixed.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTypedefGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Typedef.idl"
argument_list|,
literal|"/idl/expected_Typedef.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/String.idl"
argument_list|,
literal|"/idl/expected_String.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAttributesGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Attributes.idl"
argument_list|,
literal|"/idl/expected_Attributes.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSequenceGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Sequence.idl"
argument_list|,
literal|"/idl/expected_Sequence.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArrayGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Array.idl"
argument_list|,
literal|"/idl/expected_Array.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnonarrayGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Anonarray.idl"
argument_list|,
literal|"/idl/expected_Anonarray.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnonsequenceGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Anonsequence.idl"
argument_list|,
literal|"/idl/expected_Anonsequence.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnonboundedsequenceGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Anonboundedsequence.idl"
argument_list|,
literal|"/idl/expected_Anonboundedsequence.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnonstringGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Anonstring.idl"
argument_list|,
literal|"/idl/expected_Anonstring.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleDeclaratorsGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/Declarators.idl"
argument_list|,
literal|"/idl/expected_Declarators.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectReferenceGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ObjectRef.idl"
argument_list|,
literal|"/idl/expected_ObjectRef.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopingOperationGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/scopingOperation.idl"
argument_list|,
literal|"/idl/expected_scopingOperation.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopingObjectRefGlobalGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/scopingObjectRefGlobal.idl"
argument_list|,
literal|"/idl/expected_scopingObjectRefGlobal.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopingObjectRefGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/scopingObjectRef.idl"
argument_list|,
literal|"/idl/expected_scopingObjectRef.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopingStringGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/scopedString.idl"
argument_list|,
literal|"/idl/expected_scopedString.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterface.idl"
argument_list|,
literal|"/idl/expected_ForwardInterface.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceParam
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceParam.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceParam.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceStructUnion
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceStructUnion.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceStructUnion.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceSequence
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceSequence.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceSequence.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceArray
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceArray.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceArray.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceAttributes.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceAttributes.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceExceptions
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceException.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceException.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardInterfaceTypedef
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardInterfaceTypedef.idl"
argument_list|,
literal|"/idl/expected_ForwardInterfaceTypedef.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardStruct
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardStruct.idl"
argument_list|,
literal|"/idl/expected_ForwardStruct.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForwardUnion
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/ForwardUnion.idl"
argument_list|,
literal|"/idl/expected_ForwardUnion.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncludeGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/included.idl"
argument_list|,
literal|"/idl/expected_Included.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInterfaceInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|testWSDLGeneration
argument_list|(
literal|"/idl/inheritance.idl"
argument_list|,
literal|"/idl/expected_Inheritance.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDuplicateOperationNames
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests operations with the same name but in different scopes
name|testWSDLGeneration
argument_list|(
literal|"/idl/duplicateOpNames.idl"
argument_list|,
literal|"/idl/expected_duplicateOpNames.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstScopedNames
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests consts where their types are scoped names
name|testWSDLGeneration
argument_list|(
literal|"/idl/ConstScopename.idl"
argument_list|,
literal|"/idl/expected_ConstScopename.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTypedfOctet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests typedef sequence of octets.
name|testWSDLGeneration
argument_list|(
literal|"/idl/Octet.idl"
argument_list|,
literal|"/idl/expected_Octet.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRecursiveStructs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests for recursive structs
name|testWSDLGeneration
argument_list|(
literal|"/idl/RecursiveStruct.idl"
argument_list|,
literal|"/idl/expected_RecursiveStruct.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRecursiveUnions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests for recursive unions
name|testWSDLGeneration
argument_list|(
literal|"/idl/RecursiveUnion.idl"
argument_list|,
literal|"/idl/expected_RecursiveUnion.wsdl"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLogicalPhysicalSchemaGeneration
parameter_list|(
name|String
name|idlFilename
parameter_list|,
name|String
name|logicalName
parameter_list|,
name|String
name|physicalName
parameter_list|,
name|String
name|schemaFilename
parameter_list|,
name|String
name|defaultFilename
parameter_list|,
name|String
name|importName
parameter_list|,
name|String
name|defaultImportName
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|idl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|idlFilename
argument_list|)
decl_stmt|;
name|ProcessorEnvironment
name|env
init|=
operator|new
name|ProcessorEnvironment
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfg
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLFILE
argument_list|,
name|idl
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|logicalName
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_LOGICAL
argument_list|,
name|logicalName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|physicalName
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_PHYSICAL
argument_list|,
name|physicalName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaFilename
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_SCHEMA
argument_list|,
name|schemaFilename
argument_list|)
expr_stmt|;
block|}
name|env
operator|.
name|setParameters
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|IDLToWSDLProcessor
name|processor
init|=
operator|new
name|IDLToWSDLProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|outD
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setOutputWriter
argument_list|(
name|outD
argument_list|)
expr_stmt|;
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|outL
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|outP
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|outS
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
argument_list|()
decl_stmt|;
if|if
condition|(
name|logicalName
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setLogicalOutputWriter
argument_list|(
name|outL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|physicalName
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setPhysicalOutputWriter
argument_list|(
name|outP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaFilename
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setSchemaOutputWriter
argument_list|(
name|outS
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|String
name|userdir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
decl_stmt|;
name|String
name|sep
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|userdir
operator|+
name|sep
operator|+
name|importName
argument_list|)
decl_stmt|;
name|String
name|location
init|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|File
name|schemaFile
init|=
operator|new
name|File
argument_list|(
name|userdir
operator|+
name|sep
operator|+
name|schemaFilename
argument_list|)
decl_stmt|;
name|String
name|schemaLocation
init|=
name|schemaFile
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|File
name|defaultFile
init|=
operator|new
name|File
argument_list|(
name|userdir
operator|+
name|sep
operator|+
name|defaultImportName
argument_list|)
decl_stmt|;
name|String
name|defaultLocation
init|=
name|defaultFile
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|logicalName
operator|!=
literal|null
condition|)
block|{
name|testCompare
argument_list|(
name|logicalName
argument_list|,
name|outL
argument_list|,
name|schemaLocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|physicalName
operator|!=
literal|null
condition|)
block|{
name|testCompare
argument_list|(
name|physicalName
argument_list|,
name|outP
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaFilename
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|origSchemaStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/idl/"
operator|+
name|schemaFilename
argument_list|)
decl_stmt|;
name|InputStream
name|actualSchemaStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|outS
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertWsdlEquals
argument_list|(
name|origSchemaStream
argument_list|,
name|actualSchemaStream
argument_list|,
name|SCHEMA_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultFilename
operator|!=
literal|null
condition|)
block|{
name|testCompare
argument_list|(
name|defaultFilename
argument_list|,
name|outD
argument_list|,
name|defaultLocation
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|testCompare
parameter_list|(
name|String
name|filename
parameter_list|,
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
name|outWriter
parameter_list|,
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|origExpectedStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/idl/"
operator|+
name|filename
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|expectedByteStream
init|=
name|get
argument_list|(
name|origExpectedStream
argument_list|,
name|location
argument_list|)
decl_stmt|;
name|InputStream
name|actualPhysicalStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|outWriter
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|actualByteStream
init|=
name|get
argument_list|(
name|actualPhysicalStream
argument_list|,
name|location
argument_list|)
decl_stmt|;
name|assertWsdlEquals
argument_list|(
name|expectedByteStream
argument_list|,
name|actualByteStream
argument_list|,
name|SCHEMA_IGNORE_ATTR
argument_list|,
name|DEFAULT_IGNORE_TAG
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|ByteArrayInputStream
name|get
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|BufferedWriter
name|bw
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|bos
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|br
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
literal|null
operator|!=
name|line
condition|)
block|{
comment|// replace line if necessary
name|String
name|modifiedLine
init|=
name|line
decl_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|modifiedLine
operator|=
name|line
operator|.
name|replace
argument_list|(
literal|"@LOCATION@"
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
name|bw
operator|.
name|write
argument_list|(
name|modifiedLine
argument_list|)
expr_stmt|;
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|bw
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSchemaOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests if -T option is only passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionsSchema.idl"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"expected_Schema.xsd"
argument_list|,
literal|"expected_OptionsSchema.wsdl"
argument_list|,
literal|"expected_Schema.xsd"
argument_list|,
literal|"expected_Schema.xsd"
argument_list|)
expr_stmt|;
block|}
comment|// default files generated in user dir - no full path specified.
comment|// This tests if -P and -T options are passed.
annotation|@
name|Test
specifier|public
name|void
name|testPhysicalSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionsPT.idl"
argument_list|,
literal|null
argument_list|,
literal|"expected_PhysicalPT.wsdl"
argument_list|,
literal|"expected_SchemaPT.xsd"
argument_list|,
literal|"expected_OptionsPT.wsdl"
argument_list|,
literal|"OptionsPT.wsdl"
argument_list|,
literal|"expected_SchemaPT.xsd"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogicalSchema
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests -L and -T options are passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionsLT.idl"
argument_list|,
literal|"expected_LogicalLT.wsdl"
argument_list|,
literal|null
argument_list|,
literal|"expected_SchemaLT.xsd"
argument_list|,
literal|"expected_OptionsLT.wsdl"
argument_list|,
literal|"OptionsLT.wsdl"
argument_list|,
literal|"expected_LogicalLT.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogicalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests if only -L option is passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionL.idl"
argument_list|,
literal|"expected_Logical.wsdl"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"expected_OptionL.wsdl"
argument_list|,
literal|"expected_Logical.wsdl"
argument_list|,
literal|"expected_Logical.wsdl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogicalPhysical
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests if -L and -P options are passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionsLP.idl"
argument_list|,
literal|"expected_LogicalLP.wsdl"
argument_list|,
literal|"expected_PhysicalLP.wsdl"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"expected_LogicalLP.wsdl"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhysicalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests if -P option is only passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionP.idl"
argument_list|,
literal|null
argument_list|,
literal|"expected_Physical.wsdl"
argument_list|,
literal|null
argument_list|,
literal|"expected_OptionP.wsdl"
argument_list|,
literal|"OptionP.wsdl"
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogicalPyhsicalSchema
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This tests if -L, -P and -T options are passed.
name|testLogicalPhysicalSchemaGeneration
argument_list|(
literal|"/idl/OptionsLPT.idl"
argument_list|,
literal|"expected_LogicalLPT.wsdl"
argument_list|,
literal|"expected_PhysicalLPT.wsdl"
argument_list|,
literal|"expected_SchemaLPT.xsd"
argument_list|,
literal|null
argument_list|,
literal|"expected_LogicalLPT.wsdl"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodingGeneration
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|sourceIdlFilename
init|=
literal|"/idl/Enum.idl"
decl_stmt|;
name|URL
name|idl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|sourceIdlFilename
argument_list|)
decl_stmt|;
name|ProcessorEnvironment
name|env
init|=
operator|new
name|ProcessorEnvironment
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cfg
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_IDLFILE
argument_list|,
name|idl
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|put
argument_list|(
name|ToolCorbaConstants
operator|.
name|CFG_WSDL_ENCODING
argument_list|,
literal|"UTF-16"
argument_list|)
expr_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|IDLToWSDLProcessor
name|processor
init|=
operator|new
name|IDLToWSDLProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|Writer
name|out
init|=
name|processor
operator|.
name|getOutputWriter
argument_list|(
literal|"Enum.wsdl"
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|OutputStreamWriter
condition|)
block|{
name|OutputStreamWriter
name|writer
init|=
operator|(
name|OutputStreamWriter
operator|)
name|out
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Encoding should be UTF-16"
argument_list|,
name|writer
operator|.
name|getEncoding
argument_list|()
argument_list|,
literal|"UTF-16"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
operator|new
name|File
argument_list|(
literal|"Enum.wsdl"
argument_list|)
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

