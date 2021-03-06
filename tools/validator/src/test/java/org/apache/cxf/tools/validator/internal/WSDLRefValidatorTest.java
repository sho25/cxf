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
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|BusFactory
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|validator
operator|.
name|internal
operator|.
name|model
operator|.
name|XNode
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
name|wsdl
operator|.
name|WSDLManager
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
name|assertFalse
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLRefValidatorTest
block|{
specifier|private
name|Definition
name|getWSDL
parameter_list|(
name|String
name|wsdl
parameter_list|)
throws|throws
name|Exception
block|{
name|Bus
name|b
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
return|return
name|b
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|wsdl
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoService
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/b.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|validator
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|ValidationResult
name|results
init|=
name|validator
operator|.
name|getValidationResults
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDLImport1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/a.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|validator
operator|.
name|isValid
argument_list|()
expr_stmt|;
name|ValidationResult
name|results
init|=
name|validator
operator|.
name|getValidationResults
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|t
init|=
name|results
operator|.
name|getErrors
argument_list|()
operator|.
name|pop
argument_list|()
decl_stmt|;
name|String
name|text
init|=
literal|"{http://apache.org/hello_world/messages}[portType:GreeterA][operation:sayHi]"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|possibles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|//woodstox
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
literal|27
argument_list|,
literal|2
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//Sun java6 stax parser
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
literal|27
argument_list|,
literal|31
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//sjsxp
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|possibles
operator|.
name|contains
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDLImport2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/physicalpt.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validator
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"/wsdl:definitions[@targetNamespace='http://schemas.apache.org/yoko/idl/OptionsPT']"
operator|+
literal|"/wsdl:portType[@name='foo.bar']"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|xpath
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|XNode
name|node
range|:
name|validator
operator|.
name|vNodes
control|)
block|{
name|xpath
operator|.
name|add
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|xpath
operator|.
name|contains
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoTypeRef
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/NoTypeRef.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|validator
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|validator
operator|.
name|getValidationResults
argument_list|()
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"Part<header_info> in Message "
operator|+
literal|"<{http://apache.org/samples/headers}inHeaderRequest>"
operator|+
literal|" referenced Type<{http://apache.org/samples/headers}SOAPHeaderInfo> "
operator|+
literal|"can not be found in the schemas"
decl_stmt|;
name|String
name|t
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|!
name|validator
operator|.
name|getValidationResults
argument_list|()
operator|.
name|getErrors
argument_list|()
operator|.
name|empty
argument_list|()
condition|)
block|{
name|t
operator|=
name|validator
operator|.
name|getValidationResults
argument_list|()
operator|.
name|getErrors
argument_list|()
operator|.
name|pop
argument_list|()
expr_stmt|;
if|if
condition|(
name|expected
operator|.
name|equals
argument_list|(
name|t
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoBindingWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/nobinding.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|validator
operator|.
name|isValid
argument_list|()
expr_stmt|;
name|ValidationResult
name|results
init|=
name|validator
operator|.
name|getValidationResults
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLRefValidator
name|v
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|v
operator|.
name|setSuppressWarnings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|v
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogicalWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/logical.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|validator
operator|.
name|isValid
argument_list|()
expr_stmt|;
name|ValidationResult
name|results
init|=
name|validator
operator|.
name|getValidationResults
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|text
init|=
literal|"{http://schemas.apache.org/yoko/idl/OptionsPT}[message:getEmployee]"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|possibles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|//woodstox
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
literal|42
argument_list|,
literal|6
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//Sun java6 stax parser
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
literal|42
argument_list|,
literal|70
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//sjsxp
name|possibles
operator|.
name|add
argument_list|(
operator|new
name|Message
argument_list|(
literal|"FAILED_AT_POINT"
argument_list|,
name|WSDLRefValidator
operator|.
name|LOG
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
name|wsdl
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|text
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|possibles
operator|.
name|contains
argument_list|(
name|results
operator|.
name|getErrors
argument_list|()
operator|.
name|pop
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotAWsdl
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/c.xsd"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|validator
operator|.
name|isValid
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|expected
init|=
literal|"WSDLException (at /xs:schema): faultCode=INVALID_WSDL: "
operator|+
literal|"Expected element '{http://schemas.xmlsoap.org/wsdl/}definitions'."
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXSDAnyType
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/anytype.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|WSDLRefValidator
name|validator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|getWSDL
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validator
operator|.
name|isValid
argument_list|()
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
literal|"Valid wsdl, no exception should be thrown"
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

