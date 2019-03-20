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
name|sql
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
name|SearchParseException
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
name|SearchUtils
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
name|AbstractUntypedSearchConditionVisitor
import|;
end_import

begin_class
specifier|public
class|class
name|SQLPrinterVisitor
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractUntypedSearchConditionVisitor
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
block|{
specifier|private
name|String
name|primaryTable
decl_stmt|;
specifier|private
name|String
name|tableAlias
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|columns
decl_stmt|;
specifier|private
name|StringBuilder
name|topBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|joinDone
decl_stmt|;
comment|// Can be useful when some other code will build Select and From clauses.
specifier|public
name|SQLPrinterVisitor
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLPrinterVisitor
parameter_list|(
name|String
name|table
parameter_list|,
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|table
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|columns
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLPrinterVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|,
name|String
name|table
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|columns
parameter_list|)
block|{
name|this
argument_list|(
name|fieldMap
argument_list|,
name|table
argument_list|,
literal|null
argument_list|,
name|columns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLPrinterVisitor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|,
name|String
name|table
parameter_list|,
name|String
name|tableAlias
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|columns
parameter_list|)
block|{
name|super
argument_list|(
name|fieldMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
name|this
operator|.
name|primaryTable
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|tableAlias
operator|=
name|tableAlias
expr_stmt|;
name|prepareTopStringBuilder
argument_list|()
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
name|StringBuilder
name|sb
init|=
name|getStringBuilder
argument_list|()
decl_stmt|;
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
name|String
name|property
init|=
name|statement
operator|.
name|getProperty
argument_list|()
decl_stmt|;
name|String
index|[]
name|properties
init|=
name|property
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
literal|"SQL Visitor supports only a single JOIN"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|properties
operator|.
name|length
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|joinDone
condition|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|(
literal|"SQL Visitor has already created JOIN"
argument_list|)
throw|;
block|}
name|joinDone
operator|=
literal|true
expr_stmt|;
name|String
name|joinTable
init|=
name|getRealPropertyName
argument_list|(
name|properties
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|// Joining key can be pre-configured
name|String
name|joiningKey
init|=
name|primaryTable
decl_stmt|;
if|if
condition|(
name|joiningKey
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
name|joiningKey
operator|=
name|joiningKey
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|joiningKey
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|joiningKey
operator|+=
literal|"_id"
expr_stmt|;
name|topBuilder
operator|.
name|append
argument_list|(
literal|" left join "
argument_list|)
operator|.
name|append
argument_list|(
name|joinTable
argument_list|)
expr_stmt|;
name|topBuilder
operator|.
name|append
argument_list|(
literal|" on "
argument_list|)
operator|.
name|append
argument_list|(
name|primaryTable
argument_list|)
operator|.
name|append
argument_list|(
literal|".id"
argument_list|)
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
operator|.
name|append
argument_list|(
name|joinTable
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|joiningKey
argument_list|)
expr_stmt|;
name|property
operator|=
name|joinTable
operator|+
literal|"."
operator|+
name|getRealPropertyName
argument_list|(
name|properties
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
name|String
name|name
init|=
name|getRealPropertyName
argument_list|(
name|property
argument_list|)
decl_stmt|;
name|String
name|originalValue
init|=
name|getPropertyValue
argument_list|(
name|name
argument_list|,
name|statement
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|validatePropertyValue
argument_list|(
name|name
argument_list|,
name|originalValue
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|SearchUtils
operator|.
name|toSqlWildcardString
argument_list|(
name|originalValue
argument_list|,
name|isWildcardStringMatch
argument_list|()
argument_list|)
decl_stmt|;
name|value
operator|=
name|SearchUtils
operator|.
name|duplicateSingleQuoteIfNeeded
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|tableAlias
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|tableAlias
operator|+
literal|"."
operator|+
name|name
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|SearchUtils
operator|.
name|conditionTypeToSqlOperator
argument_list|(
name|sc
operator|.
name|getConditionType
argument_list|()
argument_list|,
name|value
argument_list|,
name|originalValue
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
block|}
else|else
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
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
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|sc
operator|.
name|getConditionType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
name|saveStringBuilder
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|condition
operator|.
name|accept
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|sb
operator|=
name|getStringBuilder
argument_list|()
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
name|saveStringBuilder
argument_list|(
name|sb
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|StringBuilder
name|getStringBuilder
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
name|super
operator|.
name|getStringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|sb
operator|==
literal|null
condition|)
block|{
name|sb
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
return|return
name|sb
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
name|removeStringBuilder
argument_list|()
decl_stmt|;
return|return
name|sb
operator|==
literal|null
condition|?
literal|null
else|:
name|topBuilder
operator|.
name|toString
argument_list|()
operator|+
literal|" WHERE "
operator|+
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|prepareTopStringBuilder
parameter_list|()
block|{
if|if
condition|(
name|primaryTable
operator|!=
literal|null
condition|)
block|{
name|SearchUtils
operator|.
name|startSqlQuery
argument_list|(
name|topBuilder
argument_list|,
name|primaryTable
argument_list|,
name|tableAlias
argument_list|,
name|columns
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

