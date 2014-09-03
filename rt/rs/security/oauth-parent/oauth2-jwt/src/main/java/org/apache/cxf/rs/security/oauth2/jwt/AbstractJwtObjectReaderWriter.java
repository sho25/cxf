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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Map
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

begin_class
specifier|public
class|class
name|AbstractJwtObjectReaderWriter
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|DATE_PROPERTIES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_EXPIRY
argument_list|,
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|,
name|JwtConstants
operator|.
name|CLAIM_NOT_BEFORE
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|format
decl_stmt|;
specifier|protected
name|String
name|toJson
parameter_list|(
name|AbstractJwtObject
name|jwt
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
name|jwt
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|void
name|toJsonInternal
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|toJsonInternal
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|Object
index|[]
name|array
parameter_list|)
block|{
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|toJsonInternal
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|Collection
argument_list|<
name|?
argument_list|>
name|coll
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
name|formatIfNeeded
argument_list|(
name|sb
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|coll
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|formatIfNeeded
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|toJsonInternal
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|hasNext
parameter_list|)
block|{
if|if
condition|(
name|AbstractJwtObject
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|toJson
argument_list|(
operator|(
name|AbstractJwtObject
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|toJsonInternal
argument_list|(
name|sb
argument_list|,
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hasNext
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|formatIfNeeded
argument_list|(
name|sb
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|formatIfNeeded
parameter_list|(
name|StringBuilder
name|sb
parameter_list|)
block|{
if|if
condition|(
name|format
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\r\n "
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|fromJsonInternal
parameter_list|(
name|AbstractJwtObject
name|jwt
parameter_list|,
name|String
name|json
parameter_list|)
block|{
name|String
name|theJson
init|=
name|json
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
init|=
name|readJwtObjectAsMap
argument_list|(
name|theJson
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|theJson
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|fromJsonInternal
argument_list|(
name|jwt
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|fromJsonInternal
parameter_list|(
name|AbstractJwtObject
name|jwt
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|values
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|jwt
operator|.
name|setValue
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|readJwtObjectAsMap
parameter_list|(
name|String
name|json
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|json
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|isWhiteSpace
argument_list|(
name|json
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|int
name|closingQuote
init|=
name|json
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|,
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|from
init|=
name|json
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'"'
condition|?
name|i
operator|+
literal|1
else|:
name|i
decl_stmt|;
name|String
name|name
init|=
name|json
operator|.
name|substring
argument_list|(
name|from
argument_list|,
name|closingQuote
argument_list|)
decl_stmt|;
name|int
name|sepIndex
init|=
name|json
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|,
name|closingQuote
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|j
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|isWhiteSpace
argument_list|(
name|json
operator|.
name|charAt
argument_list|(
name|sepIndex
operator|+
name|j
argument_list|)
argument_list|)
condition|)
block|{
name|j
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|json
operator|.
name|charAt
argument_list|(
name|sepIndex
operator|+
name|j
argument_list|)
operator|==
literal|'{'
condition|)
block|{
name|int
name|closingIndex
init|=
name|getClosingIndex
argument_list|(
name|json
argument_list|,
literal|'{'
argument_list|,
literal|'}'
argument_list|,
name|sepIndex
operator|+
name|j
argument_list|)
decl_stmt|;
name|String
name|newJson
init|=
name|json
operator|.
name|substring
argument_list|(
name|sepIndex
operator|+
name|j
operator|+
literal|1
argument_list|,
name|closingIndex
argument_list|)
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|readJwtObjectAsMap
argument_list|(
name|newJson
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|=
name|closingIndex
operator|+
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|json
operator|.
name|charAt
argument_list|(
name|sepIndex
operator|+
name|j
argument_list|)
operator|==
literal|'['
condition|)
block|{
name|int
name|closingIndex
init|=
name|getClosingIndex
argument_list|(
name|json
argument_list|,
literal|'['
argument_list|,
literal|']'
argument_list|,
name|sepIndex
operator|+
name|j
argument_list|)
decl_stmt|;
name|String
name|newJson
init|=
name|json
operator|.
name|substring
argument_list|(
name|sepIndex
operator|+
name|j
operator|+
literal|1
argument_list|,
name|closingIndex
argument_list|)
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|readJwtObjectAsList
argument_list|(
name|newJson
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|=
name|closingIndex
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
name|int
name|commaIndex
init|=
name|getCommaIndex
argument_list|(
name|json
argument_list|,
name|sepIndex
operator|+
name|j
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|readPrimitiveValue
argument_list|(
name|json
argument_list|,
name|sepIndex
operator|+
name|j
argument_list|,
name|commaIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|DATE_PROPERTIES
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|value
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|values
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|i
operator|=
name|commaIndex
operator|+
literal|1
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|readJwtObjectAsList
parameter_list|(
name|String
name|json
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
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
name|json
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|isWhiteSpace
argument_list|(
name|json
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|json
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'{'
condition|)
block|{
name|int
name|closingIndex
init|=
name|getClosingIndex
argument_list|(
name|json
argument_list|,
literal|'{'
argument_list|,
literal|'}'
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
name|readJwtObjectAsMap
argument_list|(
name|json
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|closingIndex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|=
name|closingIndex
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
name|int
name|commaIndex
init|=
name|getCommaIndex
argument_list|(
name|json
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|readPrimitiveValue
argument_list|(
name|json
argument_list|,
name|i
argument_list|,
name|commaIndex
argument_list|)
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|i
operator|=
name|commaIndex
operator|+
literal|1
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
specifier|protected
name|Object
name|readPrimitiveValue
parameter_list|(
name|String
name|json
parameter_list|,
name|int
name|from
parameter_list|,
name|int
name|to
parameter_list|)
block|{
name|Object
name|value
init|=
name|json
operator|.
name|substring
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
decl_stmt|;
name|String
name|valueStr
init|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueStr
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|value
operator|=
name|valueStr
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|valueStr
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|valueStr
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|protected
specifier|static
name|int
name|getCommaIndex
parameter_list|(
name|String
name|json
parameter_list|,
name|int
name|from
parameter_list|)
block|{
name|int
name|commaIndex
init|=
name|json
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|,
name|from
argument_list|)
decl_stmt|;
if|if
condition|(
name|commaIndex
operator|==
operator|-
literal|1
condition|)
block|{
name|commaIndex
operator|=
name|json
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|commaIndex
return|;
block|}
specifier|protected
name|int
name|getClosingIndex
parameter_list|(
name|String
name|json
parameter_list|,
name|char
name|openChar
parameter_list|,
name|char
name|closeChar
parameter_list|,
name|int
name|from
parameter_list|)
block|{
name|int
name|nextOpenIndex
init|=
name|json
operator|.
name|indexOf
argument_list|(
name|openChar
argument_list|,
name|from
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|closingIndex
init|=
name|json
operator|.
name|indexOf
argument_list|(
name|closeChar
argument_list|,
name|from
operator|+
literal|1
argument_list|)
decl_stmt|;
while|while
condition|(
name|nextOpenIndex
operator|!=
operator|-
literal|1
operator|&&
name|nextOpenIndex
operator|<
name|closingIndex
condition|)
block|{
name|nextOpenIndex
operator|=
name|json
operator|.
name|indexOf
argument_list|(
name|openChar
argument_list|,
name|closingIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|closingIndex
operator|=
name|json
operator|.
name|indexOf
argument_list|(
name|closeChar
argument_list|,
name|closingIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|closingIndex
return|;
block|}
specifier|protected
name|boolean
name|isWhiteSpace
parameter_list|(
name|char
name|jsonChar
parameter_list|)
block|{
return|return
name|jsonChar
operator|==
literal|' '
operator|||
name|jsonChar
operator|==
literal|'\r'
operator|||
name|jsonChar
operator|==
literal|'\n'
operator|||
name|jsonChar
operator|==
literal|'\t'
return|;
block|}
specifier|public
name|void
name|setFormat
parameter_list|(
name|boolean
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
block|}
end_class

end_unit

