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
name|helpers
operator|.
name|JavaUtils
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
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AnonEnumList"
argument_list|,
literal|"AnonymousType"
argument_list|,
literal|"AnyURIRestriction"
argument_list|,
literal|"Base64BinaryRestriction"
argument_list|,
literal|"ChoiceArray"
argument_list|,
literal|"ChoiceOfChoice"
argument_list|,
literal|"ChoiceOfSeq"
argument_list|,
literal|"ChoiceWithAnyAttribute"
argument_list|,
literal|"ChoiceWithGroupChoice"
argument_list|,
literal|"ChoiceWithGroups"
argument_list|,
literal|"ChoiceWithGroupSeq"
argument_list|,
literal|"ChoiceWithSubstitutionGroup"
argument_list|,
literal|"ChoiceWithSubstitutionGroupAbstract"
argument_list|,
literal|"ChoiceWithSubstitutionGroupNil"
argument_list|,
literal|"ComplexArray"
argument_list|,
literal|"ComplexChoice"
argument_list|,
literal|"ComplexStruct"
argument_list|,
literal|"ComplexRestriction"
argument_list|,
literal|"ComplexRestriction2"
argument_list|,
literal|"ComplexRestriction3"
argument_list|,
literal|"ComplexRestriction4"
argument_list|,
literal|"ComplexRestriction5"
argument_list|,
literal|"ComplexTypeWithAttributeGroup"
argument_list|,
literal|"ComplexTypeWithAttributeGroup1"
argument_list|,
literal|"ComplexTypeWithAttributes"
argument_list|,
literal|"DateTime"
argument_list|,
literal|"DerivedAllBaseAll"
argument_list|,
literal|"DerivedAllBaseChoice"
argument_list|,
literal|"DerivedAllBaseStruct"
argument_list|,
literal|"DerivedChoiceBaseAll"
argument_list|,
literal|"DerivedChoiceBaseArray"
argument_list|,
literal|"DerivedChoiceBaseChoice"
argument_list|,
literal|"DerivedChoiceBaseComplex"
argument_list|,
literal|"DerivedChoiceBaseStruct"
argument_list|,
literal|"DerivedNoContent"
argument_list|,
literal|"DerivedStructBaseChoice"
argument_list|,
literal|"DerivedStructBaseEmpty"
argument_list|,
literal|"DerivedStructBaseStruct"
argument_list|,
literal|"Document"
argument_list|,
literal|"EmptyStruct"
argument_list|,
literal|"ExtBase64Binary"
argument_list|,
literal|"ExtColourEnum"
argument_list|,
literal|"ExtendsSimpleContent"
argument_list|,
literal|"ExtendsSimpleType"
argument_list|,
literal|"GroupDirectlyInComplexType"
argument_list|,
literal|"HexBinaryRestriction"
argument_list|,
literal|"IDTypeAttribute"
argument_list|,
literal|"InheritanceEmptyAllDerivedEmpty"
argument_list|,
literal|"InheritanceNestedStruct"
argument_list|,
literal|"InheritanceSimpleChoiceDerivedStruct"
argument_list|,
literal|"InheritanceSimpleStructDerivedStruct"
argument_list|,
literal|"InheritanceUnboundedArrayDerivedChoice"
argument_list|,
literal|"MRecSeqA"
argument_list|,
literal|"MRecSeqC"
argument_list|,
literal|"NestedStruct"
argument_list|,
literal|"NMTOKENS"
argument_list|,
literal|"NumberList"
argument_list|,
literal|"Occuri ngStruct2"
argument_list|,
literal|"OccuringAll"
argument_list|,
literal|"OccuringChoice"
argument_list|,
literal|"OccuringChoice1"
argument_list|,
literal|"OccuringChoice2"
argument_list|,
literal|"OccuringChoiceWithAnyAttribute"
argument_list|,
literal|"OccuringStruct"
argument_list|,
literal|"OccuringStruct1"
argument_list|,
literal|"OccuringStruct2"
argument_list|,
literal|"OccuringStructWithAnyAttribute"
argument_list|,
literal|"QName"
argument_list|,
literal|"QNameList"
argument_list|,
literal|"RecElType"
argument_list|,
literal|"RecOuterType"
argument_list|,
literal|"RecSeqB6918"
argument_list|,
literal|"RecursiveStruct"
argument_list|,
literal|"RecursiveStructArray"
argument_list|,
literal|"RecursiveUnion"
argument_list|,
literal|"RecursiveUnionData"
argument_list|,
literal|"RestrictedAllBaseAll"
argument_list|,
literal|"RestrictedChoiceBaseChoice"
argument_list|,
literal|"RestrictedStructBaseStruct"
argument_list|,
literal|"SequenceWithGroupChoice"
argument_list|,
literal|"SequenceWithGroups"
argument_list|,
literal|"SequenceWithGroupSeq"
argument_list|,
literal|"SequenceWithOccuringGroup"
argument_list|,
literal|"SimpleAll"
argument_list|,
literal|"SimpleContent1"
argument_list|,
literal|"SimpleContent2"
argument_list|,
literal|"SimpleContent3"
argument_list|,
literal|"SimpleContentExtWithAnyAttribute"
argument_list|,
literal|"SimpleListRestriction2"
argument_list|,
literal|"SimpleRestriction2"
argument_list|,
literal|"SimpleRestriction3"
argument_list|,
literal|"SimpleRestriction5"
argument_list|,
literal|"SimpleRestriction6"
argument_list|,
literal|"SimpleStruct"
argument_list|,
literal|"SimpleUnionList"
argument_list|,
literal|"StringI18N"
argument_list|,
literal|"StringList"
argument_list|,
literal|"StructWithAny"
argument_list|,
literal|"StructWithAnyArray"
argument_list|,
literal|"StructWithAnyArrayLax"
argument_list|,
literal|"StructWithAnyArrayLaxComplex"
argument_list|,
literal|"StructWithAnyAttribute"
argument_list|,
literal|"StructWithAnyStrict"
argument_list|,
literal|"StructWithAnyStrictComplex"
argument_list|,
literal|"StructWithAnyXsi"
argument_list|,
literal|"StructWithInvalidAny"
argument_list|,
literal|"StructWithInvalidAnyArray"
argument_list|,
literal|"StructWithList"
argument_list|,
literal|"StructWithMultipleSubstitutionGroups"
argument_list|,
literal|"StructWithNillableChoice"
argument_list|,
literal|"StructWithNillableStruct"
argument_list|,
literal|"StructWithOccuringChoice"
argument_list|,
literal|"StructWithOccuringStruct"
argument_list|,
literal|"StructWithOccuringStruct2"
argument_list|,
literal|"StructWithOptionals"
argument_list|,
literal|"StructWithSubstitutionGroup"
argument_list|,
literal|"StructWithSubstitutionGroupAbstract"
argument_list|,
literal|"StructWithSubstitutionGroupNil"
argument_list|,
literal|"StructWithUnion"
argument_list|,
literal|"UnionSimpleContent"
argument_list|,
literal|"UnionWithAnonEnum"
argument_list|,
literal|"UnionWithAnonList"
argument_list|,
literal|"UnionWithStringList"
argument_list|,
literal|"UnionWithStringListRestriction"
argument_list|,
literal|"UnsignedByte"
argument_list|)
argument_list|)
decl_stmt|;
static|static
block|{
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
literal|"AnyURIEnum"
argument_list|,
literal|"NMTokenEnum"
argument_list|,
literal|"DecimalEnum"
argument_list|,
literal|"StringEnum"
argument_list|,
literal|"NumberEnum"
argument_list|,
literal|"ColourEnum"
argument_list|,
literal|"Base64Binary"
argument_list|,
literal|"HexBinary"
argument_list|,
literal|"Decimal"
argument_list|,
literal|"UnsignedShort"
argument_list|,
literal|"SimpleChoice"
argument_list|,
literal|"EmptyChoice"
argument_list|,
literal|"NestedArray"
argument_list|,
literal|"CompoundArray"
argument_list|,
literal|"UnboundedArray"
argument_list|,
literal|"BoundedArray"
argument_list|,
literal|"FixedArray"
argument_list|,
literal|"AnonymousStruct"
argument_list|,
literal|"StructWithNillables"
argument_list|,
literal|"ChoiceWithBinary"
argument_list|,
literal|"StructWithBinary"
argument_list|,
literal|"MultipleOccursSequenceInSequence"
argument_list|,
literal|"DerivedEmptyBaseEmptyChoice"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|JavaUtils
operator|.
name|isJava9Compatible
argument_list|()
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
literal|"SimpleRestriction"
argument_list|,
literal|"SimpleRestriction4"
argument_list|,
literal|"AnyURIEnum"
argument_list|,
literal|"DecimalEnum"
argument_list|,
literal|"NumberEnum"
argument_list|,
literal|"StringEnum"
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
return|return
name|super
operator|.
name|shouldRunTest
argument_list|(
name|name
argument_list|)
return|;
block|}
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

