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
name|LinkedHashMap
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
name|FiqlParser
argument_list|<
name|T
argument_list|>
name|parser
init|=
name|getParser
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|String
name|expression
init|=
name|getSearchExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expression
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
name|expression
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FiqlParseException
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
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
operator|&&
operator|(
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
operator|)
condition|)
block|{
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
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
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
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|FiqlParser
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
parameter_list|)
block|{
comment|// we can use this method as a parser factory, ex
comment|// we can get parsers capable of parsing XQuery and other languages
comment|// depending on the properties set by a user
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
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
argument_list|)
return|;
block|}
block|}
end_class

end_unit

