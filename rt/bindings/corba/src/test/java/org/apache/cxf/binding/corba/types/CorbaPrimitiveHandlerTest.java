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
name|CorbaPrimitiveHandlerTest
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
name|testCreateCorbaBoolean
parameter_list|()
block|{
name|Boolean
name|val
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"boolean"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_BOOLEAN
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_boolean
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Boolean
argument_list|)
expr_stmt|;
name|Boolean
name|boolResult
init|=
operator|(
name|Boolean
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|boolResult
operator|.
name|booleanValue
argument_list|()
operator|==
name|val
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaChararacter
parameter_list|()
block|{
name|Character
name|val
init|=
operator|new
name|Character
argument_list|(
literal|'c'
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"char"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_CHAR
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_char
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
comment|//CXF corba maps the XML char type to a Byte so we need to provide the string data as a Byte value
name|Byte
name|byteValue
init|=
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
name|val
operator|.
name|charValue
argument_list|()
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|byteValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|Byte
name|byteResult
init|=
operator|new
name|Byte
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|byteResult
operator|.
name|byteValue
argument_list|()
operator|==
name|byteValue
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// However, internally, we also hold the data as a character to make it easier to marshal the data
comment|// for CORBA.
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Character
argument_list|)
expr_stmt|;
name|Character
name|charResult
init|=
operator|(
name|Character
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|charResult
operator|.
name|charValue
argument_list|()
operator|==
name|val
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaWChararacter
parameter_list|()
block|{
name|Character
name|val
init|=
operator|new
name|Character
argument_list|(
literal|'w'
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"wchar"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_WCHAR
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_wchar
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
literal|"w"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|charValue
argument_list|()
operator|==
name|result
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Character
argument_list|)
expr_stmt|;
name|Character
name|charResult
init|=
operator|(
name|Character
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|charResult
operator|.
name|charValue
argument_list|()
operator|==
name|val
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaOctet
parameter_list|()
block|{
name|Byte
name|val
init|=
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|100
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"octet"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_OCTET
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_octet
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Byte
argument_list|)
expr_stmt|;
name|Byte
name|byteResult
init|=
operator|(
name|Byte
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|byteResult
operator|.
name|byteValue
argument_list|()
operator|==
name|val
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaShort
parameter_list|()
block|{
name|Short
name|val
init|=
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|1234
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"short"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_SHORT
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_short
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Short
argument_list|)
expr_stmt|;
name|Short
name|shortResult
init|=
operator|(
name|Short
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|shortResult
operator|.
name|shortValue
argument_list|()
operator|==
name|val
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaUShort
parameter_list|()
block|{
name|Short
name|val
init|=
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|4321
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"ushort"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_USHORT
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_ushort
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Short
argument_list|)
expr_stmt|;
name|Short
name|shortResult
init|=
operator|(
name|Short
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|shortResult
operator|.
name|shortValue
argument_list|()
operator|==
name|val
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaLong
parameter_list|()
block|{
name|Integer
name|val
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|123456
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"long"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONG
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
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|Integer
name|longResult
init|=
operator|(
name|Integer
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|longResult
operator|.
name|intValue
argument_list|()
operator|==
name|val
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaULong
parameter_list|()
block|{
name|Integer
name|val
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|654321
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"ulong"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONG
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_ulong
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|Integer
name|ulongResult
init|=
operator|(
name|Integer
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|ulongResult
operator|.
name|intValue
argument_list|()
operator|==
name|val
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaLongLong
parameter_list|()
block|{
name|Long
name|val
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|123456789
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"longlong"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONGLONG
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_longlong
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|Long
name|longlongResult
init|=
operator|(
name|Long
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|longlongResult
operator|.
name|longValue
argument_list|()
operator|==
name|val
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaULongLong
parameter_list|()
block|{
name|Long
name|val
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|987654321
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"ulonglong"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONGLONG
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_ulonglong
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|Long
name|longlongResult
init|=
operator|(
name|Long
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|longlongResult
operator|.
name|longValue
argument_list|()
operator|==
name|val
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaFloat
parameter_list|()
block|{
name|Float
name|val
init|=
operator|new
name|Float
argument_list|(
literal|1234.56
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"float"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_FLOAT
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_float
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Float
argument_list|)
expr_stmt|;
name|Float
name|floatResult
init|=
operator|(
name|Float
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|floatResult
operator|.
name|floatValue
argument_list|()
operator|==
name|val
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaDouble
parameter_list|()
block|{
name|Double
name|val
init|=
name|Double
operator|.
name|valueOf
argument_list|(
literal|123456.789
argument_list|)
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"double"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_DOUBLE
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_double
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|Double
argument_list|)
expr_stmt|;
name|Double
name|doubleResult
init|=
operator|(
name|Double
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|doubleResult
operator|.
name|doubleValue
argument_list|()
operator|==
name|val
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaString
parameter_list|()
block|{
name|String
name|val
init|=
literal|"Test String"
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"string"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
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
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|String
name|stringResult
init|=
operator|(
name|String
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|stringResult
operator|.
name|equals
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateCorbaWString
parameter_list|()
block|{
name|String
name|val
init|=
literal|"Test Wide String"
decl_stmt|;
name|CorbaPrimitiveHandler
name|obj
init|=
operator|new
name|CorbaPrimitiveHandler
argument_list|(
operator|new
name|QName
argument_list|(
literal|"wstring"
argument_list|)
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_WSTRING
argument_list|,
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_wstring
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValueFromData
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|obj
operator|.
name|getDataFromValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|val
operator|.
name|equals
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Object
name|resultObj
init|=
name|obj
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultObj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultObj
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|String
name|stringResult
init|=
operator|(
name|String
operator|)
name|resultObj
decl_stmt|;
name|assertTrue
argument_list|(
name|stringResult
operator|.
name|equals
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

