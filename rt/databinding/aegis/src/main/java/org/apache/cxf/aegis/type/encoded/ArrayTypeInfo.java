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
name|encoded
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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|NamespaceContext
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|StringUtils
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
name|helpers
operator|.
name|CastUtils
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
name|constants
operator|.
name|Constants
import|;
end_import

begin_import
import|import static
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
name|encoded
operator|.
name|SoapEncodingUtil
operator|.
name|readAttributeValue
import|;
end_import

begin_class
specifier|public
class|class
name|ArrayTypeInfo
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_ENCODING_NS_1_1
init|=
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getSoapEncodingStyle
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ARRAY_TYPE
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_NS_1_1
argument_list|,
literal|"arrayType"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ARRAY_OFFSET
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_NS_1_1
argument_list|,
literal|"offset"
argument_list|)
decl_stmt|;
specifier|private
name|AegisType
name|type
decl_stmt|;
specifier|private
name|QName
name|typeName
decl_stmt|;
specifier|private
name|int
name|ranks
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|dimensions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|offset
decl_stmt|;
specifier|public
name|ArrayTypeInfo
parameter_list|(
name|QName
name|typeName
parameter_list|,
name|int
name|ranks
parameter_list|,
name|Integer
modifier|...
name|dimensions
parameter_list|)
block|{
name|this
operator|.
name|typeName
operator|=
name|typeName
expr_stmt|;
name|this
operator|.
name|ranks
operator|=
name|ranks
expr_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|dimensions
argument_list|,
name|dimensions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArrayTypeInfo
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|TypeMapping
name|tm
parameter_list|)
block|{
name|this
argument_list|(
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|getNamespaceContext
argument_list|()
argument_list|,
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ARRAY_TYPE
argument_list|)
argument_list|,
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ARRAY_OFFSET
argument_list|)
argument_list|)
expr_stmt|;
comment|// if type is xsd:ur-type replace it with xsd:anyType
name|String
name|namespace
init|=
name|reader
operator|.
name|getNamespaceForPrefix
argument_list|(
name|typeName
operator|.
name|getPrefix
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
if|if
condition|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|&&
literal|"ur-type"
operator|.
name|equals
argument_list|(
name|typeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|typeName
operator|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"anyType"
argument_list|,
name|typeName
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|typeName
operator|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|typeName
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|typeName
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tm
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
if|if
condition|(
name|ranks
operator|>
literal|0
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|componentType
init|=
name|type
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|ranks
operator|+
name|dimensions
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|componentType
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|componentType
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
name|SoapArrayType
name|arrayType
init|=
operator|new
name|SoapArrayType
argument_list|()
decl_stmt|;
name|arrayType
operator|.
name|setTypeClass
argument_list|(
name|componentType
argument_list|)
expr_stmt|;
name|arrayType
operator|.
name|setTypeMapping
argument_list|(
name|type
operator|.
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|=
name|arrayType
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ArrayTypeInfo
parameter_list|(
name|NamespaceContext
name|namespaceContext
parameter_list|,
name|String
name|arrayTypeValue
parameter_list|)
block|{
name|this
argument_list|(
name|namespaceContext
argument_list|,
name|arrayTypeValue
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArrayTypeInfo
parameter_list|(
name|NamespaceContext
name|namespaceContext
parameter_list|,
name|String
name|arrayTypeValue
parameter_list|,
name|String
name|offsetString
parameter_list|)
block|{
if|if
condition|(
name|arrayTypeValue
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"arrayTypeValue is null"
argument_list|)
throw|;
block|}
comment|// arrayTypeValue = atype , asize ;
comment|// atype = QName , [ rank ] ;
comment|// rank = "[" , { "," } , "]" ;
comment|// asize = "[" , length , { ","  length} , "]" ;
comment|// length = DIGIT , { DIGIT } ;
comment|//
comment|// x:foo[,,,][1,2,3,4]
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|arrayTypeValue
argument_list|,
literal|"[],:"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tokens
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|list
argument_list|(
name|tokenizer
argument_list|)
argument_list|)
decl_stmt|;
comment|// ArrayType QName
if|if
condition|(
name|tokens
operator|.
name|size
argument_list|()
operator|<
literal|3
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
if|if
condition|(
literal|":"
operator|.
name|equals
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|typeName
operator|=
operator|new
name|QName
argument_list|(
name|namespaceContext
operator|.
name|getNamespaceURI
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|tokens
operator|.
name|subList
argument_list|(
literal|3
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|typeName
operator|=
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|tokens
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|"["
operator|.
name|equals
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
comment|// Rank: [,,,,]
name|boolean
name|hasRank
init|=
name|tokens
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"["
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasRank
condition|)
block|{
comment|// there are atleast [] there is one rank
name|ranks
operator|=
literal|1
expr_stmt|;
for|for
control|(
name|String
name|token
range|:
name|tokens
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
literal|"]"
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
if|if
condition|(
name|tokens
operator|.
name|size
argument_list|()
operator|<
name|ranks
operator|+
literal|1
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
name|tokens
operator|=
name|tokens
operator|.
name|subList
argument_list|(
name|ranks
operator|+
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
literal|","
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|ranks
operator|++
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
block|}
block|}
comment|// Dimensions [1,2,3,4]
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|tokens
operator|.
name|size
argument_list|()
condition|;
name|i
operator|=
name|i
operator|+
literal|2
control|)
block|{
name|String
name|dimension
init|=
name|tokens
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"]"
operator|.
name|equals
argument_list|(
name|dimension
argument_list|)
condition|)
block|{
if|if
condition|(
name|i
operator|+
literal|1
operator|!=
name|tokens
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
break|break;
block|}
name|int
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
name|dimensions
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// verify next token is a ',' or ']'
name|String
name|next
init|=
name|tokens
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|","
operator|.
name|equals
argument_list|(
name|next
argument_list|)
operator|&&
operator|!
literal|"]"
operator|.
name|equals
argument_list|(
name|next
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|dimensions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid ArrayType value "
operator|+
name|arrayTypeValue
argument_list|)
throw|;
block|}
if|if
condition|(
name|offsetString
operator|!=
literal|null
condition|)
block|{
comment|// offset = "[" , length , "]" ;
name|tokens
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|list
argument_list|(
operator|new
name|StringTokenizer
argument_list|(
name|offsetString
argument_list|,
literal|"[]"
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokens
operator|.
name|size
argument_list|()
operator|!=
literal|3
operator|||
operator|!
literal|"["
operator|.
name|equals
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|||
operator|!
literal|"]"
operator|.
name|equals
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid Array offset value "
operator|+
name|offsetString
argument_list|)
throw|;
block|}
try|try
block|{
name|offset
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid Array offset value "
operator|+
name|offsetString
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|QName
name|getTypeName
parameter_list|()
block|{
return|return
name|typeName
return|;
block|}
specifier|public
name|AegisType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|int
name|getRanks
parameter_list|()
block|{
return|return
name|ranks
return|;
block|}
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|getDimensions
parameter_list|()
block|{
return|return
name|dimensions
return|;
block|}
specifier|public
name|int
name|getTotalDimensions
parameter_list|()
block|{
return|return
name|ranks
operator|+
name|dimensions
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|MessageWriter
name|writer
parameter_list|)
block|{
name|String
name|value
init|=
name|toString
argument_list|()
decl_stmt|;
name|SoapEncodingUtil
operator|.
name|writeAttribute
argument_list|(
name|writer
argument_list|,
name|SOAP_ARRAY_TYPE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|string
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// no prefix handed to us by someone else ...
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|typeName
operator|.
name|getPrefix
argument_list|()
argument_list|)
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|typeName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No prefix provided in QName for "
operator|+
name|typeName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
throw|;
block|}
comment|// typeName: foo:bar
if|if
condition|(
name|typeName
operator|.
name|getPrefix
argument_list|()
operator|!=
literal|null
operator|&&
name|typeName
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|string
operator|.
name|append
argument_list|(
name|typeName
operator|.
name|getPrefix
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
block|}
name|string
operator|.
name|append
argument_list|(
name|typeName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// ranks: [,,,,]
if|if
condition|(
name|ranks
operator|>
literal|0
condition|)
block|{
name|string
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
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
name|ranks
condition|;
name|i
operator|++
control|)
block|{
name|string
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|string
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
block|}
comment|// dimensions: [2,3,4]
name|string
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
name|dimensions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|dimension
range|:
name|dimensions
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|dimensions
operator|.
name|size
argument_list|()
argument_list|)
control|)
block|{
name|string
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
block|}
name|string
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|string
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

