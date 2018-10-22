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
name|binding
operator|.
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
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
name|binding
operator|.
name|corba
operator|.
name|CorbaTypeMap
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaConstants
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TCKind
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TypeCode
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
name|CorbaUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
name|ORB
name|orb
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
name|java
operator|.
name|util
operator|.
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"yoko.orb.id"
argument_list|,
literal|"CXF-CORBA-Server-Binding"
argument_list|)
expr_stmt|;
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Do nothing.  Throw an Exception?
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBooleanTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"boolean"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_boolean
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCharTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"char"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_char
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWCharTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"wchar"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_wchar
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctetTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"octet"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_octet
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShortTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"short"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_short
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUShortTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"ushort"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_ushort
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLongTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"long"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_long
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testULongTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"ulong"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_ulong
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLongLongTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"longlong"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_longlong
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testULongLongTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"ulonglong"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_ulonglong
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFloatTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"float"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_float
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoubleTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"double"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_double
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"string"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_string
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWStringTypeCode
parameter_list|()
block|{
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"wstring"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getPrimitiveTypeCode
argument_list|(
name|orb
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tc
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tc
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
operator|==
name|TCKind
operator|.
name|_tk_wstring
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|CorbaUtils
operator|.
name|isPrimitiveIdlType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testErrorConditionNullTypeQName
parameter_list|()
block|{
try|try
block|{
name|CorbaUtils
operator|.
name|getTypeCode
argument_list|(
name|orb
argument_list|,
literal|null
argument_list|,
operator|new
name|CorbaTypeMap
argument_list|(
literal|"dud:namespace"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect exception on null type"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CorbaBindingException
name|expected
parameter_list|)
block|{
comment|//ignore
block|}
name|CorbaTypeMap
name|typeMap
init|=
operator|new
name|CorbaTypeMap
argument_list|(
literal|"dud:namespace"
argument_list|)
decl_stmt|;
name|QName
name|seen
init|=
operator|new
name|QName
argument_list|(
literal|"bla"
argument_list|,
literal|"Bla"
argument_list|)
decl_stmt|;
name|Stack
argument_list|<
name|QName
argument_list|>
name|seenTypes
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
name|seenTypes
operator|.
name|add
argument_list|(
name|seen
argument_list|)
expr_stmt|;
try|try
block|{
name|CorbaUtils
operator|.
name|getTypeCode
argument_list|(
name|orb
argument_list|,
literal|null
argument_list|,
name|typeMap
argument_list|,
name|seenTypes
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect exception on null type"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CorbaBindingException
name|expected
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"enclosed type is present"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|seen
operator|.
name|toString
argument_list|()
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|CorbaType
name|ctype
init|=
operator|new
name|CorbaType
argument_list|()
decl_stmt|;
try|try
block|{
name|CorbaUtils
operator|.
name|getTypeCode
argument_list|(
name|orb
argument_list|,
literal|null
argument_list|,
name|ctype
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect exception on null type"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CorbaBindingException
name|expected
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"enclosed corba type is present"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|ctype
operator|.
name|toString
argument_list|()
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

