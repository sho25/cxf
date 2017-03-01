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
name|Set
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

begin_class
specifier|public
class|class
name|ErrorVisitor
block|{
specifier|public
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
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
name|ErrorVisitor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|CommandLineError
argument_list|>
name|errors
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|static
class|class
name|MissingOption
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|Option
name|o
decl_stmt|;
specifier|public
name|MissingOption
parameter_list|(
name|Option
name|op
parameter_list|)
block|{
name|this
operator|.
name|o
operator|=
name|op
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Missing option: "
operator|+
name|o
operator|.
name|getPrimarySwitch
argument_list|()
return|;
block|}
specifier|public
name|Option
name|getOption
parameter_list|()
block|{
return|return
name|o
return|;
block|}
specifier|public
name|String
name|getOptionSwitch
parameter_list|()
block|{
return|return
name|o
operator|.
name|getPrimarySwitch
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|DuplicateOption
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|option
decl_stmt|;
specifier|public
name|DuplicateOption
parameter_list|(
name|String
name|opt
parameter_list|)
block|{
name|option
operator|=
name|opt
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Duplicated option: "
operator|+
name|option
return|;
block|}
specifier|public
name|String
name|getOptionSwitch
parameter_list|()
block|{
return|return
name|option
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|DuplicateArgument
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|argument
decl_stmt|;
specifier|public
name|DuplicateArgument
parameter_list|(
name|String
name|arg
parameter_list|)
block|{
name|this
operator|.
name|argument
operator|=
name|arg
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Duplicated argument: "
operator|+
name|argument
return|;
block|}
specifier|public
name|String
name|getOptionSwitch
parameter_list|()
block|{
return|return
name|argument
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|UnexpectedOption
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|option
decl_stmt|;
specifier|public
name|UnexpectedOption
parameter_list|(
name|String
name|opt
parameter_list|)
block|{
name|this
operator|.
name|option
operator|=
name|opt
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Unexpected option: "
operator|+
name|option
return|;
block|}
specifier|public
name|String
name|getOptionSwitch
parameter_list|()
block|{
return|return
name|option
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|UnexpectedArgument
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|arg
decl_stmt|;
specifier|public
name|UnexpectedArgument
parameter_list|(
name|String
name|a
parameter_list|)
block|{
name|this
operator|.
name|arg
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Unexpected argument: "
operator|+
name|arg
return|;
block|}
specifier|public
name|String
name|getArgument
parameter_list|()
block|{
return|return
name|arg
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|InvalidOption
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|option
decl_stmt|;
specifier|public
name|InvalidOption
parameter_list|(
name|String
name|opt
parameter_list|)
block|{
name|this
operator|.
name|option
operator|=
name|opt
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Invalid option: "
operator|+
name|option
operator|+
literal|" is missing its associated argument"
return|;
block|}
specifier|public
name|String
name|getOptionSwitch
parameter_list|()
block|{
return|return
name|option
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|MissingArgument
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|arg
decl_stmt|;
specifier|public
name|MissingArgument
parameter_list|(
name|String
name|a
parameter_list|)
block|{
name|this
operator|.
name|arg
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Missing argument: "
operator|+
name|arg
return|;
block|}
specifier|public
name|String
name|getArgument
parameter_list|()
block|{
return|return
name|arg
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|UserError
implements|implements
name|CommandLineError
block|{
specifier|private
specifier|final
name|String
name|msg
decl_stmt|;
specifier|public
name|UserError
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|this
operator|.
name|msg
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|msg
return|;
block|}
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|msg
return|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|CommandLineError
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|CommandLineError
name|err
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
literal|"Adding error: "
operator|+
name|err
argument_list|)
expr_stmt|;
block|}
name|errors
operator|.
name|add
argument_list|(
name|err
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|errors
control|)
block|{
name|res
operator|.
name|append
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|res
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

