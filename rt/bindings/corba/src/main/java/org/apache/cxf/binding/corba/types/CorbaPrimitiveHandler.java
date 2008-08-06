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
name|CorbaStreamable
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
name|TypeCode
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaPrimitiveHandler
extends|extends
name|CorbaObjectHandler
block|{
specifier|private
specifier|static
specifier|final
name|int
name|UNSIGNED_MAX
init|=
literal|256
decl_stmt|;
specifier|private
name|Object
name|value
decl_stmt|;
specifier|private
name|boolean
name|objectSet
decl_stmt|;
specifier|private
name|Any
name|any
decl_stmt|;
specifier|public
name|CorbaPrimitiveHandler
parameter_list|(
name|QName
name|primName
parameter_list|,
name|QName
name|primIdlType
parameter_list|,
name|TypeCode
name|primTC
parameter_list|,
name|Object
name|primType
parameter_list|)
block|{
name|super
argument_list|(
name|primName
argument_list|,
name|primIdlType
argument_list|,
name|primTC
argument_list|,
name|primType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setIntoAny
parameter_list|(
name|Any
name|val
parameter_list|,
name|CorbaStreamable
name|stream
parameter_list|,
name|boolean
name|output
parameter_list|)
block|{
name|any
operator|=
name|val
expr_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|val
operator|.
name|insert_Streamable
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|output
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|this
operator|.
name|typeCode
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
name|any
operator|.
name|insert_boolean
argument_list|(
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_char
case|:
name|any
operator|.
name|insert_char
argument_list|(
operator|(
operator|(
name|Character
operator|)
name|value
operator|)
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wchar
case|:
name|any
operator|.
name|insert_wchar
argument_list|(
operator|(
operator|(
name|Character
operator|)
name|value
operator|)
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_octet
case|:
name|any
operator|.
name|insert_octet
argument_list|(
operator|(
operator|(
name|Byte
operator|)
name|value
operator|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_short
case|:
name|any
operator|.
name|insert_short
argument_list|(
operator|(
operator|(
name|Short
operator|)
name|value
operator|)
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ushort
case|:
name|any
operator|.
name|insert_ushort
argument_list|(
call|(
name|short
call|)
argument_list|(
operator|(
name|Integer
operator|)
name|value
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_long
case|:
name|any
operator|.
name|insert_long
argument_list|(
operator|(
operator|(
name|Integer
operator|)
name|value
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_longlong
case|:
name|any
operator|.
name|insert_longlong
argument_list|(
operator|(
operator|(
name|Long
operator|)
name|value
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulong
case|:
name|any
operator|.
name|insert_ulong
argument_list|(
call|(
name|int
call|)
argument_list|(
operator|(
name|Long
operator|)
name|value
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulonglong
case|:
name|any
operator|.
name|insert_ulonglong
argument_list|(
operator|(
operator|(
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|)
name|value
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_float
case|:
name|any
operator|.
name|insert_float
argument_list|(
operator|(
name|Float
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_double
case|:
name|any
operator|.
name|insert_double
argument_list|(
operator|(
name|Double
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_string
case|:
name|any
operator|.
name|insert_string
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wstring
case|:
name|any
operator|.
name|insert_wstring
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// Default: assume that whatever stored the data will also know how to convert it into what
comment|// it needs.
block|}
block|}
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|objectSet
operator|=
literal|true
expr_stmt|;
name|value
operator|=
name|obj
expr_stmt|;
if|if
condition|(
name|any
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|setIntoAny
argument_list|(
name|any
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getDataFromValue
parameter_list|()
block|{
if|if
condition|(
operator|!
name|objectSet
operator|&&
name|any
operator|!=
literal|null
condition|)
block|{
return|return
name|getDataFromAny
argument_list|()
return|;
block|}
name|String
name|data
init|=
literal|""
decl_stmt|;
switch|switch
condition|(
name|this
operator|.
name|typeCode
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
name|data
operator|=
operator|(
operator|(
name|Boolean
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_char
case|:
name|char
name|charValue
init|=
operator|(
operator|(
name|Character
operator|)
name|value
operator|)
operator|.
name|charValue
argument_list|()
decl_stmt|;
comment|// outside the normal range it will -256
name|data
operator|=
name|Byte
operator|.
name|toString
argument_list|(
call|(
name|byte
call|)
argument_list|(
name|charValue
operator|>
name|Byte
operator|.
name|MAX_VALUE
condition|?
name|charValue
operator|-
name|UNSIGNED_MAX
else|:
name|charValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wchar
case|:
name|data
operator|=
operator|(
operator|(
name|Character
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_octet
case|:
name|data
operator|=
operator|(
operator|(
name|Byte
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_short
case|:
name|data
operator|=
operator|(
operator|(
name|Short
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ushort
case|:
name|data
operator|=
operator|(
operator|(
name|Integer
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_long
case|:
name|data
operator|=
operator|(
operator|(
name|Integer
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_longlong
case|:
name|data
operator|=
operator|(
operator|(
name|Long
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulong
case|:
name|data
operator|=
operator|(
operator|(
name|Long
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulonglong
case|:
name|data
operator|=
operator|(
operator|(
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_float
case|:
if|if
condition|(
operator|(
operator|(
name|Float
operator|)
name|value
operator|)
operator|.
name|isInfinite
argument_list|()
condition|)
block|{
name|data
operator|=
literal|"INF"
expr_stmt|;
block|}
else|else
block|{
name|data
operator|=
operator|(
operator|(
name|Float
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_double
case|:
if|if
condition|(
operator|(
operator|(
name|Double
operator|)
name|value
operator|)
operator|.
name|isInfinite
argument_list|()
condition|)
block|{
name|data
operator|=
literal|"INF"
expr_stmt|;
block|}
else|else
block|{
name|data
operator|=
operator|(
operator|(
name|Double
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_string
case|:
case|case
name|TCKind
operator|.
name|_tk_wstring
case|:
name|data
operator|=
operator|(
name|String
operator|)
name|value
expr_stmt|;
break|break;
default|default:
comment|// Default: assume that whatever stored the data will also know how to convert it into what
comment|// it needs.
name|data
operator|=
name|value
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
specifier|public
name|void
name|setValueFromData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|Object
name|obj
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|typeCode
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
name|obj
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_char
case|:
comment|// A char is mapped to a byte, we need it as a character
name|Byte
name|byteValue
init|=
operator|new
name|Byte
argument_list|(
name|data
argument_list|)
decl_stmt|;
comment|// for values< 0 + 256
comment|// This means that we can directly write out the chars in the normal
comment|// range 0-127 even when using UTF-8
name|obj
operator|=
operator|new
name|Character
argument_list|(
call|(
name|char
call|)
argument_list|(
name|byteValue
operator|.
name|byteValue
argument_list|()
operator|<
literal|0
condition|?
name|byteValue
operator|.
name|byteValue
argument_list|()
operator|+
name|UNSIGNED_MAX
else|:
name|byteValue
operator|.
name|byteValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wchar
case|:
comment|// A wide char is mapped to a string, we need it as a character
name|obj
operator|=
operator|new
name|Character
argument_list|(
name|data
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_octet
case|:
name|obj
operator|=
operator|new
name|Short
argument_list|(
name|data
argument_list|)
operator|.
name|byteValue
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_short
case|:
name|obj
operator|=
operator|new
name|Short
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ushort
case|:
name|obj
operator|=
operator|new
name|Integer
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_long
case|:
name|obj
operator|=
operator|new
name|Integer
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_longlong
case|:
name|obj
operator|=
operator|new
name|Long
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulong
case|:
name|obj
operator|=
operator|new
name|Long
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulonglong
case|:
name|obj
operator|=
operator|new
name|java
operator|.
name|math
operator|.
name|BigInteger
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_float
case|:
if|if
condition|(
literal|"INF"
operator|.
name|equals
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|obj
operator|=
name|Float
operator|.
name|POSITIVE_INFINITY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-INF"
operator|.
name|equals
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|obj
operator|=
name|Float
operator|.
name|NEGATIVE_INFINITY
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
operator|new
name|Float
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_double
case|:
if|if
condition|(
literal|"INF"
operator|.
name|equals
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|obj
operator|=
name|Double
operator|.
name|POSITIVE_INFINITY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-INF"
operator|.
name|equals
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|obj
operator|=
name|Double
operator|.
name|NEGATIVE_INFINITY
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
operator|new
name|Double
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_string
case|:
case|case
name|TCKind
operator|.
name|_tk_wstring
case|:
name|obj
operator|=
name|data
expr_stmt|;
break|break;
default|default:
comment|// Default: just store the data we were given.  We'll expect that whatever stored the data
comment|// will also know how to convert it into what it needs.
name|obj
operator|=
name|data
expr_stmt|;
block|}
name|setValue
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDataFromAny
parameter_list|()
block|{
name|String
name|data
init|=
literal|""
decl_stmt|;
switch|switch
condition|(
name|this
operator|.
name|typeCode
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
name|data
operator|=
name|any
operator|.
name|extract_boolean
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_char
case|:
name|char
name|charValue
init|=
name|any
operator|.
name|extract_char
argument_list|()
decl_stmt|;
comment|// outside the normal range it will -256
name|data
operator|=
name|Byte
operator|.
name|toString
argument_list|(
call|(
name|byte
call|)
argument_list|(
name|charValue
operator|>
name|Byte
operator|.
name|MAX_VALUE
condition|?
name|charValue
operator|-
name|UNSIGNED_MAX
else|:
name|charValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wchar
case|:
name|data
operator|=
name|Character
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_wchar
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_octet
case|:
name|data
operator|=
name|Byte
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_octet
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_short
case|:
name|data
operator|=
name|Short
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_short
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ushort
case|:
name|data
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_ushort
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_long
case|:
name|data
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_long
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_longlong
case|:
name|data
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_longlong
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_ulong
case|:
block|{
name|long
name|l
init|=
name|any
operator|.
name|extract_ulong
argument_list|()
decl_stmt|;
name|data
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|l
operator|&
literal|0xFFFFFFFFL
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|TCKind
operator|.
name|_tk_ulonglong
case|:
name|data
operator|=
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|any
operator|.
name|extract_ulonglong
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_float
case|:
if|if
condition|(
name|Float
operator|.
name|isInfinite
argument_list|(
name|any
operator|.
name|extract_float
argument_list|()
argument_list|)
condition|)
block|{
name|data
operator|=
literal|"INF"
expr_stmt|;
block|}
else|else
block|{
name|data
operator|=
name|Float
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_float
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_double
case|:
if|if
condition|(
name|Double
operator|.
name|isInfinite
argument_list|(
name|any
operator|.
name|extract_double
argument_list|()
argument_list|)
condition|)
block|{
name|data
operator|=
literal|"INF"
expr_stmt|;
block|}
else|else
block|{
name|data
operator|=
name|Double
operator|.
name|toString
argument_list|(
name|any
operator|.
name|extract_double
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TCKind
operator|.
name|_tk_string
case|:
name|data
operator|=
name|any
operator|.
name|extract_string
argument_list|()
expr_stmt|;
break|break;
case|case
name|TCKind
operator|.
name|_tk_wstring
case|:
name|data
operator|=
name|any
operator|.
name|extract_wstring
argument_list|()
expr_stmt|;
break|break;
default|default:
comment|//should not get here
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unknown tc: "
operator|+
name|this
operator|.
name|typeCode
argument_list|)
throw|;
block|}
return|return
name|data
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

