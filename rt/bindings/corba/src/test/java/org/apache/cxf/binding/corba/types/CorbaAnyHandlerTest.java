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
name|omg
operator|.
name|CORBA
operator|.
name|Any
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

begin_class
specifier|public
class|class
name|CorbaAnyHandlerTest
extends|extends
name|Assert
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
name|testCorbaAnyHandler
parameter_list|()
block|{
name|Any
name|a
init|=
name|orb
operator|.
name|create_any
argument_list|()
decl_stmt|;
name|a
operator|.
name|insert_string
argument_list|(
literal|"TestMessage"
argument_list|)
expr_stmt|;
name|QName
name|anyName
init|=
operator|new
name|QName
argument_list|(
literal|"AnyHandlerName"
argument_list|)
decl_stmt|;
name|QName
name|anyIdlType
init|=
name|CorbaConstants
operator|.
name|NT_CORBA_ANY
decl_stmt|;
name|TypeCode
name|anyTC
init|=
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|from_int
argument_list|(
name|TCKind
operator|.
name|_tk_any
argument_list|)
argument_list|)
decl_stmt|;
name|CorbaTypeMap
name|tm
init|=
operator|new
name|CorbaTypeMap
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|)
decl_stmt|;
name|CorbaAnyHandler
name|anyHandler
init|=
operator|new
name|CorbaAnyHandler
argument_list|(
name|anyName
argument_list|,
name|anyIdlType
argument_list|,
name|anyTC
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|anyHandler
operator|.
name|setTypeMap
argument_list|(
name|tm
argument_list|)
expr_stmt|;
comment|// Test the get/set value methods
name|anyHandler
operator|.
name|setValue
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|Any
name|resultAny
init|=
name|anyHandler
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultAny
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|resultAny
operator|.
name|extract_string
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestMessage"
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// Test get/set CorbaTypeMap methods
name|CorbaTypeMap
name|resultTM
init|=
name|anyHandler
operator|.
name|getTypeMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|resultTM
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

