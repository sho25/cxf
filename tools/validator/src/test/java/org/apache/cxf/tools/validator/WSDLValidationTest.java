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
name|validator
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
name|Enumeration
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
name|staxutils
operator|.
name|StaxUtils
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
name|ToolTestBase
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLValidationTest
extends|extends
name|ToolTestBase
block|{
annotation|@
name|Before
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateDefaultOpMessageNames
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/defaultOpMessageNames.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|this
operator|.
name|getStdOut
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Valid WSDL"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateUniqueBody
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/doc_lit_bare.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Non Unique Body Parts Error should be discovered: "
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Non unique body part"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateMixedStyle
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_mixed_style.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Mixed style. Error should have been discovered: "
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Mixed style, invalid WSDL"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateTypeElement
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_doc_lit_type.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Must refer to type element error should have been discovered: "
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"using the element attribute"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateAttribute
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_error_attribute.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"WSDLException (at /wsdl:definitions/wsdl:message[1]/wsdl:part): "
operator|+
literal|"faultCode=INVALID_WSDL: Encountered illegal extension attribute 'test'. "
operator|+
literal|"Extension attributes must be in a namespace other than WSDL's"
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Attribute error should be discovered: "
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateReferenceError
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_error_reference.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|String
name|error
init|=
name|getStdErr
argument_list|()
decl_stmt|;
if|if
condition|(
name|StaxUtils
operator|.
name|isWoodstox
argument_list|()
condition|)
block|{
comment|// sjsxp doesn't report locations.
name|assertTrue
argument_list|(
literal|"error message does not contain [147,3]. error message: "
operator|+
name|error
argument_list|,
name|error
operator|.
name|indexOf
argument_list|(
literal|"[147,3]"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|error
operator|.
name|indexOf
argument_list|(
literal|"Caused by {http://apache.org/hello_world_soap_http}"
operator|+
literal|"[binding:Greeter_SOAPBinding1] not exist."
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBug305872
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/bug305872/http.xsd"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Expected element '{http://schemas.xmlsoap.org/wsdl/}definitions'."
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Tools should check if this file is a wsdl file: "
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImportWsdlValidation
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_import.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Is not valid wsdl!: "
operator|+
name|getStdOut
argument_list|()
operator|+
literal|"\n"
operator|+
name|getStdErr
argument_list|()
argument_list|,
name|getStdOut
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Passed Validation"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImportSchemaValidation
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_schema_import.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Is not valid wsdl: "
operator|+
name|getStdOut
argument_list|()
argument_list|,
name|getStdOut
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Passed Validation"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSOAPHeadersInMultiOperations
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/cxf1793.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
argument_list|,
name|getStdOut
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Passed Validation : Valid WSDL"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBP2210
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/soapheader.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2210"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBPR2726
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/jms_test_R2726.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2726"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBPR2205
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/jms_test2.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2205"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBPR2203
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/header_rpc_lit.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdOut
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Passed Validation : Valid WSDL"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|args
operator|=
operator|new
name|String
index|[]
block|{
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/header_rpc_lit_2203_in.wsdl"
argument_list|)
block|}
expr_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"soapbind:body element(s), only to wsdl:part element(s)"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|args
operator|=
operator|new
name|String
index|[]
block|{
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/header_rpc_lit_2203_out.wsdl"
argument_list|)
block|}
expr_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"soapbind:body element(s), only to wsdl:part element(s)"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBPR2203ExcludeMIMEParts
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/cxf6488.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdOut
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Passed Validation : Valid WSDL"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSIBPR2209
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
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/hello_world_unbound_porttype_elements.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2209"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBPR2717
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/cxf996.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
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
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2717"
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2210"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-verbose"
block|,
name|getLocation
argument_list|(
literal|"/validator_wsdl/bp2717.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLValidator
operator|.
name|main
argument_list|(
name|args
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
name|getStdErr
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"WSI-BP-1.0 R2717"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getLocation
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|Exception
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|e
init|=
name|WSDLValidationTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
name|wsdlFile
argument_list|)
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|u
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|u
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
name|f
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
name|WSDLValidationTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

