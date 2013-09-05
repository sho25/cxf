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
name|lucene
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|lucene
operator|.
name|document
operator|.
name|DateTools
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|DateTools
operator|.
name|Resolution
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|NumericRangeQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|PhraseQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|TermQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|WildcardQuery
import|;
end_import

begin_class
specifier|public
class|class
name|LuceneQueryVisitor
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractSearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|Query
argument_list|>
block|{
comment|//private Analyzer analyzer;
specifier|private
name|String
name|contentsFieldName
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|contentsFieldMap
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|List
argument_list|<
name|Query
argument_list|>
argument_list|>
name|queryStack
init|=
operator|new
name|Stack
argument_list|<
name|List
argument_list|<
name|Query
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|LuceneQueryVisitor
parameter_list|()
block|{
name|this
argument_list|(
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
name|LuceneQueryVisitor
parameter_list|(
name|String
name|contentsFieldAlias
parameter_list|,
name|String
name|contentsFieldName
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|contentsFieldAlias
argument_list|,
name|contentsFieldName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneQueryVisitor
parameter_list|(
name|String
name|contentsFieldName
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|,
name|contentsFieldName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneQueryVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldsMap
parameter_list|)
block|{
name|this
argument_list|(
name|fieldsMap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneQueryVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldsMap
parameter_list|,
name|String
name|contentsFieldName
parameter_list|)
block|{
name|super
argument_list|(
name|fieldsMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|contentsFieldName
operator|=
name|contentsFieldName
expr_stmt|;
name|queryStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setContentsFieldMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|contentsFieldMap
operator|=
name|map
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
argument_list|<
name|Query
argument_list|>
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
name|Query
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
comment|//public void setAnalyzer(Analyzer a) {
comment|//    this.analyzer = a;
comment|//}
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
name|List
argument_list|<
name|Query
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
name|Query
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
name|Query
name|query
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|ct
condition|)
block|{
case|case
name|EQUALS
case|:
name|query
operator|=
name|createEqualsQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|NOT_EQUALS
case|:
name|BooleanQuery
name|booleanQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|booleanQuery
operator|.
name|add
argument_list|(
name|createEqualsQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST_NOT
argument_list|)
expr_stmt|;
name|query
operator|=
name|booleanQuery
expr_stmt|;
break|break;
case|case
name|GREATER_THAN
case|:
name|query
operator|=
name|createRangeQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|ct
argument_list|)
expr_stmt|;
break|break;
case|case
name|GREATER_OR_EQUALS
case|:
name|query
operator|=
name|createRangeQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|ct
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_THAN
case|:
name|query
operator|=
name|createRangeQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|ct
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_OR_EQUALS
case|:
name|query
operator|=
name|createRangeQuery
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|ct
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|query
return|;
block|}
specifier|private
name|Query
name|createEqualsQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Query
name|query
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cls
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|String
name|strValue
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|isWildCard
init|=
name|strValue
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
operator|||
name|super
operator|.
name|isWildcardStringMatch
argument_list|()
decl_stmt|;
name|String
name|theContentsFieldName
init|=
name|getContentsFieldName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|theContentsFieldName
operator|==
literal|null
condition|)
block|{
name|Term
name|term
init|=
operator|new
name|Term
argument_list|(
name|name
argument_list|,
name|strValue
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isWildCard
condition|)
block|{
name|query
operator|=
operator|new
name|TermQuery
argument_list|(
name|term
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
operator|new
name|WildcardQuery
argument_list|(
name|term
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|isWildCard
condition|)
block|{
name|PhraseQuery
name|pquery
init|=
operator|new
name|PhraseQuery
argument_list|()
decl_stmt|;
name|pquery
operator|.
name|add
argument_list|(
operator|new
name|Term
argument_list|(
name|theContentsFieldName
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|pquery
operator|.
name|add
argument_list|(
operator|new
name|Term
argument_list|(
name|theContentsFieldName
argument_list|,
name|strValue
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
name|pquery
expr_stmt|;
block|}
else|else
block|{
name|BooleanQuery
name|pquery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|pquery
operator|.
name|add
argument_list|(
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|theContentsFieldName
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
name|pquery
operator|.
name|add
argument_list|(
operator|new
name|WildcardQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|theContentsFieldName
argument_list|,
name|strValue
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
name|query
operator|=
name|pquery
expr_stmt|;
block|}
block|}
else|else
block|{
name|query
operator|=
name|createRangeQuery
argument_list|(
name|cls
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|ConditionType
operator|.
name|EQUALS
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
specifier|private
name|String
name|getContentsFieldName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|fieldName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentsFieldMap
operator|!=
literal|null
condition|)
block|{
name|fieldName
operator|=
name|contentsFieldMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fieldName
operator|==
literal|null
condition|)
block|{
name|fieldName
operator|=
name|contentsFieldName
expr_stmt|;
block|}
return|return
name|fieldName
return|;
block|}
specifier|private
name|Query
name|createRangeQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|ConditionType
name|type
parameter_list|)
block|{
if|if
condition|(
name|String
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
operator|||
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
comment|// If needed, long and double can be supported too
comment|// Also, perhaps Strings may optionally be compared with string comparators
name|Integer
name|intValue
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|min
init|=
name|type
operator|==
name|ConditionType
operator|.
name|LESS_THAN
operator|||
name|type
operator|==
name|ConditionType
operator|.
name|LESS_OR_EQUALS
condition|?
literal|null
else|:
name|intValue
decl_stmt|;
name|Integer
name|max
init|=
name|type
operator|==
name|ConditionType
operator|.
name|GREATER_THAN
operator|||
name|type
operator|==
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
condition|?
literal|null
else|:
name|intValue
decl_stmt|;
name|boolean
name|minInclusive
init|=
name|type
operator|==
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
operator|||
name|type
operator|==
name|ConditionType
operator|.
name|EQUALS
decl_stmt|;
name|boolean
name|maxInclusive
init|=
name|type
operator|==
name|ConditionType
operator|.
name|LESS_OR_EQUALS
operator|||
name|type
operator|==
name|ConditionType
operator|.
name|EQUALS
decl_stmt|;
name|Query
name|query
init|=
name|NumericRangeQuery
operator|.
name|newIntRange
argument_list|(
name|name
argument_list|,
name|min
argument_list|,
name|max
argument_list|,
name|minInclusive
argument_list|,
name|maxInclusive
argument_list|)
decl_stmt|;
return|return
name|query
return|;
block|}
elseif|else
if|if
condition|(
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
comment|// This code has not been tested - most likely needs to be fixed
comment|// Resolution should be configurable ?
name|String
name|luceneDateValue
init|=
name|DateTools
operator|.
name|dateToString
argument_list|(
operator|(
name|Date
operator|)
name|value
argument_list|,
name|Resolution
operator|.
name|MILLISECOND
argument_list|)
decl_stmt|;
name|String
name|expression
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|ConditionType
operator|.
name|LESS_THAN
condition|)
block|{
comment|// what is the base date here ?
name|expression
operator|=
literal|"["
operator|+
literal|""
operator|+
literal|" TO "
operator|+
name|luceneDateValue
operator|+
literal|"]"
expr_stmt|;
block|}
else|else
block|{
name|expression
operator|=
literal|"["
operator|+
name|luceneDateValue
operator|+
literal|" TO "
operator|+
name|DateTools
operator|.
name|dateToString
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|,
name|Resolution
operator|.
name|MILLISECOND
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|parseExpression
argument_list|(
name|name
argument_list|,
name|expression
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|Query
name|createCompositeQuery
parameter_list|(
name|List
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|,
name|boolean
name|orCondition
parameter_list|)
block|{
name|BooleanClause
operator|.
name|Occur
name|clause
init|=
name|orCondition
condition|?
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
else|:
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
decl_stmt|;
name|BooleanQuery
name|booleanQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
for|for
control|(
name|Query
name|query
range|:
name|queries
control|)
block|{
name|booleanQuery
operator|.
name|add
argument_list|(
name|query
argument_list|,
name|clause
argument_list|)
expr_stmt|;
block|}
return|return
name|booleanQuery
return|;
block|}
specifier|protected
name|Query
name|parseExpression
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
comment|//QueryParser parser = new QueryParser(Version.LUCENE_40, name, analyzer);
comment|// return parse.parse(expression);
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

