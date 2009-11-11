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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TypesUtils
block|{
specifier|private
name|TypesUtils
parameter_list|()
block|{
comment|//complete
block|}
comment|/** Returns node corresponding to the name of the CORBA primitive type node.      *       * @param node      * @return      */
specifier|public
specifier|static
name|AST
name|getCorbaTypeNameNode
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
name|AST
name|currentNode
init|=
name|node
decl_stmt|;
if|if
condition|(
name|currentNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_unsigned
condition|)
block|{
name|currentNode
operator|=
name|currentNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|currentNode
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_long
operator|&&
operator|(
name|currentNode
operator|.
name|getNextSibling
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|currentNode
operator|.
name|getNextSibling
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_long
operator|)
condition|)
block|{
name|currentNode
operator|=
name|currentNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
name|currentNode
operator|.
name|getNextSibling
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isValidIdentifier
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
comment|// From the CORBA IDL spec (section 3.2.3):
comment|//   An identifier is an arbitrarily long sequence of ASCII alphabetic, digit,
comment|//   and underscore ("_") characters. The first character must be an ASCII
comment|//   alphabetic character. All characters are significant.
comment|//
comment|// See section 3.2.3.1 for escaped identifiers (that start with a "_")
comment|//
if|if
condition|(
operator|!
name|Character
operator|.
name|isLetter
argument_list|(
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'_'
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
name|int
name|index
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|result
operator|&&
name|index
operator|<
name|id
operator|.
name|length
argument_list|()
condition|)
block|{
name|char
name|cur
init|=
name|id
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isLetterOrDigit
argument_list|(
name|cur
argument_list|)
operator|||
name|cur
operator|==
literal|'_'
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
name|index
operator|++
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|Scope
name|generateAnonymousScopedName
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|XmlSchema
name|schema
parameter_list|)
block|{
name|Scope
name|scopedName
init|=
literal|null
decl_stmt|;
name|XmlSchemaType
name|anonSchemaType
init|=
literal|null
decl_stmt|;
name|Integer
name|id
init|=
literal|0
decl_stmt|;
do|do
block|{
name|id
operator|++
expr_stmt|;
name|StringBuilder
name|name
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|name
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
name|name
operator|.
name|append
argument_list|(
literal|"Anon"
operator|+
name|id
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
name|name
operator|.
name|append
argument_list|(
name|scope
operator|.
name|tail
argument_list|()
argument_list|)
expr_stmt|;
name|scopedName
operator|=
operator|new
name|Scope
argument_list|(
name|scope
operator|.
name|getParent
argument_list|()
argument_list|,
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|scopedQName
init|=
operator|new
name|QName
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|scopedName
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|anonSchemaType
operator|=
name|schema
operator|.
name|getTypeByName
argument_list|(
name|scopedQName
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|anonSchemaType
operator|!=
literal|null
condition|)
do|;
return|return
name|scopedName
return|;
block|}
block|}
end_class

end_unit

