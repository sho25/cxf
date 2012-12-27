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
name|jpa
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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TypedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|CriteriaBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|CriteriaQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Join
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Root
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
name|OrSearchCondition
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
name|collections
operator|.
name|CollectionCheckInfo
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJPATypedQueryVisitor
parameter_list|<
name|T
parameter_list|,
name|T1
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractSearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
block|{
specifier|private
name|EntityManager
name|em
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|T1
argument_list|>
name|queryClass
decl_stmt|;
specifier|private
name|Root
argument_list|<
name|T
argument_list|>
name|root
decl_stmt|;
specifier|private
name|CriteriaBuilder
name|builder
decl_stmt|;
specifier|private
name|CriteriaQuery
argument_list|<
name|T1
argument_list|>
name|cq
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|List
argument_list|<
name|Predicate
argument_list|>
argument_list|>
name|predStack
init|=
operator|new
name|Stack
argument_list|<
name|List
argument_list|<
name|Predicate
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|criteriaFinalized
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|joinProperties
decl_stmt|;
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Class
argument_list|<
name|T1
argument_list|>
name|queryClass
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
name|queryClass
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
literal|null
argument_list|,
name|fieldMap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|joinProps
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|joinProps
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|joinProps
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
literal|null
argument_list|,
name|fieldMap
argument_list|,
name|joinProps
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Class
argument_list|<
name|T1
argument_list|>
name|queryClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
name|queryClass
argument_list|,
name|fieldMap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJPATypedQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Class
argument_list|<
name|T1
argument_list|>
name|queryClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|joinProps
parameter_list|)
block|{
name|super
argument_list|(
name|fieldMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|em
operator|=
name|em
expr_stmt|;
name|this
operator|.
name|tClass
operator|=
name|tClass
expr_stmt|;
name|this
operator|.
name|queryClass
operator|=
name|toQueryClass
argument_list|(
name|queryClass
argument_list|,
name|tClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|joinProperties
operator|=
name|joinProps
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|joinProps
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Class
argument_list|<
name|E
argument_list|>
name|toQueryClass
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|queryClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|tClass
parameter_list|)
block|{
return|return
name|queryClass
operator|!=
literal|null
condition|?
name|queryClass
else|:
operator|(
name|Class
argument_list|<
name|E
argument_list|>
operator|)
name|tClass
return|;
block|}
specifier|protected
name|EntityManager
name|getEntityManager
parameter_list|()
block|{
return|return
name|em
return|;
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
if|if
condition|(
name|builder
operator|==
literal|null
condition|)
block|{
name|builder
operator|=
name|em
operator|.
name|getCriteriaBuilder
argument_list|()
expr_stmt|;
name|cq
operator|=
name|builder
operator|.
name|createQuery
argument_list|(
name|queryClass
argument_list|)
expr_stmt|;
name|root
operator|=
name|cq
operator|.
name|from
argument_list|(
name|tClass
argument_list|)
expr_stmt|;
name|predStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Predicate
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sc
operator|.
name|getStatement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|predStack
operator|.
name|peek
argument_list|()
operator|.
name|add
argument_list|(
name|buildPredicate
argument_list|(
name|sc
operator|.
name|getStatement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|predStack
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Predicate
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
name|List
argument_list|<
name|Predicate
argument_list|>
name|predsList
init|=
name|predStack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Predicate
index|[]
name|preds
init|=
name|predsList
operator|.
name|toArray
argument_list|(
operator|new
name|Predicate
index|[
name|predsList
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|Predicate
name|newPred
decl_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|OrSearchCondition
condition|)
block|{
name|newPred
operator|=
name|builder
operator|.
name|or
argument_list|(
name|preds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newPred
operator|=
name|builder
operator|.
name|and
argument_list|(
name|preds
argument_list|)
expr_stmt|;
block|}
name|predStack
operator|.
name|peek
argument_list|()
operator|.
name|add
argument_list|(
name|newPred
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|CriteriaBuilder
name|getCriteriaBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|T1
argument_list|>
name|getQueryClass
parameter_list|()
block|{
return|return
name|queryClass
return|;
block|}
specifier|public
name|Root
argument_list|<
name|T
argument_list|>
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|public
name|TypedQuery
argument_list|<
name|T1
argument_list|>
name|getTypedQuery
parameter_list|()
block|{
return|return
name|em
operator|.
name|createQuery
argument_list|(
name|getCriteriaQuery
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|CriteriaQuery
argument_list|<
name|T1
argument_list|>
name|getCriteriaQuery
parameter_list|()
block|{
if|if
condition|(
operator|!
name|criteriaFinalized
condition|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|>
name|predsList
init|=
name|predStack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|cq
operator|.
name|where
argument_list|(
name|predsList
operator|.
name|toArray
argument_list|(
operator|new
name|Predicate
index|[
name|predsList
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|criteriaFinalized
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|cq
return|;
block|}
specifier|private
name|Predicate
name|buildPredicate
parameter_list|(
name|PrimitiveStatement
name|ps
parameter_list|)
block|{
name|String
name|name
init|=
name|ps
operator|.
name|getProperty
argument_list|()
decl_stmt|;
name|name
operator|=
name|super
operator|.
name|getRealPropertyName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|ClassValue
name|cv
init|=
name|getPrimitiveFieldClass
argument_list|(
name|ps
argument_list|,
name|name
argument_list|,
name|ps
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|ps
operator|.
name|getValueType
argument_list|()
argument_list|,
name|ps
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|CollectionCheckInfo
name|collInfo
init|=
name|cv
operator|.
name|getCollectionCheckInfo
argument_list|()
decl_stmt|;
name|Path
argument_list|<
name|?
argument_list|>
name|path
init|=
name|getPath
argument_list|(
name|root
argument_list|,
name|name
argument_list|,
name|cv
argument_list|,
name|collInfo
argument_list|)
decl_stmt|;
name|Predicate
name|pred
init|=
name|collInfo
operator|==
literal|null
condition|?
name|doBuildPredicate
argument_list|(
name|ps
operator|.
name|getCondition
argument_list|()
argument_list|,
name|path
argument_list|,
name|cv
operator|.
name|getCls
argument_list|()
argument_list|,
name|cv
operator|.
name|getValue
argument_list|()
argument_list|)
else|:
name|doBuildCollectionPredicate
argument_list|(
name|ps
operator|.
name|getCondition
argument_list|()
argument_list|,
name|path
argument_list|,
name|collInfo
argument_list|)
decl_stmt|;
return|return
name|pred
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|private
name|Predicate
name|doBuildPredicate
parameter_list|(
name|ConditionType
name|ct
parameter_list|,
name|Path
argument_list|<
name|?
argument_list|>
name|path
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|valueClazz
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Comparable
argument_list|>
name|clazz
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Comparable
argument_list|>
operator|)
name|valueClazz
decl_stmt|;
name|Expression
argument_list|<
name|?
extends|extends
name|Comparable
argument_list|>
name|exp
init|=
name|path
operator|.
name|as
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|Predicate
name|pred
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|ct
condition|)
block|{
case|case
name|GREATER_THAN
case|:
name|pred
operator|=
name|builder
operator|.
name|greaterThan
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|EQUALS
case|:
if|if
condition|(
name|clazz
operator|.
name|equals
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
name|String
name|theValue
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|theValue
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|theValue
operator|=
operator|(
operator|(
name|String
operator|)
name|value
operator|)
operator|.
name|replaceAll
argument_list|(
literal|"\\*"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|pred
operator|=
name|builder
operator|.
name|like
argument_list|(
operator|(
name|Expression
argument_list|<
name|String
argument_list|>
operator|)
name|exp
argument_list|,
literal|"%"
operator|+
name|theValue
operator|+
literal|"%"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pred
operator|=
name|builder
operator|.
name|equal
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|NOT_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|notEqual
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_THAN
case|:
name|pred
operator|=
name|builder
operator|.
name|lessThan
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_OR_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|lessThanOrEqualTo
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GREATER_OR_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|greaterThanOrEqualTo
argument_list|(
name|exp
argument_list|,
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|pred
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|private
name|Predicate
name|doBuildCollectionPredicate
parameter_list|(
name|ConditionType
name|ct
parameter_list|,
name|Path
argument_list|<
name|?
argument_list|>
name|path
parameter_list|,
name|CollectionCheckInfo
name|collInfo
parameter_list|)
block|{
name|Predicate
name|pred
init|=
literal|null
decl_stmt|;
name|Expression
argument_list|<
name|Integer
argument_list|>
name|exp
init|=
name|builder
operator|.
name|size
argument_list|(
operator|(
name|Expression
argument_list|<
name|?
extends|extends
name|Collection
argument_list|>
operator|)
name|path
argument_list|)
decl_stmt|;
name|Integer
name|value
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|collInfo
operator|.
name|getCollectionCheckValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|ct
condition|)
block|{
case|case
name|GREATER_THAN
case|:
name|pred
operator|=
name|builder
operator|.
name|greaterThan
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|equal
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|NOT_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|notEqual
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_THAN
case|:
name|pred
operator|=
name|builder
operator|.
name|lessThan
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|LESS_OR_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|lessThanOrEqualTo
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|GREATER_OR_EQUALS
case|:
name|pred
operator|=
name|builder
operator|.
name|greaterThanOrEqualTo
argument_list|(
name|exp
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|pred
return|;
block|}
specifier|private
name|Path
argument_list|<
name|?
argument_list|>
name|getPath
parameter_list|(
name|Path
argument_list|<
name|?
argument_list|>
name|element
parameter_list|,
name|String
name|name
parameter_list|,
name|ClassValue
name|cv
parameter_list|,
name|CollectionCheckInfo
name|collSize
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|String
name|pre
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|post
init|=
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
name|getPath
argument_list|(
name|getNextPath
argument_list|(
name|element
argument_list|,
name|pre
argument_list|,
name|cv
argument_list|,
literal|null
argument_list|)
argument_list|,
name|post
argument_list|,
name|cv
argument_list|,
name|collSize
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getNextPath
argument_list|(
name|element
argument_list|,
name|name
argument_list|,
name|cv
argument_list|,
name|collSize
argument_list|)
return|;
block|}
block|}
specifier|private
name|Path
argument_list|<
name|?
argument_list|>
name|getNextPath
parameter_list|(
name|Path
argument_list|<
name|?
argument_list|>
name|element
parameter_list|,
name|String
name|name
parameter_list|,
name|ClassValue
name|cv
parameter_list|,
name|CollectionCheckInfo
name|collSize
parameter_list|)
block|{
if|if
condition|(
name|collSize
operator|==
literal|null
operator|&&
operator|(
name|cv
operator|.
name|isCollection
argument_list|(
name|name
argument_list|)
operator|||
name|isJoinProperty
argument_list|(
name|name
argument_list|)
operator|)
operator|&&
operator|(
name|element
operator|==
name|root
operator|||
name|element
operator|instanceof
name|Join
operator|)
condition|)
block|{
return|return
name|element
operator|==
name|root
condition|?
name|root
operator|.
name|join
argument_list|(
name|name
argument_list|)
else|:
operator|(
operator|(
name|Join
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|element
operator|)
operator|.
name|join
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|element
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
specifier|private
name|boolean
name|isJoinProperty
parameter_list|(
name|String
name|prop
parameter_list|)
block|{
return|return
name|joinProperties
operator|==
literal|null
condition|?
literal|false
else|:
name|joinProperties
operator|.
name|contains
argument_list|(
name|prop
argument_list|)
return|;
block|}
block|}
end_class

end_unit

