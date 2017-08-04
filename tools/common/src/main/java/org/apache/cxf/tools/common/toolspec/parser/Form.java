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
name|Node
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
name|helpers
operator|.
name|DOMUtils
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
name|Form
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
name|Form
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
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|optionGroups
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Form
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
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|element
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"optionGroup"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|optionGroups
operator|.
name|add
argument_list|(
operator|new
name|OptionGroup
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|elemList
operator|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|element
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"argument"
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|arguments
operator|.
name|add
argument_list|(
operator|new
name|Argument
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getOptions
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getOptions
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
name|Node
name|node
init|=
name|el
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"option"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
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
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Attempt to consume all the args in the input stream by matching them to      * options, optionGroups and argument specified in the usage definition.      */
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
literal|"Accepting token stream for form of usage: "
operator|+
name|this
operator|+
literal|", tokens are "
operator|+
name|args
argument_list|)
expr_stmt|;
block|}
name|int
name|oldpos
init|=
name|args
operator|.
name|getPosition
argument_list|()
decl_stmt|;
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
literal|"Position is: "
operator|+
name|oldpos
argument_list|)
expr_stmt|;
block|}
name|boolean
name|hasInfo
init|=
name|hasInfoOption
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|args
operator|.
name|setPosition
argument_list|(
name|oldpos
argument_list|)
expr_stmt|;
while|while
condition|(
name|args
operator|.
name|available
argument_list|()
operator|>
literal|0
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
literal|"Args is available"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|accepted
init|=
literal|false
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
name|optionGroups
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|OptionGroup
name|optionGroup
init|=
operator|(
name|OptionGroup
operator|)
name|optionGroups
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|optionGroup
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|options
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Option
name|option
init|=
operator|(
name|Option
operator|)
name|options
operator|.
name|get
argument_list|(
name|i
argument_list|)
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
name|accepted
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|accepted
condition|)
block|{
break|break;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|optionGroups
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|OptionGroup
name|optionGroup
init|=
operator|(
name|OptionGroup
operator|)
name|optionGroups
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|optionGroup
operator|.
name|isSatisfied
argument_list|(
name|errors
argument_list|)
operator|&&
operator|!
name|hasInfo
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|options
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Option
name|option
init|=
operator|(
name|Option
operator|)
name|options
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|option
operator|.
name|isSatisfied
argument_list|(
name|errors
argument_list|)
operator|&&
operator|!
name|hasInfo
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|arguments
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arguments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Argument
name|argument
init|=
operator|(
name|Argument
operator|)
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|argument
operator|.
name|accept
argument_list|(
name|args
argument_list|,
name|result
argument_list|,
name|errors
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|argument
operator|.
name|isSatisfied
argument_list|(
name|errors
argument_list|)
operator|&&
operator|!
name|hasInfo
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|args
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|next
init|=
name|args
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ErrorVisitor
operator|.
name|UnexpectedOption
argument_list|(
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ErrorVisitor
operator|.
name|UnexpectedArgument
argument_list|(
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|+
literal|" form is returning false as there are more args available"
operator|+
literal|" that haven't been consumed"
argument_list|)
expr_stmt|;
block|}
name|args
operator|.
name|setPosition
argument_list|(
name|oldpos
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// If we have got here than we have fully consumed all the arguments.
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
literal|"Form "
operator|+
name|this
operator|+
literal|" is returning true"
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|hasInfoOption
parameter_list|(
name|TokenInputStream
name|args
parameter_list|)
block|{
name|int
name|pos
init|=
name|args
operator|.
name|getPosition
argument_list|()
decl_stmt|;
name|args
operator|.
name|setPosition
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|String
name|optionValue
decl_stmt|;
while|while
condition|(
name|args
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|optionValue
operator|=
name|args
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"-?"
operator|.
name|equals
argument_list|(
name|optionValue
argument_list|)
operator|||
literal|"-help"
operator|.
name|equals
argument_list|(
name|optionValue
argument_list|)
operator|||
literal|"-h"
operator|.
name|equals
argument_list|(
name|optionValue
argument_list|)
operator|||
literal|"-v"
operator|.
name|equals
argument_list|(
name|optionValue
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|args
operator|.
name|setPosition
argument_list|(
name|pos
argument_list|)
expr_stmt|;
return|return
literal|false
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
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|element
operator|.
name|hasAttribute
argument_list|(
literal|"value"
argument_list|)
condition|)
block|{
return|return
name|element
operator|.
name|getAttribute
argument_list|(
literal|"value"
argument_list|)
return|;
block|}
return|return
literal|"default"
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

