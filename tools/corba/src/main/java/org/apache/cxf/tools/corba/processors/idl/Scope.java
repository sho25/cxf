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
name|processors
operator|.
name|idl
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
name|antlr
operator|.
name|collections
operator|.
name|AST
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaConstants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Scope
implements|implements
name|Comparable
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"."
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|scope
decl_stmt|;
specifier|private
name|Scope
name|parent
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|public
name|Scope
parameter_list|()
block|{
name|scope
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|parent
operator|=
name|this
expr_stmt|;
block|}
specifier|public
name|Scope
parameter_list|(
name|String
name|scopes
parameter_list|,
name|String
name|separator
parameter_list|)
block|{
name|java
operator|.
name|util
operator|.
name|StringTokenizer
name|tokens
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|StringTokenizer
argument_list|(
name|scopes
argument_list|,
name|separator
argument_list|)
decl_stmt|;
name|Scope
name|rootScope
init|=
operator|new
name|Scope
argument_list|()
decl_stmt|;
name|Scope
name|prevScope
init|=
name|rootScope
operator|.
name|parent
decl_stmt|;
name|scope
operator|=
name|rootScope
operator|.
name|scope
expr_stmt|;
name|parent
operator|=
name|this
expr_stmt|;
while|while
condition|(
name|tokens
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|token
init|=
name|tokens
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|parent
operator|=
name|prevScope
expr_stmt|;
name|prevScope
operator|=
operator|new
name|Scope
argument_list|(
name|prevScope
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|scope
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Scope
parameter_list|(
name|Scope
name|containingScope
parameter_list|)
block|{
name|scope
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|containingScope
operator|.
name|scope
argument_list|)
expr_stmt|;
name|parent
operator|=
name|containingScope
operator|.
name|getParent
argument_list|()
expr_stmt|;
name|this
operator|.
name|setPrefix
argument_list|(
name|parent
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Scope
parameter_list|(
name|Scope
name|containingScope
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|scope
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|containingScope
operator|.
name|scope
argument_list|)
expr_stmt|;
name|scope
operator|.
name|add
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|parent
operator|=
name|containingScope
expr_stmt|;
name|this
operator|.
name|setPrefix
argument_list|(
name|parent
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// This is used for interface inheritance
specifier|public
name|Scope
parameter_list|(
name|Scope
name|containingScope
parameter_list|,
name|Scope
name|prefixScope
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|scope
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|containingScope
operator|.
name|scope
argument_list|)
expr_stmt|;
name|scope
operator|.
name|addAll
argument_list|(
name|prefixScope
operator|.
name|scope
argument_list|)
expr_stmt|;
name|scope
operator|.
name|add
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|parent
operator|=
name|containingScope
expr_stmt|;
name|this
operator|.
name|setPrefix
argument_list|(
name|parent
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Scope
parameter_list|(
name|Scope
name|containingScope
parameter_list|,
name|AST
name|node
parameter_list|)
block|{
name|scope
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|containingScope
operator|.
name|scope
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|scope
operator|.
name|add
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|parent
operator|=
name|containingScope
expr_stmt|;
name|this
operator|.
name|setPrefix
argument_list|(
name|parent
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|tail
parameter_list|()
block|{
name|int
name|size
init|=
name|scope
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
return|return
name|scope
operator|.
name|get
argument_list|(
name|size
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
specifier|public
name|Scope
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|scope
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toString
argument_list|(
name|SEPARATOR
argument_list|)
return|;
block|}
specifier|public
name|String
name|toIDLRepositoryID
parameter_list|()
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|CorbaConstants
operator|.
name|REPO_STRING
argument_list|)
expr_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|prefix
operator|+
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|append
argument_list|(
name|toString
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|CorbaConstants
operator|.
name|IDL_VERSION
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|otherScope
parameter_list|)
block|{
if|if
condition|(
name|otherScope
operator|instanceof
name|Scope
condition|)
block|{
return|return
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Scope
operator|)
name|otherScope
operator|)
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|otherScope
parameter_list|)
block|{
if|if
condition|(
name|otherScope
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot compare a null object"
argument_list|)
throw|;
block|}
if|if
condition|(
name|otherScope
operator|instanceof
name|Scope
condition|)
block|{
return|return
name|toString
argument_list|()
operator|.
name|compareTo
argument_list|(
name|otherScope
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
literal|"Scope class expected but found "
operator|+
name|otherScope
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
block|}
end_class

end_unit

