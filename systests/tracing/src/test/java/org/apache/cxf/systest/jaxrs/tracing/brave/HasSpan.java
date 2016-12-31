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
name|systest
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|TypeSafeMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsCollectionContaining
import|;
end_import

begin_import
import|import
name|zipkin
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|zipkin
operator|.
name|Span
import|;
end_import

begin_class
specifier|public
class|class
name|HasSpan
extends|extends
name|IsCollectionContaining
argument_list|<
name|Span
argument_list|>
block|{
specifier|public
name|HasSpan
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HasSpan
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Matcher
argument_list|<
name|Iterable
argument_list|<
name|?
super|super
name|Annotation
argument_list|>
argument_list|>
name|matcher
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TypeSafeMatcher
argument_list|<
name|Span
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|describeTo
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|description
operator|.
name|appendText
argument_list|(
literal|"span with name "
argument_list|)
operator|.
name|appendValue
argument_list|(
name|name
argument_list|)
operator|.
name|appendText
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|!=
literal|null
condition|)
block|{
name|description
operator|.
name|appendText
argument_list|(
literal|" and "
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|describeTo
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|matchesSafely
parameter_list|(
name|Span
name|item
parameter_list|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|item
operator|.
name|name
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|matcher
operator|!=
literal|null
condition|)
block|{
return|return
name|matcher
operator|.
name|matches
argument_list|(
name|item
operator|.
name|annotations
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|HasSpan
name|hasSpan
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|HasSpan
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|HasSpan
name|hasSpan
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Matcher
argument_list|<
name|Iterable
argument_list|<
name|?
super|super
name|Annotation
argument_list|>
argument_list|>
name|matcher
parameter_list|)
block|{
return|return
operator|new
name|HasSpan
argument_list|(
name|name
argument_list|,
name|matcher
argument_list|)
return|;
block|}
block|}
end_class

end_unit

