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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|ContextProvider
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
annotation|@
name|Provider
specifier|public
class|class
name|QueryContextProvider
implements|implements
name|ContextProvider
argument_list|<
name|QueryContext
argument_list|>
block|{
specifier|public
name|QueryContext
name|createContext
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|QueryContextImpl
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|QueryContextImpl
implements|implements
name|QueryContext
block|{
specifier|private
name|SearchContext
name|searchContext
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
name|QueryContextImpl
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|searchContext
operator|=
operator|new
name|SearchContextImpl
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|String
name|getConvertedExpression
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|beanClass
parameter_list|)
block|{
return|return
name|getConvertedExpression
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
name|beanClass
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
name|E
name|getConvertedExpression
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|beanClass
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|queryClass
parameter_list|)
block|{
return|return
name|getConvertedExpression
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
name|beanClass
argument_list|,
name|queryClass
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|String
name|getConvertedExpression
parameter_list|(
name|String
name|originalExpression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanClass
parameter_list|)
block|{
return|return
name|getConvertedExpression
argument_list|(
name|originalExpression
argument_list|,
name|beanClass
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
name|E
name|getConvertedExpression
parameter_list|(
name|String
name|originalExpression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanClass
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|queryClass
parameter_list|)
block|{
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|visitor
init|=
name|getVisitor
argument_list|()
decl_stmt|;
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SearchCondition
argument_list|<
name|T
argument_list|>
name|cond
init|=
name|searchContext
operator|.
name|getCondition
argument_list|(
name|originalExpression
argument_list|,
name|beanClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|cond
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|cond
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
return|return
name|queryClass
operator|.
name|cast
argument_list|(
name|visitor
operator|.
name|getQuery
argument_list|()
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
parameter_list|,
name|Y
parameter_list|>
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|Y
argument_list|>
name|getVisitor
parameter_list|()
block|{
name|Object
name|visitor
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SearchUtils
operator|.
name|SEARCH_VISITOR_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|//TODO: consider introducing SearchConditionVisitor.getBeanClass&&
comment|//      SearchConditionVisitor.getQueryClass to avoid such casts
return|return
operator|(
name|SearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|Y
argument_list|>
operator|)
name|visitor
return|;
block|}
block|}
block|}
end_class

end_unit

