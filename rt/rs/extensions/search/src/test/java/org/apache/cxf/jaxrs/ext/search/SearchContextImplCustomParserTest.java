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
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|SearchContextImplCustomParserTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testQuery
parameter_list|()
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SearchContextImpl
operator|.
name|CUSTOM_SEARCH_QUERY_PARAM_NAME
argument_list|,
literal|"$customfilter"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SearchContextImpl
operator|.
name|CUSTOM_SEARCH_PARSER_PROPERTY
argument_list|,
operator|new
name|CustomParser
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
literal|"$customfilter=color is red"
argument_list|)
expr_stmt|;
name|SearchCondition
argument_list|<
name|Color
argument_list|>
name|sc
init|=
operator|new
name|SearchContextImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getCondition
argument_list|(
name|Color
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|isMet
argument_list|(
operator|new
name|Color
argument_list|(
literal|"red"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|isMet
argument_list|(
operator|new
name|Color
argument_list|(
literal|"blue"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|CustomParser
implements|implements
name|SearchConditionParser
argument_list|<
name|Color
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|SearchCondition
argument_list|<
name|Color
argument_list|>
name|parse
parameter_list|(
name|String
name|searchExpression
parameter_list|)
throws|throws
name|SearchParseException
block|{
if|if
condition|(
operator|!
name|searchExpression
operator|.
name|startsWith
argument_list|(
literal|"color is "
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SearchParseException
argument_list|()
throw|;
block|}
name|String
name|value
init|=
name|searchExpression
operator|.
name|substring
argument_list|(
literal|9
argument_list|)
decl_stmt|;
return|return
operator|new
name|PrimitiveSearchCondition
argument_list|<
name|Color
argument_list|>
argument_list|(
literal|"color"
argument_list|,
name|value
argument_list|,
name|ConditionType
operator|.
name|EQUALS
argument_list|,
operator|new
name|Color
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|Color
block|{
specifier|private
name|String
name|color
decl_stmt|;
name|Color
parameter_list|(
name|String
name|color
parameter_list|)
block|{
name|this
operator|.
name|color
operator|=
name|color
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|String
name|getColor
parameter_list|()
block|{
return|return
name|color
return|;
block|}
block|}
block|}
end_class

end_unit

