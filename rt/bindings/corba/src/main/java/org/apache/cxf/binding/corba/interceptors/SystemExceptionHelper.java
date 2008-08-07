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
name|interceptors
package|;
end_package

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
name|SystemException
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
name|omg
operator|.
name|CORBA
operator|.
name|portable
operator|.
name|InputStream
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
specifier|final
class|class
name|SystemExceptionHelper
implements|implements
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|portable
operator|.
name|Streamable
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BAD_CONTEXT
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BAD_INV_ORDER
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BAD_OPERATION
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BAD_PARAM
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BAD_QOS
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BAD_TYPECODE
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CODESET_INCOMPATIBLE
init|=
literal|6
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COMM_FAILURE
init|=
literal|7
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DATA_CONVERSION
init|=
literal|8
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FREE_MEM
init|=
literal|9
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|IMP_LIMIT
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INITIALIZE
init|=
literal|11
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INTERNAL
init|=
literal|12
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INTF_REPOS
init|=
literal|13
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INVALID_TRANSACTION
init|=
literal|14
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INV_FLAG
init|=
literal|15
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INV_IDENT
init|=
literal|16
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INV_OBJREF
init|=
literal|17
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INV_POLICY
init|=
literal|18
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MARSHAL
init|=
literal|19
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NO_IMPLEMENT
init|=
literal|20
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NO_MEMORY
init|=
literal|21
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NO_PERMISSION
init|=
literal|22
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NO_RESOURCES
init|=
literal|23
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NO_RESPONSE
init|=
literal|24
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|OBJECT_NOT_EXIST
init|=
literal|25
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|OBJ_ADAPTER
init|=
literal|26
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PERSIST_STORE
init|=
literal|27
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|REBIND
init|=
literal|28
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TIMEOUT
init|=
literal|29
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TRANSACTION_MODE
init|=
literal|30
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TRANSACTION_REQUIRED
init|=
literal|31
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TRANSACTION_ROLLEDBACK
init|=
literal|32
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TRANSACTION_UNAVAILABLE
init|=
literal|33
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TRANSIENT
init|=
literal|34
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|UNKNOWN
init|=
literal|35
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|IDS
init|=
block|{
literal|"IDL:omg.org/CORBA/BAD_CONTEXT:1.0"
block|,
literal|"IDL:omg.org/CORBA/BAD_INV_ORDER:1.0"
block|,
literal|"IDL:omg.org/CORBA/BAD_OPERATION:1.0"
block|,
literal|"IDL:omg.org/CORBA/BAD_PARAM:1.0"
block|,
literal|"IDL:omg.org/CORBA/BAD_QOS:1.0"
block|,
literal|"IDL:omg.org/CORBA/BAD_TYPECODE:1.0"
block|,
literal|"IDL:omg.org/CORBA/CODESET_INCOMPATIBLE:1.0"
block|,
literal|"IDL:omg.org/CORBA/COMM_FAILURE:1.0"
block|,
literal|"IDL:omg.org/CORBA/DATA_CONVERSION:1.0"
block|,
literal|"IDL:omg.org/CORBA/FREE_MEM:1.0"
block|,
literal|"IDL:omg.org/CORBA/IMP_LIMIT:1.0"
block|,
literal|"IDL:omg.org/CORBA/INITIALIZE:1.0"
block|,
literal|"IDL:omg.org/CORBA/INTERNAL:1.0"
block|,
literal|"IDL:omg.org/CORBA/INTF_REPOS:1.0"
block|,
literal|"IDL:omg.org/CORBA/INVALID_TRANSACTION:1.0"
block|,
literal|"IDL:omg.org/CORBA/INV_FLAG:1.0"
block|,
literal|"IDL:omg.org/CORBA/INV_IDENT:1.0"
block|,
literal|"IDL:omg.org/CORBA/INV_OBJREF:1.0"
block|,
literal|"IDL:omg.org/CORBA/INV_POLICY:1.0"
block|,
literal|"IDL:omg.org/CORBA/MARSHAL:1.0"
block|,
literal|"IDL:omg.org/CORBA/NO_IMPLEMENT:1.0"
block|,
literal|"IDL:omg.org/CORBA/NO_MEMORY:1.0"
block|,
literal|"IDL:omg.org/CORBA/NO_PERMISSION:1.0"
block|,
literal|"IDL:omg.org/CORBA/NO_RESOURCES:1.0"
block|,
literal|"IDL:omg.org/CORBA/NO_RESPONSE:1.0"
block|,
literal|"IDL:omg.org/CORBA/OBJECT_NOT_EXIST:1.0"
block|,
literal|"IDL:omg.org/CORBA/OBJ_ADAPTER:1.0"
block|,
literal|"IDL:omg.org/CORBA/PERSIST_STORE:1.0"
block|,
literal|"IDL:omg.org/CORBA/REBIND:1.0"
block|,
literal|"IDL:omg.org/CORBA/TIMEOUT:1.0"
block|,
literal|"IDL:omg.org/CORBA/TRANSACTION_MODE:1.0"
block|,
literal|"IDL:omg.org/CORBA/TRANSACTION_REQUIRED:1.0"
block|,
literal|"IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0"
block|,
literal|"IDL:omg.org/CORBA/TRANSACTION_UNAVAILABLE:1.0"
block|,
literal|"IDL:omg.org/CORBA/TRANSIENT:1.0"
block|,
literal|"IDL:omg.org/CORBA/UNKNOWN:1.0"
block|}
decl_stmt|;
name|SystemException
name|value
decl_stmt|;
name|TypeCode
name|typeCode
decl_stmt|;
specifier|private
name|SystemExceptionHelper
parameter_list|()
block|{     }
specifier|private
name|SystemExceptionHelper
parameter_list|(
name|SystemException
name|ex
parameter_list|)
block|{
name|value
operator|=
name|ex
expr_stmt|;
block|}
specifier|private
specifier|static
name|int
name|binarySearch
parameter_list|(
name|String
index|[]
name|arr
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|int
name|left
init|=
literal|0
decl_stmt|;
name|int
name|right
init|=
name|arr
operator|.
name|length
decl_stmt|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|left
operator|<
name|right
condition|)
block|{
name|int
name|m
init|=
operator|(
name|left
operator|+
name|right
operator|)
operator|/
literal|2
decl_stmt|;
name|int
name|res
init|=
name|arr
index|[
name|m
index|]
operator|.
name|compareTo
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|0
condition|)
block|{
name|index
operator|=
name|m
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|res
operator|>
literal|0
condition|)
block|{
name|right
operator|=
name|m
expr_stmt|;
block|}
else|else
block|{
name|left
operator|=
name|m
operator|+
literal|1
expr_stmt|;
block|}
block|}
return|return
name|index
return|;
block|}
specifier|public
specifier|static
name|void
name|insert
parameter_list|(
name|Any
name|any
parameter_list|,
name|SystemException
name|val
parameter_list|)
block|{
name|any
operator|.
name|insert_Streamable
argument_list|(
operator|new
name|SystemExceptionHelper
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//CHECKSTYLE:OFF
comment|//NCSS is to high for this due to the massive switch statement
specifier|public
specifier|static
name|SystemException
name|read
parameter_list|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|portable
operator|.
name|InputStream
name|in
parameter_list|)
block|{
name|String
name|id
init|=
name|in
operator|.
name|read_string
argument_list|()
decl_stmt|;
name|int
name|minor
init|=
name|in
operator|.
name|read_ulong
argument_list|()
decl_stmt|;
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|CompletionStatus
name|status
init|=
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|CompletionStatus
operator|.
name|from_int
argument_list|(
name|in
operator|.
name|read_ulong
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|binarySearch
argument_list|(
name|IDS
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|SystemException
name|ex
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|n
condition|)
block|{
case|case
name|BAD_CONTEXT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_CONTEXT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|BAD_INV_ORDER
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_INV_ORDER
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|BAD_OPERATION
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_OPERATION
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|BAD_PARAM
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_PARAM
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|BAD_QOS
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_QOS
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|BAD_TYPECODE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|BAD_TYPECODE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|CODESET_INCOMPATIBLE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|CODESET_INCOMPATIBLE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|COMM_FAILURE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|COMM_FAILURE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|DATA_CONVERSION
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|DATA_CONVERSION
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|FREE_MEM
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|FREE_MEM
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|IMP_LIMIT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|IMP_LIMIT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INITIALIZE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INITIALIZE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INTERNAL
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INTERNAL
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INTF_REPOS
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INTF_REPOS
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INVALID_TRANSACTION
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INVALID_TRANSACTION
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INV_FLAG
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INV_FLAG
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INV_IDENT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INV_IDENT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INV_OBJREF
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INV_OBJREF
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|INV_POLICY
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|INV_POLICY
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|MARSHAL
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|MARSHAL
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_IMPLEMENT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NO_IMPLEMENT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_MEMORY
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NO_MEMORY
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_PERMISSION
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NO_PERMISSION
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_RESOURCES
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NO_RESOURCES
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_RESPONSE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NO_RESPONSE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|OBJECT_NOT_EXIST
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|OBJECT_NOT_EXIST
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|OBJ_ADAPTER
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|OBJ_ADAPTER
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|PERSIST_STORE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|PERSIST_STORE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|REBIND
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|REBIND
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TIMEOUT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TIMEOUT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSACTION_MODE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TRANSACTION_MODE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSACTION_REQUIRED
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TRANSACTION_REQUIRED
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSACTION_ROLLEDBACK
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TRANSACTION_ROLLEDBACK
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSACTION_UNAVAILABLE
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TRANSACTION_UNAVAILABLE
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSIENT
case|:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TRANSIENT
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNKNOWN
case|:
default|default:
name|ex
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|UNKNOWN
argument_list|(
name|minor
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
return|return
name|ex
return|;
block|}
comment|//CHECKSTYLE:ON
specifier|public
name|void
name|_read
parameter_list|(
name|InputStream
name|instream
parameter_list|)
block|{
name|value
operator|=
name|read
argument_list|(
name|instream
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TypeCode
name|_type
parameter_list|()
block|{
if|if
condition|(
name|typeCode
operator|==
literal|null
condition|)
block|{
name|ORB
name|orb
init|=
name|ORB
operator|.
name|init
argument_list|()
decl_stmt|;
name|StructMember
index|[]
name|smBuf
init|=
operator|new
name|StructMember
index|[
literal|2
index|]
decl_stmt|;
name|TypeCode
name|minortc
init|=
name|orb
operator|.
name|get_primitive_tc
argument_list|(
name|TCKind
operator|.
name|tk_long
argument_list|)
decl_stmt|;
name|smBuf
index|[
literal|0
index|]
operator|=
operator|new
name|StructMember
argument_list|(
literal|"minor"
argument_list|,
name|minortc
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|csLabels
index|[]
init|=
block|{
literal|"COMPLETED_YES"
block|,
literal|"COMPLETED_NO"
block|,
literal|"COMPLETED_MAYBE"
block|}
decl_stmt|;
name|TypeCode
name|completedtc
init|=
name|orb
operator|.
name|create_enum_tc
argument_list|(
literal|"IDL:omg.org/CORBA/CompletionStatus:1.0"
argument_list|,
literal|"CompletionStatus"
argument_list|,
name|csLabels
argument_list|)
decl_stmt|;
name|smBuf
index|[
literal|1
index|]
operator|=
operator|new
name|StructMember
argument_list|(
literal|"completed"
argument_list|,
name|completedtc
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|id
decl_stmt|;
name|String
name|name
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"SystemException"
expr_stmt|;
name|id
operator|=
literal|"IDL:omg.org/CORBA/SystemException:1.0"
expr_stmt|;
block|}
else|else
block|{
name|String
name|className
init|=
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|name
operator|=
name|className
operator|.
name|substring
argument_list|(
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|id
operator|=
literal|"IDL:omg.org/CORBA/"
operator|+
name|name
operator|+
literal|":1.0"
expr_stmt|;
block|}
name|typeCode
operator|=
name|orb
operator|.
name|create_exception_tc
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|smBuf
argument_list|)
expr_stmt|;
block|}
return|return
name|typeCode
return|;
block|}
specifier|public
name|void
name|_write
parameter_list|(
name|OutputStream
name|outstream
parameter_list|)
block|{
name|String
name|id
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
operator|new
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|UNKNOWN
argument_list|()
expr_stmt|;
name|id
operator|=
literal|"IDL:omg.org/CORBA/UNKNOWN"
expr_stmt|;
block|}
else|else
block|{
name|String
name|className
init|=
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|id
operator|=
literal|"IDL:omg.org/CORBA/"
operator|+
name|className
operator|.
name|substring
argument_list|(
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|":1.0"
expr_stmt|;
block|}
name|outstream
operator|.
name|write_string
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|outstream
operator|.
name|write_ulong
argument_list|(
name|value
operator|.
name|minor
argument_list|)
expr_stmt|;
name|outstream
operator|.
name|write_ulong
argument_list|(
name|value
operator|.
name|completed
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

