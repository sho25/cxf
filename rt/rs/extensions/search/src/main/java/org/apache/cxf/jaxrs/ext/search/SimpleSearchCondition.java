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
name|Type
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|Beanspector
operator|.
name|TypeInfo
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
name|CollectionCheckCondition
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

begin_comment
comment|/**  * Simple search condition comparing primitive objects or complex object by its getters. For details see  * {@link #isMet(Object)} description.  *  * @param<T> type of search condition.  *  */
end_comment

begin_class
specifier|public
class|class
name|SimpleSearchCondition
parameter_list|<
name|T
parameter_list|>
implements|implements
name|SearchCondition
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
specifier|static
specifier|final
name|Set
argument_list|<
name|ConditionType
argument_list|>
name|SUPPORTED_TYPES
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ConditionType
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|)
expr_stmt|;
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|NOT_EQUALS
argument_list|)
expr_stmt|;
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|)
expr_stmt|;
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
argument_list|)
expr_stmt|;
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|)
expr_stmt|;
name|SUPPORTED_TYPES
operator|.
name|add
argument_list|(
name|ConditionType
operator|.
name|LESS_OR_EQUALS
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|ConditionType
name|joiningType
init|=
name|ConditionType
operator|.
name|AND
decl_stmt|;
specifier|private
name|T
name|condition
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|T
argument_list|>
argument_list|>
name|scts
decl_stmt|;
comment|/**      * Creates search condition with same operator (equality, inequality) applied in all comparison; see      * {@link #isMet(Object)} for details of comparison.      *      * @param cType shared condition type      * @param condition template object      */
specifier|public
name|SimpleSearchCondition
parameter_list|(
name|ConditionType
name|cType
parameter_list|,
name|T
name|condition
parameter_list|)
block|{
if|if
condition|(
name|cType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"cType is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|condition
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"condition is null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|SUPPORTED_TYPES
operator|.
name|contains
argument_list|(
name|cType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unsupported condition type: "
operator|+
name|cType
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|condition
operator|=
name|condition
expr_stmt|;
name|scts
operator|=
name|createConditions
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|cType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates search condition with different operators (equality, inequality etc) specified for each getter;      * see {@link #isMet(Object)} for details of comparison. Cannot be used for primitive T type due to      * per-getter comparison strategy.      *      * @param getters2operators getters names and operators to be used with them during comparison      * @param realGetters      * @param propertyTypeInfo      * @param condition template object      */
specifier|public
name|SimpleSearchCondition
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
name|getters2operators
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|realGetters
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|TypeInfo
argument_list|>
name|propertyTypeInfo
parameter_list|,
name|T
name|condition
parameter_list|)
block|{
if|if
condition|(
name|getters2operators
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"getters2operators is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|condition
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"condition is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isBuiltIn
argument_list|(
name|condition
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"mapped operators strategy is "
operator|+
literal|"not supported for primitive type "
operator|+
name|condition
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|condition
operator|=
name|condition
expr_stmt|;
for|for
control|(
name|ConditionType
name|ct
range|:
name|getters2operators
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|SUPPORTED_TYPES
operator|.
name|contains
argument_list|(
name|ct
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unsupported condition type: "
operator|+
name|ct
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|scts
operator|=
name|createConditions
argument_list|(
name|getters2operators
argument_list|,
name|realGetters
argument_list|,
name|propertyTypeInfo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SimpleSearchCondition
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
name|getters2operators
parameter_list|,
name|T
name|condition
parameter_list|)
block|{
name|this
argument_list|(
name|getters2operators
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|condition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|getCondition
parameter_list|()
block|{
return|return
name|condition
return|;
block|}
comment|/**      * {@inheritDoc}      *<p>      * When constructor with map is used it returns null.      */
annotation|@
name|Override
specifier|public
name|ConditionType
name|getConditionType
parameter_list|()
block|{
if|if
condition|(
name|scts
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return
name|joiningType
return|;
block|}
return|return
name|scts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatement
argument_list|()
operator|.
name|getCondition
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|T
argument_list|>
argument_list|>
name|getSearchConditions
parameter_list|()
block|{
if|if
condition|(
name|scts
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|scts
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|T
argument_list|>
argument_list|>
name|createConditions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
name|getters2operators
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|realGetters
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|TypeInfo
argument_list|>
name|propertyTypeInfo
parameter_list|,
name|ConditionType
name|sharedType
parameter_list|)
block|{
if|if
condition|(
name|isBuiltIn
argument_list|(
name|condition
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|SearchCondition
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|PrimitiveSearchCondition
argument_list|<>
argument_list|(
literal|null
argument_list|,
name|condition
argument_list|,
literal|null
argument_list|,
name|sharedType
argument_list|,
name|condition
argument_list|)
argument_list|)
return|;
block|}
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|T
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|get2val
init|=
name|getGettersAndValues
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
init|=
name|get2val
operator|!=
literal|null
condition|?
name|get2val
operator|.
name|keySet
argument_list|()
else|:
operator|(
operator|(
name|SearchBean
operator|)
name|condition
operator|)
operator|.
name|getKeySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|getter
range|:
name|keySet
control|)
block|{
name|ConditionType
name|ct
init|=
name|getters2operators
operator|==
literal|null
condition|?
name|sharedType
else|:
name|getters2operators
operator|.
name|get
argument_list|(
name|getter
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Object
name|rval
init|=
name|get2val
operator|!=
literal|null
condition|?
name|get2val
operator|.
name|get
argument_list|(
name|getter
argument_list|)
else|:
operator|(
operator|(
name|SearchBean
operator|)
name|condition
operator|)
operator|.
name|get
argument_list|(
name|getter
argument_list|)
decl_stmt|;
if|if
condition|(
name|rval
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|realGetter
init|=
name|realGetters
operator|!=
literal|null
operator|&&
name|realGetters
operator|.
name|containsKey
argument_list|(
name|getter
argument_list|)
condition|?
name|realGetters
operator|.
name|get
argument_list|(
name|getter
argument_list|)
else|:
name|getter
decl_stmt|;
name|TypeInfo
name|tInfo
init|=
name|propertyTypeInfo
operator|!=
literal|null
condition|?
name|propertyTypeInfo
operator|.
name|get
argument_list|(
name|getter
argument_list|)
else|:
literal|null
decl_stmt|;
name|Type
name|genType
init|=
name|tInfo
operator|!=
literal|null
condition|?
name|tInfo
operator|.
name|getGenericType
argument_list|()
else|:
name|rval
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|CollectionCheckInfo
name|checkInfo
init|=
name|tInfo
operator|!=
literal|null
condition|?
name|tInfo
operator|.
name|getCollectionCheckInfo
argument_list|()
else|:
literal|null
decl_stmt|;
name|PrimitiveSearchCondition
argument_list|<
name|T
argument_list|>
name|pc
init|=
name|checkInfo
operator|==
literal|null
condition|?
operator|new
name|PrimitiveSearchCondition
argument_list|<>
argument_list|(
name|realGetter
argument_list|,
name|rval
argument_list|,
name|genType
argument_list|,
name|ct
argument_list|,
name|condition
argument_list|)
else|:
operator|new
name|CollectionCheckCondition
argument_list|<>
argument_list|(
name|realGetter
argument_list|,
name|rval
argument_list|,
name|genType
argument_list|,
name|ct
argument_list|,
name|condition
argument_list|,
name|checkInfo
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|pc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This search condition is empty and can not be used"
argument_list|)
throw|;
block|}
return|return
name|list
return|;
block|}
comment|/**      * Compares given object against template condition object.      *<p>      * For built-in type T like String, Number (precisely, from type T located in subpackage of "java.lang.*")      * given object is directly compared with template object. Comparison for {@link ConditionType#EQUALS}      * requires correct implementation of {@link Object#equals(Object)}, using inequalities requires type T      * implementing {@link Comparable}.      *<p>      * For other types the comparison of given object against template object is done using its      *<b>getters</b>; Value returned by {@linkplain #isMet(Object)} operation is<b>conjunction ('and'      * operator)</b> of comparisons of each getter accessible in object of type T. Getters of template object      * that return null or throw exception are not used in comparison. Finally, if all getters      * return nulls (are excluded) it is interpreted as no filter (match every pojo).      *<p>      * If {@link #SimpleSearchCondition(ConditionType, Object) constructor with shared operator} was used,      * then getters are compared using the same operator. If {@link #SimpleSearchCondition(Map, Object)      * constructor with map of operators} was used then for every getter specified operator is used (getters      * for missing mapping are ignored). The way that comparison per-getter is done depending on operator type      * per getter - comparison for {@link ConditionType#EQUALS} requires correct implementation of      * {@link Object#equals(Object)}, using inequalities requires that getter type implements      * {@link Comparable}.      *<p>      * For equality comparison and String type in template object (either being built-in or getter from client      * provided type) it is allowed to used asterisk at the beginning or at the end of text as wild card (zero      * or more of any characters) e.g. "foo*", "*foo" or "*foo*". Inner asterisks are not interpreted as wild      * cards.      *<p>      *<b>Example:</b>      *      *<pre>      * SimpleSearchCondition&lt;Integer&gt; ssc = new SimpleSearchCondition&lt;Integer&gt;(      *   ConditionType.GREATER_THAN, 10);      * ssc.isMet(20);      * // true since 20&gt;10      *      * class Entity {      *   public String getName() {...      *   public int getLevel() {...      *   public String getMessage() {...      * }      *      * Entity template = new Entity("bbb", 10, null);      * ssc = new SimpleSearchCondition&lt;Entity&gt;(      *   ConditionType.GREATER_THAN, template);      *      * ssc.isMet(new Entity("aaa", 20, "some mesage"));      * // false: is not met, expression '"aaa"&gt;"bbb" and 20&gt;10' is not true      * // since "aaa" is not greater than "bbb"; not that message is null in template hence ingored      *      * ssc.isMet(new Entity("ccc", 30, "other message"));      * // true: is met, expression '"ccc"&gt;"bbb" and 30&gt;10' is true      *      * Map&lt;String, ConditionType&gt; map;      * map.put("name", ConditionType.EQUALS);      * map.put("level", ConditionType.GREATER_THAN);      * ssc = new SimpleSearchCondition&lt;Entity&gt;(      *   ConditionType.GREATER_THAN, template);      *      * ssc.isMet(new Entity("ccc", 30, "other message"));      * // false due to expression '"aaa"=="ccc" and 30&gt;10"' (note different operators)      *      *</pre>      *      * @throws IllegalAccessException when security manager disallows reflective call of getters.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isMet
parameter_list|(
name|T
name|pojo
parameter_list|)
block|{
for|for
control|(
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|sc
range|:
name|scts
control|)
block|{
if|if
condition|(
operator|!
name|sc
operator|.
name|isMet
argument_list|(
name|pojo
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
comment|/**      * Creates cache of getters from template (condition) object and its values returned during one-pass      * invocation. Method isMet() will use its keys to introspect getters of passed pojo object, and values      * from map in comparison.      *      * @return template (condition) object getters mapped to their non-null values      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getGettersAndValues
parameter_list|()
block|{
if|if
condition|(
operator|!
name|SearchBean
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|condition
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getters2values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Beanspector
argument_list|<
name|T
argument_list|>
name|beanspector
init|=
operator|new
name|Beanspector
argument_list|<>
argument_list|(
name|condition
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|getter
range|:
name|beanspector
operator|.
name|getGettersNames
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|getValue
argument_list|(
name|beanspector
argument_list|,
name|getter
argument_list|,
name|condition
argument_list|)
decl_stmt|;
name|getters2values
operator|.
name|put
argument_list|(
name|getter
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|//we do not need compare class objects
name|getters2values
operator|.
name|keySet
argument_list|()
operator|.
name|remove
argument_list|(
literal|"class"
argument_list|)
expr_stmt|;
return|return
name|getters2values
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Object
name|getValue
parameter_list|(
name|Beanspector
argument_list|<
name|T
argument_list|>
name|beanspector
parameter_list|,
name|String
name|getter
parameter_list|,
name|T
name|pojo
parameter_list|)
block|{
try|try
block|{
return|return
name|beanspector
operator|.
name|swap
argument_list|(
name|pojo
argument_list|)
operator|.
name|getValue
argument_list|(
name|getter
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|boolean
name|isBuiltIn
parameter_list|(
name|T
name|pojo
parameter_list|)
block|{
return|return
name|pojo
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java.lang"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|findAll
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|pojos
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|pojo
range|:
name|pojos
control|)
block|{
if|if
condition|(
name|isMet
argument_list|(
name|pojo
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|String
name|toSQL
parameter_list|(
name|String
name|table
parameter_list|,
name|String
modifier|...
name|columns
parameter_list|)
block|{
return|return
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|this
argument_list|,
name|table
argument_list|,
name|columns
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrimitiveStatement
name|getStatement
parameter_list|()
block|{
if|if
condition|(
name|scts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|scts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatement
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|accept
parameter_list|(
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
name|visitor
parameter_list|)
block|{
name|visitor
operator|.
name|visit
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

