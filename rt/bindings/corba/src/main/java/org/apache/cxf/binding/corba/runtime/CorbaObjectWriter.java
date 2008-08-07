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
name|runtime
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|types
operator|.
name|CorbaAnyHandler
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
name|types
operator|.
name|CorbaArrayHandler
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
name|types
operator|.
name|CorbaEnumHandler
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
name|types
operator|.
name|CorbaExceptionHandler
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
name|types
operator|.
name|CorbaFixedHandler
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
name|types
operator|.
name|CorbaObjectHandler
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
name|types
operator|.
name|CorbaObjectReferenceHandler
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
name|types
operator|.
name|CorbaOctetSequenceHandler
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
name|types
operator|.
name|CorbaPrimitiveHandler
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
name|types
operator|.
name|CorbaSequenceHandler
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
name|types
operator|.
name|CorbaStructHandler
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
name|types
operator|.
name|CorbaUnionHandler
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
name|Enum
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
name|Enumerator
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
name|Exception
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
name|Union
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
name|Unionbranch
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
name|portable
operator|.
name|OutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaObjectWriter
block|{
specifier|private
name|OutputStream
name|stream
decl_stmt|;
specifier|public
name|CorbaObjectWriter
parameter_list|(
name|OutputStream
name|outStream
parameter_list|)
block|{
name|stream
operator|=
name|outStream
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
block|{
assert|assert
name|obj
operator|!=
literal|null
assert|;
switch|switch
condition|(
name|obj
operator|.
name|getTypeCode
argument_list|()
operator|.
name|kind
argument_list|()
operator|.
name|value
argument_list|()
condition|)
block|{
case|case
name|TCKind
operator|.
name|_tk_boolean
case|:
name|this
operator|.
name|writeBoolean
argument_list|(
call|(
name|Boolean
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_char
case|:
name|this
operator|.
name|writeChar
argument_list|(
call|(
name|Character
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wchar
case|:
name|this
operator|.
name|writeWChar
argument_list|(
call|(
name|Character
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_octet
case|:
name|this
operator|.
name|writeOctet
argument_list|(
call|(
name|Byte
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_short
case|:
name|this
operator|.
name|writeShort
argument_list|(
call|(
name|Short
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ushort
case|:
name|this
operator|.
name|writeUShort
argument_list|(
call|(
name|Integer
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_long
case|:
name|this
operator|.
name|writeLong
argument_list|(
call|(
name|Integer
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulong
case|:
name|this
operator|.
name|writeULong
argument_list|(
call|(
name|Long
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_longlong
case|:
name|this
operator|.
name|writeLongLong
argument_list|(
call|(
name|Long
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulonglong
case|:
name|this
operator|.
name|writeULongLong
argument_list|(
call|(
name|BigInteger
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_float
case|:
name|this
operator|.
name|writeFloat
argument_list|(
call|(
name|Float
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_double
case|:
name|this
operator|.
name|writeDouble
argument_list|(
call|(
name|Double
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_string
case|:
name|this
operator|.
name|writeString
argument_list|(
call|(
name|String
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wstring
case|:
name|this
operator|.
name|writeWString
argument_list|(
call|(
name|String
call|)
argument_list|(
operator|(
name|CorbaPrimitiveHandler
operator|)
name|obj
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_any
case|:
name|this
operator|.
name|writeAny
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
comment|// Now for the complex types
case|case
name|TCKind
operator|.
name|_tk_array
case|:
name|this
operator|.
name|writeArray
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_sequence
case|:
name|this
operator|.
name|writeSequence
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_struct
case|:
name|this
operator|.
name|writeStruct
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_enum
case|:
name|this
operator|.
name|writeEnum
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_except
case|:
name|this
operator|.
name|writeException
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_fixed
case|:
name|this
operator|.
name|writeFixed
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_union
case|:
name|this
operator|.
name|writeUnion
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_objref
case|:
name|this
operator|.
name|writeObjectReference
argument_list|(
name|obj
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// TODO: Provide Implementation. Do we throw an exception.
block|}
block|}
comment|// -- primitive types --
specifier|public
name|void
name|writeBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_boolean
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_boolean
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeChar
parameter_list|(
name|Character
name|c
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_char
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_char
argument_list|(
name|c
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeWChar
parameter_list|(
name|Character
name|c
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_wchar
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_wchar
argument_list|(
name|c
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeOctet
parameter_list|(
name|Byte
name|b
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_octet
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_octet
argument_list|(
name|b
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeShort
parameter_list|(
name|Short
name|s
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_short
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_short
argument_list|(
name|s
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeUShort
parameter_list|(
name|Integer
name|s
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_ushort
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_ushort
argument_list|(
name|s
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeLong
parameter_list|(
name|Integer
name|l
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_long
argument_list|(
operator|(
name|int
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_long
argument_list|(
name|l
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeULong
parameter_list|(
name|Long
name|l
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_ulong
argument_list|(
operator|(
name|int
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_ulong
argument_list|(
name|l
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeLongLong
parameter_list|(
name|Long
name|l
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_longlong
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_longlong
argument_list|(
name|l
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeULongLong
parameter_list|(
name|java
operator|.
name|math
operator|.
name|BigInteger
name|l
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_ulonglong
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_ulonglong
argument_list|(
name|l
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeFloat
parameter_list|(
name|Float
name|f
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_float
argument_list|(
operator|(
name|float
operator|)
literal|0.0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_float
argument_list|(
name|f
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeDouble
parameter_list|(
name|Double
name|d
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_double
argument_list|(
operator|(
name|double
operator|)
literal|0.0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_double
argument_list|(
name|d
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeString
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_string
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_string
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeWString
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|stream
operator|.
name|write_wstring
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|.
name|write_wstring
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeAny
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaAnyHandler
name|anyHandler
init|=
operator|(
name|CorbaAnyHandler
operator|)
name|obj
decl_stmt|;
name|CorbaObjectHandler
name|containedType
init|=
name|anyHandler
operator|.
name|getAnyContainedType
argument_list|()
decl_stmt|;
name|Any
name|a
init|=
name|anyHandler
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// This is true if we have an empty any
if|if
condition|(
name|containedType
operator|!=
literal|null
condition|)
block|{
name|a
operator|.
name|type
argument_list|(
name|containedType
operator|.
name|getTypeCode
argument_list|()
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|a
operator|.
name|create_output_stream
argument_list|()
decl_stmt|;
name|CorbaObjectWriter
name|writer
init|=
operator|new
name|CorbaObjectWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|containedType
argument_list|)
expr_stmt|;
name|a
operator|.
name|read_value
argument_list|(
name|os
operator|.
name|create_input_stream
argument_list|()
argument_list|,
name|containedType
operator|.
name|getTypeCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|stream
operator|.
name|write_any
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|// -- complex types --
specifier|public
name|void
name|writeEnum
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaEnumHandler
name|enumHandler
init|=
operator|(
name|CorbaEnumHandler
operator|)
name|obj
decl_stmt|;
name|Enum
name|enumType
init|=
operator|(
name|Enum
operator|)
name|enumHandler
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|enumLabel
init|=
name|enumHandler
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Enumerator
argument_list|>
name|enumerators
init|=
name|enumType
operator|.
name|getEnumerator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|enumerators
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|enumerators
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|enumLabel
argument_list|)
condition|)
block|{
name|stream
operator|.
name|write_long
argument_list|(
name|i
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"CorbaObjectWriter: unable to find enumeration label"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|writeStruct
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaStructHandler
name|structHandler
init|=
operator|(
name|CorbaStructHandler
operator|)
name|obj
decl_stmt|;
name|List
argument_list|<
name|CorbaObjectHandler
argument_list|>
name|structElements
init|=
name|structHandler
operator|.
name|getMembers
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|structElements
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|write
argument_list|(
name|structElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeException
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaExceptionHandler
name|exHandler
init|=
operator|(
name|CorbaExceptionHandler
operator|)
name|obj
decl_stmt|;
name|Exception
name|exType
init|=
operator|(
name|Exception
operator|)
name|exHandler
operator|.
name|getType
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CorbaObjectHandler
argument_list|>
name|exMembers
init|=
name|exHandler
operator|.
name|getMembers
argument_list|()
decl_stmt|;
name|stream
operator|.
name|write_string
argument_list|(
name|exType
operator|.
name|getRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exMembers
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|write
argument_list|(
name|exMembers
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeFixed
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaFixedHandler
name|fixedHandler
init|=
operator|(
name|CorbaFixedHandler
operator|)
name|obj
decl_stmt|;
name|short
name|scale
init|=
operator|(
name|short
operator|)
name|fixedHandler
operator|.
name|getScale
argument_list|()
decl_stmt|;
name|short
name|fixed
init|=
operator|(
name|short
operator|)
name|fixedHandler
operator|.
name|getDigits
argument_list|()
decl_stmt|;
comment|//the write_fixed method is a "late addition" and not all orbs implement it.
comment|//Some of them have a "write_fixed(BigDecimal, short, short)" method, we'll try that
try|try
block|{
name|Method
name|m
init|=
name|stream
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"write_fixed"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|BigDecimal
operator|.
name|class
block|,
name|Short
operator|.
name|TYPE
block|,
name|Short
operator|.
name|TYPE
block|}
argument_list|)
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|stream
argument_list|,
name|fixedHandler
operator|.
name|getValue
argument_list|()
argument_list|,
name|fixed
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e1
parameter_list|)
block|{
name|stream
operator|.
name|write_fixed
argument_list|(
name|fixedHandler
operator|.
name|getValue
argument_list|()
operator|.
name|movePointRight
argument_list|(
name|scale
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeUnion
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|Union
name|unionType
init|=
operator|(
name|Union
operator|)
name|obj
operator|.
name|getType
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Unionbranch
argument_list|>
name|branches
init|=
name|unionType
operator|.
name|getUnionbranch
argument_list|()
decl_stmt|;
if|if
condition|(
name|branches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|CorbaObjectHandler
name|discriminator
init|=
operator|(
operator|(
name|CorbaUnionHandler
operator|)
name|obj
operator|)
operator|.
name|getDiscriminator
argument_list|()
decl_stmt|;
name|this
operator|.
name|write
argument_list|(
name|discriminator
argument_list|)
expr_stmt|;
name|CorbaObjectHandler
name|unionValue
init|=
operator|(
operator|(
name|CorbaUnionHandler
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|unionValue
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|write
argument_list|(
name|unionValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|writeArray
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaArrayHandler
name|arrayHandler
init|=
operator|(
name|CorbaArrayHandler
operator|)
name|obj
decl_stmt|;
name|List
argument_list|<
name|CorbaObjectHandler
argument_list|>
name|arrayElements
init|=
name|arrayHandler
operator|.
name|getElements
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrayElements
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|write
argument_list|(
name|arrayElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeSequence
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
if|if
condition|(
name|obj
operator|instanceof
name|CorbaOctetSequenceHandler
condition|)
block|{
name|byte
index|[]
name|value
init|=
operator|(
operator|(
name|CorbaOctetSequenceHandler
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|stream
operator|.
name|write_ulong
argument_list|(
name|value
operator|.
name|length
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write_octet_array
argument_list|(
name|value
argument_list|,
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CorbaSequenceHandler
name|seqHandler
init|=
operator|(
name|CorbaSequenceHandler
operator|)
name|obj
decl_stmt|;
name|List
argument_list|<
name|CorbaObjectHandler
argument_list|>
name|seqElements
init|=
name|seqHandler
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|seqElements
operator|.
name|size
argument_list|()
decl_stmt|;
name|stream
operator|.
name|write_ulong
argument_list|(
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
operator|++
name|i
control|)
block|{
name|this
operator|.
name|write
argument_list|(
name|seqElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|writeObjectReference
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|CorbaObjectReferenceHandler
name|objHandler
init|=
operator|(
name|CorbaObjectReferenceHandler
operator|)
name|obj
decl_stmt|;
name|stream
operator|.
name|write_Object
argument_list|(
name|objHandler
operator|.
name|getReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

