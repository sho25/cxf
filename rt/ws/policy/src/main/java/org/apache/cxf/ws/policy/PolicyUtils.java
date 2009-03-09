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
name|ws
operator|.
name|policy
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
name|ws
operator|.
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|NestedPrimitiveAssertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyOperator
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PolicyUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INDENT
init|=
literal|"  "
decl_stmt|;
specifier|private
name|PolicyUtils
parameter_list|()
block|{     }
comment|/**      * Determine if a collection of assertions contains a given assertion, using      * the equal method from the Assertion interface.      *       * @param assertions a collection of assertions      * @param candidate the assertion to test      * @return true iff candidate is equal to one of the assertions in the collection      */
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|assertions
parameter_list|,
name|Assertion
name|candidate
parameter_list|)
block|{
for|for
control|(
name|Assertion
name|a
range|:
name|assertions
control|)
block|{
if|if
condition|(
name|a
operator|.
name|equal
argument_list|(
name|candidate
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Determine if one collection of assertions contains another collection of assertion, using      * the equal method from the Assertion interface.      *       * @param assertions a collection of assertions      * @param candidates the collections of assertion to test      * @return true iff each candidate is equal to one of the assertions in the collection      */
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|assertions
parameter_list|,
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|candidates
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|candidates
operator|||
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Assertion
name|c
range|:
name|candidates
control|)
block|{
if|if
condition|(
operator|!
name|contains
argument_list|(
name|assertions
argument_list|,
name|c
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
specifier|static
name|void
name|logPolicy
parameter_list|(
name|Logger
name|log
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|msg
parameter_list|,
name|PolicyComponent
name|pc
parameter_list|)
block|{
if|if
condition|(
operator|!
name|log
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|null
operator|==
name|pc
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|msg
argument_list|)
expr_stmt|;
return|return;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|nl
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|printPolicyComponent
argument_list|(
name|pc
argument_list|,
name|buf
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|log
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|printPolicyComponent
parameter_list|(
name|PolicyComponent
name|pc
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|printPolicyComponent
argument_list|(
name|pc
argument_list|,
name|buf
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|printPolicyComponent
parameter_list|(
name|PolicyComponent
name|pc
parameter_list|,
name|StringBuffer
name|buf
parameter_list|,
name|int
name|level
parameter_list|)
block|{
name|indent
argument_list|(
name|buf
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"type: "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|typeToString
argument_list|(
name|pc
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|Constants
operator|.
name|TYPE_ASSERTION
operator|==
name|pc
operator|.
name|getType
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
operator|(
operator|(
name|Assertion
operator|)
name|pc
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|(
name|Assertion
operator|)
name|pc
operator|)
operator|.
name|isOptional
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" (optional)"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
operator|(
name|Assertion
operator|)
name|pc
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|nl
argument_list|(
name|buf
argument_list|)
expr_stmt|;
if|if
condition|(
name|pc
operator|instanceof
name|NestedPrimitiveAssertion
condition|)
block|{
name|PolicyComponent
name|nested
init|=
operator|(
operator|(
name|NestedPrimitiveAssertion
operator|)
name|pc
operator|)
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
name|level
operator|++
expr_stmt|;
name|printPolicyComponent
argument_list|(
name|nested
argument_list|,
name|buf
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|level
operator|--
expr_stmt|;
block|}
block|}
else|else
block|{
name|level
operator|++
expr_stmt|;
name|List
argument_list|<
name|PolicyComponent
argument_list|>
name|children
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
operator|(
name|PolicyOperator
operator|)
name|pc
operator|)
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|nl
argument_list|(
name|buf
argument_list|)
expr_stmt|;
for|for
control|(
name|PolicyComponent
name|child
range|:
name|children
control|)
block|{
name|printPolicyComponent
argument_list|(
name|child
argument_list|,
name|buf
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
name|level
operator|--
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|indent
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|int
name|level
parameter_list|)
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
name|level
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|INDENT
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|nl
parameter_list|(
name|StringBuffer
name|buf
parameter_list|)
block|{
name|buf
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
specifier|private
specifier|static
name|String
name|typeToString
parameter_list|(
name|short
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Constants
operator|.
name|TYPE_ASSERTION
case|:
return|return
literal|"Assertion"
return|;
case|case
name|Constants
operator|.
name|TYPE_ALL
case|:
return|return
literal|"All"
return|;
case|case
name|Constants
operator|.
name|TYPE_EXACTLYONE
case|:
return|return
literal|"ExactlyOne"
return|;
case|case
name|Constants
operator|.
name|TYPE_POLICY
case|:
return|return
literal|"Policy"
return|;
case|case
name|Constants
operator|.
name|TYPE_POLICY_REF
case|:
return|return
literal|"PolicyReference"
return|;
default|default:
break|break;
block|}
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

