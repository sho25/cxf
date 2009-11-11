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
name|TokenInputStream
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
name|TokenInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|tokens
decl_stmt|;
specifier|private
name|int
name|pos
decl_stmt|;
specifier|public
name|TokenInputStream
parameter_list|(
name|String
index|[]
name|t
parameter_list|)
block|{
name|this
operator|.
name|tokens
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|String
name|read
parameter_list|()
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
literal|"Reading token "
operator|+
name|tokens
index|[
name|pos
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
index|[
name|pos
operator|++
index|]
return|;
block|}
specifier|public
name|String
name|readNext
parameter_list|()
block|{
return|return
name|read
argument_list|(
name|pos
operator|++
argument_list|)
return|;
block|}
specifier|public
name|String
name|read
parameter_list|(
name|int
name|position
parameter_list|)
block|{
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
name|pos
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|position
operator|>
name|tokens
operator|.
name|length
condition|)
block|{
name|pos
operator|=
name|tokens
operator|.
name|length
operator|-
literal|1
expr_stmt|;
block|}
return|return
name|tokens
index|[
name|pos
index|]
return|;
block|}
specifier|public
name|String
name|readPre
parameter_list|()
block|{
if|if
condition|(
name|pos
operator|!=
literal|0
condition|)
block|{
name|pos
operator|--
expr_stmt|;
block|}
return|return
name|tokens
index|[
name|pos
index|]
return|;
block|}
specifier|public
name|String
name|peek
parameter_list|()
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
literal|"Peeking token "
operator|+
name|tokens
index|[
name|pos
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
index|[
name|pos
index|]
return|;
block|}
specifier|public
name|String
name|peekPre
parameter_list|()
block|{
if|if
condition|(
name|pos
operator|==
literal|0
condition|)
block|{
return|return
name|tokens
index|[
name|pos
index|]
return|;
block|}
return|return
name|tokens
index|[
name|pos
operator|-
literal|1
index|]
return|;
block|}
specifier|public
name|String
name|peek
parameter_list|(
name|int
name|position
parameter_list|)
block|{
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
return|return
name|tokens
index|[
literal|0
index|]
return|;
block|}
if|if
condition|(
name|position
operator|>
name|tokens
operator|.
name|length
condition|)
block|{
return|return
name|tokens
index|[
name|tokens
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
return|return
name|tokens
index|[
name|position
index|]
return|;
block|}
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|pos
return|;
block|}
specifier|public
name|void
name|setPosition
parameter_list|(
name|int
name|p
parameter_list|)
block|{
name|this
operator|.
name|pos
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|int
name|available
parameter_list|()
block|{
return|return
name|tokens
operator|.
name|length
operator|-
name|pos
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|available
argument_list|()
operator|>
literal|1
return|;
block|}
specifier|public
name|boolean
name|isOutOfBound
parameter_list|()
block|{
return|return
name|pos
operator|>=
name|tokens
operator|.
name|length
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"[ "
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|pos
init|;
name|i
operator|<
name|tokens
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|tokens
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|tokens
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|" ]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

