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

begin_comment
comment|// import org.apache.axis.utils.Messages;
end_comment

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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|Proxy
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
comment|/**  * This is the class file reader for obtaining the parameter names for declared  * methods in a class. The class must have debugging attributes for us to obtain  * this information.  *<p>  * This does not work for inherited methods. To obtain parameter names for  * inherited methods, you must use a paramReader for the class that originally  * declared the method.  *<p>  * don't get tricky, it's the bare minimum. Instances of this class are not  * threadsafe -- don't share them.  */
end_comment

begin_class
class|class
name|ParamReader
extends|extends
name|ClassReader
block|{
specifier|private
name|String
name|methodName
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MethodInfo
argument_list|>
name|methods
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramTypes
decl_stmt|;
comment|/**      * process a class file, given it's class. We'll use the defining      * classloader to locate the bytecode.      *      * @param c      * @throws IOException      */
name|ParamReader
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
name|this
argument_list|(
name|getBytes
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * process the given class bytes directly.      *      * @param b      * @throws IOException      */
name|ParamReader
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|b
argument_list|,
name|findAttributeReaders
argument_list|(
name|ParamReader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the magic number
if|if
condition|(
name|readInt
argument_list|()
operator|!=
literal|0xCAFEBABE
condition|)
block|{
comment|// not a class file!
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
name|readShort
argument_list|()
expr_stmt|;
comment|// minor version
name|readShort
argument_list|()
expr_stmt|;
comment|// major version
name|readCpool
argument_list|()
expr_stmt|;
comment|// slurp in the constant pool
name|readShort
argument_list|()
expr_stmt|;
comment|// access flags
name|readShort
argument_list|()
expr_stmt|;
comment|// this class name
name|readShort
argument_list|()
expr_stmt|;
comment|// super class name
name|int
name|count
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// ifaces count
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
comment|// interface index
block|}
name|count
operator|=
name|readShort
argument_list|()
expr_stmt|;
comment|// fields count
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
comment|// access flags
name|readShort
argument_list|()
expr_stmt|;
comment|// name index
name|readShort
argument_list|()
expr_stmt|;
comment|// descriptor index
name|skipAttributes
argument_list|()
expr_stmt|;
comment|// field attributes
block|}
name|count
operator|=
name|readShort
argument_list|()
expr_stmt|;
comment|// methods count
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
comment|// access flags
name|int
name|m
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// name index
name|String
name|name
init|=
name|resolveUtf8
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|int
name|d
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// descriptor index
name|this
operator|.
name|methodName
operator|=
name|name
operator|+
name|resolveUtf8
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|readAttributes
argument_list|()
expr_stmt|;
comment|// method attributes
block|}
block|}
comment|/**      * Retrieve a list of function parameter names from a method Returns null if      * unable to read parameter names (i.e. bytecode not built with debug).      */
specifier|static
name|String
index|[]
name|getParameterNamesFromDebugInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
comment|// Don't worry about it if there are no params.
name|int
name|numParams
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|numParams
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// get declaring class
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|method
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
comment|// Don't worry about it if the class is a Java dynamic proxy
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|// get the parameter names
try|try
init|(
name|ParamReader
name|pr
init|=
operator|new
name|ParamReader
argument_list|(
name|c
argument_list|)
init|)
block|{
return|return
name|pr
operator|.
name|getParameterNames
argument_list|(
name|method
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// log it and leave
comment|// log.info(Messages.getMessage("error00") + ":" + e);
return|return
literal|null
return|;
block|}
block|}
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
name|int
name|maxLocals
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// max locals
name|MethodInfo
name|info
init|=
operator|new
name|MethodInfo
argument_list|(
name|maxLocals
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|!=
literal|null
operator|&&
name|methodName
operator|!=
literal|null
condition|)
block|{
name|methods
operator|.
name|put
argument_list|(
name|methodName
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * return the names of the declared parameters for the given constructor. If      * we cannot determine the names, return null. The returned array will have      * one name per parameter. The length of the array will be the same as the      * length of the Class[] array returned by Constructor.getParameterTypes().      *      * @param ctor      * @return String[] array of names, one per parameter, or null      */
specifier|public
name|String
index|[]
name|getParameterNames
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
parameter_list|)
block|{
name|paramTypes
operator|=
name|ctor
operator|.
name|getParameterTypes
argument_list|()
expr_stmt|;
return|return
name|getParameterNames
argument_list|(
name|ctor
argument_list|,
name|paramTypes
argument_list|)
return|;
block|}
comment|/**      * return the names of the declared parameters for the given method. If we      * cannot determine the names, return null. The returned array will have one      * name per parameter. The length of the array will be the same as the      * length of the Class[] array returned by Method.getParameterTypes().      *      * @param method      * @return String[] array of names, one per parameter, or null      */
specifier|public
name|String
index|[]
name|getParameterNames
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|paramTypes
operator|=
name|method
operator|.
name|getParameterTypes
argument_list|()
expr_stmt|;
return|return
name|getParameterNames
argument_list|(
name|method
argument_list|,
name|paramTypes
argument_list|)
return|;
block|}
specifier|protected
name|String
index|[]
name|getParameterNames
parameter_list|(
name|Member
name|member
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|pTypes
parameter_list|)
block|{
comment|// look up the names for this method
name|MethodInfo
name|info
init|=
name|methods
operator|.
name|get
argument_list|(
name|getSignature
argument_list|(
name|member
argument_list|,
name|pTypes
argument_list|)
argument_list|)
decl_stmt|;
comment|// we know all the local variable names, but we only need to return
comment|// the names of the parameters.
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|paramNames
init|=
operator|new
name|String
index|[
name|pTypes
operator|.
name|length
index|]
decl_stmt|;
name|int
name|j
init|=
name|Modifier
operator|.
name|isStatic
argument_list|(
name|member
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
comment|// did we find any non-null names
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|paramNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|info
operator|.
name|names
index|[
name|j
index|]
operator|!=
literal|null
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|paramNames
index|[
name|i
index|]
operator|=
name|info
operator|.
name|names
index|[
name|j
index|]
expr_stmt|;
block|}
name|j
operator|++
expr_stmt|;
if|if
condition|(
name|pTypes
index|[
name|i
index|]
operator|==
name|double
operator|.
name|class
operator|||
name|pTypes
index|[
name|i
index|]
operator|==
name|long
operator|.
name|class
condition|)
block|{
comment|// skip a slot for 64bit params
name|j
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|paramNames
return|;
block|}
return|return
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
class|class
name|MethodInfo
block|{
name|String
index|[]
name|names
decl_stmt|;
name|MethodInfo
parameter_list|(
name|int
name|maxLocals
parameter_list|)
block|{
name|names
operator|=
operator|new
name|String
index|[
name|maxLocals
index|]
expr_stmt|;
block|}
block|}
specifier|private
name|MethodInfo
name|getMethodInfo
parameter_list|()
block|{
name|MethodInfo
name|info
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|methods
operator|!=
literal|null
operator|&&
name|methodName
operator|!=
literal|null
condition|)
block|{
name|info
operator|=
name|methods
operator|.
name|get
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
comment|/**      * this is invoked when a LocalVariableTable attribute is encountered.      *      * @throws IOException      */
specifier|public
name|void
name|readLocalVariableTable
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|len
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// table length
name|MethodInfo
name|info
init|=
name|getMethodInfo
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|len
condition|;
name|j
operator|++
control|)
block|{
name|readShort
argument_list|()
expr_stmt|;
comment|// start pc
name|readShort
argument_list|()
expr_stmt|;
comment|// length
name|int
name|nameIndex
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// name_index
name|readShort
argument_list|()
expr_stmt|;
comment|// descriptor_index
name|int
name|index
init|=
name|readShort
argument_list|()
decl_stmt|;
comment|// local index
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|names
index|[
name|index
index|]
operator|=
name|resolveUtf8
argument_list|(
name|nameIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

