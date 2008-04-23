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
name|type_test
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
name|File
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
name|systest
operator|.
name|type_test
operator|.
name|AbstractTypeTestClient5
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|CORBADocLitClientTypeTest
extends|extends
name|AbstractTypeTestClient5
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|WSDL_PATH
init|=
literal|"/wsdl/type_test_corba/type_test_corba-corba.wsdl"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/type_test/doc"
argument_list|,
literal|"TypeTestCORBAService"
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/type_test/doc"
argument_list|,
literal|"TypeTestCORBAPort"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|WORKING_TESTS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|NOT_RUN_TESTS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|String
name|working
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"InheritanceEmptyAllDerivedEmpty"
block|,
literal|"DerivedEmptyBaseEmptyAll"
block|,
literal|"DerivedEmptyBaseEmptyChoice"
block|,
literal|"MultipleOccursSequenceInSequence"
block|,
literal|"StructWithBinary"
block|,
literal|"ChoiceWithBinary"
block|,
literal|"SimpleChoice"
block|,
literal|"UnboundedArray"
block|,
literal|"EmptyAll"
block|,
literal|"StructWithNillables"
block|,
literal|"AnonymousStruct"
block|,
literal|"FixedArray"
block|,
literal|"BoundedArray"
block|,
literal|"CompoundArray"
block|,
literal|"NestedArray"
block|,
literal|"EmptyChoice"
block|,
literal|"Name"
block|,
literal|"Void"
block|,
literal|"Oneway"
block|,
literal|"Byte"
block|,
literal|"Short"
block|,
literal|"UnsignedShort"
block|,
literal|"Int"
block|,
comment|//"UnsignedInt",
literal|"Long"
block|,
literal|"UnsignedLong"
block|,
literal|"Float"
block|,
literal|"Double"
block|,
comment|//"UnsignedByte",
literal|"Boolean"
block|,
literal|"String"
block|,
comment|//"StringI18N",
literal|"Date"
block|,
comment|//"DateTime",
literal|"Time"
block|,
literal|"GYear"
block|,
literal|"GYearMonth"
block|,
literal|"GMonth"
block|,
literal|"GMonthDay"
block|,
literal|"GDay"
block|,
literal|"Duration"
block|,
literal|"NormalizedString"
block|,
literal|"Token"
block|,
literal|"Language"
block|,
literal|"NMTOKEN"
block|,
comment|//"NMTOKENS",
literal|"NCName"
block|,
literal|"ID"
block|,
literal|"Decimal"
block|,
literal|"Integer"
block|,
literal|"PositiveInteger"
block|,
literal|"NonPositiveInteger"
block|,
literal|"NegativeInteger"
block|,
literal|"NonNegativeInteger"
block|,
literal|"HexBinary"
block|,
literal|"Base64Binary"
block|,
literal|"AnyURI"
block|,
literal|"ColourEnum"
block|,
literal|"NumberEnum"
block|,
literal|"StringEnum"
block|,
literal|"DecimalEnum"
block|,
literal|"NMTokenEnum"
block|,
literal|"AnyURIEnum"
block|,
literal|"SimpleRestriction"
block|,
literal|"SimpleRestriction4"
block|,         }
decl_stmt|;
name|WORKING_TESTS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|working
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|ok
init|=
name|launchServer
argument_list|(
name|CORBADocLitServerImpl
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"failed to launch server"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|initClient
argument_list|(
name|AbstractTypeTestClient5
operator|.
name|class
argument_list|,
name|SERVICE_NAME
argument_list|,
name|PORT_NAME
argument_list|,
name|WSDL_PATH
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|printNotRun
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./TypeTest.ref"
argument_list|)
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
comment|//for (String s : NOT_RUN_TESTS) {
comment|//System.out.println(s);
comment|//}
block|}
specifier|public
name|boolean
name|shouldRunTest
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
name|WORKING_TESTS
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|NOT_RUN_TESTS
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testA
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|protected
name|float
index|[]
index|[]
name|getTestFloatData
parameter_list|()
block|{
return|return
operator|new
name|float
index|[]
index|[]
block|{
block|{
literal|0.0f
block|,
literal|1.0f
block|}
block|,
block|{
operator|-
literal|1.0f
block|,
operator|(
name|float
operator|)
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|PI
block|}
block|,
block|{
operator|-
literal|100.0f
block|,
literal|100.0f
block|}
block|}
return|;
block|}
specifier|protected
name|double
index|[]
index|[]
name|getTestDoubleData
parameter_list|()
block|{
return|return
operator|new
name|double
index|[]
index|[]
block|{
block|{
literal|0.0f
block|,
literal|1.0f
block|}
block|,
block|{
operator|-
literal|1
block|,
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|PI
block|}
block|,
block|{
operator|-
literal|100.0
block|,
literal|100.0
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

