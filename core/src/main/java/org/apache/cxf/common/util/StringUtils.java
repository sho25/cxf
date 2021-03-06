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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|StringUtils
block|{
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|NOT_EMPTY
init|=
parameter_list|(
name|String
name|s
parameter_list|)
lambda|->
operator|!
name|s
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|HEX
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|}
decl_stmt|;
specifier|private
name|StringUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
name|int
name|len
init|=
name|str
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|len
condition|;
operator|++
name|x
control|)
block|{
if|if
condition|(
name|str
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
operator|>
literal|' '
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|Predicate
argument_list|<
name|String
argument_list|>
name|notEmpty
parameter_list|()
block|{
return|return
name|NOT_EMPTY
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|isEmpty
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|diff
parameter_list|(
name|String
name|str1
parameter_list|,
name|String
name|str2
parameter_list|)
block|{
name|int
name|index
init|=
name|str1
operator|.
name|lastIndexOf
argument_list|(
name|str2
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
operator|-
literal|1
condition|)
block|{
return|return
name|str1
operator|.
name|substring
argument_list|(
name|str2
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|str1
return|;
block|}
specifier|public
specifier|static
name|String
name|getFirstFound
parameter_list|(
name|String
name|contents
parameter_list|,
name|String
name|regex
parameter_list|)
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|regex
argument_list|)
operator|||
name|isEmpty
argument_list|(
name|contents
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|,
name|Pattern
operator|.
name|UNICODE_CASE
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|contents
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
if|if
condition|(
name|matcher
operator|.
name|groupCount
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|matcher
operator|.
name|group
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|addDefaultPortIfMissing
parameter_list|(
name|String
name|urlString
parameter_list|)
block|{
return|return
name|addDefaultPortIfMissing
argument_list|(
name|urlString
argument_list|,
literal|"80"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|addDefaultPortIfMissing
parameter_list|(
name|String
name|urlString
parameter_list|,
name|String
name|defaultPort
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|new
name|URL
argument_list|(
name|urlString
argument_list|)
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|urlString
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
name|urlString
return|;
block|}
name|String
name|regex
init|=
literal|"http://([^/]+)"
decl_stmt|;
name|String
name|found
init|=
name|StringUtils
operator|.
name|getFirstFound
argument_list|(
name|urlString
argument_list|,
name|regex
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|found
argument_list|)
condition|)
block|{
name|String
name|replacer
init|=
literal|"http://"
operator|+
name|found
operator|+
literal|':'
operator|+
name|defaultPort
decl_stmt|;
return|return
name|urlString
operator|.
name|replaceFirst
argument_list|(
name|regex
argument_list|,
name|replacer
argument_list|)
return|;
block|}
return|return
name|urlString
return|;
block|}
comment|/**      * Return input string with first character in upper case.      * @param name input string.      * @return capitalized form.      */
specifier|public
specifier|static
name|String
name|capitalize
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|changeFirstCharacterCase
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|uncapitalize
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|changeFirstCharacterCase
argument_list|(
name|str
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|changeFirstCharacterCase
parameter_list|(
name|String
name|str
parameter_list|,
name|boolean
name|capitalize
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|str
return|;
block|}
name|char
name|baseChar
init|=
name|str
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|char
name|updatedChar
init|=
name|capitalize
condition|?
name|Character
operator|.
name|toUpperCase
argument_list|(
name|baseChar
argument_list|)
else|:
name|Character
operator|.
name|toLowerCase
argument_list|(
name|baseChar
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseChar
operator|==
name|updatedChar
condition|)
block|{
return|return
name|str
return|;
block|}
name|char
index|[]
name|chars
init|=
name|str
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|chars
index|[
literal|0
index|]
operator|=
name|updatedChar
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|chars
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|toBytesUTF8
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|toBytes
argument_list|(
name|str
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|toBytesASCII
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|toBytes
argument_list|(
name|str
argument_list|,
literal|"US-ASCII"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|toBytes
parameter_list|(
name|String
name|str
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
try|try
block|{
return|return
name|str
operator|.
name|getBytes
argument_list|(
name|enc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|toHexString
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|bytes
operator|.
name|length
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|byteToHex
argument_list|(
name|b
argument_list|,
name|sb
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|static
name|void
name|byteToHex
parameter_list|(
name|byte
name|b
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|HEX
index|[
operator|(
literal|0xF0
operator|&
name|b
operator|)
operator|>>
literal|4
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|HEX
index|[
literal|0x0F
operator|&
name|b
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

