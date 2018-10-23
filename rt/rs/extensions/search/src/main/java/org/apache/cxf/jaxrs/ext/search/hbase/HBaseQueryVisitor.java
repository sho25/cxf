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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|hbase
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|ConditionType
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|PrimitiveStatement
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchCondition
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|visitor
operator|.
name|AbstractSearchConditionVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|BinaryComparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|ByteArrayComparable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|CompareFilter
operator|.
name|CompareOp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|FilterList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|RegexStringComparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|SingleColumnValueFilter
import|;
end_import

begin_class
specifier|public
class|class
name|HBaseQueryVisitor
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractSearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|Filter
argument_list|>
block|{
specifier|private
name|Stack
argument_list|<
name|List
argument_list|<
name|Filter
argument_list|>
argument_list|>
name|queryStack
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|family
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|familyMap
decl_stmt|;
specifier|public
name|HBaseQueryVisitor
parameter_list|(
name|String
name|family
parameter_list|)
block|{
name|this
argument_list|(
name|family
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HBaseQueryVisitor
parameter_list|(
name|String
name|family
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldsMap
parameter_list|)
block|{
name|super
argument_list|(
name|fieldsMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|family
operator|=
name|family
expr_stmt|;
name|queryStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HBaseQueryVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|familyMap
parameter_list|)
block|{
name|this
argument_list|(
name|familyMap
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HBaseQueryVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|familyMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldsMap
parameter_list|)
block|{
name|super
argument_list|(
name|fieldsMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|familyMap
operator|=
name|familyMap
expr_stmt|;
name|queryStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|sc
parameter_list|)
block|{
name|PrimitiveStatement
name|statement
init|=
name|sc
operator|.
name|getStatement
argument_list|()
decl_stmt|;
if|if
condition|(
name|statement
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|statement
operator|.
name|getProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|queryStack
operator|.
name|peek
argument_list|()
operator|.
name|add
argument_list|(
name|buildSimpleQuery
argument_list|(
name|sc
operator|.
name|getConditionType
argument_list|()
argument_list|,
name|statement
operator|.
name|getProperty
argument_list|()
argument_list|,
name|statement
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|queryStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|condition
range|:
name|sc
operator|.
name|getSearchConditions
argument_list|()
control|)
block|{
name|condition
operator|.
name|accept
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|boolean
name|orCondition
init|=
name|sc
operator|.
name|getConditionType
argument_list|()
operator|==
name|ConditionType
operator|.
name|OR
decl_stmt|;
name|List
argument_list|<
name|Filter
argument_list|>
name|queries
init|=
name|queryStack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|queryStack
operator|.
name|peek
argument_list|()
operator|.
name|add
argument_list|(
name|createCompositeQuery
argument_list|(
name|queries
argument_list|,
name|orCondition
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Filter
name|getQuery
parameter_list|()
block|{
name|List
argument_list|<
name|Filter
argument_list|>
name|queries
init|=
name|queryStack
operator|.
name|peek
argument_list|()
decl_stmt|;
return|return
name|queries
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|queries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|private
name|Filter
name|buildSimpleQuery
parameter_list|(
name|ConditionType
name|ct
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|name
operator|=
name|super
operator|.
name|getRealPropertyName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|validatePropertyValue
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|getPrimitiveFieldClass
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|CompareOp
name|compareOp
init|=
literal|null
decl_stmt|;
name|boolean
name|regexCompRequired
init|=
literal|false
decl_stmt|;
switch|switch
condition|(
name|ct
condition|)
block|{
case|case
name|EQUALS
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|EQUAL
expr_stmt|;
name|regexCompRequired
operator|=
name|String
operator|.
name|class
operator|==
name|clazz
operator|&&
name|value
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
break|break;
case|case
name|NOT_EQUALS
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|NOT_EQUAL
expr_stmt|;
name|regexCompRequired
operator|=
name|String
operator|.
name|class
operator|==
name|clazz
operator|&&
name|value
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
break|break;
case|case
name|GREATER_THAN
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|GREATER
expr_stmt|;
break|break;
case|case
name|GREATER_OR_EQUALS
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|GREATER_OR_EQUAL
expr_stmt|;
break|break;
case|case
name|LESS_THAN
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|LESS
expr_stmt|;
break|break;
case|case
name|LESS_OR_EQUALS
case|:
name|compareOp
operator|=
name|CompareOp
operator|.
name|LESS_OR_EQUAL
expr_stmt|;
break|break;
default|default:
break|break;
block|}
name|String
name|qualifier
init|=
name|name
decl_stmt|;
name|String
name|theFamily
init|=
name|family
operator|!=
literal|null
condition|?
name|family
else|:
name|familyMap
operator|.
name|get
argument_list|(
name|qualifier
argument_list|)
decl_stmt|;
name|ByteArrayComparable
name|byteArrayComparable
init|=
name|regexCompRequired
condition|?
operator|new
name|RegexStringComparator
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|"*"
argument_list|,
literal|"."
argument_list|)
argument_list|)
else|:
operator|new
name|BinaryComparator
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|SingleColumnValueFilter
argument_list|(
name|theFamily
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|qualifier
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|compareOp
argument_list|,
name|byteArrayComparable
argument_list|)
return|;
block|}
specifier|private
name|Filter
name|createCompositeQuery
parameter_list|(
name|List
argument_list|<
name|Filter
argument_list|>
name|queries
parameter_list|,
name|boolean
name|orCondition
parameter_list|)
block|{
name|FilterList
operator|.
name|Operator
name|oper
init|=
name|orCondition
condition|?
name|FilterList
operator|.
name|Operator
operator|.
name|MUST_PASS_ONE
else|:
name|FilterList
operator|.
name|Operator
operator|.
name|MUST_PASS_ALL
decl_stmt|;
name|FilterList
name|list
init|=
operator|new
name|FilterList
argument_list|(
name|oper
argument_list|)
decl_stmt|;
for|for
control|(
name|Filter
name|query
range|:
name|queries
control|)
block|{
name|list
operator|.
name|addFilter
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

