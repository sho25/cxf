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
name|types
package|;
end_package

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
name|MemberType
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
name|Struct
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
name|StructMember
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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

begin_comment
comment|// Since the exception handler is essentially the same as the struct handler (just included in case
end_comment

begin_comment
comment|// structs and exceptions diverge at a later date), this test should cover both.
end_comment

begin_class
specifier|public
class|class
name|CorbaStructHandlerTest
block|{
specifier|private
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
name|testCorbaStructHandler
parameter_list|()
block|{
name|Struct
name|structType
init|=
operator|new
name|Struct
argument_list|()
decl_stmt|;
name|structType
operator|.
name|setName
argument_list|(
literal|"TestStruct"
argument_list|)
expr_stmt|;
name|structType
operator|.
name|setRepositoryID
argument_list|(
literal|"IDL:TestStruct:1.0"
argument_list|)
expr_stmt|;
name|MemberType
name|member0
init|=
operator|new
name|MemberType
argument_list|()
decl_stmt|;
name|member0
operator|.
name|setIdltype
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_LONG
argument_list|)
expr_stmt|;
name|member0
operator|.
name|setName
argument_list|(
literal|"member0"
argument_list|)
expr_stmt|;
name|MemberType
name|member1
init|=
operator|new
name|MemberType
argument_list|()
decl_stmt|;
name|member1
operator|.
name|setIdltype
argument_list|(
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|member1
operator|.
name|setName
argument_list|(
literal|"member1"
argument_list|)
expr_stmt|;
name|QName
name|structName
init|=
operator|new
name|QName
argument_list|(
literal|"TestStruct"
argument_list|)
decl_stmt|;
name|QName
name|structIdlType
init|=
operator|new
name|QName
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
literal|"testStruct"
argument_list|,
name|CorbaConstants
operator|.
name|NP_WSDL_CORBA
argument_list|)
decl_stmt|;
name|StructMember
index|[]
name|structMembers
init|=
operator|new
name|StructMember
index|[
literal|2
index|]
decl_stmt|;
name|structMembers
index|[
literal|0
index|]
operator|=
operator|new
name|StructMember
argument_list|(
literal|"member0"
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_long
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|structMembers
index|[
literal|1
index|]
operator|=
operator|new
name|StructMember
argument_list|(
literal|"member1"
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_string
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|TypeCode
name|structTC
init|=
name|orb
operator|.
name|create_struct_tc
argument_list|(
name|structType
operator|.
name|getRepositoryID
argument_list|()
argument_list|,
name|structType
operator|.
name|getName
argument_list|()
argument_list|,
name|structMembers
argument_list|)
decl_stmt|;
name|CorbaStructHandler
name|obj
init|=
operator|new
name|CorbaStructHandler
argument_list|(
name|structName
argument_list|,
name|structIdlType
argument_list|,
name|structTC
argument_list|,
name|structType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|CorbaPrimitiveHandler
name|objMember0
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
name|member0
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|member0
operator|.
name|getIdltype
argument_list|()
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_long
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objMember0
argument_list|)
expr_stmt|;
name|obj
operator|.
name|addMember
argument_list|(
name|objMember0
argument_list|)
expr_stmt|;
name|CorbaPrimitiveHandler
name|objMember1
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
name|member1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|member1
operator|.
name|getIdltype
argument_list|()
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_string
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objMember1
argument_list|)
expr_stmt|;
name|obj
operator|.
name|addMember
argument_list|(
name|objMember1
argument_list|)
expr_stmt|;
name|int
name|memberSize
init|=
name|obj
operator|.
name|getMembers
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|memberSize
operator|==
literal|2
argument_list|)
expr_stmt|;
name|QName
name|nameResult
init|=
name|obj
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|structName
operator|.
name|equals
argument_list|(
name|nameResult
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|idlTypeResult
init|=
name|obj
operator|.
name|getIdlType
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|structIdlType
operator|.
name|equals
argument_list|(
name|idlTypeResult
argument_list|)
argument_list|)
expr_stmt|;
name|CorbaObjectHandler
name|member0Result
init|=
name|obj
operator|.
name|getMemberByName
argument_list|(
literal|"member0"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|member0Result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|member0Result
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|objMember0
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CorbaObjectHandler
name|member1Result
init|=
name|obj
operator|.
name|getMember
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|member1Result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|member1Result
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|objMember1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

