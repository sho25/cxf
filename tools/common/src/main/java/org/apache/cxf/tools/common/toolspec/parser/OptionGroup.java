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
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|parser
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
name|Iterator
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
name|logging
operator|.
name|Level
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|Tool
import|;
end_import

begin_class
specifier|public
class|class
name|OptionGroup
implements|implements
name|TokenConsumer
block|{
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
name|OptionGroup
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Element
name|element
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|OptionGroup
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|el
expr_stmt|;
name|NodeList
name|optionEls
init|=
name|element
operator|.
name|getElementsByTagNameNS
argument_list|(
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"option"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|optionEls
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|options
operator|.
name|add
argument_list|(
operator|new
name|Option
argument_list|(
operator|(
name|Element
operator|)
name|optionEls
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|TokenInputStream
name|args
parameter_list|,
name|Element
name|result
parameter_list|,
name|ErrorVisitor
name|errors
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Accepting token stream for optionGroup: "
operator|+
name|this
operator|+
literal|", tokens are now "
operator|+
name|args
operator|+
literal|", running through "
operator|+
name|options
operator|.
name|size
argument_list|()
operator|+
literal|" options"
argument_list|)
expr_stmt|;
block|}
comment|// Give all the options the chance to exclusively consume the given
comment|// string:
name|boolean
name|accepted
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|options
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Option
name|option
init|=
operator|(
name|Option
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|option
operator|.
name|accept
argument_list|(
name|args
argument_list|,
name|result
argument_list|,
name|errors
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Option "
operator|+
name|option
operator|+
literal|" accepted the token"
argument_list|)
expr_stmt|;
block|}
name|accepted
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|accepted
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No option accepted the token, returning"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isSatisfied
parameter_list|(
name|ErrorVisitor
name|errors
parameter_list|)
block|{
comment|// Return conjunction of all isSatisfied results from every option
for|for
control|(
name|Iterator
name|it
init|=
name|options
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
operator|!
operator|(
operator|(
name|Option
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|isSatisfied
argument_list|(
name|errors
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
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|element
operator|.
name|hasAttribute
argument_list|(
literal|"id"
argument_list|)
condition|)
block|{
return|return
name|getId
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

