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
name|wsdl
operator|.
name|service
operator|.
name|factory
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Member
import|;
end_import

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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This is the class file reader for obtaining the parameter names for declared  * methods in a class. The class must have debugging attributes for us to obtain  * this information.  *<p>  * This does not work for inherited methods. To obtain parameter names for  * inherited methods, you must use a paramReader for the class that originally  * declared the method.  *<p>  * don't get tricky, it's the bare minimum. Instances of this class are not  * threadsafe -- don't share them.  *<p>  */
end_comment

begin_class
class|class
name|ClassReader
extends|extends
name|ByteArrayInputStream
block|{
comment|// constants values that appear in java class files,
comment|// from jvm spec 2nd ed, section 4.4, pp 103
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_CLASS
init|=
literal|7
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_FIELDREF
init|=
literal|9
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_METHODREF
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_INTERFACE_METHOD_REF
init|=
literal|11
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_STRING
init|=
literal|8
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_INTEGER
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_FLOAT
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_LONG
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_DOUBLE
init|=
literal|6
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_NAME_AND_TYPE
init|=
literal|12
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CONSTANT_UTF_8
init|=
literal|1
decl_stmt|;
comment|/**      * the constant pool. constant pool indices in the class file directly index      * into this array. The value stored in this array is the position in the      * class file where that constant begins.      */
specifier|private
name|int
index|[]
name|cpoolIndex
decl_stmt|;
specifier|private
name|Object
index|[]
name|cpool
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|attrMethods
decl_stmt|;
specifier|protected
name|ClassReader
parameter_list|(
name|byte
name|buf
index|[]
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|attrMethods
parameter_list|)
block|{
name|super
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|this
operator|.
name|attrMethods
operator|=
name|attrMethods
expr_stmt|;
block|}
comment|/**      * load the bytecode for a given class, by using the class's defining      * classloader and assuming that for a class named P.C, the bytecodes are in      * a resource named /P/C.class.      *       * @param c the class of interest      * @return a byte array containing the bytecode      * @throws IOException      */
specifier|protected
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|fin
init|=
name|c
operator|.
name|getResourceAsStream
argument_list|(
literal|'/'
operator|+
name|c
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
argument_list|)
init|;
name|ByteArrayOutputStream
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
if|if
condition|(
name|fin
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|actual
decl_stmt|;
do|do
block|{
name|actual
operator|=
name|fin
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
if|if
condition|(
name|actual
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|actual
operator|>
literal|0
condition|)
do|;
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
specifier|static
name|String
name|classDescriptorToName
parameter_list|(
name|String
name|desc
parameter_list|)
block|{
return|return
name|desc
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|findAttributeReaders
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
argument_list|()
decl_stmt|;
name|Method
index|[]
name|methods
init|=
name|c
operator|.
name|getMethods
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
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"read"
argument_list|)
operator|&&
name|methods
index|[
name|i
index|]
operator|.
name|getReturnType
argument_list|()
operator|==
name|void
operator|.
name|class
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
argument_list|,
name|methods
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|protected
specifier|static
name|String
name|getSignature
parameter_list|(
name|Member
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramTypes
parameter_list|)
block|{
comment|// compute the method descriptor
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
operator|(
name|method
operator|instanceof
name|Method
operator|)
condition|?
name|method
operator|.
name|getName
argument_list|()
else|:
literal|"<init>"
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'('
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
name|paramTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|addDescriptor
argument_list|(
name|b
argument_list|,
name|paramTypes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
if|if
condition|(
name|method
operator|instanceof
name|Method
condition|)
block|{
name|addDescriptor
argument_list|(
name|b
argument_list|,
operator|(
operator|(
name|Method
operator|)
name|method
operator|)
operator|.
name|getReturnType
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|method
operator|instanceof
name|Constructor
condition|)
block|{
name|addDescriptor
argument_list|(
name|b
argument_list|,
name|void
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|addDescriptor
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
if|if
condition|(
name|c
operator|==
name|void
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'V'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|int
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'I'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'Z'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|byte
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'B'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|short
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'S'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|long
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'J'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|char
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'C'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|float
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'F'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|double
operator|.
name|class
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'D'
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|c
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|addDescriptor
argument_list|(
name|b
argument_list|,
name|c
operator|.
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|'L'
argument_list|)
operator|.
name|append
argument_list|(
name|c
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return the next unsigned 16 bit value      */
specifier|protected
specifier|final
name|int
name|readShort
parameter_list|()
block|{
return|return
operator|(
name|read
argument_list|()
operator|<<
literal|8
operator|)
operator||
name|read
argument_list|()
return|;
block|}
comment|/**      * @return the next signed 32 bit value      */
specifier|protected
specifier|final
name|int
name|readInt
parameter_list|()
block|{
return|return
operator|(
name|read
argument_list|()
operator|<<
literal|24
operator|)
operator||
operator|(
name|read
argument_list|()
operator|<<
literal|16
operator|)
operator||
operator|(
name|read
argument_list|()
operator|<<
literal|8
operator|)
operator||
name|read
argument_list|()
return|;
block|}
comment|/**      * skip n bytes in the input stream.      */
specifier|protected
name|void
name|skipFully
parameter_list|(
name|int
name|n
parameter_list|)
throws|throws
name|IOException
block|{
while|while
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|int
name|c
init|=
operator|(
name|int
operator|)
name|skip
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
name|n
operator|-=
name|c
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|Member
name|resolveMethod
parameter_list|(
name|int
name|index
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
throws|,
name|NoSuchMethodException
block|{
name|int
name|oldPos
init|=
name|pos
decl_stmt|;
try|try
block|{
name|Member
name|m
init|=
operator|(
name|Member
operator|)
name|cpool
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|pos
operator|=
name|cpoolIndex
index|[
name|index
index|]
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|owner
init|=
name|resolveClass
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|NameAndType
name|nt
init|=
name|resolveNameAndType
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|signature
init|=
name|nt
operator|.
name|name
operator|+
name|nt
operator|.
name|type
decl_stmt|;
if|if
condition|(
literal|"<init>"
operator|.
name|equals
argument_list|(
name|nt
operator|.
name|name
argument_list|)
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
index|[]
name|ctors
init|=
name|owner
operator|.
name|getConstructors
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
name|ctors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|sig
init|=
name|getSignature
argument_list|(
name|ctors
index|[
name|i
index|]
argument_list|,
name|ctors
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sig
operator|.
name|equals
argument_list|(
name|signature
argument_list|)
condition|)
block|{
name|cpool
index|[
name|index
index|]
operator|=
name|ctors
index|[
name|i
index|]
expr_stmt|;
name|m
operator|=
name|ctors
index|[
name|i
index|]
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
block|}
else|else
block|{
name|Method
index|[]
name|methods
init|=
name|owner
operator|.
name|getDeclaredMethods
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
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|sig
init|=
name|getSignature
argument_list|(
name|methods
index|[
name|i
index|]
argument_list|,
name|methods
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sig
operator|.
name|equals
argument_list|(
name|signature
argument_list|)
condition|)
block|{
name|cpool
index|[
name|index
index|]
operator|=
name|methods
index|[
name|i
index|]
expr_stmt|;
name|m
operator|=
name|methods
index|[
name|i
index|]
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|NoSuchMethodException
argument_list|(
name|signature
argument_list|)
throw|;
block|}
return|return
name|m
return|;
block|}
finally|finally
block|{
name|pos
operator|=
name|oldPos
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|Field
name|resolveField
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
throws|,
name|NoSuchFieldException
block|{
name|int
name|oldPos
init|=
name|pos
decl_stmt|;
try|try
block|{
name|Field
name|f
init|=
operator|(
name|Field
operator|)
name|cpool
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|pos
operator|=
name|cpoolIndex
index|[
name|i
index|]
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|owner
init|=
name|resolveClass
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|NameAndType
name|nt
init|=
name|resolveNameAndType
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|cpool
index|[
name|i
index|]
operator|=
name|owner
operator|.
name|getDeclaredField
argument_list|(
name|nt
operator|.
name|name
argument_list|)
expr_stmt|;
name|f
operator|=
name|owner
operator|.
name|getDeclaredField
argument_list|(
name|nt
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
finally|finally
block|{
name|pos
operator|=
name|oldPos
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|NameAndType
name|resolveNameAndType
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|oldPos
init|=
name|pos
decl_stmt|;
try|try
block|{
name|NameAndType
name|nt
init|=
operator|(
name|NameAndType
operator|)
name|cpool
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|nt
operator|==
literal|null
condition|)
block|{
name|pos
operator|=
name|cpoolIndex
index|[
name|i
index|]
expr_stmt|;
name|String
name|name
init|=
name|resolveUtf8
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|resolveUtf8
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|cpool
index|[
name|i
index|]
operator|=
operator|new
name|NameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|nt
operator|=
operator|new
name|NameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|nt
return|;
block|}
finally|finally
block|{
name|pos
operator|=
name|oldPos
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|int
name|oldPos
init|=
name|pos
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|cpool
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|pos
operator|=
name|cpoolIndex
index|[
name|i
index|]
expr_stmt|;
name|String
name|name
init|=
name|resolveUtf8
argument_list|(
name|readShort
argument_list|()
argument_list|)
decl_stmt|;
name|cpool
index|[
name|i
index|]
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|classDescriptorToName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|classDescriptorToName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
finally|finally
block|{
name|pos
operator|=
name|oldPos
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|String
name|resolveUtf8
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|oldPos
init|=
name|pos
decl_stmt|;
try|try
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|cpool
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|pos
operator|=
name|cpoolIndex
index|[
name|i
index|]
expr_stmt|;
name|int
name|len
init|=
name|readShort
argument_list|()
decl_stmt|;
name|skipFully
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|cpool
index|[
name|i
index|]
operator|=
operator|new
name|String
argument_list|(
name|buf
argument_list|,
name|pos
operator|-
name|len
argument_list|,
name|len
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
name|s
operator|=
operator|new
name|String
argument_list|(
name|buf
argument_list|,
name|pos
operator|-
name|len
argument_list|,
name|len
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
finally|finally
block|{
name|pos
operator|=
name|oldPos
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
specifier|protected
specifier|final
name|void
name|readCpool
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|count
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// cpool count
name|cpoolIndex
operator|=
operator|new
name|int
index|[
name|count
index|]
expr_stmt|;
name|cpool
operator|=
operator|new
name|Object
index|[
name|count
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|int
name|c
init|=
name|read
argument_list|()
decl_stmt|;
name|cpoolIndex
index|[
name|i
index|]
operator|=
name|super
operator|.
name|pos
expr_stmt|;
comment|// constant pool tag
switch|switch
condition|(
name|c
condition|)
block|{
case|case
name|CONSTANT_FIELDREF
case|:
case|case
name|CONSTANT_METHODREF
case|:
case|case
name|CONSTANT_INTERFACE_METHOD_REF
case|:
case|case
name|CONSTANT_NAME_AND_TYPE
case|:
name|readShort
argument_list|()
expr_stmt|;
comment|// class index or (12) name index
name|readShort
argument_list|()
expr_stmt|;
comment|// string index or class index
break|break;
case|case
name|CONSTANT_CLASS
case|:
case|case
name|CONSTANT_STRING
case|:
name|readShort
argument_list|()
expr_stmt|;
comment|// string index or class index
break|break;
case|case
name|CONSTANT_LONG
case|:
case|case
name|CONSTANT_DOUBLE
case|:
name|readInt
argument_list|()
expr_stmt|;
comment|// hi-value
comment|// see jvm spec section 4.4.5 - double and long cpool
comment|// entries occupy two "slots" in the cpool table.
name|i
operator|++
expr_stmt|;
name|readInt
argument_list|()
expr_stmt|;
comment|// value
break|break;
case|case
name|CONSTANT_INTEGER
case|:
case|case
name|CONSTANT_FLOAT
case|:
name|readInt
argument_list|()
expr_stmt|;
comment|// value
break|break;
case|case
name|CONSTANT_UTF_8
case|:
name|int
name|len
init|=
name|readShort
argument_list|()
decl_stmt|;
name|skipFully
argument_list|(
name|len
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// corrupt class file
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
block|}
specifier|protected
specifier|final
name|void
name|skipAttributes
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|count
init|=
name|readShort
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|readShort
argument_list|()
expr_stmt|;
comment|// name index
name|skipFully
argument_list|(
name|readInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * read an attributes array. the elements of a class file that can contain      * attributes are: fields, methods, the class itself, and some other types      * of attributes.      */
specifier|protected
specifier|final
name|void
name|readAttributes
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|count
init|=
name|readShort
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|int
name|nameIndex
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// name index
name|int
name|attrLen
init|=
name|readInt
argument_list|()
decl_stmt|;
name|int
name|curPos
init|=
name|pos
decl_stmt|;
name|String
name|attrName
init|=
name|resolveUtf8
argument_list|(
name|nameIndex
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|attrMethods
operator|.
name|get
argument_list|(
name|attrName
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|m
operator|.
name|invoke
argument_list|(
name|this
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|pos
operator|=
name|curPos
expr_stmt|;
name|skipFully
argument_list|(
name|attrLen
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
try|try
block|{
throw|throw
name|e
operator|.
name|getTargetException
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|pos
operator|=
name|curPos
expr_stmt|;
name|skipFully
argument_list|(
name|attrLen
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// don't care what attribute this is
name|skipFully
argument_list|(
name|attrLen
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * read a code attribute      *       * @throws IOException      */
specifier|public
name|void
name|readCode
parameter_list|()
throws|throws
name|IOException
block|{
name|readShort
argument_list|()
expr_stmt|;
comment|// max stack
name|readShort
argument_list|()
expr_stmt|;
comment|// max locals
name|skipFully
argument_list|(
name|readInt
argument_list|()
argument_list|)
expr_stmt|;
comment|// code
name|skipFully
argument_list|(
literal|8
operator|*
name|readShort
argument_list|()
argument_list|)
expr_stmt|;
comment|// exception table
comment|// read the code attributes (recursive). This is where
comment|// we will find the LocalVariableTable attribute.
name|readAttributes
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|NameAndType
block|{
name|String
name|name
decl_stmt|;
name|String
name|type
decl_stmt|;
name|NameAndType
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

