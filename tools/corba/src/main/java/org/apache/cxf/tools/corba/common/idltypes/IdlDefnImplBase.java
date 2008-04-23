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
name|corba
operator|.
name|common
operator|.
name|idltypes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Vector
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|IdlDefnImplBase
implements|implements
name|IdlDefn
block|{
specifier|private
specifier|static
name|StringBuffer
name|indent
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|private
name|IdlScopeBase
name|parent
decl_stmt|;
specifier|private
name|IdlScopedName
name|name
decl_stmt|;
name|IdlDefnImplBase
parameter_list|(
name|IdlScopeBase
name|parentScope
parameter_list|,
name|String
name|scopeName
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parentScope
expr_stmt|;
name|this
operator|.
name|name
operator|=
operator|new
name|IdlScopedName
argument_list|(
name|parent
argument_list|,
name|scopeName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IdlScopeBase
name|definedIn
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|String
name|localName
parameter_list|()
block|{
return|return
name|name
operator|.
name|localName
argument_list|()
return|;
block|}
specifier|public
name|IdlScopedName
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|()
block|{
return|return
name|name
operator|.
name|fullName
argument_list|()
return|;
block|}
specifier|public
name|IdlScopedName
name|scopeName
parameter_list|()
block|{
name|IdlScopedName
name|result
decl_stmt|;
name|IdlScopeBase
name|scope
init|=
name|definedIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|IdlDefn
operator|)
name|scope
operator|)
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|IdlScopedName
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|(
name|IdlScopedName
name|relativeTo
parameter_list|)
block|{
return|return
name|name
operator|.
name|fullName
argument_list|(
name|relativeTo
argument_list|)
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|,
name|String
name|definitionName
parameter_list|)
block|{
name|pw
operator|.
name|print
argument_list|(
name|definitionName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeFwd
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
comment|// COMPLETE;
block|}
specifier|public
name|boolean
name|isEmptyDef
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isCircular
parameter_list|()
block|{
return|return
name|getCircularScope
argument_list|(
literal|null
argument_list|,
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|IdlScopeBase
name|getCircularScope
parameter_list|(
name|IdlScopeBase
name|startScope
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|doneDefn
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|flush
parameter_list|()
block|{
comment|// COMPLETE
block|}
specifier|static
name|String
name|indent
parameter_list|()
block|{
return|return
name|indent
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|static
name|void
name|indentMore
parameter_list|()
block|{
name|indent
operator|.
name|append
argument_list|(
literal|"    "
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|indentLess
parameter_list|()
block|{
name|indent
operator|.
name|delete
argument_list|(
name|indent
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|,
name|indent
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

