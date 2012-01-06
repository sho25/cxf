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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|AegisType
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
name|TypeMapping
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
name|common
operator|.
name|util
operator|.
name|Base64Utility
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
name|util
operator|.
name|SOAPConstants
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
name|XmlSchemaSimpleType
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
name|XmlSchemaSimpleTypeRestriction
import|;
end_import

begin_comment
comment|/**  * AegisType for runtime inspection of types. Looks as the class to be written, and  * looks to see if there is a type for that class. If there is, it writes out  * the value and inserts a<em>xsi:type</em> attribute to signal what the type  * of the value is. Can specify an optional set of dependent<code>AegisType</code>'s  * in the constructor, in the case that the type is a custom type that may not  * have its schema in the WSDL. Can specify whether or not unknown objects  * should be serialized as a byte stream.  *  */
end_comment

begin_class
specifier|public
class|class
name|ObjectType
extends|extends
name|AegisType
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|XSI_TYPE
init|=
operator|new
name|QName
argument_list|(
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|XSI_NIL
init|=
operator|new
name|QName
argument_list|(
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|,
literal|"nil"
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|AegisType
argument_list|>
name|dependencies
decl_stmt|;
specifier|private
name|boolean
name|serializedWhenUnknown
decl_stmt|;
specifier|private
name|boolean
name|readToDocument
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ObjectType
parameter_list|()
block|{
name|this
argument_list|(
name|Collections
operator|.
name|EMPTY_SET
argument_list|)
expr_stmt|;
name|readToDocument
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|ObjectType
parameter_list|(
name|Set
argument_list|<
name|AegisType
argument_list|>
name|dependencies
parameter_list|)
block|{
name|this
argument_list|(
name|dependencies
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ObjectType
parameter_list|(
name|boolean
name|serializeWhenUnknown
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
name|EMPTY_SET
argument_list|,
name|serializeWhenUnknown
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectType
parameter_list|(
name|Set
argument_list|<
name|AegisType
argument_list|>
name|dependencies
parameter_list|,
name|boolean
name|serializeWhenUnknown
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|=
name|dependencies
expr_stmt|;
name|this
operator|.
name|serializedWhenUnknown
operator|=
name|serializeWhenUnknown
expr_stmt|;
block|}
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
if|if
condition|(
name|isNil
argument_list|(
name|reader
operator|.
name|getAttributeReader
argument_list|(
name|XSI_NIL
argument_list|)
argument_list|)
condition|)
block|{
while|while
condition|(
name|reader
operator|.
name|hasMoreElementReaders
argument_list|()
condition|)
block|{
name|reader
operator|.
name|getNextElementReader
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
name|MessageReader
name|typeReader
init|=
name|reader
operator|.
name|getAttributeReader
argument_list|(
name|XSI_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|typeReader
operator|&&
operator|!
name|readToDocument
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Missing 'xsi:type' attribute"
argument_list|)
throw|;
block|}
name|String
name|typeName
init|=
name|typeReader
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|typeName
operator|&&
operator|!
name|readToDocument
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Missing 'xsi:type' attribute value"
argument_list|)
throw|;
block|}
name|AegisType
name|type
init|=
literal|null
decl_stmt|;
name|QName
name|typeQName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|typeName
operator|!=
literal|null
condition|)
block|{
name|typeName
operator|=
name|typeName
operator|.
name|trim
argument_list|()
expr_stmt|;
name|typeQName
operator|=
name|extractQName
argument_list|(
name|reader
argument_list|,
name|typeName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|typeQName
operator|=
name|reader
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|TypeMapping
name|tm
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|tm
operator|==
literal|null
condition|)
block|{
name|tm
operator|=
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
name|type
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|typeQName
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
name|this
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not determine how to read type: "
operator|+
name|typeQName
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
name|readToDocument
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|Document
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|type
condition|)
block|{
comment|// TODO should check namespace as well..
if|if
condition|(
name|serializedWhenUnknown
operator|&&
literal|"serializedJavaObject"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
return|return
name|reconstituteJavaObject
argument_list|(
name|reader
argument_list|)
return|;
block|}
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"No mapped type for '"
operator|+
name|typeName
operator|+
literal|"' ("
operator|+
name|typeQName
operator|+
literal|")"
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
return|;
block|}
specifier|private
name|QName
name|extractQName
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|String
name|typeName
parameter_list|)
block|{
name|int
name|colon
init|=
name|typeName
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|==
name|colon
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|reader
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|typeName
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|QName
argument_list|(
name|reader
operator|.
name|getNamespaceForPrefix
argument_list|(
name|typeName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colon
argument_list|)
argument_list|)
argument_list|,
name|typeName
operator|.
name|substring
argument_list|(
name|colon
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|private
name|Object
name|reconstituteJavaObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|)
throws|throws
name|DatabindingException
block|{
try|try
block|{
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
name|reader
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|ObjectInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|readObject
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Unable to reconstitute serialized object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|isNil
parameter_list|(
name|MessageReader
name|reader
parameter_list|)
block|{
return|return
literal|null
operator|!=
name|reader
operator|&&
literal|"true"
operator|.
name|equals
argument_list|(
name|reader
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|reader
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
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
if|if
condition|(
literal|null
operator|==
name|object
condition|)
block|{
name|MessageWriter
name|nilWriter
init|=
name|writer
operator|.
name|getAttributeWriter
argument_list|(
name|XSI_NIL
argument_list|)
decl_stmt|;
name|nilWriter
operator|.
name|writeValue
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|nilWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|AegisType
name|type
init|=
name|determineType
argument_list|(
name|context
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|type
condition|)
block|{
name|TypeMapping
name|tm
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|tm
operator|==
literal|null
condition|)
block|{
name|tm
operator|=
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
name|type
operator|=
name|tm
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeXsiType
argument_list|(
name|type
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|nextIsBeanType
init|=
name|type
operator|instanceof
name|BeanType
decl_stmt|;
if|if
condition|(
name|nextIsBeanType
condition|)
block|{
operator|(
operator|(
name|BeanType
operator|)
name|type
operator|)
operator|.
name|writeObjectFromObjectType
argument_list|(
name|object
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|.
name|writeObject
argument_list|(
name|object
argument_list|,
name|writer
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|AegisType
name|determineType
parameter_list|(
name|Context
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|TypeMapping
name|tm
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|tm
operator|==
literal|null
condition|)
block|{
name|tm
operator|=
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
name|AegisType
name|type
init|=
name|tm
operator|.
name|getType
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|type
condition|)
block|{
return|return
name|type
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|clazz
operator|.
name|getInterfaces
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
name|interfaces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|anInterface
init|=
name|interfaces
index|[
name|i
index|]
decl_stmt|;
name|type
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|anInterface
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|type
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|superclass
init|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|superclass
operator|||
name|Object
operator|.
name|class
operator|.
name|equals
argument_list|(
name|superclass
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|determineType
argument_list|(
name|context
argument_list|,
name|superclass
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isReadToDocument
parameter_list|()
block|{
return|return
name|readToDocument
return|;
block|}
specifier|public
name|void
name|setReadToDocument
parameter_list|(
name|boolean
name|readToDocument
parameter_list|)
block|{
name|this
operator|.
name|readToDocument
operator|=
name|readToDocument
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSerializedWhenUnknown
parameter_list|()
block|{
return|return
name|serializedWhenUnknown
return|;
block|}
specifier|public
name|void
name|setSerializedWhenUnknown
parameter_list|(
name|boolean
name|serializedWhenUnknown
parameter_list|)
block|{
name|this
operator|.
name|serializedWhenUnknown
operator|=
name|serializedWhenUnknown
expr_stmt|;
block|}
specifier|public
name|void
name|setDependencies
parameter_list|(
name|Set
argument_list|<
name|AegisType
argument_list|>
name|dependencies
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|=
name|dependencies
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|AegisType
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
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
name|serializedWhenUnknown
condition|)
block|{
name|XmlSchemaSimpleType
name|simple
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|root
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|simple
operator|.
name|setName
argument_list|(
literal|"serializedJavaObject"
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|restriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|simple
operator|.
name|setContent
argument_list|(
name|restriction
argument_list|)
expr_stmt|;
name|restriction
operator|.
name|setBaseTypeName
argument_list|(
name|XmlSchemaConstants
operator|.
name|BASE64BINARY_QNAME
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

