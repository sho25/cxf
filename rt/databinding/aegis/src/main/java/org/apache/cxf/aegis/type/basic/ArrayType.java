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
name|aegis
operator|.
name|type
operator|.
name|basic
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
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|type
operator|.
name|Type
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
name|aegis
operator|.
name|type
operator|.
name|TypeUtil
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementReader
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|xmlschema
operator|.
name|XmlSchemaConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaComplexType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSequence
import|;
end_import

begin_comment
comment|/**  * An ArrayType.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArrayType
extends|extends
name|Type
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ArrayType
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|QName
name|componentName
decl_stmt|;
specifier|private
name|long
name|minOccurs
decl_stmt|;
specifier|private
name|long
name|maxOccurs
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
specifier|public
name|ArrayType
parameter_list|()
block|{     }
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|QName
name|flatElementName
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
try|try
block|{
name|Collection
name|values
init|=
name|readCollection
argument_list|(
name|reader
argument_list|,
name|flatElementName
argument_list|,
name|context
argument_list|)
decl_stmt|;
return|return
name|makeArray
argument_list|(
name|getComponentType
argument_list|()
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Illegal argument."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * This version is not called for the flat case.       */
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
return|return
name|readObject
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|,
name|context
argument_list|)
return|;
block|}
specifier|protected
name|Collection
argument_list|<
name|Object
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Read the elements of an array or array-like item.      * @param reader reader to read from.      * @param flatElementName if flat, the elements we are looking for. When we see      * something else. we stop.      * @param context context.      * @return a collection of the objects.      * @throws DatabindingException      */
specifier|protected
name|Collection
name|readCollection
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|QName
name|flatElementName
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|createCollection
argument_list|()
decl_stmt|;
comment|/**          * If we are 'flat' (writeOuter is false), then we aren't reading children. We're reading starting          * from where we are.          */
if|if
condition|(
name|isFlat
argument_list|()
condition|)
block|{
comment|// the reader does some really confusing things.
name|XMLStreamReader
name|xmlReader
init|=
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
decl_stmt|;
while|while
condition|(
name|xmlReader
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|flatElementName
argument_list|)
condition|)
block|{
name|Type
name|compType
init|=
name|TypeUtil
operator|.
name|getReadType
argument_list|(
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
argument_list|,
name|context
operator|.
name|getGlobalContext
argument_list|()
argument_list|,
name|getComponentType
argument_list|()
argument_list|)
decl_stmt|;
comment|// gosh, what about message readers of some other type?
name|ElementReader
name|thisItemReader
init|=
operator|new
name|ElementReader
argument_list|(
name|xmlReader
argument_list|)
decl_stmt|;
name|collectOneItem
argument_list|(
name|context
argument_list|,
name|values
argument_list|,
name|thisItemReader
argument_list|,
name|compType
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
while|while
condition|(
name|reader
operator|.
name|hasMoreElementReaders
argument_list|()
condition|)
block|{
name|MessageReader
name|creader
init|=
name|reader
operator|.
name|getNextElementReader
argument_list|()
decl_stmt|;
name|Type
name|compType
init|=
name|TypeUtil
operator|.
name|getReadType
argument_list|(
name|creader
operator|.
name|getXMLStreamReader
argument_list|()
argument_list|,
name|context
operator|.
name|getGlobalContext
argument_list|()
argument_list|,
name|getComponentType
argument_list|()
argument_list|)
decl_stmt|;
name|collectOneItem
argument_list|(
name|context
argument_list|,
name|values
argument_list|,
name|creader
argument_list|,
name|compType
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check min occurs
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|<
name|minOccurs
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"The number of elements in "
operator|+
name|getSchemaType
argument_list|()
operator|+
literal|" does not meet the minimum of "
operator|+
name|minOccurs
argument_list|)
throw|;
block|}
return|return
name|values
return|;
block|}
specifier|private
name|void
name|collectOneItem
parameter_list|(
name|Context
name|context
parameter_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|,
name|MessageReader
name|creader
parameter_list|,
name|Type
name|compType
parameter_list|)
block|{
if|if
condition|(
name|creader
operator|.
name|isXsiNil
argument_list|()
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|creader
operator|.
name|readToEnd
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|values
operator|.
name|add
argument_list|(
name|compType
operator|.
name|readObject
argument_list|(
name|creader
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// check max occurs
name|int
name|size
init|=
name|values
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
name|maxOccurs
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"The number of elements in "
operator|+
name|getSchemaType
argument_list|()
operator|+
literal|" exceeds the maximum of "
operator|+
name|maxOccurs
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|Object
name|makeArray
parameter_list|(
name|Class
name|arrayType
parameter_list|,
name|Collection
name|values
parameter_list|)
block|{
name|int
name|i
decl_stmt|;
name|int
name|n
decl_stmt|;
name|Object
name|array
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Integer
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Integer
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Long
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Long
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Short
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Short
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Double
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Double
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Float
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Byte
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Boolean
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
name|values
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|array
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|Character
operator|.
name|TYPE
argument_list|,
name|objects
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|objects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|array
operator|==
literal|null
condition|?
name|values
operator|.
name|toArray
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|getComponentType
argument_list|()
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
else|:
name|array
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|values
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|writeObject
argument_list|(
name|values
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write an array type, using the desired element name in the flattened case.      * @param values values to write.      * @param writer writer to sent it to.      * @param context the aegis context.      * @param flatElementName name to use for the element if flat.      * @throws DatabindingException      */
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|values
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|,
name|QName
name|flatElementName
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|boolean
name|forceXsiWrite
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Type
name|type
init|=
name|getComponentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't find type for array."
argument_list|)
throw|;
block|}
if|if
condition|(
name|XmlSchemaConstants
operator|.
name|ANY_TYPE_QNAME
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getSchemaType
argument_list|()
argument_list|)
condition|)
block|{
name|forceXsiWrite
operator|=
literal|true
expr_stmt|;
block|}
name|String
name|ns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
name|ns
operator|=
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ns
operator|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
comment|/*           * This is not the right name in the 'flat' case. In the flat case,          * we need the element name that would have been attached          * one level out.          */
name|String
name|name
decl_stmt|;
if|if
condition|(
name|isFlat
argument_list|()
condition|)
block|{
name|name
operator|=
name|flatElementName
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|ns
operator|=
name|flatElementName
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
comment|// override the namespace.
block|}
else|else
block|{
name|name
operator|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
name|Class
name|arrayType
init|=
name|type
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|boolean
name|oldXsiWrite
init|=
name|context
operator|.
name|getGlobalContext
argument_list|()
operator|.
name|isWriteXsiTypes
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|forceXsiWrite
condition|)
block|{
name|context
operator|.
name|getGlobalContext
argument_list|()
operator|.
name|setWriteXsiTypes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|int
name|i
decl_stmt|;
name|int
name|n
decl_stmt|;
if|if
condition|(
name|Object
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Integer
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|int
index|[]
name|objects
init|=
operator|(
name|int
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Integer
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Long
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|long
index|[]
name|objects
init|=
operator|(
name|long
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Long
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Short
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|short
index|[]
name|objects
init|=
operator|(
name|short
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Short
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Double
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|double
index|[]
name|objects
init|=
operator|(
name|double
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Double
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|float
index|[]
name|objects
init|=
operator|(
name|float
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Float
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|byte
index|[]
name|objects
init|=
operator|(
name|byte
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Byte
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|boolean
index|[]
name|objects
init|=
operator|(
name|boolean
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|arrayType
argument_list|)
condition|)
block|{
name|char
index|[]
name|objects
init|=
operator|(
name|char
index|[]
operator|)
name|values
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
operator|,
name|n
operator|=
name|objects
operator|.
name|length
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|writeValue
argument_list|(
operator|new
name|Character
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|context
operator|.
name|getGlobalContext
argument_list|()
operator|.
name|setWriteXsiTypes
argument_list|(
name|oldXsiWrite
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|writeValue
parameter_list|(
name|Object
name|value
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|,
name|Type
name|type
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|type
operator|=
name|TypeUtil
operator|.
name|getWriteType
argument_list|(
name|context
operator|.
name|getGlobalContext
argument_list|()
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|MessageWriter
name|cwriter
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|isFlatArray
argument_list|()
condition|)
block|{
name|cwriter
operator|=
name|writer
operator|.
name|getElementWriter
argument_list|(
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cwriter
operator|=
name|writer
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|type
operator|.
name|isNillable
argument_list|()
condition|)
block|{
name|cwriter
operator|.
name|writeXsiNil
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|type
operator|.
name|writeObject
argument_list|(
name|value
argument_list|,
name|cwriter
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|type
operator|.
name|isFlatArray
argument_list|()
condition|)
block|{
name|cwriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeSchema
parameter_list|(
name|XmlSchema
name|root
parameter_list|)
block|{
if|if
condition|(
name|isFlat
argument_list|()
condition|)
block|{
return|return;
comment|// there is no extra level of type.
block|}
if|if
condition|(
name|hasDefinedArray
argument_list|(
name|root
argument_list|)
condition|)
block|{
return|return;
block|}
name|XmlSchemaComplexType
name|complex
init|=
operator|new
name|XmlSchemaComplexType
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|complex
operator|.
name|setName
argument_list|(
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|root
operator|.
name|addType
argument_list|(
name|complex
argument_list|)
expr_stmt|;
name|root
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|complex
argument_list|)
expr_stmt|;
name|XmlSchemaSequence
name|seq
init|=
operator|new
name|XmlSchemaSequence
argument_list|()
decl_stmt|;
name|complex
operator|.
name|setParticle
argument_list|(
name|seq
argument_list|)
expr_stmt|;
name|Type
name|componentType
init|=
name|getComponentType
argument_list|()
decl_stmt|;
name|XmlSchemaElement
name|element
init|=
operator|new
name|XmlSchemaElement
argument_list|()
decl_stmt|;
name|element
operator|.
name|setName
argument_list|(
name|componentType
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|element
operator|.
name|setSchemaTypeName
argument_list|(
name|componentType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
name|seq
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
name|componentType
operator|.
name|isNillable
argument_list|()
condition|)
block|{
name|element
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|element
operator|.
name|setMinOccurs
argument_list|(
name|getMinOccurs
argument_list|()
argument_list|)
expr_stmt|;
name|element
operator|.
name|setMaxOccurs
argument_list|(
name|getMaxOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Since both an Array and a List can have the same type definition, double check that there isn't already      * a defined type already.      *       * @param root      * @return      */
specifier|private
name|boolean
name|hasDefinedArray
parameter_list|(
name|XmlSchema
name|root
parameter_list|)
block|{
return|return
name|root
operator|.
name|getTypeByName
argument_list|(
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * We need to write a complex type schema for Beans, so return true.      *       * @see org.apache.cxf.aegis.type.Type#isComplex()      */
annotation|@
name|Override
specifier|public
name|boolean
name|isComplex
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|QName
name|getComponentName
parameter_list|()
block|{
return|return
name|componentName
return|;
block|}
specifier|public
name|void
name|setComponentName
parameter_list|(
name|QName
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|componentName
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.type.Type#getDependencies()      */
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getDependencies
parameter_list|()
block|{
name|Set
argument_list|<
name|Type
argument_list|>
name|deps
init|=
operator|new
name|HashSet
argument_list|<
name|Type
argument_list|>
argument_list|()
decl_stmt|;
name|deps
operator|.
name|add
argument_list|(
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|deps
return|;
block|}
comment|/**      * Get the<code>Type</code> of the elements in the array.      *       * @return      */
specifier|public
name|Type
name|getComponentType
parameter_list|()
block|{
name|Class
name|compType
init|=
name|getTypeClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
name|Type
name|type
decl_stmt|;
if|if
condition|(
name|componentName
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|compType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
comment|// We couldn't find the type the user specified. One is created
comment|// below instead.
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Couldn't find array component type "
operator|+
name|componentName
operator|+
literal|". Creating one instead."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|compType
argument_list|)
expr_stmt|;
name|getTypeMapping
argument_list|()
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
specifier|public
name|long
name|getMaxOccurs
parameter_list|()
block|{
return|return
name|maxOccurs
return|;
block|}
specifier|public
name|void
name|setMaxOccurs
parameter_list|(
name|long
name|maxOccurs
parameter_list|)
block|{
name|this
operator|.
name|maxOccurs
operator|=
name|maxOccurs
expr_stmt|;
block|}
specifier|public
name|long
name|getMinOccurs
parameter_list|()
block|{
return|return
name|minOccurs
return|;
block|}
specifier|public
name|void
name|setMinOccurs
parameter_list|(
name|long
name|minOccurs
parameter_list|)
block|{
name|this
operator|.
name|minOccurs
operator|=
name|minOccurs
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFlat
parameter_list|()
block|{
return|return
name|isFlatArray
argument_list|()
return|;
block|}
specifier|public
name|void
name|setFlat
parameter_list|(
name|boolean
name|flat
parameter_list|)
block|{
name|setWriteOuter
argument_list|(
operator|!
name|flat
argument_list|)
expr_stmt|;
name|setFlatArray
argument_list|(
name|flat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasMaxOccurs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasMinOccurs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

