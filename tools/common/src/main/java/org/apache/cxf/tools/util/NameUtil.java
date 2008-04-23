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
name|tools
operator|.
name|util
package|;
end_package

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
name|helpers
operator|.
name|JavaUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|NameUtil
block|{
specifier|static
specifier|final
name|int
name|UPPER_LETTER
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|LOWER_LETTER
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|OTHER_LETTER
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|DIGIT
init|=
literal|3
decl_stmt|;
specifier|static
specifier|final
name|int
name|OTHER
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|ACTION_CHECK_PUNCT
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|ACTION_CHECK_C2
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|ACTION_BREAK
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
name|ACTION_NOBREAK
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|ACTION_TABLE
init|=
operator|new
name|byte
index|[
literal|5
operator|*
literal|5
index|]
decl_stmt|;
specifier|private
name|NameUtil
parameter_list|()
block|{     }
static|static
block|{
for|for
control|(
name|int
name|t0
init|=
literal|0
init|;
name|t0
operator|<
literal|5
condition|;
name|t0
operator|++
control|)
block|{
for|for
control|(
name|int
name|t1
init|=
literal|0
init|;
name|t1
operator|<
literal|5
condition|;
name|t1
operator|++
control|)
block|{
name|ACTION_TABLE
index|[
name|t0
operator|*
literal|5
operator|+
name|t1
index|]
operator|=
name|decideAction
argument_list|(
name|t0
argument_list|,
name|t1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|static
name|boolean
name|isPunct
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|boolean
name|isPunct
init|=
name|c
operator|==
literal|'-'
operator|||
name|c
operator|==
literal|'.'
operator|||
name|c
operator|==
literal|':'
operator|||
name|c
operator|==
literal|'_'
decl_stmt|;
name|boolean
name|isUnicodePunct
init|=
name|c
operator|==
literal|'\u00b7'
operator|||
name|c
operator|==
literal|'\u0387'
operator|||
name|c
operator|==
literal|'\u06dd'
operator|||
name|c
operator|==
literal|'\u06de'
decl_stmt|;
return|return
name|isPunct
operator|||
name|isUnicodePunct
return|;
block|}
specifier|protected
specifier|static
name|boolean
name|isLower
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|||
name|Character
operator|.
name|isLowerCase
argument_list|(
name|c
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|capitalize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isLower
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|s
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
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
specifier|private
specifier|static
name|int
name|nextBreak
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|int
name|n
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
name|char
name|c1
init|=
name|s
operator|.
name|charAt
argument_list|(
name|start
argument_list|)
decl_stmt|;
name|int
name|t1
init|=
name|classify
argument_list|(
name|c1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
operator|+
literal|1
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|int
name|t0
init|=
name|t1
decl_stmt|;
name|c1
operator|=
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|t1
operator|=
name|classify
argument_list|(
name|c1
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|ACTION_TABLE
index|[
name|t0
operator|*
literal|5
operator|+
name|t1
index|]
condition|)
block|{
case|case
name|ACTION_CHECK_PUNCT
case|:
if|if
condition|(
name|isPunct
argument_list|(
name|c1
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
break|break;
case|case
name|ACTION_CHECK_C2
case|:
if|if
condition|(
name|i
operator|<
name|n
operator|-
literal|1
condition|)
block|{
name|char
name|c2
init|=
name|s
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|isLower
argument_list|(
name|c2
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
break|break;
case|case
name|ACTION_BREAK
case|:
return|return
name|i
return|;
default|default:
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|private
specifier|static
name|byte
name|decideAction
parameter_list|(
name|int
name|t0
parameter_list|,
name|int
name|t1
parameter_list|)
block|{
if|if
condition|(
name|t0
operator|==
name|OTHER
operator|&&
name|t1
operator|==
name|OTHER
condition|)
block|{
return|return
name|ACTION_CHECK_PUNCT
return|;
block|}
if|if
condition|(
operator|!
name|xor
argument_list|(
name|t0
operator|==
name|DIGIT
argument_list|,
name|t1
operator|==
name|DIGIT
argument_list|)
operator|||
operator|(
name|t0
operator|==
name|LOWER_LETTER
operator|&&
name|t1
operator|!=
name|LOWER_LETTER
operator|)
condition|)
block|{
return|return
name|ACTION_BREAK
return|;
block|}
if|if
condition|(
operator|!
name|xor
argument_list|(
name|t0
operator|<=
name|OTHER_LETTER
argument_list|,
name|t1
operator|<=
name|OTHER_LETTER
argument_list|)
condition|)
block|{
return|return
name|ACTION_BREAK
return|;
block|}
if|if
condition|(
operator|!
name|xor
argument_list|(
name|t0
operator|==
name|OTHER_LETTER
argument_list|,
name|t1
operator|==
name|OTHER_LETTER
argument_list|)
condition|)
block|{
return|return
name|ACTION_BREAK
return|;
block|}
if|if
condition|(
name|t0
operator|==
name|UPPER_LETTER
operator|&&
name|t1
operator|==
name|UPPER_LETTER
condition|)
block|{
return|return
name|ACTION_CHECK_C2
return|;
block|}
return|return
name|ACTION_NOBREAK
return|;
block|}
specifier|private
specifier|static
name|boolean
name|xor
parameter_list|(
name|boolean
name|x
parameter_list|,
name|boolean
name|y
parameter_list|)
block|{
return|return
operator|(
name|x
operator|&&
name|y
operator|)
operator|||
operator|(
operator|!
name|x
operator|&&
operator|!
name|y
operator|)
return|;
block|}
specifier|protected
specifier|static
name|int
name|classify
parameter_list|(
name|char
name|c0
parameter_list|)
block|{
switch|switch
condition|(
name|Character
operator|.
name|getType
argument_list|(
name|c0
argument_list|)
condition|)
block|{
case|case
name|Character
operator|.
name|UPPERCASE_LETTER
case|:
return|return
name|UPPER_LETTER
return|;
case|case
name|Character
operator|.
name|LOWERCASE_LETTER
case|:
return|return
name|LOWER_LETTER
return|;
case|case
name|Character
operator|.
name|TITLECASE_LETTER
case|:
case|case
name|Character
operator|.
name|MODIFIER_LETTER
case|:
case|case
name|Character
operator|.
name|OTHER_LETTER
case|:
return|return
name|OTHER_LETTER
return|;
case|case
name|Character
operator|.
name|DECIMAL_DIGIT_NUMBER
case|:
return|return
name|DIGIT
return|;
default|default:
return|return
name|OTHER
return|;
block|}
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|toWordList
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ss
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|n
init|=
name|s
operator|.
name|length
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
name|n
condition|;
control|)
block|{
while|while
condition|(
name|i
operator|<
name|n
condition|)
block|{
if|if
condition|(
operator|!
name|isPunct
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
break|break;
block|}
name|i
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|>=
name|n
condition|)
block|{
break|break;
block|}
name|int
name|b
init|=
name|nextBreak
argument_list|(
name|s
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|String
name|w
init|=
operator|(
name|b
operator|==
operator|-
literal|1
operator|)
condition|?
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|)
else|:
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|ss
operator|.
name|add
argument_list|(
name|escape
argument_list|(
name|capitalize
argument_list|(
name|w
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|b
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|i
operator|=
name|b
expr_stmt|;
block|}
return|return
name|ss
return|;
block|}
specifier|protected
specifier|static
name|String
name|toMixedCaseName
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ss
parameter_list|,
name|boolean
name|startUpper
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ss
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|startUpper
condition|?
name|ss
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|ss
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toLowerCase
argument_list|()
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
name|ss
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ss
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
specifier|static
name|String
name|toMixedCaseVariableName
parameter_list|(
name|String
index|[]
name|ss
parameter_list|,
name|boolean
name|startUpper
parameter_list|,
name|boolean
name|cdrUpper
parameter_list|)
block|{
if|if
condition|(
name|cdrUpper
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|ss
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ss
index|[
name|i
index|]
operator|=
name|capitalize
argument_list|(
name|ss
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|ss
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|startUpper
condition|?
name|ss
index|[
literal|0
index|]
else|:
name|ss
index|[
literal|0
index|]
operator|.
name|toLowerCase
argument_list|()
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
name|ss
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ss
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|escape
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|s
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|int
name|n
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|<=
literal|'\u000f'
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"000"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<=
literal|'\u00ff'
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"00"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<=
literal|'\u0fff'
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|c
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|escape
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|int
name|n
init|=
name|s
operator|.
name|length
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
name|n
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|escape
argument_list|(
name|sb
argument_list|,
name|s
argument_list|,
name|i
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
name|s
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isJavaIdentifier
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|JavaUtils
operator|.
name|isJavaKeyword
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|s
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
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|String
name|mangleNameToClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|toMixedCaseName
argument_list|(
name|toWordList
argument_list|(
name|name
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|mangleNameToVariableName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|toMixedCaseName
argument_list|(
name|toWordList
argument_list|(
name|name
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

