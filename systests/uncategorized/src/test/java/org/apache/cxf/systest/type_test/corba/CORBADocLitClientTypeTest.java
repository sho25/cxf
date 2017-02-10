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
literal|"/wsdl_systest/type_test_corba/type_test_corba-corba.wsdl"
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
name|NOT_WORKING_TESTS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|RUN_TESTS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|String
name|notWorking
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"AnonEnumList"
block|,
literal|"AnonymousType"
block|,
literal|"AnyURIRestriction"
block|,
literal|"Base64BinaryRestriction"
block|,
literal|"ChoiceArray"
block|,
literal|"ChoiceOfChoice"
block|,
literal|"ChoiceOfSeq"
block|,
literal|"ChoiceWithAnyAttribute"
block|,
literal|"ChoiceWithGroupChoice"
block|,
literal|"ChoiceWithGroups"
block|,
literal|"ChoiceWithGroupSeq"
block|,
literal|"ChoiceWithSubstitutionGroup"
block|,
literal|"ChoiceWithSubstitutionGroupAbstract"
block|,
literal|"ChoiceWithSubstitutionGroupNil"
block|,
literal|"ComplexRestriction"
block|,
literal|"ComplexRestriction2"
block|,
literal|"ComplexRestriction3"
block|,
literal|"ComplexRestriction4"
block|,
literal|"ComplexRestriction5"
block|,
literal|"ComplexTypeWithAttributeGroup"
block|,
literal|"ComplexTypeWithAttributeGroup1"
block|,
literal|"ComplexTypeWithAttributes"
block|,
literal|"DateTime"
block|,
literal|"DerivedChoiceBaseArray"
block|,
literal|"DerivedChoiceBaseChoice"
block|,
literal|"DerivedChoiceBaseStruct"
block|,
literal|"DerivedNoContent"
block|,
literal|"DerivedStructBaseChoice"
block|,
literal|"DerivedStructBaseEmpty"
block|,
literal|"DerivedStructBaseStruct"
block|,
literal|"Document"
block|,
literal|"EmptyStruct"
block|,
literal|"ExtBase64Binary"
block|,
literal|"ExtColourEnum"
block|,
literal|"ExtendsSimpleContent"
block|,
literal|"ExtendsSimpleType"
block|,
literal|"GroupDirectlyInComplexType"
block|,
literal|"HexBinaryRestriction"
block|,
literal|"IDTypeAttribute"
block|,
literal|"InheritanceEmptyAllDerivedEmpty"
block|,
literal|"InheritanceNestedStruct"
block|,
literal|"InheritanceSimpleChoiceDerivedStruct"
block|,
literal|"InheritanceSimpleStructDerivedStruct"
block|,
literal|"InheritanceUnboundedArrayDerivedChoice"
block|,
literal|"MRecSeqA"
block|,
literal|"MRecSeqC"
block|,
literal|"NestedStruct"
block|,
literal|"NMTOKENS"
block|,
literal|"NumberList"
block|,
literal|"Occuri ngStruct2"
block|,
literal|"OccuringAll"
block|,
literal|"OccuringChoice"
block|,
literal|"OccuringChoice1"
block|,
literal|"OccuringChoice2"
block|,
literal|"OccuringChoiceWithAnyAttribute"
block|,
literal|"OccuringStruct"
block|,
literal|"OccuringStruct1"
block|,
literal|"OccuringStruct2"
block|,
literal|"OccuringStructWithAnyAttribute"
block|,
literal|"QName"
block|,
literal|"QNameList"
block|,
literal|"RecElType"
block|,
literal|"RecOuterType"
block|,
literal|"RecSeqB6918"
block|,
literal|"RecursiveStruct"
block|,
literal|"RecursiveStructArray"
block|,
literal|"RecursiveUnion"
block|,
literal|"RecursiveUnionData"
block|,
literal|"RestrictedAllBaseAll"
block|,
literal|"RestrictedChoiceBaseChoice"
block|,
literal|"RestrictedStructBaseStruct"
block|,
literal|"SequenceWithGroupChoice"
block|,
literal|"SequenceWithGroups"
block|,
literal|"SequenceWithGroupSeq"
block|,
literal|"SequenceWithOccuringGroup"
block|,
literal|"SimpleAll"
block|,
literal|"SimpleContent1"
block|,
literal|"SimpleContent2"
block|,
literal|"SimpleContent3"
block|,
literal|"SimpleContentExtWithAnyAttribute"
block|,
literal|"SimpleListRestriction2"
block|,
literal|"SimpleRestriction2"
block|,
literal|"SimpleRestriction3"
block|,
literal|"SimpleRestriction5"
block|,
literal|"SimpleRestriction6"
block|,
literal|"SimpleStruct"
block|,
literal|"SimpleUnionList"
block|,
literal|"StringI18N"
block|,
literal|"StringList"
block|,
literal|"StructWithAny"
block|,
literal|"StructWithAnyArray"
block|,
literal|"StructWithAnyAttribute"
block|,
literal|"StructWithAnyXsi"
block|,
literal|"StructWithInvalidAny"
block|,
literal|"StructWithInvalidAnyArray"
block|,
literal|"StructWithList"
block|,
literal|"StructWithMultipleSubstitutionGroups"
block|,
literal|"StructWithNillableChoice"
block|,
literal|"StructWithNillableStruct"
block|,
literal|"StructWithOccuringChoice"
block|,
literal|"StructWithOccuringStruct"
block|,
literal|"StructWithOccuringStruct2"
block|,
literal|"StructWithOptionals"
block|,
literal|"StructWithSubstitutionGroup"
block|,
literal|"StructWithSubstitutionGroupAbstract"
block|,
literal|"StructWithSubstitutionGroupNil"
block|,
literal|"StructWithUnion"
block|,
literal|"UnionSimpleContent"
block|,
literal|"UnionWithAnonEnum"
block|,
literal|"UnionWithAnonList"
block|,
literal|"UnionWithStringList"
block|,
literal|"UnionWithStringListRestriction"
block|,
literal|"UnsignedByte"
block|,         }
decl_stmt|;
name|NOT_WORKING_TESTS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|notWorking
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|notWorkingIBM
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"AnyURIEnum"
block|,
literal|"NMTokenEnum"
block|,
literal|"DecimalEnum"
block|,
literal|"StringEnum"
block|,
literal|"NumberEnum"
block|,
literal|"ColourEnum"
block|,
literal|"Base64Binary"
block|,
literal|"HexBinary"
block|,
literal|"Decimal"
block|,
literal|"UnsignedShort"
block|,
literal|"SimpleChoice"
block|,
literal|"EmptyChoice"
block|,
literal|"NestedArray"
block|,
literal|"CompoundArray"
block|,
literal|"UnboundedArray"
block|,
literal|"BoundedArray"
block|,
literal|"FixedArray"
block|,
literal|"AnonymousStruct"
block|,
literal|"StructWithNillables"
block|,
literal|"ChoiceWithBinary"
block|,
literal|"StructWithBinary"
block|,
literal|"MultipleOccursSequenceInSequence"
block|,
literal|"DerivedEmptyBaseEmptyChoice"
block|}
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"IBM"
argument_list|)
condition|)
block|{
name|NOT_WORKING_TESTS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|notWorkingIBM
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|deleteRefFile
parameter_list|()
throws|throws
name|Exception
block|{
comment|//System.out.println(NOT_WORKING_TESTS.size());
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
comment|//for (String s : RUN_TESTS) {
comment|//System.out.println(s);
comment|//}
comment|//System.out.println(RUN_TESTS.size());
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
name|NOT_WORKING_TESTS
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|boolean
name|b
init|=
name|super
operator|.
name|shouldRunTest
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
condition|)
block|{
name|RUN_TESTS
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|b
return|;
block|}
comment|//return true;
return|return
literal|false
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
block|,
block|{
name|Float
operator|.
name|NEGATIVE_INFINITY
block|,
name|Float
operator|.
name|POSITIVE_INFINITY
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
block|,
block|{
name|Double
operator|.
name|NEGATIVE_INFINITY
block|,
name|Double
operator|.
name|POSITIVE_INFINITY
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

