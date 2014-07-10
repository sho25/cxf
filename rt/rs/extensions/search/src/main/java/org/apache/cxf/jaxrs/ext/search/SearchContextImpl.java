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
name|LinkedHashMap
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|util
operator|.
name|PropertyUtils
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
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|client
operator|.
name|CompleteCondition
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
name|client
operator|.
name|SearchConditionBuilder
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
name|fiql
operator|.
name|FiqlParser
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
name|utils
operator|.
name|InjectionUtils
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
name|utils
operator|.
name|JAXRSUtils
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SearchContextImpl
implements|implements
name|SearchContext
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SEARCH_QUERY
init|=
literal|"_search"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHORT_SEARCH_QUERY
init|=
literal|"_s"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CUSTOM_SEARCH_PARSER_PROPERTY
init|=
literal|"search.parser"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CUSTOM_SEARCH_QUERY_PARAM_NAME
init|=
literal|"search.query.parameter.name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_PLAIN_QUERY_PARAMETERS
init|=
literal|"search.use.plain.queries"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_ALL_QUERY_COMPONENT
init|=
literal|"search.use.all.query.component"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BLOCK_SEARCH_EXCEPTION
init|=
literal|"search.block.search.exception"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEEP_QUERY_ENCODED
init|=
literal|"search.keep.query.encoded"
decl_stmt|;
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
name|SearchContextImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|public
name|SearchContextImpl
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|getCondition
argument_list|(
literal|null
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|)
block|{
return|return
name|getCondition
argument_list|(
literal|null
argument_list|,
name|cls
argument_list|,
name|beanProperties
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parserProperties
parameter_list|)
block|{
return|return
name|getCondition
argument_list|(
literal|null
argument_list|,
name|cls
argument_list|,
name|beanProperties
argument_list|,
name|parserProperties
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|getCondition
argument_list|(
name|expression
argument_list|,
name|cls
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|)
block|{
return|return
name|getCondition
argument_list|(
name|expression
argument_list|,
name|cls
argument_list|,
name|beanProperties
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|getCondition
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parserProperties
parameter_list|)
block|{
if|if
condition|(
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|String
name|errorMessage
init|=
literal|"Primitive condition types are not supported"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
name|SearchConditionParser
argument_list|<
name|T
argument_list|>
name|parser
init|=
name|getParser
argument_list|(
name|cls
argument_list|,
name|beanProperties
argument_list|,
name|parserProperties
argument_list|)
decl_stmt|;
name|String
name|theExpression
init|=
name|expression
operator|==
literal|null
condition|?
name|getSearchExpression
argument_list|()
else|:
name|expression
decl_stmt|;
if|if
condition|(
name|theExpression
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|parser
operator|.
name|parse
argument_list|(
name|theExpression
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SearchParseException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|BLOCK_SEARCH_EXCEPTION
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|String
name|getSearchExpression
parameter_list|()
block|{
name|String
name|queryStr
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryStr
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|USE_ALL_QUERY_COMPONENT
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|queryStr
return|;
block|}
name|boolean
name|encoded
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|getKeepEncodedProperty
argument_list|()
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|queryStr
argument_list|,
literal|"&"
argument_list|,
operator|!
name|encoded
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|customQueryParamName
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|CUSTOM_SEARCH_QUERY_PARAM_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|customQueryParamName
operator|!=
literal|null
condition|)
block|{
return|return
name|params
operator|.
name|getFirst
argument_list|(
name|customQueryParamName
argument_list|)
return|;
block|}
if|if
condition|(
name|queryStr
operator|.
name|contains
argument_list|(
name|SHORT_SEARCH_QUERY
argument_list|)
operator|||
name|queryStr
operator|.
name|contains
argument_list|(
name|SEARCH_QUERY
argument_list|)
condition|)
block|{
if|if
condition|(
name|params
operator|.
name|containsKey
argument_list|(
name|SHORT_SEARCH_QUERY
argument_list|)
condition|)
block|{
return|return
name|params
operator|.
name|getFirst
argument_list|(
name|SHORT_SEARCH_QUERY
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|params
operator|.
name|getFirst
argument_list|(
name|SEARCH_QUERY
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|USE_PLAIN_QUERY_PARAMETERS
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|convertPlainQueriesToFiqlExp
argument_list|(
name|params
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|convertPlainQueriesToFiqlExp
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|SearchConditionBuilder
name|builder
init|=
name|SearchConditionBuilder
operator|.
name|instance
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CompleteCondition
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|CompleteCondition
argument_list|>
argument_list|(
name|params
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|getOrCondition
argument_list|(
name|builder
argument_list|,
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|and
argument_list|(
name|list
argument_list|)
operator|.
name|query
argument_list|()
return|;
block|}
specifier|private
name|String
name|getKeepEncodedProperty
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|KEEP_QUERY_ENCODED
argument_list|)
return|;
block|}
specifier|private
name|CompleteCondition
name|getOrCondition
parameter_list|(
name|SearchConditionBuilder
name|builder
parameter_list|,
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
parameter_list|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|ConditionType
name|ct
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|"From"
argument_list|)
condition|)
block|{
name|ct
operator|=
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
expr_stmt|;
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|"Till"
argument_list|)
condition|)
block|{
name|ct
operator|=
name|ConditionType
operator|.
name|LESS_OR_EQUALS
expr_stmt|;
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ct
operator|=
name|ConditionType
operator|.
name|EQUALS
expr_stmt|;
block|}
name|List
argument_list|<
name|CompleteCondition
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|CompleteCondition
argument_list|>
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|builder
operator|.
name|is
argument_list|(
name|key
argument_list|)
operator|.
name|comparesTo
argument_list|(
name|ct
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|or
argument_list|(
name|list
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|SearchConditionParser
argument_list|<
name|T
argument_list|>
name|getParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProperties
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parserProperties
parameter_list|)
block|{
name|Object
name|parserProp
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|CUSTOM_SEARCH_PARSER_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|parserProp
operator|!=
literal|null
condition|)
block|{
return|return
name|getCustomParser
argument_list|(
name|parserProp
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parserProperties
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|DATE_FORMAT_PROPERTY
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|DATE_FORMAT_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|TIMEZONE_SUPPORT_PROPERTY
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|TIMEZONE_SUPPORT_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|LAX_PROPERTY_MATCH
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|LAX_PROPERTY_MATCH
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|DECODE_QUERY_VALUES
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|DECODE_QUERY_VALUES
argument_list|)
argument_list|)
expr_stmt|;
comment|// FIQL specific
name|props
operator|.
name|put
argument_list|(
name|FiqlParser
operator|.
name|SUPPORT_SINGLE_EQUALS
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|FiqlParser
operator|.
name|SUPPORT_SINGLE_EQUALS
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|=
name|parserProperties
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanProps
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|beanProperties
operator|==
literal|null
condition|)
block|{
name|beanProps
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|BEAN_PROPERTY_MAP
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|beanProps
operator|=
name|beanProperties
expr_stmt|;
block|}
return|return
operator|new
name|FiqlParser
argument_list|<
name|T
argument_list|>
argument_list|(
name|cls
argument_list|,
name|props
argument_list|,
name|beanProps
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|SearchConditionParser
argument_list|<
name|T
argument_list|>
name|getCustomParser
parameter_list|(
name|Object
name|parserProp
parameter_list|)
block|{
return|return
operator|(
name|SearchConditionParser
argument_list|<
name|T
argument_list|>
operator|)
name|parserProp
return|;
block|}
block|}
end_class

end_unit

